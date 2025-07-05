package com.open.mcp.server.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dubbo service general invocation utility class
 */
public class DubboServiceInvoker {
    private static final Logger logger = LoggerFactory.getLogger(DubboServiceInvoker.class);
    
    // Cache ReferenceConfig objects
    private static final Map<String, ReferenceConfig<GenericService>> REFERENCE_CONFIG_CACHE = new ConcurrentHashMap<>();
    
    // Application name
    private static final String APP_NAME = "dubbo-service-invoker";
    
    /**
     * Invoke Dubbo service
     *
     * @param zkAddress     Registry address, format like: testzkdsf1.infra.inner-dmall.com:2181
     * @param interfaceName Interface fully qualified name
     * 
     * @param version       Service version
     * @param methodName    Method name
     * @param paramTypes    Parameter type array
     * @param params        Parameter value array
     * @return Invocation result
     */
    public static Object invoke(String zkAddress, String interfaceName, String version, 
                                String methodName, String[] paramTypes, Object[] params) {
        try {
            // Get generic service
            GenericService genericService = getGenericService(zkAddress, interfaceName, version);
            
            // Invoke method
            return genericService.$invoke(methodName, paramTypes, params);
        } catch (Exception e) {
            logger.error("Failed to invoke Dubbo service: interface={}, method={}, error={}", 
                    interfaceName, methodName, e.getMessage(), e);
            throw new RuntimeException("Failed to invoke Dubbo service: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get generic service
     */
    private static GenericService getGenericService(String zkAddress, String interfaceName, String version) {
        String key = zkAddress + "#" + interfaceName + "#" + (version == null ? "" : version);
        ReferenceConfig<GenericService> referenceConfig = REFERENCE_CONFIG_CACHE.get(key);
        
        if (referenceConfig == null) {
            referenceConfig = createReferenceConfig(zkAddress, interfaceName, version);
            REFERENCE_CONFIG_CACHE.put(key, referenceConfig);
        }
        
        return referenceConfig.get();
    }
    
    /**
     * Create reference configuration
     */
    private static ReferenceConfig<GenericService> createReferenceConfig(
            String zkAddress, String interfaceName, String version) {
        // Create application configuration
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(APP_NAME);
        applicationConfig.setQosEnable(false); // Disable QOS service
        
        // Create registry configuration - add more connection parameters
        RegistryConfig registryConfig = new RegistryConfig();
        // Add timeout and session parameters
        registryConfig.setAddress("zookeeper://" + zkAddress + "?timeout=60000&session=120000");
        registryConfig.setCheck(false); // Don't check registry availability
        registryConfig.setTimeout(60000); // Timeout 60 seconds
        
        // Create reference configuration - add more parameters
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(interfaceName);
        if (version != null && !version.isEmpty()) {
            referenceConfig.setVersion(version);
        }
        referenceConfig.setGeneric("true");
        referenceConfig.setTimeout(30000);
        referenceConfig.setCheck(false);
        referenceConfig.setRetries(3); // Add retry count
        referenceConfig.setCluster("failfast"); // Set fast fail mode
        
        // Add system properties
        System.setProperty("dubbo.consumer.check", "false");
        System.setProperty("dubbo.registry.check", "false");
        System.setProperty("dubbo.registry.client", "curator");
        
        return referenceConfig;
    }
    
    /**
     * Main method, used to directly invoke Dubbo service from command line
     */
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: java DubboServiceInvoker <zk address> <interface name> <version> <method name> <JSON params>");
            System.out.println("Example: java DubboServiceInvoker testzkdsf1.infra.inner-dmall.com:2181 com.dmall.esc.api.sdk.ESCAuthDataService 1.0 getAuthStoreList '{\"venderId\":1,\"userId\":2231881}'");
            System.out.println("Or: java DubboServiceInvoker testzkdsf1.infra.inner-dmall.com:2181 com.dm.kb.api.dubbo.ChatService 1.0 chat '{\"param1\":1,\"param2\":2231881}'");
            return;
        }
        
        String zkAddress = args[0];
        String interfaceName = args[1];
        String version = args[2];
        String methodName = args[3];
        String paramsJson = args[4];
        
        // Add more debug information and system configuration
        System.setProperty("dubbo.consumer.check", "false");
        System.setProperty("dubbo.registry.check", "false");
        System.setProperty("dubbo.registry.timeout", "60000");
        
        try {
            // Parse parameters
            Map<String, Object> paramsMap = com.alibaba.fastjson2.JSON.parseObject(paramsJson);
            List<Object> paramsList = new ArrayList<>(paramsMap.values());
            
            // Get parameter types and values - support more types
            String[] paramTypes = new String[paramsList.size()];
            Object[] params = new Object[paramsList.size()];
            
            // Check if parameter types are specified
            boolean hasParamTypes = paramsMap.containsKey("paramTypes");
            List<String> typesList = null;
            
            if (hasParamTypes) {
                typesList = com.alibaba.fastjson2.JSON.parseArray(paramsMap.get("paramTypes").toString(), String.class);
                // Remove paramTypes to keep only actual parameters
                paramsMap.remove("paramTypes");
                // Re-get parameter list
                paramsList = new ArrayList<>(paramsMap.values());
            }
            
            for (int i = 0; i < paramsList.size(); i++) {
                // If parameter types are specified, use the specified types
                if (hasParamTypes && typesList != null && i < typesList.size()) {
                    paramTypes[i] = typesList.get(i);
                } else {
                    // Otherwise automatically determine based on value type
                    Object value = paramsList.get(i);
                    if (value instanceof Integer) {
                        paramTypes[i] = "java.lang.Integer";
                        params[i] = value;
                    } else if (value instanceof Long) {
                        paramTypes[i] = "java.lang.Long";
                        params[i] = value;
                    } else if (value instanceof Double) {
                        paramTypes[i] = "java.lang.Double";
                        params[i] = value;
                    } else if (value instanceof Boolean) {
                        paramTypes[i] = "java.lang.Boolean";
                        params[i] = value;
                    } else {
                        // Default convert to Long type, most parameters are IDs
                        paramTypes[i] = "java.lang.Long";
                        try {
                            params[i] = Long.valueOf(value.toString());
                        } catch (NumberFormatException e) {
                            // If conversion fails, use string type
                            paramTypes[i] = "java.lang.String";
                            params[i] = value.toString();
                        }
                    }
                }
            }
            
            // Invoke service
            System.out.println("Invoking Dubbo service...");
            System.out.println("Registry: " + zkAddress);
            System.out.println("Interface: " + interfaceName);
            System.out.println("Version: " + version);
            System.out.println("Method: " + methodName);
            System.out.println("Parameters: " + paramsJson);
            
            // Print more detailed parameter type information
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.println("Parameter" + (i+1) + " type: " + paramTypes[i] + ", value: " + params[i]);
            }
            
            // Add ZK timeout and session information
            String fullZkAddress = "zookeeper://" + zkAddress + "?timeout=60000&session=120000";
            System.out.println("Complete registry address: " + fullZkAddress);
            
            // Use encapsulated parameter types and values to invoke
            Object result = invoke(zkAddress, interfaceName, version, methodName, paramTypes, params);
            
            System.out.println("\nInvocation result:");
            System.out.println(com.alibaba.fastjson2.JSON.toJSONString(result));
            
        } catch (Exception e) {
            System.err.println("Invocation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 