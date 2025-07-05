package com.open.mcp.server.service;

import java.util.List;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.HttpRequest;

public interface HttpRequestService {
    void insert(DatabaseConnection conn, HttpRequest req) throws Exception;
    HttpRequest selectById(DatabaseConnection conn, Long id) throws Exception;
    void update(DatabaseConnection conn, HttpRequest req) throws Exception;
    void delete(DatabaseConnection conn, Long id) throws Exception;
    List<HttpRequest> getAllServices() throws Exception;
    HttpRequest getServiceById(Long id) throws Exception;
    HttpRequest saveService(HttpRequest HttpRequest) throws Exception;
    HttpRequest updateService(HttpRequest HttpRequest) throws Exception;
    boolean deleteService(Long id) throws Exception;
} 