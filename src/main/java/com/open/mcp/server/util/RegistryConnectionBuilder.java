package com.open.mcp.server.util;

import com.open.mcp.server.entity.RegistryConnection;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Registry connection builder
 * Used to generate connection addresses for different registries based on configuration
 */
public class RegistryConnectionBuilder {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistryConnectionBuilder.class);
    
    /**
     * Build registry connection address
     *
     * @param connection Registry connection configuration
     * @return Complete connection address
     */
    public static String buildRegistryAddress(RegistryConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Registry connection configuration cannot be null");
        }
        
        String registryType = connection.getRegistryType();
        if (registryType == null || registryType.trim().isEmpty()) {
            throw new IllegalArgumentException("Registry type cannot be empty");
        }
        
        switch (registryType.toLowerCase()) {
            case "zookeeper":
                return buildZooKeeperAddress(connection);
            case "consul":
                return buildConsulAddress(connection);
            case "etcd":
                return buildEtcdAddress(connection);
            case "nacos":
                return buildNacosAddress(connection);
            case "eureka":
                return buildEurekaAddress(connection);
            case "apollo":
                return buildApolloAddress(connection);
            default:
                throw new IllegalArgumentException("Unsupported registry type: " + registryType);
        }
    }
    
    /**
     * Build ZooKeeper connection address
     */
    private static String buildZooKeeperAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("zookeeper://");
        
        // Process cluster nodes
        if (connection.getClusterNodes() != null && !connection.getClusterNodes().trim().isEmpty()) {
            try {
                JSONArray nodes = JSON.parseArray(connection.getClusterNodes());
                List<String> nodeList = new ArrayList<>();
                
                for (int i = 0; i < nodes.size(); i++) {
                    JSONObject node = nodes.getJSONObject(i);
                    String host = node.getString("host");
                    Integer port = node.getInteger("port");
                    if (host != null && port != null) {
                        nodeList.add(host + ":" + port);
                    }
                }
                
                if (!nodeList.isEmpty()) {
                    address.append(String.join(",", nodeList));
                } else {
                    // Fallback to standalone config
                    address.append(connection.getHost()).append(":").append(connection.getPort());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse ZooKeeper cluster nodes, using standalone config: {}", e.getMessage());
                address.append(connection.getHost()).append(":").append(connection.getPort());
            }
        } else {
            // Standalone config
            address.append(connection.getHost()).append(":").append(connection.getPort());
        }
        
        // Add root path
        String rootPath = connection.getZkRootPath();
        if (rootPath != null && !rootPath.trim().isEmpty() && !"/".equals(rootPath)) {
            address.append(rootPath);
        }
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        if (connection.getSessionTimeout() != null) {
            params.add("session=" + connection.getSessionTimeout());
        }
        
        // Retry config
        if (connection.getRetryCount() != null) {
            params.add("retries=" + connection.getRetryCount());
        }
        
        // Health check config
        if (connection.getHealthCheckEnabled() != null && !connection.getHealthCheckEnabled()) {
            params.add("check=false");
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Build Consul connection address
     */
    private static String buildConsulAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("consul://");
        address.append(connection.getHost()).append(":").append(connection.getPort());
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Datacenter
        if (connection.getConsulDatacenter() != null && !connection.getConsulDatacenter().trim().isEmpty()) {
            params.add("datacenter=" + connection.getConsulDatacenter());
        }
        
        // ACL token
        if (connection.getConsulAclToken() != null && !connection.getConsulAclToken().trim().isEmpty()) {
            params.add("token=" + connection.getConsulAclToken());
        }
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Build etcd connection address
     */
    private static String buildEtcdAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("etcd://");
        address.append(connection.getHost()).append(":").append(connection.getPort());
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Key prefix
        if (connection.getEtcdPrefix() != null && !connection.getEtcdPrefix().trim().isEmpty()) {
            params.add("prefix=" + connection.getEtcdPrefix());
        }
        
        // Lease TTL
        if (connection.getEtcdLeaseTtl() != null) {
            params.add("lease-ttl=" + connection.getEtcdLeaseTtl());
        }
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Build Nacos connection address
     */
    private static String buildNacosAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("nacos://");
        
        // Process cluster nodes
        if (connection.getClusterNodes() != null && !connection.getClusterNodes().trim().isEmpty()) {
            try {
                JSONArray nodes = JSON.parseArray(connection.getClusterNodes());
                List<String> nodeList = new ArrayList<>();
                
                for (int i = 0; i < nodes.size(); i++) {
                    JSONObject node = nodes.getJSONObject(i);
                    String host = node.getString("host");
                    Integer port = node.getInteger("port");
                    if (host != null && port != null) {
                        nodeList.add(host + ":" + port);
                    }
                }
                
                if (!nodeList.isEmpty()) {
                    address.append(String.join(",", nodeList));
                } else {
                    // Fallback to standalone config
                    address.append(connection.getHost()).append(":").append(connection.getPort());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse Nacos cluster nodes, using standalone config: {}", e.getMessage());
                address.append(connection.getHost()).append(":").append(connection.getPort());
            }
        } else {
            // Standalone config
            address.append(connection.getHost()).append(":").append(connection.getPort());
        }
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Namespace
        if (connection.getNamespace() != null && !connection.getNamespace().trim().isEmpty()) {
            params.add("namespace=" + connection.getNamespace());
        }
        
        // Group
        if (connection.getGroupName() != null && !connection.getGroupName().trim().isEmpty()) {
            params.add("group=" + connection.getGroupName());
        }
        
        // Username and password
        if (connection.getUsername() != null && !connection.getUsername().trim().isEmpty()) {
            params.add("username=" + connection.getUsername());
        }
        if (connection.getPassword() != null && !connection.getPassword().trim().isEmpty()) {
            params.add("password=" + connection.getPassword());
        }
        
        // Access key
        if (connection.getAccessKey() != null && !connection.getAccessKey().trim().isEmpty()) {
            params.add("access-key=" + connection.getAccessKey());
        }
        if (connection.getSecretKey() != null && !connection.getSecretKey().trim().isEmpty()) {
            params.add("secret-key=" + connection.getSecretKey());
        }
        
        // Tenant ID
        if (connection.getNacosTenant() != null && !connection.getNacosTenant().trim().isEmpty()) {
            params.add("tenant=" + connection.getNacosTenant());
        }
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Build Eureka connection address
     */
    private static String buildEurekaAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("eureka://");
        address.append(connection.getHost()).append(":").append(connection.getPort());
        address.append("/eureka/");
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Build Apollo connection address
     */
    private static String buildApolloAddress(RegistryConnection connection) {
        StringBuilder address = new StringBuilder();
        address.append("apollo://");
        address.append(connection.getHost()).append(":").append(connection.getPort());
        
        // Add query parameters
        List<String> params = new ArrayList<>();
        
        // Namespace
        if (connection.getNamespace() != null && !connection.getNamespace().trim().isEmpty()) {
            params.add("namespace=" + connection.getNamespace());
        }
        
        // Username and password
        if (connection.getUsername() != null && !connection.getUsername().trim().isEmpty()) {
            params.add("username=" + connection.getUsername());
        }
        if (connection.getPassword() != null && !connection.getPassword().trim().isEmpty()) {
            params.add("password=" + connection.getPassword());
        }
        
        // Timeout config
        if (connection.getConnectionTimeout() != null) {
            params.add("timeout=" + connection.getConnectionTimeout());
        }
        
        // Extra parameters
        if (connection.getExtraParams() != null && !connection.getExtraParams().trim().isEmpty()) {
            try {
                JSONObject extraParams = JSON.parseObject(connection.getExtraParams());
                for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                    params.add(entry.getKey() + "=" + entry.getValue());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse extra parameters: {}", e.getMessage());
            }
        }
        
        if (!params.isEmpty()) {
            address.append("?").append(String.join("&", params));
        }
        
        return address.toString();
    }
    
    /**
     * Get summary information of connection configuration (for logging)
     */
    public static String getConnectionSummary(RegistryConnection connection) {
        if (connection == null) {
            return "null";
        }
        
        return String.format("RegistryConnection{type=%s, host=%s, port=%d, name=%s, active=%s}",
                connection.getRegistryType(),
                connection.getHost(),
                connection.getPort(),
                connection.getConnectionName(),
                connection.getIsActive());
    }
} 