package com.open.mcp.server.entity;

import lombok.Data;
import java.util.Date;

@Data
public class HttpRequest {
    private Long id;
    private String method;
    private String url;
    private String queryParams;
    private String headers;
    private String body;
    private String contentType;
    private String cookies;
    private String httpVersion;
    private Integer timeout;
    private Date expiresAt;
    private Date createdAt;
    private Date updatedAt;
    private String description;
    private Integer statusCode;
    private String statusMessage;
    private String responseHeaders;
    private String responseBody;
    private String authenticationType;
    private String clientIp;
    private String serverIp;
    private Boolean isHttps;
    private String attachments;
    private String referer;
    private String userAgent;
} 