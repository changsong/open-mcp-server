package com.open.mcp.server.service;

import java.util.List;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.SoapRequest;

public interface SoapRequestService {
    void insert(DatabaseConnection conn, SoapRequest req) throws Exception;
    SoapRequest selectById(DatabaseConnection conn, Long id) throws Exception;
    void update(DatabaseConnection conn, SoapRequest req) throws Exception;
    void delete(DatabaseConnection conn, Long id) throws Exception;
    List<SoapRequest> getAllServices() throws Exception;
    SoapRequest getServiceById(Long id) throws Exception;
    SoapRequest saveService(SoapRequest SoapRequest) throws Exception;
    SoapRequest updateService(SoapRequest SoapRequest) throws Exception;
    boolean deleteService(Long id) throws Exception;
} 