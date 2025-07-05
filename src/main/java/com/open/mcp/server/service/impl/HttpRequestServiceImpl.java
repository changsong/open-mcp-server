package com.open.mcp.server.service.impl;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.HttpRequest;
import com.open.mcp.server.dao.HttpRequestDao;
import com.open.mcp.server.service.HttpRequestService;
import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class HttpRequestServiceImpl implements HttpRequestService {
    private final HttpRequestDao httpRequestDao = new HttpRequestDao();

    @Override
    public void insert(DatabaseConnection conn, HttpRequest req) throws Exception {
        httpRequestDao.insert(conn, req);
    }

    @Override
    public HttpRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        return httpRequestDao.selectById(conn, id);
    }

    @Override
    public void update(DatabaseConnection conn, HttpRequest req) throws Exception {
        httpRequestDao.update(conn, req);
    }

    @Override
    public void delete(DatabaseConnection conn, Long id) throws Exception {
        httpRequestDao.delete(conn, id);
    }

    private Connection getDefaultConnection() throws Exception {
        // Assume there is a default connection configuration, can be adjusted as needed
        return com.open.mcp.server.db.DatabaseConnectionManager.getJdbcConnection(new com.open.mcp.server.db.DatabaseConnection());
    }

    @Override
    public List<HttpRequest> getAllServices() throws Exception {
        List<HttpRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM http_request";
        try (Connection conn = getDefaultConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HttpRequest req = httpRequestDao.selectById(null, rs.getLong("id"));
                if (req != null) list.add(req);
            }
        }
        return list;
    }


    @Override
    public HttpRequest getServiceById(Long id) throws Exception {
        return getAllServices().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public HttpRequest saveService(HttpRequest httpRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            httpRequestDao.insert(null, httpRequest);
            return httpRequest;
        }
    }

    @Override
    public HttpRequest updateService(HttpRequest httpRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            httpRequestDao.update(null, httpRequest);
            return httpRequest;
        }
    }

    @Override
    public boolean deleteService(Long id) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            httpRequestDao.delete(null, id);
            return true;
        }
    }
} 