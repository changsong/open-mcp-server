package com.open.mcp.server.http;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * HTTP request adapter
 * 
 * @author song.chang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpMcpAdapter {

    private final OkHttpClient okHttpClient;

    /**
     * Send HTTP request
     *
     * @param url Request URL
     * @param method Request method, POST or GET
     * @param params Request parameters JSON string
     * @param headers Request headers JSON string, optional
     * @return Response result
     */
    @Tool(name = "sendHttpRequest",description = "http接口调用工具,提供http调用")
    public String sendHttpRequest(
            @ToolParam(description = "请求地址") String url,
            @ToolParam(description = "请求方式：POST或GET") String method,
            @ToolParam(description = "请求参数JSON字符串") String params,
            @ToolParam(description = "请求头JSON字符串，可选") String headers) {

        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // Add request headers
            if (headers != null && !headers.isEmpty()) {
                Map<String, String> headerMap = JSON.parseObject(headers, Map.class);
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // Build request based on method
            if ("POST".equalsIgnoreCase(method)) {
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, params);
                requestBuilder.post(requestBody);
            } else if ("GET".equalsIgnoreCase(method)) {
                // Build URL parameters for GET request
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                if (params != null && !params.isEmpty()) {
                    Map<String, Object> paramMap = JSON.parseObject(params, Map.class);
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
                requestBuilder.url(urlBuilder.build());
                requestBuilder.get();
            } else {
                return JSON.toJSONString(new JSONObject()
                        .fluentPut("success", false)
                        .fluentPut("message", "不支持的请求方式: " + method));
            }
            
            // Send request
            Request request = requestBuilder.build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    return JSON.toJSONString(new JSONObject()
                            .fluentPut("success", response.isSuccessful())
                            .fluentPut("code", response.code())
                            .fluentPut("data", responseBody));
                } else {
                    return JSON.toJSONString(new JSONObject()
                            .fluentPut("success", response.isSuccessful())
                            .fluentPut("code", response.code())
                            .fluentPut("data", null));
                }
            }
        } catch (IOException e) {
            log.error("HTTP request failed:", e);
            return JSON.toJSONString(new JSONObject()
                    .fluentPut("success", false)
                    .fluentPut("message", e.getMessage()));
        }
    }
    
    /**
     * Send HTTP request with Cookie
     *
     * @param url Request URL
     * @param method Request method, POST or GET
     * @param params Request parameters JSON string
     * @param cookie Cookie value
     * @return Response result
     */
    @Tool(name = "sendHttpRequestWithCookie", description = "发送携带Cookie的HTTP请求")
    public String sendHttpRequestWithCookie(
            @ToolParam(description = "请求地址") String url,
            @ToolParam(description = "请求方式：POST或GET") String method,
            @ToolParam(description = "请求参数JSON字符串") String params,
            @ToolParam(description = "Cookie值") String cookie) {
        
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // Add Cookie header
            requestBuilder.addHeader("Cookie", cookie);
            
            // Add common headers
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.addHeader("purchase_order_gray", "1");
            
            // Build request based on method
            if ("POST".equalsIgnoreCase(method)) {
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, params);
                requestBuilder.post(requestBody);
            } else if ("GET".equalsIgnoreCase(method)) {
                // Build URL parameters for GET request
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                if (params != null && !params.isEmpty()) {
                    Map<String, Object> paramMap = JSON.parseObject(params, Map.class);
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
                requestBuilder.url(urlBuilder.build());
                requestBuilder.get();
            } else {
                return JSON.toJSONString(new JSONObject()
                        .fluentPut("success", false)
                        .fluentPut("message", "不支持的请求方式: " + method));
            }
            
            // Send request
            Request request = requestBuilder.build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    return JSON.toJSONString(new JSONObject()
                            .fluentPut("success", response.isSuccessful())
                            .fluentPut("code", response.code())
                            .fluentPut("data", responseBody));
                } else {
                    return JSON.toJSONString(new JSONObject()
                            .fluentPut("success", response.isSuccessful())
                            .fluentPut("code", response.code())
                            .fluentPut("data", null));
                }
            }
        } catch (IOException e) {
            log.error("HTTP request failed:", e);
            return JSON.toJSONString(new JSONObject()
                    .fluentPut("success", false)
                    .fluentPut("message", e.getMessage()));
        }
    }
} 