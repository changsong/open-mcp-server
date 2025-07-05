package com.open.mcp.server.service;

import java.util.List;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.GrpcRequest;

public interface GrpcRequestService {
    void insert(DatabaseConnection conn, GrpcRequest req) throws Exception;
    GrpcRequest selectById(DatabaseConnection conn, Long id) throws Exception;
    void update(DatabaseConnection conn, GrpcRequest req) throws Exception;
    void delete(DatabaseConnection conn, Long id) throws Exception;
    List<GrpcRequest> getAllServices() throws Exception;
    GrpcRequest getServiceById(Long id) throws Exception;
    GrpcRequest saveService(GrpcRequest GrpcRequest) throws Exception;
    GrpcRequest updateService(GrpcRequest GrpcRequest) throws Exception;
    boolean deleteService(Long id) throws Exception;
} 