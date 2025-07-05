package com.open.mcp.server.entity;

import lombok.Data;
import java.util.Date;

@Data
public class DubboRequest {
    private Long id;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private String methodArgumentNames;
    private String parameterTypes;
    private String parameters;
    private String version;
    private String groupName;
    private Integer timeout;
    private Integer retries;
    private String loadbalance;
    private String protocol;
    private String serialization;
    private Boolean isAsync;
    private Boolean isOneway;
    private String invokeMode;
    private String attachments;
    private String registry;
    private String application;
    private String consumerIp;
    private String providerIp;
    private String status;
    private String response;
    private String exception;
    private String attachmentsResponse;
    private Date expiresAt;
    private Date createdAt;
    private Date updatedAt;
    private String description;
} 