CREATE TABLE http_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    method VARCHAR(10) NOT NULL COMMENT 'HTTP request method, e.g., GET, POST, PUT, DELETE, etc.',
    url VARCHAR(2048) NOT NULL COMMENT 'Request URL',
    query_params TEXT COMMENT 'URL query parameters, stored in JSON format',
    headers TEXT COMMENT 'HTTP request headers, stored in JSON format',
    body TEXT COMMENT 'Request body content, for POST/PUT, etc.',
    content_type VARCHAR(255) COMMENT 'Content-Type of the request body',
    cookies TEXT COMMENT 'Cookies sent with the request, stored in JSON format',
    http_version VARCHAR(10) DEFAULT 'HTTP/1.1' COMMENT 'HTTP protocol version',
    timeout INT DEFAULT 30 COMMENT 'Request timeout in seconds',
    expires_at DATETIME COMMENT 'Request expiration time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
    description VARCHAR(512) COMMENT 'Description or remarks for the request'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='HTTP request information table';

INSERT INTO http_request (
    method, url, query_params, headers, body, content_type, cookies, http_version, timeout, expires_at,
    created_at, updated_at, description, status_code, status_message, response_headers, response_body,
    authentication_type, client_ip, server_ip, is_https, attachments, referer, user_agent
) VALUES
-- 普通GET请求
('GET', 'https://api.example.com/user', '{"id": "123"}', '{"Accept":"application/json"}', NULL, NULL, NULL, 'HTTP/1.1', 30, '2024-07-01 12:00:00',
 NOW(), NOW(), 'Get user info', 200, 'OK', '{"Content-Type":"application/json"}', '{"id":123,"name":"Alice"}',
 NULL, '192.168.1.10', '10.0.0.1', TRUE, NULL, 'https://www.example.com', 'Mozilla/5.0'),

-- POST请求，带认证和附件
('POST', 'https://api.example.com/upload', NULL, '{"Authorization":"Bearer abc123"}', '{"file":"base64data"}', 'application/json', NULL, 'HTTP/1.1', 60, '2024-07-01 12:10:00',
 NOW(), NOW(), 'File upload', 201, 'Created', '{"Content-Type":"application/json"}', '{"fileId":"file_001"}',
 'Bearer', '192.168.1.20', '10.0.0.2', TRUE, '[{"fileName":"test.txt","contentType":"text/plain"}]', NULL, 'curl/7.68.0'),

-- PUT请求，带Cookie
('PUT', 'http://api.example.com/profile', NULL, '{"Content-Type":"application/json"}', '{"name":"Bob"}', 'application/json', '{"sessionid":"xyz"}', 'HTTP/1.1', 30, '2024-07-01 12:20:00',
 NOW(), NOW(), 'Update profile', 204, 'No Content', NULL, NULL,
 NULL, '192.168.1.30', '10.0.0.3', FALSE, NULL, NULL, 'PostmanRuntime/7.28.0');

CREATE TABLE dubbo_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    request_id VARCHAR(64) COMMENT 'Unique request ID',
    interface_name VARCHAR(255) NOT NULL COMMENT 'Dubbo service interface name',
    method_name VARCHAR(128) NOT NULL COMMENT 'Method name to invoke',
    method_argument_names TEXT COMMENT 'Method argument names, stored in JSON format',
    parameter_types TEXT COMMENT 'Parameter types, stored in JSON or comma-separated format',
    parameters TEXT COMMENT 'Method parameters, stored in JSON format',
    version VARCHAR(32) DEFAULT '1.0.0' COMMENT 'Service version',
    group_name VARCHAR(64) COMMENT 'Service group',
    timeout INT DEFAULT 3000 COMMENT 'Request timeout in milliseconds',
    retries INT DEFAULT 2 COMMENT 'Number of retries',
    loadbalance VARCHAR(32) DEFAULT 'random' COMMENT 'Load balancing strategy',
    protocol VARCHAR(32) DEFAULT 'dubbo' COMMENT 'Protocol type, e.g., dubbo, tri, etc.',
    serialization VARCHAR(32) DEFAULT 'hessian2' COMMENT 'Serialization type',
    is_async BOOLEAN DEFAULT FALSE COMMENT 'Whether the call is async',
    is_oneway BOOLEAN DEFAULT FALSE COMMENT 'Whether the call is oneway',
    invoke_mode VARCHAR(32) DEFAULT 'sync' COMMENT 'Invoke mode: sync, async, future, callback',
    attachments TEXT COMMENT 'Additional attachments, stored in JSON format',
    registry VARCHAR(255) COMMENT 'Registry address or identifier',
    application VARCHAR(128) COMMENT 'Application name',
    consumer_ip VARCHAR(64) COMMENT 'Consumer IP address',
    provider_ip VARCHAR(64) COMMENT 'Provider IP address',
    status VARCHAR(32) COMMENT 'Request status: success, fail, timeout, etc.',
    response TEXT COMMENT 'Response content',
    exception TEXT COMMENT 'Exception information',
    attachments_response TEXT COMMENT 'Response attachments, stored in JSON format',
    expires_at DATETIME COMMENT 'Request expiration time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
    description VARCHAR(512) COMMENT 'Description or remarks for the request'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Dubbo request information table (full fields)';
INSERT INTO dubbo_request (
    request_id, interface_name, method_name, method_argument_names, parameter_types, parameters, version, group_name,
    timeout, retries, loadbalance, protocol, serialization, is_async, is_oneway, invoke_mode, attachments,
    registry, application, consumer_ip, provider_ip, status, response, exception, attachments_response,
    expires_at, description
) VALUES
-- 普通同步调用
('req-1001', 'com.example.UserService', 'getUser', '["id"]', '["java.lang.Long"]', '{"id":123}', '1.0.0', 'user',
 3000, 2, 'random', 'dubbo', 'hessian2', FALSE, FALSE, 'sync', '{"token":"abc123"}',
 'zookeeper://127.0.0.1:2181', 'user-app', '192.168.1.10', '10.0.0.1', 'success', '{"id":123,"name":"Alice"}', NULL, NULL,
 '2024-07-01 12:00:00', 'Get user info'),

-- 异步调用
('req-1002', 'com.example.OrderService', 'createOrder', '["userId","amount"]', '["java.lang.Long","java.math.BigDecimal"]', '{"userId":123,"amount":99.99}', '1.0.0', 'order',
 5000, 1, 'leastactive', 'dubbo', 'fastjson', TRUE, FALSE, 'async', '{"traceId":"trace-001"}',
 'nacos://127.0.0.1:8848', 'order-app', '192.168.1.20', '10.0.0.2', 'success', '{"orderId":456}', NULL, NULL,
 '2024-07-01 12:10:00', 'Async create order'),

-- 单向调用，异常
('req-1003', 'com.example.NotifyService', 'notify', '["msg"]', '["java.lang.String"]', '{"msg":"hello"}', '1.0.0', NULL,
 2000, 0, 'random', 'dubbo', 'hessian2', FALSE, TRUE, 'oneway', NULL,
 'zookeeper://127.0.0.1:2181', 'notify-app', '192.168.1.30', '10.0.0.3', 'fail', NULL, 'TimeoutException', NULL,
 '2024-07-01 12:20:00', 'Oneway notify with exception');


CREATE TABLE grpc_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    service_name VARCHAR(255) NOT NULL COMMENT 'gRPC service name',
    method_name VARCHAR(128) NOT NULL COMMENT 'Method name to invoke',
    request_message TEXT COMMENT 'Request message (serialized, e.g., JSON or protobuf text format)',
    request_type VARCHAR(255) COMMENT 'Request message type (protobuf type name)',
    response_type VARCHAR(255) COMMENT 'Expected response message type (protobuf type name)',
    metadata TEXT COMMENT 'gRPC metadata, stored in JSON format',
    deadline DATETIME COMMENT 'Request deadline (absolute time for timeout)',
    timeout INT DEFAULT 3000 COMMENT 'Request timeout in milliseconds',
    is_streaming_request BOOLEAN DEFAULT FALSE COMMENT 'Whether the request is client streaming',
    is_streaming_response BOOLEAN DEFAULT FALSE COMMENT 'Whether the response is server streaming',
    is_bidirectional_streaming BOOLEAN DEFAULT FALSE COMMENT 'Whether the call is bidirectional streaming',
    grpc_version VARCHAR(16) DEFAULT '1.0' COMMENT 'gRPC protocol version',
    authority VARCHAR(255) COMMENT 'Authority (host) header',
    content_type VARCHAR(64) DEFAULT 'application/grpc' COMMENT 'Content-Type of the request',
    compression VARCHAR(32) COMMENT 'Compression algorithm, e.g., gzip, deflate, snappy',
    enable_compression BOOLEAN DEFAULT FALSE COMMENT 'Whether compression is enabled',
    authentication_type VARCHAR(64) COMMENT 'Authentication type, e.g., SSL/TLS, JWT, OAuth2, etc.',
    access_token TEXT COMMENT 'Access token for authentication (e.g., JWT/OAuth2)',
    client_certificate TEXT COMMENT 'Client certificate (PEM format or reference)',
    client_key TEXT COMMENT 'Client private key (PEM format or reference)',
    server_certificate TEXT COMMENT 'Server certificate (PEM format or reference)',
    is_tls BOOLEAN DEFAULT FALSE COMMENT 'Whether to use TLS/SSL',
    status_code INT COMMENT 'Expected or actual gRPC status code',
    status_message VARCHAR(255) COMMENT 'gRPC status message',
    expires_at DATETIME COMMENT 'Request expiration time',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
    description VARCHAR(512) COMMENT 'Description or remarks for the request'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='gRPC request information table (with streaming, compression, and authentication details)';



INSERT INTO grpc_request (
    service_name, method_name, request_message, request_type, response_type, metadata, deadline, timeout,
    is_streaming_request, is_streaming_response, is_bidirectional_streaming, grpc_version, authority, content_type,
    compression, enable_compression, authentication_type, access_token, client_certificate, client_key, server_certificate, is_tls,
    status_code, status_message, expires_at, description
) VALUES
-- 普通请求
('com.example.GreeterService', 'SayHello', '{"name":"Alice"}', 'HelloRequest', 'HelloReply',
 '{"user-agent":"grpc-java/1.40.0"}', '2024-07-01 12:00:00', 3000,
 FALSE, FALSE, FALSE, '1.0', 'greeter.example.com', 'application/grpc',
 NULL, FALSE, NULL, NULL, NULL, NULL, NULL, FALSE,
 0, NULL, '2024-07-01 12:01:00', 'Simple unary gRPC request'),

-- 客户端流式请求
('com.example.UploadService', 'UploadData', '[{"chunk":"abc"},{"chunk":"def"}]', 'UploadRequest', 'UploadReply',
 '{"authorization":"Bearer abc123"}', '2024-07-01 12:05:00', 5000,
 TRUE, FALSE, FALSE, '1.0', 'upload.example.com', 'application/grpc',
 'gzip', TRUE, 'JWT', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...', NULL, NULL, NULL, FALSE,
 0, NULL, '2024-07-01 12:06:00', 'Client streaming with JWT and gzip'),

-- 服务端流式请求
('com.example.ReportService', 'GetReportStream', '{"reportId":42}', 'ReportRequest', 'ReportReply',
 '{"custom-header":"value"}', '2024-07-01 12:10:00', 4000,
 FALSE, TRUE, FALSE, '1.0', 'report.example.com', 'application/grpc',
 NULL, FALSE, NULL, NULL, NULL, NULL, NULL, FALSE,
 0, NULL, '2024-07-01 12:11:00', 'Server streaming'),

-- 双向流式请求，带TLS和证书
('com.example.ChatService', 'Chat', '[{"msg":"hi"},{"msg":"hello"}]', 'ChatMessage', 'ChatMessage',
 '{"trace-id":"xyz-123"}', '2024-07-01 12:20:00', 10000,
 TRUE, TRUE, TRUE, '1.0', 'chat.example.com', 'application/grpc',
 NULL, FALSE, 'SSL/TLS', NULL, '-----BEGIN CERTIFICATE-----...-----END CERTIFICATE-----', '-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----', '-----BEGIN CERTIFICATE-----...-----END CERTIFICATE-----', TRUE,
 0, NULL, '2024-07-01 12:21:00', 'Bidirectional streaming with TLS certs');



CREATE TABLE soap_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    endpoint_url VARCHAR(2048) NOT NULL COMMENT 'SOAP service endpoint URL',
    soap_action VARCHAR(512) COMMENT 'SOAPAction HTTP header',
    method_name VARCHAR(128) NOT NULL COMMENT 'SOAP method (operation) name',
    namespace VARCHAR(512) COMMENT 'SOAP namespace URI',
    request_headers TEXT COMMENT 'HTTP headers, stored in JSON format',
    soap_headers TEXT COMMENT 'SOAP headers, stored in XML or JSON format',
    request_body TEXT COMMENT 'SOAP request body (XML format)',
    parameters TEXT COMMENT 'Method parameters, stored in JSON format',
    soap_version VARCHAR(16) DEFAULT '1.1' COMMENT 'SOAP protocol version (e.g., 1.1, 1.2)',
    content_type VARCHAR(64) DEFAULT 'text/xml' COMMENT 'Content-Type of the request',
    charset VARCHAR(32) DEFAULT 'UTF-8' COMMENT 'Character encoding',
    timeout INT DEFAULT 30000 COMMENT 'Request timeout in milliseconds',
    expires_at DATETIME COMMENT 'Request expiration time',
    status_code INT COMMENT 'Expected or actual HTTP status code',
    status_message VARCHAR(255) COMMENT 'HTTP status message or SOAP fault string',
    -- 新增字段
    ws_security TEXT COMMENT 'WS-Security information, stored in XML or JSON format',
    authentication_type VARCHAR(64) COMMENT 'Authentication type, e.g., Basic, WS-Security, OAuth, etc.',
    username VARCHAR(128) COMMENT 'Username for authentication',
    password VARCHAR(128) COMMENT 'Password for authentication (should be encrypted or hashed)',
    client_certificate TEXT COMMENT 'Client certificate (PEM format or reference)',
    client_key TEXT COMMENT 'Client private key (PEM format or reference)',
    attachments TEXT COMMENT 'SOAP attachments (MIME, MTOM, SwA), stored in JSON or as references',
    is_encrypted BOOLEAN DEFAULT FALSE COMMENT 'Whether the SOAP message is encrypted',
    is_signed BOOLEAN DEFAULT FALSE COMMENT 'Whether the SOAP message is digitally signed',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
    description VARCHAR(512) COMMENT 'Description or remarks for the request'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SOAP request information table (with security and attachment details)';

INSERT INTO soap_request (
    endpoint_url, soap_action, method_name, namespace, request_headers, soap_headers, request_body, parameters,
    soap_version, content_type, charset, timeout, expires_at, status_code, status_message,
    ws_security, authentication_type, username, password, client_certificate, client_key, attachments,
    is_encrypted, is_signed, created_at, updated_at, description
) VALUES
-- 普通SOAP请求
('https://service.example.com/soap', 'urn:SayHello', 'SayHello', 'http://example.com/hello',
 '{"User-Agent":"SOAP-Client/1.0"}', NULL,
 '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:hel="http://example.com/hello"><soapenv:Header/><soapenv:Body><hel:SayHello><hel:name>Alice</hel:name></hel:SayHello></soapenv:Body></soapenv:Envelope>',
 '{"name":"Alice"}', '1.1', 'text/xml', 'UTF-8', 30000, '2024-07-01 12:00:00', 200, 'OK',
 NULL, NULL, NULL, NULL, NULL, NULL, NULL, FALSE, FALSE, NOW(), NOW(), 'Simple SOAP request'),

-- 带WS-Security和用户名密码认证
('https://secure.example.com/soap', 'urn:GetBalance', 'GetBalance', 'http://example.com/bank',
 '{"User-Agent":"SOAP-Client/1.0"}',
 '<wsse:Security><wsse:UsernameToken><wsse:Username>bob</wsse:Username><wsse:Password>***</wsse:Password></wsse:UsernameToken></wsse:Security>',
 '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ban="http://example.com/bank"><soapenv:Header/><soapenv:Body><ban:GetBalance><ban:accountId>12345</ban:accountId></ban:GetBalance></soapenv:Body></soapenv:Envelope>',
 '{"accountId":"12345"}', '1.2', 'text/xml', 'UTF-8', 60000, '2024-07-01 12:10:00', 200, 'OK',
 '<wsse:Security><wsse:UsernameToken><wsse:Username>bob</wsse:Username><wsse:Password>***</wsse:Password></wsse:UsernameToken></wsse:Security>',
 'WS-Security', 'bob', 'encrypted_password', NULL, NULL, NULL, FALSE, FALSE, NOW(), NOW(), 'SOAP with WS-Security UsernameToken'),

-- 带附件、加密和签名
('https://file.example.com/soap', 'urn:UploadFile', 'UploadFile', 'http://example.com/file',
 '{"User-Agent":"SOAP-Client/1.0"}', NULL,
 '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:file="http://example.com/file"><soapenv:Header/><soapenv:Body><file:UploadFile><file:fileName>test.txt</file:fileName></file:UploadFile></soapenv:Body></soapenv:Envelope>',
 '{"fileName":"test.txt"}', '1.1', 'text/xml', 'UTF-8', 120000, '2024-07-01 12:20:00', 200, 'OK',
 NULL, 'Basic', 'alice', 'encrypted_password',
 '-----BEGIN CERTIFICATE-----...-----END CERTIFICATE-----', '-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----',
 '[{"fileName":"test.txt","contentType":"text/plain","reference":"file_storage_id_001"}]', TRUE, TRUE, NOW(), NOW(), 'SOAP with attachment, encryption and signature');



 CREATE TABLE database_connection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    connection_name VARCHAR(128) NOT NULL COMMENT 'Connection name for identification',
    db_type VARCHAR(32) NOT NULL COMMENT 'Database type: mysql, postgresql, oracle, sqlserver, mongodb, redis, elasticsearch, etc.',
    host VARCHAR(255) NOT NULL COMMENT 'Database host address',
    port INT NOT NULL COMMENT 'Database port number',
    database_name VARCHAR(128) COMMENT 'Database name/schema',
    username VARCHAR(128) COMMENT 'Username for authentication',
    password VARCHAR(255) COMMENT 'Password (should be encrypted)',
    connection_string TEXT COMMENT 'Full connection string (if provided)',
    
    -- 连接池配置
    max_connections INT DEFAULT 10 COMMENT 'Maximum number of connections in pool',
    min_connections INT DEFAULT 2 COMMENT 'Minimum number of connections in pool',
    connection_timeout INT DEFAULT 30000 COMMENT 'Connection timeout in milliseconds',
    idle_timeout INT DEFAULT 600000 COMMENT 'Idle timeout in milliseconds',
    
    -- 数据库特定配置
    charset VARCHAR(32) DEFAULT 'utf8mb4' COMMENT 'Character set',
    timezone VARCHAR(64) DEFAULT '+00:00' COMMENT 'Timezone',
    ssl_enabled BOOLEAN DEFAULT FALSE COMMENT 'Whether SSL is enabled',
    ssl_certificate TEXT COMMENT 'SSL certificate (PEM format)',
    ssl_key TEXT COMMENT 'SSL private key (PEM format)',
    ssl_ca TEXT COMMENT 'SSL CA certificate (PEM format)',
    
    -- 额外参数
    extra_params TEXT COMMENT 'Additional connection parameters, stored in JSON format',
    
    -- 状态和元数据
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Whether the connection is active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
    description VARCHAR(512) COMMENT 'Description or remarks for the connection',
    
    UNIQUE KEY uk_connection_name (connection_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Database connection information table';


INSERT INTO database_connection (
    connection_name, db_type, host, port, database_name, username, password,
    connection_string, max_connections, min_connections, connection_timeout, idle_timeout,
    charset, timezone, ssl_enabled, extra_params, description
) VALUES
-- MySQL
('mysql_prod', 'mysql', 'localhost', 3306, 'myapp', 'root', 'encrypted_password',
 NULL, 20, 5, 30000, 600000, 'utf8mb4', '+08:00', FALSE,
 '{"useSSL":false,"allowPublicKeyRetrieval":true}', 'Production MySQL database'),

-- PostgreSQL
('postgres_dev', 'postgresql', 'localhost', 5432, 'myapp_dev', 'postgres', 'encrypted_password',
 NULL, 15, 3, 30000, 600000, 'UTF8', '+08:00', FALSE,
 '{"sslmode":"disable"}', 'Development PostgreSQL database'),

-- Oracle
('oracle_prod', 'oracle', 'localhost', 1521, 'XE', 'system', 'encrypted_password',
 'jdbc:oracle:thin:@localhost:1521:XE', 10, 2, 30000, 600000, 'AL32UTF8', '+08:00', FALSE,
 '{"oracle.net.CONNECT_TIMEOUT":30000}', 'Production Oracle database'),

-- SQL Server
('sqlserver_test', 'sqlserver', 'localhost', 1433, 'master', 'sa', 'encrypted_password',
 NULL, 10, 2, 30000, 600000, 'UTF-8', '+08:00', FALSE,
 '{"encrypt":false,"trustServerCertificate":true}', 'Test SQL Server database'),

-- MongoDB
('mongodb_prod', 'mongodb', 'localhost', 27017, 'myapp', 'admin', 'encrypted_password',
 NULL, 10, 2, 30000, 600000, NULL, '+08:00', FALSE,
 '{"authSource":"admin","authMechanism":"SCRAM-SHA-1"}', 'Production MongoDB database'),

-- Redis
('redis_cache', 'redis', 'localhost', 6379, '0', NULL, 'encrypted_password',
 NULL, 20, 5, 30000, 600000, NULL, '+08:00', FALSE,
 '{"timeout":3000,"retryDelayOnFailover":100}', 'Redis cache server'),

-- Elasticsearch
('es_search', 'elasticsearch', 'localhost', 9200, NULL, 'elastic', 'encrypted_password',
 NULL, 10, 2, 30000, 600000, NULL, '+08:00', FALSE,
 '{"scheme":"http","sniffing":false}', 'Elasticsearch search engine');

-- Registry connection configuration table
CREATE TABLE registry_connection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
    connection_name VARCHAR(128) NOT NULL COMMENT 'Connection name for identification',
    registry_type VARCHAR(32) NOT NULL COMMENT 'Registry type: zookeeper, consul, etcd, nacos, eureka, apollo, etc.',
    
    -- Basic connection information
    host VARCHAR(255) NOT NULL COMMENT 'Registry host address',
    port INT NOT NULL COMMENT 'Registry port number',
    scheme VARCHAR(16) DEFAULT 'http' COMMENT 'Protocol type: http, https, tcp, etc.',
    
    -- Cluster configuration
    cluster_nodes TEXT COMMENT 'Cluster nodes list, stored in JSON format for multiple node information',
    namespace VARCHAR(128) COMMENT 'Namespace (supported by Nacos, Consul, etc.)',
    group_name VARCHAR(128) COMMENT 'Group name (supported by Nacos, etc.)',
    
    -- Authentication information
    username VARCHAR(128) COMMENT 'Username',
    password VARCHAR(255) COMMENT 'Password (should be encrypted)',
    token VARCHAR(512) COMMENT 'Access token (supported by Consul, etcd, etc.)',
    access_key VARCHAR(255) COMMENT 'Access key (supported by Nacos, etc.)',
    secret_key VARCHAR(255) COMMENT 'Secret key (supported by Nacos, etc.)',
    
    -- Connection configuration
    connection_timeout INT DEFAULT 30000 COMMENT 'Connection timeout in milliseconds',
    session_timeout INT DEFAULT 60000 COMMENT 'Session timeout in milliseconds',
    retry_count INT DEFAULT 3 COMMENT 'Number of retries',
    retry_interval INT DEFAULT 1000 COMMENT 'Retry interval in milliseconds',
    
    -- Health check configuration
    health_check_enabled BOOLEAN DEFAULT TRUE COMMENT 'Whether health check is enabled',
    health_check_interval INT DEFAULT 30000 COMMENT 'Health check interval in milliseconds',
    health_check_timeout INT DEFAULT 5000 COMMENT 'Health check timeout in milliseconds',
    
    -- Security configuration
    ssl_enabled BOOLEAN DEFAULT FALSE COMMENT 'Whether SSL/TLS is enabled',
    ssl_certificate TEXT COMMENT 'SSL certificate (PEM format)',
    ssl_key TEXT COMMENT 'SSL private key (PEM format)',
    ssl_ca TEXT COMMENT 'SSL CA certificate (PEM format)',
    ssl_verify_host BOOLEAN DEFAULT TRUE COMMENT 'Whether to verify hostname',
    
    -- Registry-specific configuration
    zk_root_path VARCHAR(255) DEFAULT '/' COMMENT 'ZooKeeper root path',
    zk_client_type VARCHAR(32) DEFAULT 'curator' COMMENT 'ZooKeeper client type: curator, zkclient, etc.',
    
    consul_datacenter VARCHAR(128) COMMENT 'Consul datacenter name',
    consul_acl_token VARCHAR(512) COMMENT 'Consul ACL token',
    
    etcd_prefix VARCHAR(255) DEFAULT '/' COMMENT 'etcd key prefix',
    etcd_lease_ttl INT DEFAULT 30 COMMENT 'etcd lease TTL in seconds',
    
    nacos_config_type VARCHAR(32) DEFAULT 'properties' COMMENT 'Nacos config type: properties, yaml, json, etc.',
    nacos_tenant VARCHAR(128) COMMENT 'Nacos tenant ID',
    
    -- Extra parameters
    extra_params TEXT COMMENT 'Extra connection parameters, stored in JSON format',
    
    -- Status and metadata
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Whether the connection is active',
    is_default BOOLEAN DEFAULT FALSE COMMENT 'Whether it is the default connection',
    priority INT DEFAULT 0 COMMENT 'Priority, lower number means higher priority',
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    created_by VARCHAR(64) COMMENT 'Created by',
    updated_by VARCHAR(64) COMMENT 'Updated by',
    description VARCHAR(512) COMMENT 'Connection description',
    
    UNIQUE KEY uk_connection_name (connection_name),
    INDEX idx_registry_type (registry_type),
    INDEX idx_is_active (is_active),
    INDEX idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Registry connection configuration table';

-- Insert sample data
INSERT INTO registry_connection (
    connection_name, registry_type, host, port, scheme, cluster_nodes, namespace, group_name,
    username, password, token, access_key, secret_key,
    connection_timeout, session_timeout, retry_count, retry_interval,
    health_check_enabled, health_check_interval, health_check_timeout,
    ssl_enabled, zk_root_path, zk_client_type, consul_datacenter, etcd_prefix, nacos_config_type,
    extra_params, is_active, is_default, priority, description
) VALUES
-- ZooKeeper standalone configuration
('zk_local', 'zookeeper', 'localhost', 2181, 'tcp', NULL, NULL, NULL,
 NULL, NULL, NULL, NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, '/', 'curator', NULL, NULL, NULL,
 '{"dubbo.registry.check":"false","dubbo.consumer.check":"false"}', TRUE, TRUE, 0, 'Local ZooKeeper standalone configuration'),

-- ZooKeeper cluster configuration
('zk_cluster', 'zookeeper', 'zk1.example.com', 2181, 'tcp', 
 '[{"host":"zk1.example.com","port":2181},{"host":"zk2.example.com","port":2181},{"host":"zk3.example.com","port":2181}]', 
 NULL, NULL,
 NULL, NULL, NULL, NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, '/', 'curator', NULL, NULL, NULL,
 '{"dubbo.registry.check":"false","dubbo.consumer.check":"false"}', TRUE, FALSE, 1, 'ZooKeeper cluster configuration'),

-- Consul configuration
('consul_prod', 'consul', 'consul.example.com', 8500, 'http', NULL, 'default', NULL,
 NULL, NULL, 'consul-token-123', NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, 'dc1', NULL, NULL,
 '{"consul.acl.enabled":"true","consul.session.ttl":"30s"}', TRUE, FALSE, 2, 'Production Consul configuration'),

-- etcd configuration
('etcd_dev', 'etcd', 'etcd.example.com', 2379, 'http', NULL, NULL, NULL,
 NULL, NULL, 'etcd-token-456', NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, NULL, '/services', NULL,
 '{"etcd.lease.ttl":"30","etcd.watch.prefix":"/services"}', TRUE, FALSE, 3, 'Development etcd configuration'),

-- Nacos configuration
('nacos_prod', 'nacos', 'nacos.example.com', 8848, 'http', NULL, 'public', 'DEFAULT_GROUP',
 'nacos', 'encrypted_password', NULL, 'nacos-access-key', 'nacos-secret-key',
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, NULL, NULL, 'properties',
 '{"nacos.config.long-poll.timeout":"30000","nacos.naming.push.enabled":"true"}', TRUE, FALSE, 4, 'Production Nacos configuration'),

-- Nacos cluster configuration
('nacos_cluster', 'nacos', 'nacos1.example.com', 8848, 'http',
 '[{"host":"nacos1.example.com","port":8848},{"host":"nacos2.example.com","port":8848},{"host":"nacos3.example.com","port":8848}]',
 'public', 'DEFAULT_GROUP',
 'nacos', 'encrypted_password', NULL, 'nacos-access-key', 'nacos-secret-key',
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, NULL, NULL, 'properties',
 '{"nacos.config.long-poll.timeout":"30000","nacos.naming.push.enabled":"true"}', TRUE, FALSE, 5, 'Nacos cluster configuration'),

-- Eureka configuration
('eureka_prod', 'eureka', 'eureka.example.com', 8761, 'http', NULL, NULL, NULL,
 NULL, NULL, NULL, NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, NULL, NULL, NULL,
 '{"eureka.client.service-url.defaultZone":"http://eureka.example.com:8761/eureka/","eureka.instance.prefer-ip-address":"true"}', TRUE, FALSE, 6, 'Production Eureka configuration'),

-- Apollo configuration
('apollo_prod', 'apollo', 'apollo.example.com', 8080, 'http', NULL, 'application', NULL,
 'apollo', 'encrypted_password', NULL, NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 FALSE, NULL, NULL, NULL, NULL, NULL,
 '{"apollo.meta":"http://apollo.example.com:8080","apollo.bootstrap.enabled":"true"}', TRUE, FALSE, 7, 'Production Apollo configuration'),

-- SSL ZooKeeper configuration
('zk_ssl', 'zookeeper', 'zk-ssl.example.com', 2181, 'tcp', NULL, NULL, NULL,
 NULL, NULL, NULL, NULL, NULL,
 30000, 60000, 3, 1000,
 TRUE, 30000, 5000,
 TRUE, '/', 'curator', NULL, NULL, NULL,
 '{"dubbo.registry.check":"false","dubbo.consumer.check":"false","ssl.enabled":"true"}', TRUE, FALSE, 8, 'SSL ZooKeeper configuration');

