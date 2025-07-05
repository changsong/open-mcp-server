package com.open.mcp.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.open.mcp.server.entity.GrpcRequest;
import com.open.mcp.server.service.GrpcRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/grpc")
@RequiredArgsConstructor
public class GrpcRequestController {

    private final GrpcRequestService grpcRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<GrpcRequest> getServiceById(@PathVariable Long id) {
        log.info("Getting Grpc service interface information by ID: {}", id);
        try {
            GrpcRequest service = grpcRequestService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Exception occurred while getting Grpc service interface information by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<GrpcRequest>> getAllServices() {
        log.info("Getting all Grpc service interface information");
        try {
            List<GrpcRequest> services = grpcRequestService.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Exception occurred while getting all Grpc service interface information", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<GrpcRequest> saveService(@RequestBody GrpcRequest grpcRequest) {
        log.info("Creating Grpc service interface information: {}", grpcRequest);
        try {
            GrpcRequest savedService = grpcRequestService.saveService(grpcRequest);
            return ResponseEntity.ok(savedService);
        } catch (Exception e) {
            log.error("Exception occurred while creating Grpc service interface information: {}", grpcRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrpcRequest> updateService(@PathVariable Long id, @RequestBody GrpcRequest grpcRequest) {
        log.info("Updating Grpc service interface information, id: {}, data: {}", id, grpcRequest);
        try {
            GrpcRequest existingService = grpcRequestService.getServiceById(id);
            if (existingService == null) {
                return ResponseEntity.notFound().build();
            }
            grpcRequest.setId(id);
            GrpcRequest updatedService = grpcRequestService.updateService(grpcRequest);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Exception occurred while updating Grpc service interface information: {}", grpcRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Deleting Grpc service interface information, id: {}", id);
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = grpcRequestService.deleteService(id);
            if (deleted) {
                result.put("success", true);
                result.put("message", "Delete successful");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "Grpc service interface information not found");
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting Grpc service interface information, id: {}", id, e);
            result.put("success", false);
            result.put("message", "Exception occurred while deleting Grpc service interface information");
            return ResponseEntity.internalServerError().body(result);
        }
    }
} 