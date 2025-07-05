package com.open.mcp.server.codegenerator.controller;

import com.open.mcp.server.codegenerator.dto.CodeGenerateRequest;
import com.open.mcp.server.codegenerator.dto.CodeGenerateResponse;
import com.open.mcp.server.codegenerator.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CodeGeneratorController {

    private final CodeGeneratorService codeGeneratorService;

    @PostMapping("/generate")
    public CodeGenerateResponse generateCode(@RequestBody CodeGenerateRequest request) {
        return codeGeneratorService.generateCode(request);
    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadCode(@RequestBody CodeGenerateRequest request) {
        try {
            Resource resource = codeGeneratorService.generateZipFile(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.getApiName() + ".zip\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(CodeGenerateResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
} 