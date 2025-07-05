# Registry Connection Configuration Table Usage Guide

## Overview

The `registry_connection` table is used to centrally manage connection configuration information for various registries, supporting mainstream registries such as ZooKeeper, Consul, etcd, Nacos, Eureka, and Apollo.

## Table Structure Design

### Main Field Descriptions

#### Basic Information
- `connection_name`: Connection name, used to identify different registry connections
- `registry_type`: Registry type (zookeeper, consul, etcd, nacos, eureka, apollo)
- `host`: Registry host address
- `port`: Registry port number
- `scheme`: Protocol type (http, https, tcp)

#### Cluster Configuration
- `cluster_nodes`: Cluster node list, stores multiple node information in JSON format
- `namespace`: Namespace (supported by Nacos, Consul, etc.)
- `group_name`: Group name (supported by Nacos, etc.)

#### Authentication Information
- `username`: Username
- `password`: Password (should be stored encrypted)
- `token`: Access token (supported by Consul, etcd, etc.)
- `access_key`: Access key (supported by Nacos, etc.)
- `secret_key`: Secret key (supported by Nacos, etc.)

#### Connection Configuration
- `connection_timeout`: Connection timeout (milliseconds)
- `session_timeout`: Session timeout (milliseconds)
- `retry_count`: Retry count
- `retry_interval`: Retry interval (milliseconds)

#### Health Check Configuration
- `health_check_enabled`: Whether to enable health check
- `health_check_interval`: Health check interval (milliseconds)
- `health_check_timeout`: Health check timeout (milliseconds)

#### Security Configuration
- `ssl_enabled`: Whether to enable SSL/TLS
- `ssl_certificate`: SSL certificate (PEM format)
- `ssl_key`: SSL private key (PEM format)
- `ssl_ca`: SSL CA certificate (PEM format)
- `ssl_verify_host`: Whether to verify hostname

#### Specific Registry Configuration
- `zk_root_path`: ZooKeeper root path
- `zk_client_type`: ZooKeeper client type
- `consul_datacenter`: Consul datacenter name
- `consul_acl_token`: Consul ACL token
- `etcd_prefix`: etcd key prefix
- `etcd_lease_ttl`: etcd lease TTL (seconds)
- `nacos_config_type`: Nacos config type
- `nacos_tenant`: Nacos tenant ID

## Usage Examples

### 1. ZooKeeper Standalone Configuration

```sql
INSERT INTO registry_connection (
    connection_name, registry_type, host, port, scheme,
    connection_timeout, session_timeout, retry_count,
    health_check_enabled, zk_root_path, zk_client_type,
    extra_params, is_active, is_default, description
) VALUES (
    'zk_local', 'zookeeper', 'localhost', 2181, 'tcp',
    30000, 60000, 3,
    FALSE, '/', 'curator',
    '{"dubbo.registry.check":"false","dubbo.consumer.check":"false"}',
    TRUE, TRUE, 'Local ZooKeeper standalone configuration'
);
```

### 2. ZooKeeper Cluster Configuration

```sql
INSERT INTO registry_connection (
    connection_name, registry_type, host, port, scheme, cluster_nodes,
    connection_timeout, session_timeout, retry_count,
    health_check_enabled, zk_root_path, zk_client_type,
    extra_params, is_active, description
) VALUES (
    'zk_cluster', 'zookeeper', 'zk1.example.com', 2181, 'tcp',
    '[{"host":"zk1.example.com","port":2181},{"host":"zk2.example.com","port":2181},{"host":"zk3.example.com","port":2181}]',
    30000, 60000, 3,
    TRUE, '/', 'curator',
    '{"dubbo.registry.check":"false","dubbo.consumer.check":"false"}',
    TRUE, 'ZooKeeper cluster configuration'
);
```

### 3. Nacos Configuration

```sql
INSERT INTO registry_connection (
    connection_name, registry_type, host, port, scheme,
    namespace, group_name, username, password,
    access_key, secret_key, connection_timeout,
    health_check_enabled, nacos_config_type, nacos_tenant,
    extra_params, is_active, description
) VALUES (
    'nacos_prod', 'nacos', 'nacos.example.com', 8848, 'http',
    'public', 'DEFAULT_GROUP', 'nacos', 'encrypted_password',
    'nacos-access-key', 'nacos-secret-key', 30000,
    TRUE, 'properties', 'tenant-001',
    '{"nacos.config.long-poll.timeout":"30000","nacos.naming.push.enabled":"true"}',
    TRUE, 'Production Nacos configuration'
);
```

### 4. Consul Configuration

```sql
INSERT INTO registry_connection (
    connection_name, registry_type, host, port, scheme,
    namespace, token, connection_timeout,
    health_check_enabled, consul_datacenter, consul_acl_token,
    extra_params, is_active, description
) VALUES (
    'consul_prod', 'consul', 'consul.example.com', 8500, 'http',
    'default', 'consul-token-123', 30000,
    TRUE, 'dc1', 'consul-acl-token-456',
    '{"consul.acl.enabled":"true","consul.session.ttl":"30s"}',
    TRUE, 'Production Consul configuration'
);
```

## Java Code Usage Examples

### 1. Using RegistryConnectionBuilder

```java
// Create registry connection configuration
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

// Build connection address
String registryAddress = RegistryConnectionBuilder.buildRegistryAddress(connection);
System.out.println("Registry address: " + registryAddress);
// Output: zookeeper://localhost:2181?timeout=30000&session=60000&retries=3&check=false&dubbo.registry.check=false&dubbo.consumer.check=false
```

### 2. Using RegistryBasedDubboInvoker

```java
// Invoke Dubbo service by connection name
Object result = RegistryBasedDubboInvoker.invoke(
    "zk_local",                                    // Connection name
    "com.example.UserService",                     // Interface name
    "1.0.0",                                       // Version
    "getUser",                                     // Method name
    new String[]{"java.lang.Long"},               // Parameter types
    new Object[]{123L}                            // Parameter values
);

// Invoke Dubbo service by configuration object
RegistryConnection connection = getRegistryConnection("zk_local");
Object result = RegistryBasedDubboInvoker.invoke(
    connection,                                    // Registry configuration
    "com.example.UserService",                     // Interface name
    "1.0.0",                                       // Version
    "getUser",                                     // Method name
    new String[]{"java.lang.Long"},               // Parameter types
    new Object[]{123L}                            // Parameter values
);
```

## Supported Registry Types

### 1. ZooKeeper
- Supports standalone and cluster configuration
- Supports custom root path
- Supports multiple client types (curator, zkclient, etc.)
- Supports SSL/TLS

### 2. Nacos
- Supports standalone and cluster configuration
- Supports namespace and group
- Supports username/password and access key authentication
- Supports tenant isolation

### 3. Consul
- Supports datacenter configuration
- Supports ACL token authentication
- Supports namespace
- Supports health check

### 4. etcd
- Supports key prefix configuration
- Supports lease TTL
- Supports token authentication
- Supports SSL/TLS

### 5. Eureka
- Supports Eureka Server address configuration
- Supports timeout configuration
- Supports extra parameters

### 6. Apollo
- Supports Apollo Meta Server configuration
- Supports namespace
- Supports username/password authentication

## Best Practices

### 1. Connection Naming Convention
- Use meaningful connection names, such as `zk_prod`, `nacos_dev`, `consul_test`
- It is recommended to include environment identifiers (prod, dev, test)

### 2. Security Configuration
- Passwords and tokens should be stored encrypted
- Enable SSL/TLS for better security
- Rotate access keys regularly

### 3. Cluster Configuration
- Store cluster node information in JSON format
- Ensure high availability of cluster nodes
- Configure appropriate timeout and retry parameters

### 4. Monitoring and Maintenance
- Regularly check connection status
- Monitor connection pool usage
- Clean up invalid connections in time

### 5. Cache Management
- Use connection cache to improve performance
- Clean cache regularly
- Monitor cache hit rate

## Notes

1. **Password Encryption**: Please use encryption algorithms for storing passwords, do not store in plain text
2. **Permission Control**: Configure appropriate access permissions as needed
3. **Network Configuration**: Ensure stable network connection and configure appropriate timeout
4. **Version Compatibility**: Pay attention to compatibility between different registry versions
5. **Backup Strategy**: Regularly back up configuration information to prevent data loss

## Extensibility

This table design is highly extensible and can be extended in the following ways:

1. **Add new registry types**: Add new types in the `registry_type` field
2. **Add configuration parameters**: Store additional JSON configuration in the `extra_params` field
3. **Add specific fields**: Add new fields to the table as needed
4. **Support more protocols**: Add new protocol types in the `scheme` field 