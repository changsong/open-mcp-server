package com.open.mcp.server.entity;

import lombok.Data;
import java.util.Date;

/**
 * 注册中心连接配置实体类
 * 支持ZooKeeper、Consul、etcd、Nacos、Eureka、Apollo等主流注册中心
 */
@Data
public class RegistryConnection {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 连接名称，用于标识
     */
    private String connectionName;
    
    /**
     * 注册中心类型: zookeeper, consul, etcd, nacos, eureka, apollo等
     */
    private String registryType;
    
    /**
     * 注册中心主机地址
     */
    private String host;
    
    /**
     * 注册中心端口号
     */
    private Integer port;
    
    /**
     * 协议类型: http, https, tcp等
     */
    private String scheme;
    
    /**
     * 集群节点列表，JSON格式存储多个节点信息
     */
    private String clusterNodes;
    
    /**
     * 命名空间（Nacos、Consul等支持）
     */
    private String namespace;
    
    /**
     * 分组名称（Nacos等支持）
     */
    private String groupName;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（需要加密存储）
     */
    private String password;
    
    /**
     * 访问令牌（Consul、etcd等支持）
     */
    private String token;
    
    /**
     * 访问密钥（Nacos等支持）
     */
    private String accessKey;
    
    /**
     * 密钥（Nacos等支持）
     */
    private String secretKey;
    
    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectionTimeout;
    
    /**
     * 会话超时时间（毫秒）
     */
    private Integer sessionTimeout;
    
    /**
     * 重试次数
     */
    private Integer retryCount;
    
    /**
     * 重试间隔（毫秒）
     */
    private Integer retryInterval;
    
    /**
     * 是否启用健康检查
     */
    private Boolean healthCheckEnabled;
    
    /**
     * 健康检查间隔（毫秒）
     */
    private Integer healthCheckInterval;
    
    /**
     * 健康检查超时时间（毫秒）
     */
    private Integer healthCheckTimeout;
    
    /**
     * 是否启用SSL/TLS
     */
    private Boolean sslEnabled;
    
    /**
     * SSL证书（PEM格式）
     */
    private String sslCertificate;
    
    /**
     * SSL私钥（PEM格式）
     */
    private String sslKey;
    
    /**
     * SSL CA证书（PEM格式）
     */
    private String sslCa;
    
    /**
     * 是否验证主机名
     */
    private Boolean sslVerifyHost;
    
    /**
     * ZooKeeper根路径
     */
    private String zkRootPath;
    
    /**
     * ZooKeeper客户端类型: curator, zkclient等
     */
    private String zkClientType;
    
    /**
     * Consul数据中心名称
     */
    private String consulDatacenter;
    
    /**
     * Consul ACL令牌
     */
    private String consulAclToken;
    
    /**
     * etcd键前缀
     */
    private String etcdPrefix;
    
    /**
     * etcd租约TTL（秒）
     */
    private Integer etcdLeaseTtl;
    
    /**
     * Nacos配置类型: properties, yaml, json等
     */
    private String nacosConfigType;
    
    /**
     * Nacos租户ID
     */
    private String nacosTenant;
    
    /**
     * 额外连接参数，JSON格式存储
     */
    private String extraParams;
    
    /**
     * 连接是否激活
     */
    private Boolean isActive;
    
    /**
     * 是否为默认连接
     */
    private Boolean isDefault;
    
    /**
     * 优先级，数字越小优先级越高
     */
    private Integer priority;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 更新时间
     */
    private Date updatedAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 连接描述
     */
    private String description;
} 