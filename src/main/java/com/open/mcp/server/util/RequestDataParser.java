package com.open.mcp.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.mcp.server.codegenerator.service.CodeGeneratorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RequestDataParser {
    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageSource messageSource;

    @Data
    public static class FieldInfo {
        private String name;
        private String type;
        private String comment;

        public FieldInfo(String name, String type, String comment) {
            this.name = name;
            this.type = type;
            this.comment = comment;
        }
    }

    public List<FieldInfo> parseRequestData(String data) {
        List<FieldInfo> fields = new ArrayList<>();
        
        if (data == null || data.trim().isEmpty()) {
            return fields;
        }

        try {
            if (isJson(data)) {
                fields.addAll(parseJson(data));
            } else {
                fields.addAll(parseFormData(data));
            }
        } catch (Exception e) {
            log.error(messageSource.getMessage("error.parse.request.data", null, LocaleContextHolder.getLocale()), e);
        }

        return fields;
    }

    private boolean isJson(String data) {
        try {
            objectMapper.readTree(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<FieldInfo> parseJson(String jsonData) {
        List<FieldInfo> fields = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            parseJsonNode("", rootNode, fields);
        } catch (Exception e) {
            log.error(messageSource.getMessage("error.parse.json", null, LocaleContextHolder.getLocale()), e);
        }
        return fields;
    }

    private void parseJsonNode(String prefix, JsonNode node, List<FieldInfo> fields) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String fieldName = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                JsonNode value = entry.getValue();
                
                if (value.isObject()) {
                    String className = capitalize(entry.getKey());
                    fields.add(new FieldInfo(entry.getKey(), className, 
                        messageSource.getMessage("comment.nested.object", null, LocaleContextHolder.getLocale())));
                    parseJsonNode(fieldName, value, fields);
                } else if (value.isArray()) {
                    String type = "List<" + getArrayType(value) + ">";
                    fields.add(new FieldInfo(entry.getKey(), type,
                        messageSource.getMessage("comment.array.list", null, LocaleContextHolder.getLocale())));
                } else {
                    fields.add(new FieldInfo(entry.getKey(), getJsonValueType(value), ""));
                }
            });
        }
    }

    private List<FieldInfo> parseFormData(String formData) {
        List<FieldInfo> fields = new ArrayList<>();
        String[] pairs = formData.split("&");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 0) {
                String name = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                fields.add(new FieldInfo(name, inferType(value), ""));
            }
        }
        
        return fields;
    }

    private String getJsonValueType(JsonNode node) {
        if (node.isTextual()) return "String";
        if (node.isInt()) return "Integer";
        if (node.isLong()) return "Long";
        if (node.isDouble() || node.isFloat()) return "Double";
        if (node.isBoolean()) return "Boolean";
        if (node.isNull()) return "Object";
        return "String";
    }

    private String getArrayType(JsonNode arrayNode) {
        if (arrayNode.size() > 0) {
            JsonNode firstElement = arrayNode.get(0);
            if (firstElement.isObject()) {
                return "Object";
            } else {
                return getJsonValueType(firstElement);
            }
        }
        return "Object";
    }

    private String inferType(String value) {
        if (value == null || value.isEmpty()) return "String";
        
        try {
            Integer.parseInt(value);
            return "Integer";
        } catch (NumberFormatException e1) {
            try {
                Double.parseDouble(value);
                return "Double";
            } catch (NumberFormatException e2) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    return "Boolean";
                }
                return "String";
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
} 