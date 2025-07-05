package com.open.mcp.server.api.service;

import com.open.mcp.server.annotation.Tool;
import com.open.mcp.server.annotation.ToolParam;
import com.open.mcp.server.api.StockApi;
import com.open.mcp.server.api.dto.StockRequest;
import com.open.mcp.server.api.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockApi api;

    @Value("${stock.cookie:}")
    private String cookie;

    @Value("${juhe.stock.apikey:37adc26d15e8ef6022f75e9ffc48684d}")
    private String apiKey;

    @Tool(name = "getStock", description = "获取股票信息接口")
    public StockResponse getStock(@ToolParam(name = "request", description = "请求参数") StockRequest request) {
        try {
            return api.getStock(cookie, apiKey, request.getGid(), request.getType()).execute().body();
        } catch (Exception e) {
            log.error("Failed to get stock information, error message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
