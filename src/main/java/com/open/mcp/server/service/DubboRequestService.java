package com.open.mcp.server.service;

import java.util.List;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.DubboRequest;

public interface DubboRequestService {
    void insert(DatabaseConnection conn, DubboRequest req) throws Exception;
    DubboRequest selectById(DatabaseConnection conn, Long id) throws Exception;
    void update(DatabaseConnection conn, DubboRequest req) throws Exception;
    void delete(DatabaseConnection conn, Long id) throws Exception;
    List<DubboRequest> getAllServices() throws Exception;
    DubboRequest getServiceById(Long id) throws Exception;
    DubboRequest saveService(DubboRequest DubboRequest) throws Exception;
    DubboRequest updateService(DubboRequest DubboRequest) throws Exception;
    boolean deleteService(Long id) throws Exception;
} 