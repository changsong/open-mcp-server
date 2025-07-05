package com.open.mcp.server.entity;

import lombok.Data;
import java.util.Date;

@Data
public class GrpcRequest {
    private Long id;
    private String serviceName;
    private String methodName;
    private String requestMessage;
    private String requestType;
    private String responseType;
    private String metadata;
    private Date deadline;
    private Integer timeout;
    private Boolean isStreamingRequest;
    private Boolean isStreamingResponse;
    private Boolean isBidirectionalStreaming;
    private String grpcVersion;
    private String authority;
    private String contentType;
    private String compression;
    private Boolean enableCompression;
    private String authenticationType;
    private String accessToken;
    private String clientCertificate;
    private String clientKey;
    private String serverCertificate;
    private Boolean isTls;
    private Integer statusCode;
    private String statusMessage;
    private Date expiresAt;
    private Date createdAt;
    private Date updatedAt;
    private String description;
} 