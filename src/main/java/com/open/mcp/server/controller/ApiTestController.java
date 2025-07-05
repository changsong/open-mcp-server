package com.open.mcp.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiTestController {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    @Data
    public static class TestRequest {
        private String apiUrl;
        private String cookie;
        private String requestData;
        private String method = "GET"; // Default to GET method
    }

    @PostMapping("/api/test")
    public ResponseEntity<JsonNode> testApi(@RequestBody TestRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            
            // If cookie is provided, add it to the request header
            if (request.getCookie() != null && !request.getCookie().isEmpty()) {
                headers.add("Cookie", request.getCookie());
            }

            HttpEntity<String> entity;
            if (request.getRequestData() != null && !request.getRequestData().isEmpty()) {
                entity = new HttpEntity<>(request.getRequestData(), headers);
            } else {
                entity = new HttpEntity<>(headers);
            }

            // Get HTTP method
            HttpMethod httpMethod = getHttpMethod(request.getMethod());
            
            // For GET requests, if there is request data, add it as URL parameters
            final StringBuilder urlBuilder = new StringBuilder(request.getApiUrl());
            if (httpMethod == HttpMethod.GET && request.getRequestData() != null && !request.getRequestData().isEmpty()) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(request.getRequestData());
                    StringBuilder queryParams = new StringBuilder();
                    jsonNode.fields().forEachRemaining(entry -> {
                        if (queryParams.length() > 0) {
                            queryParams.append("&");
                        } else if (!urlBuilder.toString().contains("?")) {
                            queryParams.append("?");
                        }
                        queryParams.append(entry.getKey()).append("=").append(entry.getValue().asText());
                    });
                    urlBuilder.append(queryParams.toString());
                } catch (Exception e) {
                    Locale locale = LocaleContextHolder.getLocale();
                    String message = messageSource.getMessage("log.parse.get.params.failed", new Object[]{e.getMessage()}, locale);
                    log.warn(message);
                }
            }

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                urlBuilder.toString(),
                httpMethod,
                entity,
                JsonNode.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            String logMessage = messageSource.getMessage("log.test.api.failed", new Object[]{e.getMessage()}, locale);
            log.error(logMessage);
            
            String errorMessage = messageSource.getMessage("error.test.api.failed", new Object[]{e.getMessage()}, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(objectMapper.createObjectNode()
                    .put("error", errorMessage));
        }
    }

    private HttpMethod getHttpMethod(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("log.unsupported.method", new Object[]{method}, locale);
            log.warn(message);
            return HttpMethod.GET;
        }
    }
} 