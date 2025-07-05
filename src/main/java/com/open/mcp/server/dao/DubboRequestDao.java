package com.open.mcp.server.dao;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.db.DatabaseConnectionManager;
import com.open.mcp.server.entity.DubboRequest;
import java.sql.*;

public class DubboRequestDao {
    public void insert(DatabaseConnection conn, DubboRequest req) throws Exception {
        String sql = "INSERT INTO dubbo_request (request_id, interface_name, method_name, method_argument_names, parameter_types, parameters, version, group_name, timeout, retries, loadbalance, protocol, serialization, is_async, is_oneway, invoke_mode, attachments, registry, application, consumer_ip, provider_ip, status, response, exception, attachments_response, expires_at, created_at, updated_at, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            ps.setString(i++, req.getRequestId());
            ps.setString(i++, req.getInterfaceName());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getMethodArgumentNames());
            ps.setString(i++, req.getParameterTypes());
            ps.setString(i++, req.getParameters());
            ps.setString(i++, req.getVersion());
            ps.setString(i++, req.getGroupName());
            ps.setInt(i++, req.getTimeout());
            ps.setInt(i++, req.getRetries());
            ps.setString(i++, req.getLoadbalance());
            ps.setString(i++, req.getProtocol());
            ps.setString(i++, req.getSerialization());
            ps.setObject(i++, req.getIsAsync(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsOneway(), Types.BOOLEAN);
            ps.setString(i++, req.getInvokeMode());
            ps.setString(i++, req.getAttachments());
            ps.setString(i++, req.getRegistry());
            ps.setString(i++, req.getApplication());
            ps.setString(i++, req.getConsumerIp());
            ps.setString(i++, req.getProviderIp());
            ps.setString(i++, req.getStatus());
            ps.setString(i++, req.getResponse());
            ps.setString(i++, req.getException());
            ps.setString(i++, req.getAttachmentsResponse());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    req.setId(rs.getLong(1));
                }
            }
        }
    }

    public DubboRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        String sql = "SELECT * FROM dubbo_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DubboRequest req = new DubboRequest();
                    req.setId(rs.getLong("id"));
                    req.setRequestId(rs.getString("request_id"));
                    req.setInterfaceName(rs.getString("interface_name"));
                    req.setMethodName(rs.getString("method_name"));
                    req.setMethodArgumentNames(rs.getString("method_argument_names"));
                    req.setParameterTypes(rs.getString("parameter_types"));
                    req.setParameters(rs.getString("parameters"));
                    req.setVersion(rs.getString("version"));
                    req.setGroupName(rs.getString("group_name"));
                    req.setTimeout(rs.getInt("timeout"));
                    req.setRetries(rs.getInt("retries"));
                    req.setLoadbalance(rs.getString("loadbalance"));
                    req.setProtocol(rs.getString("protocol"));
                    req.setSerialization(rs.getString("serialization"));
                    req.setIsAsync(rs.getObject("is_async") == null ? null : rs.getBoolean("is_async"));
                    req.setIsOneway(rs.getObject("is_oneway") == null ? null : rs.getBoolean("is_oneway"));
                    req.setInvokeMode(rs.getString("invoke_mode"));
                    req.setAttachments(rs.getString("attachments"));
                    req.setRegistry(rs.getString("registry"));
                    req.setApplication(rs.getString("application"));
                    req.setConsumerIp(rs.getString("consumer_ip"));
                    req.setProviderIp(rs.getString("provider_ip"));
                    req.setStatus(rs.getString("status"));
                    req.setResponse(rs.getString("response"));
                    req.setException(rs.getString("exception"));
                    req.setAttachmentsResponse(rs.getString("attachments_response"));
                    req.setExpiresAt(rs.getTimestamp("expires_at"));
                    req.setCreatedAt(rs.getTimestamp("created_at"));
                    req.setUpdatedAt(rs.getTimestamp("updated_at"));
                    req.setDescription(rs.getString("description"));
                    return req;
                }
            }
        }
        return null;
    }

    public void update(DatabaseConnection conn, DubboRequest req) throws Exception {
        String sql = "UPDATE dubbo_request SET request_id=?, interface_name=?, method_name=?, method_argument_names=?, parameter_types=?, parameters=?, version=?, group_name=?, timeout=?, retries=?, loadbalance=?, protocol=?, serialization=?, is_async=?, is_oneway=?, invoke_mode=?, attachments=?, registry=?, application=?, consumer_ip=?, provider_ip=?, status=?, response=?, exception=?, attachments_response=?, expires_at=?, created_at=?, updated_at=?, description=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, req.getRequestId());
            ps.setString(i++, req.getInterfaceName());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getMethodArgumentNames());
            ps.setString(i++, req.getParameterTypes());
            ps.setString(i++, req.getParameters());
            ps.setString(i++, req.getVersion());
            ps.setString(i++, req.getGroupName());
            ps.setInt(i++, req.getTimeout());
            ps.setInt(i++, req.getRetries());
            ps.setString(i++, req.getLoadbalance());
            ps.setString(i++, req.getProtocol());
            ps.setString(i++, req.getSerialization());
            ps.setObject(i++, req.getIsAsync(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsOneway(), Types.BOOLEAN);
            ps.setString(i++, req.getInvokeMode());
            ps.setString(i++, req.getAttachments());
            ps.setString(i++, req.getRegistry());
            ps.setString(i++, req.getApplication());
            ps.setString(i++, req.getConsumerIp());
            ps.setString(i++, req.getProviderIp());
            ps.setString(i++, req.getStatus());
            ps.setString(i++, req.getResponse());
            ps.setString(i++, req.getException());
            ps.setString(i++, req.getAttachmentsResponse());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.setLong(i++, req.getId());
            ps.executeUpdate();
        }
    }

    public void delete(DatabaseConnection conn, Long id) throws Exception {
        String sql = "DELETE FROM dubbo_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 