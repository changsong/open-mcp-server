package com.open.mcp.server.dao;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.db.DatabaseConnectionManager;
import com.open.mcp.server.entity.GrpcRequest;
import java.sql.*;

public class GrpcRequestDao {
    public void insert(DatabaseConnection conn, GrpcRequest req) throws Exception {
        String sql = "INSERT INTO grpc_request (service_name, method_name, request_message, request_type, response_type, metadata, deadline, timeout, is_streaming_request, is_streaming_response, is_bidirectional_streaming, grpc_version, authority, content_type, compression, enable_compression, authentication_type, access_token, client_certificate, client_key, server_certificate, is_tls, status_code, status_message, expires_at, created_at, updated_at, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            ps.setString(i++, req.getServiceName());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getRequestMessage());
            ps.setString(i++, req.getRequestType());
            ps.setString(i++, req.getResponseType());
            ps.setString(i++, req.getMetadata());
            ps.setTimestamp(i++, req.getDeadline() == null ? null : new Timestamp(req.getDeadline().getTime()));
            ps.setInt(i++, req.getTimeout());
            ps.setObject(i++, req.getIsStreamingRequest(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsStreamingResponse(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsBidirectionalStreaming(), Types.BOOLEAN);
            ps.setString(i++, req.getGrpcVersion());
            ps.setString(i++, req.getAuthority());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCompression());
            ps.setObject(i++, req.getEnableCompression(), Types.BOOLEAN);
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getAccessToken());
            ps.setString(i++, req.getClientCertificate());
            ps.setString(i++, req.getClientKey());
            ps.setString(i++, req.getServerCertificate());
            ps.setObject(i++, req.getIsTls(), Types.BOOLEAN);
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
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

    public GrpcRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        String sql = "SELECT * FROM grpc_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    GrpcRequest req = new GrpcRequest();
                    req.setId(rs.getLong("id"));
                    req.setServiceName(rs.getString("service_name"));
                    req.setMethodName(rs.getString("method_name"));
                    req.setRequestMessage(rs.getString("request_message"));
                    req.setRequestType(rs.getString("request_type"));
                    req.setResponseType(rs.getString("response_type"));
                    req.setMetadata(rs.getString("metadata"));
                    req.setDeadline(rs.getTimestamp("deadline"));
                    req.setTimeout(rs.getInt("timeout"));
                    req.setIsStreamingRequest(rs.getObject("is_streaming_request") == null ? null : rs.getBoolean("is_streaming_request"));
                    req.setIsStreamingResponse(rs.getObject("is_streaming_response") == null ? null : rs.getBoolean("is_streaming_response"));
                    req.setIsBidirectionalStreaming(rs.getObject("is_bidirectional_streaming") == null ? null : rs.getBoolean("is_bidirectional_streaming"));
                    req.setGrpcVersion(rs.getString("grpc_version"));
                    req.setAuthority(rs.getString("authority"));
                    req.setContentType(rs.getString("content_type"));
                    req.setCompression(rs.getString("compression"));
                    req.setEnableCompression(rs.getObject("enable_compression") == null ? null : rs.getBoolean("enable_compression"));
                    req.setAuthenticationType(rs.getString("authentication_type"));
                    req.setAccessToken(rs.getString("access_token"));
                    req.setClientCertificate(rs.getString("client_certificate"));
                    req.setClientKey(rs.getString("client_key"));
                    req.setServerCertificate(rs.getString("server_certificate"));
                    req.setIsTls(rs.getObject("is_tls") == null ? null : rs.getBoolean("is_tls"));
                    req.setStatusCode(rs.getObject("status_code") == null ? null : rs.getInt("status_code"));
                    req.setStatusMessage(rs.getString("status_message"));
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

    public void update(DatabaseConnection conn, GrpcRequest req) throws Exception {
        String sql = "UPDATE grpc_request SET service_name=?, method_name=?, request_message=?, request_type=?, response_type=?, metadata=?, deadline=?, timeout=?, is_streaming_request=?, is_streaming_response=?, is_bidirectional_streaming=?, grpc_version=?, authority=?, content_type=?, compression=?, enable_compression=?, authentication_type=?, access_token=?, client_certificate=?, client_key=?, server_certificate=?, is_tls=?, status_code=?, status_message=?, expires_at=?, created_at=?, updated_at=?, description=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, req.getServiceName());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getRequestMessage());
            ps.setString(i++, req.getRequestType());
            ps.setString(i++, req.getResponseType());
            ps.setString(i++, req.getMetadata());
            ps.setTimestamp(i++, req.getDeadline() == null ? null : new Timestamp(req.getDeadline().getTime()));
            ps.setInt(i++, req.getTimeout());
            ps.setObject(i++, req.getIsStreamingRequest(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsStreamingResponse(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsBidirectionalStreaming(), Types.BOOLEAN);
            ps.setString(i++, req.getGrpcVersion());
            ps.setString(i++, req.getAuthority());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCompression());
            ps.setObject(i++, req.getEnableCompression(), Types.BOOLEAN);
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getAccessToken());
            ps.setString(i++, req.getClientCertificate());
            ps.setString(i++, req.getClientKey());
            ps.setString(i++, req.getServerCertificate());
            ps.setObject(i++, req.getIsTls(), Types.BOOLEAN);
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.setLong(i++, req.getId());
            ps.executeUpdate();
        }
    }

    public void delete(DatabaseConnection conn, Long id) throws Exception {
        String sql = "DELETE FROM grpc_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 