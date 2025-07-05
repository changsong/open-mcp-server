package com.open.mcp.server.dubbo;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DubboGenericService {

    private final DubboServiceDiscovery serviceDiscovery;

    /**
     * Generic call to Dubbo service
     *
     * @param interfaceName Interface fully qualified name
     * @param methodName Method name
     * @param version Service version
     * @param paramTypes Parameter types
     * @param args Parameter values
     * @return Invocation result
     */
    public Object invoke(String interfaceName, String methodName, String version, String[] paramTypes, Object[] args) {
        try {
            GenericService genericService = serviceDiscovery.getGenericService(interfaceName, version);
            Object result = genericService.$invoke(methodName, paramTypes, args);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            log.error("Failed to invoke Dubbo service: interface={}, method={}, error={}", interfaceName, methodName, e.getMessage());
            throw new RuntimeException("Failed to invoke Dubbo service: " + e.getMessage());
        }
    }

    /**
     * Convert parameter type
     *
     * @param paramType Parameter type name
     * @param value Parameter value
     * @return Converted parameter value
     */
    public Object convertParam(String paramType, Object value) {
        try {
            if (value == null) {
                return null;
            }

            switch (paramType) {
                case "java.lang.String":
                    return value.toString();
                case "java.lang.Integer":
                case "int":
                    return Integer.valueOf(value.toString());
                case "java.lang.Long":
                case "long":
                    return Long.valueOf(value.toString());
                case "java.lang.Double":
                case "double":
                    return Double.valueOf(value.toString());
                case "java.lang.Boolean":
                case "boolean":
                    return Boolean.valueOf(value.toString());
                default:
                    if (value instanceof String) {
                        return JSON.parseObject((String) value, Class.forName(paramType));
                    }
                    return value;
            }
        } catch (Exception e) {
            log.error("Parameter type conversion failed: type={}, value={}, error={}", paramType, value, e.getMessage());
            throw new RuntimeException("Parameter type conversion failed: " + e.getMessage());
        }
    }
} 