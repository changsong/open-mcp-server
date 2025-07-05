package com.open.mcp.server.util;

import com.open.mcp.server.entity.RegistryConnection;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* Dubbo service invoker based on registry configuration table
 * Use the registry_connection table to manage registry connection configurations
 */
// Cache ReferenceConfig objects, use connection name as key
// Application name
/**
 * Invoke Dubbo service by connection name
 *
 * @param connectionName Registry connection name
 * @param interfaceName  Fully qualified interface name
 * @param version        Service version
 * @param methodName     Method name
 * @param paramTypes     Parameter type array
 * @param params         Parameter value array
 * @return Invocation result
 */
// Get registry connection config
// Get generic service
// Invoke method
// Get generic service
// Create reference config
// Create application config
// Build registry address
// Create registry config
// Create reference config
// Set system properties
// Basic config
// Set specific properties by registry type
// Set extra parameters
// Get registry connection config
// This needs to be obtained according to the actual DAO implementation
// Example implementation, replace with real database query in actual use
// Return null if config not found
// Clear cache
// Clear cache for specified connection
// Get cache statistics

public class RegistryBasedDubboInvoker {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistryBasedDubboInvoker.class);
    
    // Cache ReferenceConfig objects, use connection name as key
    private static final Map<String, ReferenceConfig<GenericService>> REFERENCE_CONFIG_CACHE = new ConcurrentHashMap<>();
    
    // Application name
    private static final String APP_NAME = "registry-based-dubbo-invoker";
    
    /**
     * Invoke Dubbo service by connection name
     *
     * @param connectionName Registry connection name
     * @param interfaceName  Fully qualified interface name
     * @param version        Service version
     * @param methodName     Method name
     * @param paramTypes     Parameter type array
     * @param params         Parameter value array
     * @return Invocation result
     */
    public static Object invoke(String connectionName, String interfaceName, String version,
                               String methodName, String[] paramTypes, Object[] params) {
        try {
            // Get registry connection config
            RegistryConnection connection = getRegistryConnection(connectionName);
            if (connection == null) {
                throw new RuntimeException("未找到注册中心连接配置: " + connectionName);
            }
            
            // Get generic service
            GenericService genericService = getGenericService(connection, interfaceName, version);
            
            // Invoke method
            return genericService.$invoke(methodName, paramTypes, params);
        } catch (Exception e) {
            logger.error("调用Dubbo服务失败: connection={}, interface={}, method={}, error={}",
                    connectionName, interfaceName, methodName, e.getMessage(), e);
            throw new RuntimeException("调用Dubbo服务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 通过注册中心配置直接调用Dubbo服务
     *
     * @param connection    注册中心连接配置
     * @param interfaceName 接口全限定名
     * @param version       服务版本
     * @param methodName    方法名
     * @param paramTypes    参数类型数组
     * @param params        参数值数组
     * @return 调用结果
     */
    public static Object invoke(RegistryConnection connection, String interfaceName, String version,
                               String methodName, String[] paramTypes, Object[] params) {
        try {
            // Get generic service
            GenericService genericService = getGenericService(connection, interfaceName, version);
            
            // Invoke method
            return genericService.$invoke(methodName, paramTypes, params);
        } catch (Exception e) {
            logger.error("调用Dubbo服务失败: connection={}, interface={}, method={}, error={}",
                    RegistryConnectionBuilder.getConnectionSummary(connection), interfaceName, methodName, e.getMessage(), e);
            throw new RuntimeException("调用Dubbo服务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get generic service
     */
    private static GenericService getGenericService(RegistryConnection connection, String interfaceName, String version) {
        String key = connection.getConnectionName() + "#" + interfaceName + "#" + (version == null ? "" : version);
        ReferenceConfig<GenericService> referenceConfig = REFERENCE_CONFIG_CACHE.get(key);
        
        if (referenceConfig == null) {
            referenceConfig = createReferenceConfig(connection, interfaceName, version);
            REFERENCE_CONFIG_CACHE.put(key, referenceConfig);
        }
        
        return referenceConfig.get();
    }
    
    /**
     * Create reference config
     */
    private static ReferenceConfig<GenericService> createReferenceConfig(
            RegistryConnection connection, String interfaceName, String version) {
        
        logger.info("创建Dubbo引用配置: {}", RegistryConnectionBuilder.getConnectionSummary(connection));
        
        // Create application config
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(APP_NAME);
        applicationConfig.setQosEnable(false); // Disable QOS service
        
        // Build registry address
        String registryAddress = RegistryConnectionBuilder.buildRegistryAddress(connection);
        logger.info("注册中心地址: {}", registryAddress);
        
        // Create registry config
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryAddress);
        registryConfig.setCheck(connection.getHealthCheckEnabled() != null ? connection.getHealthCheckEnabled() : false);
        registryConfig.setTimeout(connection.getConnectionTimeout() != null ? connection.getConnectionTimeout() : 60000);
        
        // Create reference config
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(interfaceName);
        if (version != null && !version.isEmpty()) {
            referenceConfig.setVersion(version);
        }
        referenceConfig.setGeneric("true");
        referenceConfig.setTimeout(connection.getConnectionTimeout() != null ? connection.getConnectionTimeout() : 30000);
        referenceConfig.setCheck(connection.getHealthCheckEnabled() != null ? connection.getHealthCheckEnabled() : false);
        referenceConfig.setRetries(connection.getRetryCount() != null ? connection.getRetryCount() : 3);
        referenceConfig.setCluster("failfast"); // Set fast fail mode
        
        // Set system properties
        setSystemProperties(connection);
        
        return referenceConfig;
    }
    
    /**
     * Set system properties
     */
    private static void setSystemProperties(RegistryConnection connection) {
        // Basic config
        System.setProperty("dubbo.consumer.check", 
                connection.getHealthCheckEnabled() != null ? String.valueOf(connection.getHealthCheckEnabled()) : "false");
        System.setProperty("dubbo.registry.check", 
                connection.getHealthCheckEnabled() != null ? String.valueOf(connection.getHealthCheckEnabled()) : "false");
        
        // Set specific properties by registry type
        String registryType = connection.getRegistryType();
        if (registryType != null) {
            switch (registryType.toLowerCase()) {
                case "zookeeper":
                    System.setProperty("dubbo.registry.client", 
                            connection.getZkClientType() != null ? connection.getZkClientType() : "curator");
                    break;
                case "consul":
                    System.setProperty("dubbo.registry.client", "consul");
                    break;
                case "nacos":
                    System.setProperty("dubbo.registry.client", "nacos");
                    break;
                case "etcd":
                    System.setProperty("dubbo.registry.client", "etcd");
                    break;
                case "eureka":
                    System.setProperty("dubbo.registry.client", "eureka");
                    break;
            }
        }
        
        // Set extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                com.alibaba.fastjson2.JSONObject extraParams = com.alibaba.fastjson2.JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    System.setProperty("dubbo." + entry.getKey(), String.valueOf(entry.getValue()));
                }
            } catch (Exception e) {
                logger.warn("设置额外系统属性失败: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Get registry connection config
     * This needs to be obtained according to the actual DAO implementation
     * Example implementation, replace with real database query in actual use
     */
    private static RegistryConnection getRegistryConnection(String connectionName) {
        // TODO: Should query registry_connection table from database here
        // Example implementation, return a mock config
        if ("zk_local".equals(connectionName)) {
            RegistryConnection connection = new RegistryConnection();
            connection.setConnectionName("zk_local");
            connection.setRegistryType("zookeeper");
            connection.setHost("localhost");
            connection.setPort(2181);
            connection.setScheme("tcp");
            connection.setConnectionTimeout(30000);
            connection.setSessionTimeout(60000);
            connection.setRetryCount(3);
            connection.setHealthCheckEnabled(false);
            connection.setZkRootPath("/");
            connection.setZkClientType("curator");
            connection.setExtraParams("{\"dubbo.registry.check\":\"false\",\"dubbo.consumer.check\":\"false\"}");
            connection.setIsActive(true);
            return connection;
        }
        
        // Return null if config not found
        logger.error("未找到注册中心连接配置: {}", connectionName);
        return null;
    }
    
    /**
     * Clear cache
     */
    public static void clearCache() {
        REFERENCE_CONFIG_CACHE.clear();
        logger.info("已清理Dubbo引用配置缓存");
    }
    
    /**
     * Clear cache for specified connection
     */
    public static void clearCache(String connectionName) {
        REFERENCE_CONFIG_CACHE.entrySet().removeIf(entry -> 
                entry.getKey().startsWith(connectionName + "#"));
        logger.info("已清理连接 {} 的Dubbo引用配置缓存", connectionName);
    }
    
    /**
     * Get cache statistics
     */
    public static String getCacheStats() {
        return String.format("Dubbo引用配置缓存大小: %d", REFERENCE_CONFIG_CACHE.size());
    }
} 