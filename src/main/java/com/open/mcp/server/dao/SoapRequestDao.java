package com.open.mcp.server.dao;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.db.DatabaseConnectionManager;
import com.open.mcp.server.entity.SoapRequest;
import java.sql.*;

public class SoapRequestDao {
    public void insert(DatabaseConnection conn, SoapRequest req) throws Exception {
        String sql = "INSERT INTO soap_request (endpoint_url, soap_action, method_name, namespace, request_headers, soap_headers, request_body, parameters, soap_version, content_type, charset, timeout, expires_at, status_code, status_message, ws_security, authentication_type, username, password, client_certificate, client_key, attachments, is_encrypted, is_signed, created_at, updated_at, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            ps.setString(i++, req.getEndpointUrl());
            ps.setString(i++, req.getSoapAction());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getNamespace());
            ps.setString(i++, req.getRequestHeaders());
            ps.setString(i++, req.getSoapHeaders());
            ps.setString(i++, req.getRequestBody());
            ps.setString(i++, req.getParameters());
            ps.setString(i++, req.getSoapVersion());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCharset());
            ps.setInt(i++, req.getTimeout());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
            ps.setString(i++, req.getWsSecurity());
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getUsername());
            ps.setString(i++, req.getPassword());
            ps.setString(i++, req.getClientCertificate());
            ps.setString(i++, req.getClientKey());
            ps.setString(i++, req.getAttachments());
            ps.setObject(i++, req.getIsEncrypted(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsSigned(), Types.BOOLEAN);
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

    public SoapRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        String sql = "SELECT * FROM soap_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SoapRequest req = new SoapRequest();
                    req.setId(rs.getLong("id"));
                    req.setEndpointUrl(rs.getString("endpoint_url"));
                    req.setSoapAction(rs.getString("soap_action"));
                    req.setMethodName(rs.getString("method_name"));
                    req.setNamespace(rs.getString("namespace"));
                    req.setRequestHeaders(rs.getString("request_headers"));
                    req.setSoapHeaders(rs.getString("soap_headers"));
                    req.setRequestBody(rs.getString("request_body"));
                    req.setParameters(rs.getString("parameters"));
                    req.setSoapVersion(rs.getString("soap_version"));
                    req.setContentType(rs.getString("content_type"));
                    req.setCharset(rs.getString("charset"));
                    req.setTimeout(rs.getInt("timeout"));
                    req.setExpiresAt(rs.getTimestamp("expires_at"));
                    req.setStatusCode(rs.getObject("status_code") == null ? null : rs.getInt("status_code"));
                    req.setStatusMessage(rs.getString("status_message"));
                    req.setWsSecurity(rs.getString("ws_security"));
                    req.setAuthenticationType(rs.getString("authentication_type"));
                    req.setUsername(rs.getString("username"));
                    req.setPassword(rs.getString("password"));
                    req.setClientCertificate(rs.getString("client_certificate"));
                    req.setClientKey(rs.getString("client_key"));
                    req.setAttachments(rs.getString("attachments"));
                    req.setIsEncrypted(rs.getObject("is_encrypted") == null ? null : rs.getBoolean("is_encrypted"));
                    req.setIsSigned(rs.getObject("is_signed") == null ? null : rs.getBoolean("is_signed"));
                    req.setCreatedAt(rs.getTimestamp("created_at"));
                    req.setUpdatedAt(rs.getTimestamp("updated_at"));
                    req.setDescription(rs.getString("description"));
                    return req;
                }
            }
        }
        return null;
    }

    public void update(DatabaseConnection conn, SoapRequest req) throws Exception {
        String sql = "UPDATE soap_request SET endpoint_url=?, soap_action=?, method_name=?, namespace=?, request_headers=?, soap_headers=?, request_body=?, parameters=?, soap_version=?, content_type=?, charset=?, timeout=?, expires_at=?, status_code=?, status_message=?, ws_security=?, authentication_type=?, username=?, password=?, client_certificate=?, client_key=?, attachments=?, is_encrypted=?, is_signed=?, created_at=?, updated_at=?, description=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, req.getEndpointUrl());
            ps.setString(i++, req.getSoapAction());
            ps.setString(i++, req.getMethodName());
            ps.setString(i++, req.getNamespace());
            ps.setString(i++, req.getRequestHeaders());
            ps.setString(i++, req.getSoapHeaders());
            ps.setString(i++, req.getRequestBody());
            ps.setString(i++, req.getParameters());
            ps.setString(i++, req.getSoapVersion());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCharset());
            ps.setInt(i++, req.getTimeout());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
            ps.setString(i++, req.getWsSecurity());
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getUsername());
            ps.setString(i++, req.getPassword());
            ps.setString(i++, req.getClientCertificate());
            ps.setString(i++, req.getClientKey());
            ps.setString(i++, req.getAttachments());
            ps.setObject(i++, req.getIsEncrypted(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsSigned(), Types.BOOLEAN);
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.setLong(i++, req.getId());
            ps.executeUpdate();
        }
    }

    public void delete(DatabaseConnection conn, Long id) throws Exception {
        String sql = "DELETE FROM soap_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 