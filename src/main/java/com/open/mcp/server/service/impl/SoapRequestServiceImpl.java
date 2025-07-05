package com.open.mcp.server.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.open.mcp.server.dao.SoapRequestDao;
import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.SoapRequest;
import com.open.mcp.server.service.SoapRequestService;

@Service
public class SoapRequestServiceImpl implements SoapRequestService {
    private final SoapRequestDao soapRequestDao = new SoapRequestDao();

    private Connection getDefaultConnection() throws Exception {
        // Assume there is a default connection configuration, can be adjusted as needed
        return com.open.mcp.server.db.DatabaseConnectionManager.getJdbcConnection(new com.open.mcp.server.db.DatabaseConnection());
    }

    @Override
    public void insert(DatabaseConnection conn, SoapRequest req) throws Exception {
        soapRequestDao.insert(conn, req);
    }

    @Override
    public SoapRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        return soapRequestDao.selectById(conn, id);
    }

    @Override
    public void update(DatabaseConnection conn, SoapRequest req) throws Exception {
        soapRequestDao.update(conn, req);
    }

    @Override
    public void delete(DatabaseConnection conn, Long id) throws Exception {
        soapRequestDao.delete(conn, id);
    }

    @Override
    public List<SoapRequest> getAllServices() throws Exception {
        List<SoapRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM soap_request";
        try (Connection conn = getDefaultConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SoapRequest req = soapRequestDao.selectById(null, rs.getLong("id"));
                if (req != null) list.add(req);
            }
        }
        return list;
    }

    @Override
    public SoapRequest getServiceById(Long id) throws Exception {
        return getAllServices().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public SoapRequest saveService(SoapRequest soapRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            soapRequestDao.insert(null, soapRequest);
            return soapRequest;
        }
    }

    @Override
    public SoapRequest updateService(SoapRequest soapRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            soapRequestDao.update(null, soapRequest);
            return soapRequest;
        }
    }

    @Override
    public boolean deleteService(Long id) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            soapRequestDao.delete(null, id);
            return true;
        }
    }
} 