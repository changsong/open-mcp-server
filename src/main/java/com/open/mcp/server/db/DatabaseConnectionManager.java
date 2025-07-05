package com.open.mcp.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import redis.clients.jedis.Jedis;
import org.elasticsearch.client.RestClient;
import org.apache.http.HttpHost;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseConnectionManager {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static Connection getJdbcConnection(DatabaseConnection conn) throws Exception {
        switch (conn.getDbType().toLowerCase()) {
            case "mysql":
                return DriverManager.getConnection(buildMySQLUrl(conn), conn.getUsername(), conn.getPassword());
            case "postgresql":
                return DriverManager.getConnection(buildPostgreSQLUrl(conn), conn.getUsername(), conn.getPassword());
            case "oracle":
                return DriverManager.getConnection(buildOracleUrl(conn), conn.getUsername(), conn.getPassword());
            case "sqlserver":
                return DriverManager.getConnection(buildSQLServerUrl(conn), conn.getUsername(), conn.getPassword());
            default:
                throw new UnsupportedOperationException("Unsupported database type: " + conn.getDbType());
        }
    }
    
    public static HikariDataSource getConnectionPool(DatabaseConnection conn) throws Exception {
        HikariConfig config = new HikariConfig();
        
        // Basic configuration
        config.setMaximumPoolSize(conn.getMaxConnections());
        config.setMinimumIdle(conn.getMinConnections());
        config.setConnectionTimeout(conn.getConnectionTimeout());
        config.setIdleTimeout(conn.getIdleTimeout());
        
        // Set driver and URL based on database type
        switch (conn.getDbType().toLowerCase()) {
            case "mysql":
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setJdbcUrl(buildMySQLUrl(conn));
                break;
            case "postgresql":
                config.setDriverClassName("org.postgresql.Driver");
                config.setJdbcUrl(buildPostgreSQLUrl(conn));
                break;
            case "oracle":
                config.setDriverClassName("oracle.jdbc.OracleDriver");
                config.setJdbcUrl(buildOracleUrl(conn));
                break;
            case "sqlserver":
                config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                config.setJdbcUrl(buildSQLServerUrl(conn));
                break;
        }
        
        // Authentication information
        config.setUsername(conn.getUsername());
        config.setPassword(conn.getPassword());
        
        // Extra parameters
        if (conn.getExtraParams() != null) {
            Properties props = objectMapper.readValue(conn.getExtraParams(), Properties.class);
            config.setDataSourceProperties(props);
        }
        
        return new HikariDataSource(config);
    }
    
    public static MongoClient getMongoClient(DatabaseConnection conn) throws Exception {
        String connectionString = buildMongoConnectionString(conn);
        return MongoClients.create(connectionString);
    }
    
    public static Jedis getRedisConnection(DatabaseConnection conn) throws Exception {
        Jedis jedis = new Jedis(conn.getHost(), conn.getPort());
        if (conn.getPassword() != null && !conn.getPassword().isEmpty()) {
            jedis.auth(conn.getPassword());
        }
        if (conn.getDatabaseName() != null && !conn.getDatabaseName().isEmpty()) {
            jedis.select(Integer.parseInt(conn.getDatabaseName()));
        }
        return jedis;
    }
    
    public static RestClient getElasticsearchClient(DatabaseConnection conn) throws Exception {
        return RestClient.builder(
            new HttpHost(conn.getHost(), conn.getPort(), "http")
        ).build();
    }
    
    // URL building methods
    private static String buildMySQLUrl(DatabaseConnection conn) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(conn.getHost()).append(":").append(conn.getPort());
        if (conn.getDatabaseName() != null) {
            url.append("/").append(conn.getDatabaseName());
        }
        url.append("?useSSL=").append(conn.getSslEnabled());
        url.append("&serverTimezone=").append(conn.getTimezone());
        url.append("&characterEncoding=").append(conn.getCharset());
        return url.toString();
    }
    
    private static String buildPostgreSQLUrl(DatabaseConnection conn) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:postgresql://").append(conn.getHost()).append(":").append(conn.getPort());
        if (conn.getDatabaseName() != null) {
            url.append("/").append(conn.getDatabaseName());
        }
        return url.toString();
    }
    
    private static String buildOracleUrl(DatabaseConnection conn) {
        return "jdbc:oracle:thin:@" + conn.getHost() + ":" + conn.getPort() + ":" + conn.getDatabaseName();
    }
    
    private static String buildSQLServerUrl(DatabaseConnection conn) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:sqlserver://").append(conn.getHost()).append(":").append(conn.getPort());
        if (conn.getDatabaseName() != null) {
            url.append(";databaseName=").append(conn.getDatabaseName());
        }
        return url.toString();
    }
    
    private static String buildMongoConnectionString(DatabaseConnection conn) {
        StringBuilder uri = new StringBuilder();
        uri.append("mongodb://");
        if (conn.getUsername() != null && conn.getPassword() != null) {
            uri.append(conn.getUsername()).append(":").append(conn.getPassword()).append("@");
        }
        uri.append(conn.getHost()).append(":").append(conn.getPort());
        if (conn.getDatabaseName() != null) {
            uri.append("/").append(conn.getDatabaseName());
        }
        return uri.toString();
    }
}

