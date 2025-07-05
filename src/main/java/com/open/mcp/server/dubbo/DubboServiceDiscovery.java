package com.open.mcp.server.dubbo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Component;

import com.open.mcp.server.service.RegistryConnectionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DubboServiceDiscovery {

    private final RegistryConnectionService registryConnectionService;
    
    // 添加缺失的字段
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigCache = new ConcurrentHashMap<>();
    private ApplicationConfig applicationConfig;
    private String registryAddress;
    
    public DubboServiceDiscovery(RegistryConnectionService registryConnectionService) {
        this.registryConnectionService = registryConnectionService;
    }
    
    public void loadDubboIfNeeded(String registryType, String registryAddress) {
        if ("zookeeper".equalsIgnoreCase(registryType)) {
            // 动态创建 Dubbo 配置
            this.applicationConfig = new ApplicationConfig();
            this.applicationConfig.setName("open-mcp-server");
            this.registryAddress = registryAddress;

            RegistryConfig registry = new RegistryConfig();
            registry.setAddress(registryAddress);

            // 初始化 Dubbo
            DubboBootstrap bootstrap = DubboBootstrap.getInstance();
            bootstrap.application(this.applicationConfig)
                    .registry(registry)
                    .start();

            // 你可以在这里动态引用 Dubbo 服务
            // ReferenceConfig<YourService> reference = new ReferenceConfig<>();
            // reference.setInterface(YourService.class);
            // YourService yourService = reference.get();
        }
    }

    
    public void checkAndInitDubbo() {
        // 从数据库查出 registry_type 和 registry_address
        String registryType = registryConnectionService.getRegistryType();
        String registryAddress = registryConnectionService.getRegistryAddress();

        loadDubboIfNeeded(registryType, registryAddress);
    }

    /*
     * Get Dubbo generic service
     *
     * @param interfaceName Interface fully qualified name
     * @param version Service version
     * @return GenericService instance
     */
    public GenericService getGenericService(String interfaceName, String version) {
        String key = interfaceName + ":" + (version == null ? "" : version);
        ReferenceConfig<GenericService> referenceConfig = referenceConfigCache.get(key);
        
        if (referenceConfig == null) {
            referenceConfig = createReferenceConfig(interfaceName, version);
            referenceConfigCache.put(key, referenceConfig);
        }
        
        return referenceConfig.get();
    }

    /**
     * Create ReferenceConfig
     */
    private ReferenceConfig<GenericService> createReferenceConfig(String interfaceName, String version) {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        
        // Set registry
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryAddress);
        referenceConfig.setRegistry(registryConfig);
        
        // Set interface and version
        referenceConfig.setInterface(interfaceName);
        if (version != null && !version.isEmpty()) {
            referenceConfig.setVersion(version);
        }
        
        // Set generic call
        referenceConfig.setGeneric("true");
        
        return referenceConfig;
    }

    /**
     * Get service method information
     */
    public Map<String, Object> getServiceMethodInfo(String interfaceName) {
        try {
            Class<?> interfaceClass = Class.forName(interfaceName);
            Map<String, Object> methodInfo = new HashMap<>();
            
            for (java.lang.reflect.Method method : interfaceClass.getMethods()) {
                Map<String, Object> paramInfo = new HashMap<>();
                paramInfo.put("returnType", method.getReturnType().getName());
                paramInfo.put("parameterTypes", method.getParameterTypes());
                methodInfo.put(method.getName(), paramInfo);
            }
            
            return methodInfo;
        } catch (ClassNotFoundException e) {
            log.error("Failed to get service method information: {}", e.getMessage());
            return new HashMap<>();
        }
    }
} 