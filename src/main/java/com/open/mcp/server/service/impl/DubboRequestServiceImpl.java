package com.open.mcp.server.service.impl;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.DubboRequest;
import com.open.mcp.server.dao.DubboRequestDao;
import com.open.mcp.server.service.DubboRequestService;
import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class DubboRequestServiceImpl implements DubboRequestService {
    private final DubboRequestDao dubboRequestDao = new DubboRequestDao();

    private Connection getDefaultConnection() throws Exception {
        // Assume there is a default connection configuration, can be adjusted as needed
        return com.open.mcp.server.db.DatabaseConnectionManager.getJdbcConnection(new com.open.mcp.server.db.DatabaseConnection());
    }

    @Override
    public void insert(DatabaseConnection conn, DubboRequest req) throws Exception {
        dubboRequestDao.insert(conn, req);
    }

    @Override
    public DubboRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        return dubboRequestDao.selectById(conn, id);
    }

    @Override
    public void update(DatabaseConnection conn, DubboRequest req) throws Exception {
        dubboRequestDao.update(conn, req);
    }

    @Override
    public void delete(DatabaseConnection conn, Long id) throws Exception {
        dubboRequestDao.delete(conn, id);
    }

    @Override
    public List<DubboRequest> getAllServices() throws Exception {
        List<DubboRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM dubbo_request";
        try (Connection conn = getDefaultConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DubboRequest req = dubboRequestDao.selectById(null, rs.getLong("id"));
                if (req != null) list.add(req);
            }
        }
        return list;
    }

    @Override
    public DubboRequest getServiceById(Long id) throws Exception {
        return getAllServices().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public DubboRequest saveService(DubboRequest dubboRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            dubboRequestDao.insert(null, dubboRequest);
            return dubboRequest;
        }
    }

    @Override
    public DubboRequest updateService(DubboRequest dubboRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            dubboRequestDao.update(null, dubboRequest);
            return dubboRequest;
        }
    }

    @Override
    public boolean deleteService(Long id) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            dubboRequestDao.delete(null, id);
            return true;
        }
    }
} 