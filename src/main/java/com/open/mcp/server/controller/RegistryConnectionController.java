package com.open.mcp.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.mcp.server.entity.RegistryConnection;
import com.open.mcp.server.service.RegistryConnectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/registry")
@RequiredArgsConstructor
public class RegistryConnectionController {

    @Autowired
    private RegistryConnectionService registryConnectionService;

    @GetMapping("/{id}")
    public ResponseEntity<RegistryConnection> getServiceById(@PathVariable Long id) {
        log.info("Get registry connection info by ID: {}", id);
        try {
            RegistryConnection service = registryConnectionService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Exception occurred while getting registry connection info", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<RegistryConnection>> getAllServices() {
        log.info("Get all registry connection info");
        try {
            List<RegistryConnection> services = registryConnectionService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Exception occurred while getting all registry connection info", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<RegistryConnection> saveService(@RequestBody RegistryConnection registryConnection) {
        log.info("Create registry connection info: {}", registryConnection);
        try {
            RegistryConnection savedService = registryConnectionService.saveService(registryConnection);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            log.error("Exception occurred while creating registry connection info", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistryConnection> updateService(@PathVariable Long id, @RequestBody RegistryConnection registryConnection) {
        log.info("Update registry connection info, id: {}, data: {}", id, registryConnection);
        try {
            RegistryConnection existingService = registryConnectionService.getServiceById(id);
            if (existingService == null) {
                return ResponseEntity.notFound().build();
            }
            registryConnection.setId(id);
            RegistryConnection updatedService = registryConnectionService.updateService(registryConnection);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Exception occurred while updating registry connection info", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Delete registry connection info, id: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = registryConnectionService.deleteService(id);
            if (deleted) {
                result.put("success", true);
                result.put("message", "Delete success");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "Registry connection info not found");
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting registry connection info", e);
            result.put("success", false);
            result.put("message", "Exception occurred while deleting registry connection info");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> pageQuery(
            @RequestParam(required = false) String connectionName,
            @RequestParam(required = false) String registryType,
            @RequestParam(required = false) String host,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<RegistryConnection> list = registryConnectionService.pageQuery(connectionName, registryType, host, pageNum, pageSize);
            int total = registryConnectionService.countQuery(connectionName, registryType, host);
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("list", list);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Exception occurred while paging registry connection info", e);
            return ResponseEntity.internalServerError().body(Collections.singletonMap("message", "Paging query exception"));
        }
    }

    @PostMapping("/batch-save")
    public ResponseEntity<Map<String, Object>> batchSave(@RequestBody List<RegistryConnection> list) {
        try {
            registryConnectionService.batchInsert(list);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("Exception occurred while batch saving registry connection info", e);
            return ResponseEntity.internalServerError().body(Collections.singletonMap("success", false));
        }
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestBody List<Long> ids) {
        try {
            registryConnectionService.batchDelete(ids);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("Exception occurred while batch deleting registry connection info", e);
            return ResponseEntity.internalServerError().body(Collections.singletonMap("success", false));
        }
    }
} 