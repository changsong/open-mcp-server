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

import com.open.mcp.server.entity.SoapRequest;
import com.open.mcp.server.service.SoapRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/soap")
@RequiredArgsConstructor
public class SoapRequestController {

    @Autowired
    private SoapRequestService soapRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<SoapRequest> getServiceById(@PathVariable Long id) {
        log.info("Getting Soap service interface information by ID: {}", id);
        try {
            SoapRequest service = soapRequestService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Exception occurred while getting Soap service interface information by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<SoapRequest>> getAllServices() {
        log.info("Getting all Soap service interface information");
        try {
            List<SoapRequest> services = soapRequestService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Exception occurred while getting all Soap service interface information", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<SoapRequest> saveService(@RequestBody SoapRequest soapRequest) {
        log.info("Creating Soap service interface information: {}", soapRequest);
        try {
            SoapRequest savedService = soapRequestService.saveService(soapRequest);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            log.error("Exception occurred while creating Soap service interface information: {}", soapRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoapRequest> updateService(@PathVariable Long id, @RequestBody SoapRequest soapRequest) {
        log.info("Updating Soap service interface information, id: {}, data: {}", id, soapRequest);
        try {
            SoapRequest existingService = soapRequestService.getServiceById(id);
            if (existingService == null) {
                return ResponseEntity.notFound().build();
            }
            soapRequest.setId(id);
            SoapRequest updatedService = soapRequestService.updateService(soapRequest);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Exception occurred while updating Soap service interface information: {}", soapRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Deleting Soap service interface information, id: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = soapRequestService.deleteService(id);
            if (deleted) {
                result.put("success", true);
                result.put("message", "Delete successful");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "Soap service interface information not found");
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting Soap service interface information, id: {}", id, e);
            result.put("success", false);
            result.put("message", "Exception occurred while deleting Soap service interface information");
            return ResponseEntity.internalServerError().body(result);
        }
    }
} 