package com.open.mcp.server.codegenerator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.mcp.server.codegenerator.dto.CodeGenerateRequest;
import com.open.mcp.server.codegenerator.dto.CodeGenerateResponse;
import com.open.mcp.server.util.RequestDataParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class CodeGeneratorService {
    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorService.class);

    private final MessageSource messageSource;
    private final RequestDataParser requestDataParser;
    private final ObjectMapper objectMapper;

    public CodeGenerateResponse generateCode(CodeGenerateRequest request) {
        try {
            String apiInterface = generateApiInterface(request);
            String requestDto = generateRequestDto(request);
            String responseDto = generateResponseDto(request);
            String serviceImpl = generateServiceImpl(request);

            return CodeGenerateResponse.builder()
                    .code(apiInterface + "\n\n" + requestDto + "\n\n" + responseDto + "\n\n" + serviceImpl)
                    .success(true)
                    .build();
        } catch (Exception e) {
            String logMessage = messageSource.getMessage("log.generate.code.failed", null, LocaleContextHolder.getLocale());
            log.error(logMessage, e);
            String errorMessage = messageSource.getMessage("error.generate.failed", 
                new Object[]{e.getMessage()}, LocaleContextHolder.getLocale());
            return CodeGenerateResponse.builder()
                    .message(errorMessage)
                    .success(false)
                    .build();
        }
    }

    public Resource generateZipFile(CodeGenerateRequest request) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            // 将包路径转换为完整的目录结构
            String basePath = "src/main/java/" + request.getPackagePath().replace('.', '/') + '/';
            
            // 添加API接口文件
            addToZip(zos, basePath + request.getApiName() + "Api.java", generateApiInterface(request));
            
            // 添加DTO文件
            addToZip(zos, basePath + "dto/" + request.getApiName() + "Request.java", generateRequestDto(request));
            addToZip(zos, basePath + "dto/" + request.getApiName() + "Response.java", generateResponseDto(request));
            
            // 添加Service实现文件
            addToZip(zos, basePath + "service/" + request.getApiName() + "Service.java", generateServiceImpl(request));

            zos.close();
            return new ByteArrayResource(baos.toByteArray());
        } catch (IOException e) {
            String logMessage = messageSource.getMessage("log.generate.zip.failed", null, LocaleContextHolder.getLocale());
            log.error(logMessage, e);
            String errorMessage = messageSource.getMessage("error.download.failed", 
                new Object[]{e.getMessage()}, LocaleContextHolder.getLocale());
            throw new RuntimeException(errorMessage, e);
        }
    }

    private String generateApiInterface(CodeGenerateRequest request) {
        if (request == null) return "";
        
        StringBuilder code = new StringBuilder();
        code.append("package ").append(request.getPackagePath()).append(";\n\n");
        code.append("import retrofit2.Call;\n");
        code.append("import retrofit2.http.*;\n\n");
        code.append("/**\n");
        code.append(" * ").append(messageSource.getMessage("comment.api.interface", null, LocaleContextHolder.getLocale())).append("\n");
        code.append(" */\n");
        code.append("public interface ").append(request.getApiName()).append("Api {\n\n");
        
        code.append("    @GET(\"").append(request.getApiUrl()).append("\")\n");
        code.append("    Call<").append(request.getApiName()).append("Response> ").append(getMethodName(request)).append("(\n");
        code.append("        @Header(\"Cookie\") String cookie,\n");
        code.append("        @Body ").append(request.getApiName()).append("Request request);\n");
        
        code.append("}\n");
        return code.toString();
    }

    private String generateRequestDto(CodeGenerateRequest request) {
        if (request == null) return "";
        
        StringBuilder code = new StringBuilder();
        code.append("package ").append(request.getPackagePath()).append(".dto;\n\n");
        code.append("import lombok.Data;\n");
        code.append("import lombok.Builder;\n");
        code.append("import java.util.List;\n\n");
        code.append("/**\n");
        code.append(" * ").append(messageSource.getMessage("comment.request.dto", null, LocaleContextHolder.getLocale())).append("\n");
        code.append(" */\n");
        code.append("@Data\n");
        code.append("@Builder\n");
        code.append("public class ").append(request.getApiName()).append("Request {\n");
        
        if (request.getRequestData() != null && !request.getRequestData().isEmpty()) {
            List<RequestDataParser.FieldInfo> fields = requestDataParser.parseRequestData(request.getRequestData());
            
            for (RequestDataParser.FieldInfo field : fields) {
                if (field.getComment() != null && !field.getComment().isEmpty()) {
                    String comment = field.getComment();
                    if ("嵌套对象".equals(comment)) {
                        comment = messageSource.getMessage("comment.nested.object", null, LocaleContextHolder.getLocale());
                    } else if ("数组/列表".equals(comment)) {
                        comment = messageSource.getMessage("comment.array.list", null, LocaleContextHolder.getLocale());
                    }
                    code.append("    /**\n");
                    code.append("     * ").append(comment).append("\n");
                    code.append("     */\n");
                }
                code.append("    private ").append(field.getType()).append(" ").append(field.getName()).append(";\n");
                
                if ("嵌套对象".equals(field.getComment())) {
                    code.append("\n    @Data\n");
                    code.append("    public static class ").append(field.getType()).append(" {\n");
                    code.append("        // ").append(messageSource.getMessage("comment.nested.fields", null, LocaleContextHolder.getLocale())).append("\n");
                    code.append("    }\n");
                }
            }
        }
        
        code.append("}\n");
        return code.toString();
    }

    private String generateResponseDto(CodeGenerateRequest request) {
        if (request == null || request.getResponseData() == null || request.getResponseData().isEmpty()) {
            return "";
        }
        
        StringBuilder code = new StringBuilder();
        code.append("package ").append(request.getPackagePath()).append(".dto;\n\n");
        code.append("import lombok.Data;\n");
        code.append("import java.util.List;\n\n");
        code.append("/**\n");
        code.append(" * ").append(messageSource.getMessage("comment.response.dto", null, LocaleContextHolder.getLocale())).append("\n");
        code.append(" */\n");
        code.append("@Data\n");
        code.append("public class ").append(request.getApiName()).append("Response {\n");
        
        try {
            JsonNode rootNode = objectMapper.readTree(request.getResponseData());
            generateClassFields(rootNode, code, "    ", request.getApiName() + "Response");
        } catch (Exception e) {
            log.error(messageSource.getMessage("error.parse.json", null, LocaleContextHolder.getLocale()), e);
            code.append("    // ").append(messageSource.getMessage("comment.todo.fields", null, LocaleContextHolder.getLocale())).append("\n");
        }
        
        code.append("}\n");
        return code.toString();
    }

    private void generateClassFields(JsonNode node, StringBuilder code, String indent, String className) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();
                
                if (fieldValue.isObject()) {
                    // 为嵌套对象生成内部类
                    String innerClassName = capitalize(fieldName);
                    code.append(indent).append("private ").append(innerClassName).append(" ").append(fieldName).append(";\n\n");
                    code.append(indent).append("@Data\n");
                    code.append(indent).append("public static class ").append(innerClassName).append(" {\n");
                    generateClassFields(fieldValue, code, indent + "    ", innerClassName);
                    code.append(indent).append("}\n\n");
                } else if (fieldValue.isArray()) {
                    if (fieldValue.size() > 0) {
                        JsonNode firstElement = fieldValue.get(0);
                        if (firstElement.isObject()) {
                            // 为数组元素生成内部类
                            String innerClassName = capitalize(getSingularName(fieldName));
                            code.append(indent).append("private List<").append(innerClassName).append("> ").append(fieldName).append(";\n\n");
                            code.append(indent).append("@Data\n");
                            code.append(indent).append("public static class ").append(innerClassName).append(" {\n");
                            generateClassFields(firstElement, code, indent + "    ", innerClassName);
                            code.append(indent).append("}\n\n");
                        } else {
                            // 基本类型数组
                            String elementType = getJavaType(firstElement);
                            code.append(indent).append("private List<").append(elementType).append("> ").append(fieldName).append(";\n");
                        }
                    } else {
                        // 空数组，默认使用Object
                        code.append(indent).append("private List<Object> ").append(fieldName).append(";\n");
                    }
                } else {
                    // 基本类型字段
                    String fieldType = getJavaType(fieldValue);
                    code.append(indent).append("private ").append(fieldType).append(" ").append(fieldName).append(";\n");
                }
            });
        }
    }

    private String getJavaType(JsonNode node) {
        if (node.isTextual()) return "String";
        if (node.isInt()) return "Integer";
        if (node.isLong()) return "Long";
        if (node.isDouble() || node.isFloat()) return "Double";
        if (node.isBoolean()) return "Boolean";
        if (node.isNull()) return "Object";
        return "String";
    }

    private String getSingularName(String pluralName) {
        // 简单的单复数转换规则
        if (pluralName.endsWith("ies")) {
            return pluralName.substring(0, pluralName.length() - 3) + "y";
        }
        if (pluralName.endsWith("s") && !pluralName.endsWith("ss")) {
            return pluralName.substring(0, pluralName.length() - 1);
        }
        return pluralName;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String generateServiceImpl(CodeGenerateRequest request) {
        if (request == null) return "";
        
        StringBuilder code = new StringBuilder();
        code.append("package ").append(request.getPackagePath()).append(".service;\n\n");
        code.append("import ").append(request.getPackagePath()).append(".dto.*;\n");
        code.append("import ").append(request.getPackagePath()).append(".*;\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.beans.factory.annotation.Value;\n");
        code.append("import org.springframework.stereotype.Service;\n");
        code.append("import com.open.mcp.server.annotation.Tool;\n");
        code.append("import com.open.mcp.server.annotation.ToolParam;\n\n");
        
        code.append("/**\n");
        code.append(" * ").append(messageSource.getMessage("comment.service.impl", null, LocaleContextHolder.getLocale())).append("\n");
        code.append(" */\n");
        code.append("@Slf4j\n");
        code.append("@Service\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("public class ").append(request.getApiName()).append("Service {\n\n");
        
        code.append("    private final ").append(request.getApiName()).append("Api api;\n\n");
        code.append("    @Value(\"${").append(getPropertyName(request)).append(".cookie}\")\n");
        code.append("    private String cookie;\n\n");
        
        code.append("    @Tool(name = \"").append(getMethodName(request)).append("\", description = \"调用").append(request.getApiName()).append("接口\")\n");
        code.append("    public ").append(request.getApiName()).append("Response ").append(getMethodName(request)).append("(")
            .append("@ToolParam(name = \"request\", description = \"请求参数\") ")
            .append(request.getApiName()).append("Request request) {\n");
        code.append("        try {\n");
        code.append("            return api.").append(getMethodName(request)).append("(cookie, request).execute().body();\n");
        code.append("        } catch (Exception e) {\n");
        code.append("            String errorMessage = messageSource.getMessage(\"error.api.call\", new Object[]{request.getApiName()}, LocaleContextHolder.getLocale());\n");
        code.append("            log.error(errorMessage, e);\n");
        code.append("            throw new RuntimeException(errorMessage, e);\n");
        code.append("        }\n");
        code.append("    }\n");
        
        code.append("}\n");
        return code.toString();
    }

    private String getMethodName(CodeGenerateRequest request) {
        String name = request.getApiName().toLowerCase();
        return "get" + request.getApiName();
    }

    private String getPropertyName(CodeGenerateRequest request) {
        return request.getApiName().toLowerCase().replace("service", "");
    }

    private void addToZip(ZipOutputStream zos, String fileName, String content) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(content.getBytes());
        zos.closeEntry();
    }
} 