package com.open.mcp.server.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SoapRequest {
    private Long id;
    private String endpointUrl;
    private String soapAction;
    private String methodName;
    private String namespace;
    private String requestHeaders;
    private String soapHeaders;
    private String requestBody;
    private String parameters;
    private String soapVersion;
    private String contentType;
    private String charset;
    private Integer timeout;
    private Date expiresAt;
    private Integer statusCode;
    private String statusMessage;
    private String wsSecurity;
    private String authenticationType;
    private String username;
    private String password;
    private String clientCertificate;
    private String clientKey;
    private String attachments;
    private Boolean isEncrypted;
    private Boolean isSigned;
    private Date createdAt;
    private Date updatedAt;
    private String description;
} 