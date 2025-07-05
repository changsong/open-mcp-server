package com.open.mcp.server.service;

import java.util.List;
import com.open.mcp.server.db.DatabaseConnection;
import com.open.mcp.server.entity.RegistryConnection;

public interface RegistryConnectionService {
    void insert(DatabaseConnection conn, RegistryConnection req) throws Exception;

    RegistryConnection selectById(DatabaseConnection conn, Long id) throws Exception;

    void update(DatabaseConnection conn, RegistryConnection req) throws Exception;

    void delete(DatabaseConnection conn, Long id) throws Exception;

    List<RegistryConnection> getAllServices() throws Exception;

    RegistryConnection getServiceById(Long id) throws Exception;

    RegistryConnection saveService(RegistryConnection registryConnection) throws Exception;

    RegistryConnection updateService(RegistryConnection registryConnection) throws Exception;

    boolean deleteService(Long id) throws Exception;

    List<RegistryConnection> pageQuery(String connectionName, String registryType, String host, int pageNum, int pageSize) throws Exception;

    int countQuery(String connectionName, String registryType, String host) throws Exception;

    void batchInsert(List<RegistryConnection> list) throws Exception;

    void batchDelete(List<Long> ids) throws Exception;

    /**
     * 从数据库查询注册中心连接信息
     * @return 注册中心类型
     */
    String getRegistryType();
    
    /**
     * 从数据库查询注册中心地址
     * @return 注册中心地址
     */
    String getRegistryAddress();
} 