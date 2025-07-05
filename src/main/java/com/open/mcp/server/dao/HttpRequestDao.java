package com.open.mcp.server.dao;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.db.DatabaseConnectionManager;
import com.open.mcp.server.entity.HttpRequest;
import java.sql.*;

public class HttpRequestDao {
    public void insert(DatabaseConnection conn, HttpRequest req) throws Exception {
        String sql = "INSERT INTO http_request (method, url, query_params, headers, body, content_type, cookies, http_version, timeout, expires_at, created_at, updated_at, description, status_code, status_message, response_headers, response_body, authentication_type, client_ip, server_ip, is_https, attachments, referer, user_agent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            ps.setString(i++, req.getMethod());
            ps.setString(i++, req.getUrl());
            ps.setString(i++, req.getQueryParams());
            ps.setString(i++, req.getHeaders());
            ps.setString(i++, req.getBody());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCookies());
            ps.setString(i++, req.getHttpVersion());
            ps.setInt(i++, req.getTimeout());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
            ps.setString(i++, req.getResponseHeaders());
            ps.setString(i++, req.getResponseBody());
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getClientIp());
            ps.setString(i++, req.getServerIp());
            ps.setObject(i++, req.getIsHttps(), Types.BOOLEAN);
            ps.setString(i++, req.getAttachments());
            ps.setString(i++, req.getReferer());
            ps.setString(i++, req.getUserAgent());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    req.setId(rs.getLong(1));
                }
            }
        }
    }

    public HttpRequest selectById(DatabaseConnection conn, Long id) throws Exception {
        String sql = "SELECT * FROM http_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpRequest req = new HttpRequest();
                    req.setId(rs.getLong("id"));
                    req.setMethod(rs.getString("method"));
                    req.setUrl(rs.getString("url"));
                    req.setQueryParams(rs.getString("query_params"));
                    req.setHeaders(rs.getString("headers"));
                    req.setBody(rs.getString("body"));
                    req.setContentType(rs.getString("content_type"));
                    req.setCookies(rs.getString("cookies"));
                    req.setHttpVersion(rs.getString("http_version"));
                    req.setTimeout(rs.getInt("timeout"));
                    req.setExpiresAt(rs.getTimestamp("expires_at"));
                    req.setCreatedAt(rs.getTimestamp("created_at"));
                    req.setUpdatedAt(rs.getTimestamp("updated_at"));
                    req.setDescription(rs.getString("description"));
                    req.setStatusCode(rs.getObject("status_code") == null ? null : rs.getInt("status_code"));
                    req.setStatusMessage(rs.getString("status_message"));
                    req.setResponseHeaders(rs.getString("response_headers"));
                    req.setResponseBody(rs.getString("response_body"));
                    req.setAuthenticationType(rs.getString("authentication_type"));
                    req.setClientIp(rs.getString("client_ip"));
                    req.setServerIp(rs.getString("server_ip"));
                    req.setIsHttps(rs.getObject("is_https") == null ? null : rs.getBoolean("is_https"));
                    req.setAttachments(rs.getString("attachments"));
                    req.setReferer(rs.getString("referer"));
                    req.setUserAgent(rs.getString("user_agent"));
                    return req;
                }
            }
        }
        return null;
    }

    public void update(DatabaseConnection conn, HttpRequest req) throws Exception {
        String sql = "UPDATE http_request SET method=?, url=?, query_params=?, headers=?, body=?, content_type=?, cookies=?, http_version=?, timeout=?, expires_at=?, created_at=?, updated_at=?, description=?, status_code=?, status_message=?, response_headers=?, response_body=?, authentication_type=?, client_ip=?, server_ip=?, is_https=?, attachments=?, referer=?, user_agent=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, req.getMethod());
            ps.setString(i++, req.getUrl());
            ps.setString(i++, req.getQueryParams());
            ps.setString(i++, req.getHeaders());
            ps.setString(i++, req.getBody());
            ps.setString(i++, req.getContentType());
            ps.setString(i++, req.getCookies());
            ps.setString(i++, req.getHttpVersion());
            ps.setInt(i++, req.getTimeout());
            ps.setTimestamp(i++, req.getExpiresAt() == null ? null : new Timestamp(req.getExpiresAt().getTime()));
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getDescription());
            ps.setObject(i++, req.getStatusCode(), Types.INTEGER);
            ps.setString(i++, req.getStatusMessage());
            ps.setString(i++, req.getResponseHeaders());
            ps.setString(i++, req.getResponseBody());
            ps.setString(i++, req.getAuthenticationType());
            ps.setString(i++, req.getClientIp());
            ps.setString(i++, req.getServerIp());
            ps.setObject(i++, req.getIsHttps(), Types.BOOLEAN);
            ps.setString(i++, req.getAttachments());
            ps.setString(i++, req.getReferer());
            ps.setString(i++, req.getUserAgent());
            ps.setLong(i++, req.getId());
            ps.executeUpdate();
        }
    }

    public void delete(DatabaseConnection conn, Long id) throws Exception {
        String sql = "DELETE FROM http_request WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
} 