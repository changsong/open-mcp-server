package com.open.mcp.server.service.impl;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.GrpcRequest;
import com.open.mcp.server.dao.GrpcRequestDao;
import com.open.mcp.server.service.GrpcRequestService;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.sql.*;
@Service
public class GrpcRequestServiceImpl implements GrpcRequestService {
    
    private final GrpcRequestDao grpcRequestDao = new GrpcRequestDao();

    private Connection getDefaultConnection() throws Exception {
        // Assume there is a default connection configuration, can be adjusted as needed
        return com.open.mcp.server.db.DatabaseConnectionManager.getJdbcConnection(new com.open.mcp.server.db.DatabaseConnection());
    }

    @Override
    public void insert(DatabaseConnection conn, GrpcRequest req) throws Exception {
        grpcRequestDao.insert(conn, req);
    }

    @Override
    public GrpcRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        return grpcRequestDao.selectById(conn, id);
    }

    @Override
    public void update(DatabaseConnection conn, GrpcRequest req) throws Exception {
        grpcRequestDao.update(conn, req);
    }

    @Override
    public void delete(DatabaseConnection conn, Long id) throws Exception {
        grpcRequestDao.delete(conn, id);
    }

    @Override
    public List<GrpcRequest> getAllServices() throws Exception {
        List<GrpcRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM grpc_request";
        try (Connection conn = getDefaultConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GrpcRequest req = grpcRequestDao.selectById(null, rs.getLong("id"));
                if (req != null) list.add(req);
            }
        }
        return list;
    }


    @Override
    public GrpcRequest getServiceById(Long id) throws Exception {
        return getAllServices().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public GrpcRequest saveService(GrpcRequest grpcRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            grpcRequestDao.insert(null, grpcRequest);
            return grpcRequest;
        }
    }

    @Override
    public GrpcRequest updateService(GrpcRequest grpcRequest) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            grpcRequestDao.update(null, grpcRequest);
            return grpcRequest;
        }
    }

    @Override
    public boolean deleteService(Long id) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            grpcRequestDao.delete(null, id);
            return true;
        }
    }
} 