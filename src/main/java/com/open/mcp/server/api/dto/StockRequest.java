package com.open.mcp.server.api.dto;

import lombok.Data;
import lombok.Builder;

/**
 * Request DTO
 */
@Data
@Builder
public class StockRequest {
    private String gid;  // Stock code, e.g.: sh601009
    private String type; // Stock type
}
