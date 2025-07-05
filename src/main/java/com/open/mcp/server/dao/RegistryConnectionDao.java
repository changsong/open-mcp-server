package com.open.mcp.server.dao;

import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.db.DatabaseConnectionManager;
import com.open.mcp.server.entity.RegistryConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistryConnectionDao {
    public void insert(DatabaseConnection conn, RegistryConnection req) throws Exception {
        String sql = "INSERT INTO registry_connection (connection_name, registry_type, host, port, scheme, cluster_nodes, namespace, group_name, username, password, token, access_key, secret_key, connection_timeout, session_timeout, retry_count, retry_interval, health_check_enabled, health_check_interval, health_check_timeout, ssl_enabled, ssl_certificate, ssl_key, ssl_ca, ssl_verify_host, zk_root_path, zk_client_type, consul_datacenter, consul_acl_token, etcd_prefix, etcd_lease_ttl, nacos_config_type, nacos_tenant, extra_params, is_active, is_default, priority, created_at, updated_at, created_by, updated_by, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            ps.setString(i++, req.getConnectionName());
            ps.setString(i++, req.getRegistryType());
            ps.setString(i++, req.getHost());
            ps.setInt(i++, req.getPort());
            ps.setString(i++, req.getScheme());
            ps.setString(i++, req.getClusterNodes());
            ps.setString(i++, req.getNamespace());
            ps.setString(i++, req.getGroupName());
            ps.setString(i++, req.getUsername());
            ps.setString(i++, req.getPassword());
            ps.setString(i++, req.getToken());
            ps.setString(i++, req.getAccessKey());
            ps.setString(i++, req.getSecretKey());
            ps.setObject(i++, req.getConnectionTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getSessionTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getRetryCount(), Types.INTEGER);
            ps.setObject(i++, req.getRetryInterval(), Types.INTEGER);
            ps.setObject(i++, req.getHealthCheckEnabled(), Types.BOOLEAN);
            ps.setObject(i++, req.getHealthCheckInterval(), Types.INTEGER);
            ps.setObject(i++, req.getHealthCheckTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getSslEnabled(), Types.BOOLEAN);
            ps.setString(i++, req.getSslCertificate());
            ps.setString(i++, req.getSslKey());
            ps.setString(i++, req.getSslCa());
            ps.setObject(i++, req.getSslVerifyHost(), Types.BOOLEAN);
            ps.setString(i++, req.getZkRootPath());
            ps.setString(i++, req.getZkClientType());
            ps.setString(i++, req.getConsulDatacenter());
            ps.setString(i++, req.getConsulAclToken());
            ps.setString(i++, req.getEtcdPrefix());
            ps.setObject(i++, req.getEtcdLeaseTtl(), Types.INTEGER);
            ps.setString(i++, req.getNacosConfigType());
            ps.setString(i++, req.getNacosTenant());
            ps.setString(i++, req.getExtraParams());
            ps.setObject(i++, req.getIsActive(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsDefault(), Types.BOOLEAN);
            ps.setObject(i++, req.getPriority(), Types.INTEGER);
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getCreatedBy());
            ps.setString(i++, req.getUpdatedBy());
            ps.setString(i++, req.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    req.setId(rs.getLong(1));
                }
            }
        }
    }

    public RegistryConnection selectById(DatabaseConnection conn, Long id) throws Exception {
        String sql = "SELECT * FROM registry_connection WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RegistryConnection req = new RegistryConnection();
                    req.setId(rs.getLong("id"));
                    req.setConnectionName(rs.getString("connection_name"));
                    req.setRegistryType(rs.getString("registry_type"));
                    req.setHost(rs.getString("host"));
                    req.setPort(rs.getInt("port"));
                    req.setScheme(rs.getString("scheme"));
                    req.setClusterNodes(rs.getString("cluster_nodes"));
                    req.setNamespace(rs.getString("namespace"));
                    req.setGroupName(rs.getString("group_name"));
                    req.setUsername(rs.getString("username"));
                    req.setPassword(rs.getString("password"));
                    req.setToken(rs.getString("token"));
                    req.setAccessKey(rs.getString("access_key"));
                    req.setSecretKey(rs.getString("secret_key"));
                    req.setConnectionTimeout((Integer)rs.getObject("connection_timeout"));
                    req.setSessionTimeout((Integer)rs.getObject("session_timeout"));
                    req.setRetryCount((Integer)rs.getObject("retry_count"));
                    req.setRetryInterval((Integer)rs.getObject("retry_interval"));
                    req.setHealthCheckEnabled((Boolean)rs.getObject("health_check_enabled"));
                    req.setHealthCheckInterval((Integer)rs.getObject("health_check_interval"));
                    req.setHealthCheckTimeout((Integer)rs.getObject("health_check_timeout"));
                    req.setSslEnabled((Boolean)rs.getObject("ssl_enabled"));
                    req.setSslCertificate(rs.getString("ssl_certificate"));
                    req.setSslKey(rs.getString("ssl_key"));
                    req.setSslCa(rs.getString("ssl_ca"));
                    req.setSslVerifyHost((Boolean)rs.getObject("ssl_verify_host"));
                    req.setZkRootPath(rs.getString("zk_root_path"));
                    req.setZkClientType(rs.getString("zk_client_type"));
                    req.setConsulDatacenter(rs.getString("consul_datacenter"));
                    req.setConsulAclToken(rs.getString("consul_acl_token"));
                    req.setEtcdPrefix(rs.getString("etcd_prefix"));
                    req.setEtcdLeaseTtl((Integer)rs.getObject("etcd_lease_ttl"));
                    req.setNacosConfigType(rs.getString("nacos_config_type"));
                    req.setNacosTenant(rs.getString("nacos_tenant"));
                    req.setExtraParams(rs.getString("extra_params"));
                    req.setIsActive((Boolean)rs.getObject("is_active"));
                    req.setIsDefault((Boolean)rs.getObject("is_default"));
                    req.setPriority((Integer)rs.getObject("priority"));
                    req.setCreatedAt(rs.getTimestamp("created_at"));
                    req.setUpdatedAt(rs.getTimestamp("updated_at"));
                    req.setCreatedBy(rs.getString("created_by"));
                    req.setUpdatedBy(rs.getString("updated_by"));
                    req.setDescription(rs.getString("description"));
                    return req;
                }
            }
        }
        return null;
    }

    public void update(DatabaseConnection conn, RegistryConnection req) throws Exception {
        String sql = "UPDATE registry_connection SET connection_name=?, registry_type=?, host=?, port=?, scheme=?, cluster_nodes=?, namespace=?, group_name=?, username=?, password=?, token=?, access_key=?, secret_key=?, connection_timeout=?, session_timeout=?, retry_count=?, retry_interval=?, health_check_enabled=?, health_check_interval=?, health_check_timeout=?, ssl_enabled=?, ssl_certificate=?, ssl_key=?, ssl_ca=?, ssl_verify_host=?, zk_root_path=?, zk_client_type=?, consul_datacenter=?, consul_acl_token=?, etcd_prefix=?, etcd_lease_ttl=?, nacos_config_type=?, nacos_tenant=?, extra_params=?, is_active=?, is_default=?, priority=?, created_at=?, updated_at=?, created_by=?, updated_by=?, description=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, req.getConnectionName());
            ps.setString(i++, req.getRegistryType());
            ps.setString(i++, req.getHost());
            ps.setInt(i++, req.getPort());
            ps.setString(i++, req.getScheme());
            ps.setString(i++, req.getClusterNodes());
            ps.setString(i++, req.getNamespace());
            ps.setString(i++, req.getGroupName());
            ps.setString(i++, req.getUsername());
            ps.setString(i++, req.getPassword());
            ps.setString(i++, req.getToken());
            ps.setString(i++, req.getAccessKey());
            ps.setString(i++, req.getSecretKey());
            ps.setObject(i++, req.getConnectionTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getSessionTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getRetryCount(), Types.INTEGER);
            ps.setObject(i++, req.getRetryInterval(), Types.INTEGER);
            ps.setObject(i++, req.getHealthCheckEnabled(), Types.BOOLEAN);
            ps.setObject(i++, req.getHealthCheckInterval(), Types.INTEGER);
            ps.setObject(i++, req.getHealthCheckTimeout(), Types.INTEGER);
            ps.setObject(i++, req.getSslEnabled(), Types.BOOLEAN);
            ps.setString(i++, req.getSslCertificate());
            ps.setString(i++, req.getSslKey());
            ps.setString(i++, req.getSslCa());
            ps.setObject(i++, req.getSslVerifyHost(), Types.BOOLEAN);
            ps.setString(i++, req.getZkRootPath());
            ps.setString(i++, req.getZkClientType());
            ps.setString(i++, req.getConsulDatacenter());
            ps.setString(i++, req.getConsulAclToken());
            ps.setString(i++, req.getEtcdPrefix());
            ps.setObject(i++, req.getEtcdLeaseTtl(), Types.INTEGER);
            ps.setString(i++, req.getNacosConfigType());
            ps.setString(i++, req.getNacosTenant());
            ps.setString(i++, req.getExtraParams());
            ps.setObject(i++, req.getIsActive(), Types.BOOLEAN);
            ps.setObject(i++, req.getIsDefault(), Types.BOOLEAN);
            ps.setObject(i++, req.getPriority(), Types.INTEGER);
            ps.setTimestamp(i++, req.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getCreatedAt().getTime()));
            ps.setTimestamp(i++, req.getUpdatedAt() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(req.getUpdatedAt().getTime()));
            ps.setString(i++, req.getCreatedBy());
            ps.setString(i++, req.getUpdatedBy());
            ps.setString(i++, req.getDescription());
            ps.setLong(i++, req.getId());
            ps.executeUpdate();
        }
    }

    public void delete(DatabaseConnection conn, Long id) throws Exception {
        String sql = "DELETE FROM registry_connection WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(conn);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<RegistryConnection> pageQuery(String connectionName, String registryType, String host, int offset, int limit) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM registry_connection WHERE 1=1");
        if (connectionName != null && !connectionName.isEmpty()) {
            sql.append(" AND connection_name LIKE ?");
        }
        if (registryType != null && !registryType.isEmpty()) {
            sql.append(" AND registry_type LIKE ?");
        }
        if (host != null && !host.isEmpty()) {
            sql.append(" AND host LIKE ?");
        }
        sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(null);
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            if (connectionName != null && !connectionName.isEmpty()) {
                ps.setString(idx++, "%" + connectionName + "%");
            }
            if (registryType != null && !registryType.isEmpty()) {
                ps.setString(idx++, "%" + registryType + "%");
            }
            if (host != null && !host.isEmpty()) {
                ps.setString(idx++, "%" + host + "%");
            }
            ps.setInt(idx++, limit);
            ps.setInt(idx++, offset);
            List<RegistryConnection> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RegistryConnection req = selectById(null, rs.getLong("id"));
                    if (req != null) list.add(req);
                }
            }
            return list;
        }
    }

    public int countQuery(String connectionName, String registryType, String host) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM registry_connection WHERE 1=1");
        if (connectionName != null && !connectionName.isEmpty()) {
            sql.append(" AND connection_name LIKE ?");
        }
        if (registryType != null && !registryType.isEmpty()) {
            sql.append(" AND registry_type LIKE ?");
        }
        if (host != null && !host.isEmpty()) {
            sql.append(" AND host LIKE ?");
        }
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(null);
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            if (connectionName != null && !connectionName.isEmpty()) {
                ps.setString(idx++, "%" + connectionName + "%");
            }
            if (registryType != null && !registryType.isEmpty()) {
                ps.setString(idx++, "%" + registryType + "%");
            }
            if (host != null && !host.isEmpty()) {
                ps.setString(idx++, "%" + host + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void batchInsert(List<RegistryConnection> list) throws Exception {
        if (list == null || list.isEmpty()) return;
        for (RegistryConnection req : list) {
            insert(null, req);
        }
    }

    public void batchDelete(List<Long> ids) throws Exception {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM registry_connection WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i < ids.size() - 1) sql.append(",");
        }
        sql.append(")");
        try (Connection c = DatabaseConnectionManager.getJdbcConnection(null);
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setLong(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        }
    }
} 