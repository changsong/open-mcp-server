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
import org.springframework.web.bind.annotation.RestController;

import com.open.mcp.server.entity.HttpRequest;
import com.open.mcp.server.service.HttpRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/http")
@RequiredArgsConstructor
public class HttpRequestController {

    @Autowired
    private HttpRequestService httpRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<HttpRequest> getServiceById(@PathVariable Long id) {
        log.info("Getting Http service interface information by ID: {}", id);
        try {
            HttpRequest service = httpRequestService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Exception occurred while getting Http service interface information by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<HttpRequest>> getAllServices() {
        log.info("Getting all Http service interface information");
        try {
            List<HttpRequest> services = httpRequestService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Exception occurred while getting all Http service interface information", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<HttpRequest> saveService(@RequestBody HttpRequest httpRequest) {
        log.info("Creating Http service interface information: {}", httpRequest);
        try {
            HttpRequest savedService = httpRequestService.saveService(httpRequest);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            log.error("Exception occurred while creating Http service interface information: {}", httpRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpRequest> updateService(@PathVariable Long id, @RequestBody HttpRequest httpRequest) {
        log.info("Updating Http service interface information, id: {}, data: {}", id, httpRequest);
        try {
            HttpRequest existingService = httpRequestService.getServiceById(id);
            if (existingService == null) {
                return ResponseEntity.notFound().build();
            }
            httpRequest.setId(id);
            HttpRequest updatedService = httpRequestService.updateService(httpRequest);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Exception occurred while updating Http service interface information: {}", httpRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Deleting Http service interface information, id: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = httpRequestService.deleteService(id);
            if (deleted) {
                result.put("success", true);
                result.put("message", "Delete successful");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "Http service interface information not found");
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting Http service interface information, id: {}", id, e);
            result.put("success", false);
            result.put("message", "Exception occurred while deleting Http service interface information");
            return ResponseEntity.internalServerError().body(result);
        }
    }
} 