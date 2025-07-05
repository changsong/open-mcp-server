package com.open.mcp.server.service.impl;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.RegistryConnection;
import com.open.mcp.server.dao.RegistryConnectionDao;
import com.open.mcp.server.service.RegistryConnectionService;
import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class RegistryConnectionServiceImpl implements RegistryConnectionService {
    private final RegistryConnectionDao registryConnectionDao = new RegistryConnectionDao();

    private Connection getDefaultConnection() throws Exception {
        return com.open.mcp.server.db.DatabaseConnectionManager.getJdbcConnection(new com.open.mcp.server.db.DatabaseConnection());
    }

    @Override
    public void insert(DatabaseConnection conn, RegistryConnection req) throws Exception {
        registryConnectionDao.insert(conn, req);
    }

    @Override
    public RegistryConnection selectById(DatabaseConnection conn, Long id) throws Exception {
        return registryConnectionDao.selectById(conn, id);
    }

    @Override
    public void update(DatabaseConnection conn, RegistryConnection req) throws Exception {
        registryConnectionDao.update(conn, req);
    }

    @Override
    public void delete(DatabaseConnection conn, Long id) throws Exception {
        registryConnectionDao.delete(conn, id);
    }

    @Override
    public List<RegistryConnection> getAllServices() throws Exception {
        List<RegistryConnection> list = new ArrayList<>();
        String sql = "SELECT * FROM registry_connection";
        try (Connection conn = getDefaultConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RegistryConnection req = registryConnectionDao.selectById(null, rs.getLong("id"));
                if (req != null) list.add(req);
            }
        }
        return list;
    }

    @Override
    public RegistryConnection getServiceById(Long id) throws Exception {
        return getAllServices().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public RegistryConnection saveService(RegistryConnection registryConnection) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            registryConnectionDao.insert(null, registryConnection);
            return registryConnection;
        }
    }

    @Override
    public RegistryConnection updateService(RegistryConnection registryConnection) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            registryConnectionDao.update(null, registryConnection);
            return registryConnection;
        }
    }

    @Override
    public boolean deleteService(Long id) throws Exception {
        try (Connection conn = getDefaultConnection()) {
            registryConnectionDao.delete(null, id);
            return true;
        }
    }

    @Override
    public List<RegistryConnection> pageQuery(String connectionName, String registryType, String host, int pageNum, int pageSize) throws Exception {
        int offset = (pageNum - 1) * pageSize;
        return registryConnectionDao.pageQuery(connectionName, registryType, host, offset, pageSize);
    }

    @Override
    public int countQuery(String connectionName, String registryType, String host) throws Exception {
        return registryConnectionDao.countQuery(connectionName, registryType, host);
    }

    @Override
    public void batchInsert(List<RegistryConnection> list) throws Exception {
        registryConnectionDao.batchInsert(list);
    }

    @Override
    public void batchDelete(List<Long> ids) throws Exception {
        registryConnectionDao.batchDelete(ids);
    }

    @Override
    public String getRegistryType() {
        try {
            List<RegistryConnection> connections = getAllServices();
            if (!connections.isEmpty()) {
                // 返回第一个激活连接的注册中心类型
                return connections.stream()
                    .filter(conn -> conn.getIsActive() != null && conn.getIsActive())
                    .findFirst()
                    .map(RegistryConnection::getRegistryType)
                    .orElse("zookeeper");
            }
        } catch (Exception e) {
            // 记录日志但不抛出异常，返回默认值
            System.err.println("获取注册中心类型失败: " + e.getMessage());
        }
        return "zookeeper";
    }
    
    @Override
    public String getRegistryAddress() {
        try {
            List<RegistryConnection> connections = getAllServices();
            if (!connections.isEmpty()) {
                // 返回第一个激活连接的注册中心地址
                RegistryConnection activeConnection = connections.stream()
                    .filter(conn -> conn.getIsActive() != null && conn.getIsActive())
                    .findFirst()
                    .orElse(null);
                
                if (activeConnection != null) {
                    return activeConnection.getHost() + ":" + activeConnection.getPort();
                }
            }
        } catch (Exception e) {
            // 记录日志但不抛出异常，返回默认值
            System.err.println("获取注册中心地址失败: " + e.getMessage());
        }
        return "127.0.0.1:2181";
    }
} 