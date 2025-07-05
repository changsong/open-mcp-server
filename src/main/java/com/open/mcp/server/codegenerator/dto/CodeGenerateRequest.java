package com.open.mcp.server.codegenerator.dto;

import lombok.Data;

@Data
public class CodeGenerateRequest {
    private String apiUrl;
    private String cookie;
    private String requestData;
    private String packagePath;
    private String apiName;
    private String responseData; // Test interface returned data
} 