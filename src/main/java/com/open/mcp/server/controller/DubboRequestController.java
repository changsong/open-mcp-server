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

import com.open.mcp.server.entity.DubboRequest;
import com.open.mcp.server.service.DubboRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Dubbo service interface controller
 */
@Slf4j
@RestController
@RequestMapping("/api/dubbo")
@RequiredArgsConstructor
public class DubboRequestController {

    @Autowired
    private DubboRequestService dubboRequestService;

    /**
     * Get Dubbo service interface information by ID
     * @param id Dubbo service interface ID
     * @return Dubbo service interface information
     */
    @GetMapping("/{id}")
    public ResponseEntity<DubboRequest> getServiceById(@PathVariable Long id) {
        log.info("Getting Dubbo service interface information by ID: {}", id);
        try {
            DubboRequest service = dubboRequestService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Exception occurred while getting Dubbo service interface information by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all Dubbo service interface information
     * @return List of Dubbo service interface information
     */
    @GetMapping("/list")
    public ResponseEntity<List<DubboRequest>> getAllServices() {
        log.info("Getting all Dubbo service interface information");
        try {
            List<DubboRequest> services = dubboRequestService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Exception occurred while getting all Dubbo service interface information", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Create Dubbo service interface information
     * @param DubboRequest Dubbo service interface information
     * @return Created Dubbo service interface information
     */
    @PostMapping("/save")
    public ResponseEntity<DubboRequest> saveService(@RequestBody DubboRequest DubboRequest) {
        log.info("Creating Dubbo service interface information: {}", DubboRequest);
        try {
            DubboRequest savedService = dubboRequestService.saveService(DubboRequest);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            log.error("Exception occurred while creating Dubbo service interface information: {}", DubboRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
     * Update Dubbo service interface information
     * @param id Dubbo service interface ID
     * @param DubboRequest Dubbo service interface information
     * @return Updated Dubbo service interface information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DubboRequest> updateService(@PathVariable Long id, @RequestBody DubboRequest DubboRequest) {
        log.info("Updating Dubbo service interface information, id: {}, data: {}", id, DubboRequest);
        try {
            DubboRequest existingService = dubboRequestService.getServiceById(id);
            if (existingService == null) {
                return ResponseEntity.notFound().build();
            }
            DubboRequest.setId(id);
            DubboRequest updatedService = dubboRequestService.updateService(DubboRequest);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Exception occurred while updating Dubbo service interface information: {}", DubboRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete Dubbo service interface information
     * @param id Dubbo service interface ID
     * @return Result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Deleting Dubbo service interface information, id: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = dubboRequestService.deleteService(id);
            if (deleted) {
                result.put("success", true);
                result.put("message", "Delete successful");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "Dubbo service interface information not found");
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting Dubbo service interface information, id: {}", id, e);
            result.put("success", false);
            result.put("message", "Exception occurred while deleting Dubbo service interface information");
            return ResponseEntity.internalServerError().body(result);
        }
    }
} 