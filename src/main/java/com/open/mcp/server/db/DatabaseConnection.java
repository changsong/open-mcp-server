package com.open.mcp.server.db;

import lombok.Data;

// Database connection entity class
@Data
public class DatabaseConnection {
    private Long id;
    private String connectionName;
    private String dbType;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private String connectionString;
    private Integer maxConnections;
    private Integer minConnections;
    private Integer connectionTimeout;
    private Integer idleTimeout;
    private String charset;
    private String timezone;
    private Boolean sslEnabled;
    private String extraParams;
    private Boolean isActive;
}
