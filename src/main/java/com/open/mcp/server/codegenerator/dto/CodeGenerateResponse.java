package com.open.mcp.server.codegenerator.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CodeGenerateResponse {
    private String code;
    private String message;
    private boolean success;
} 