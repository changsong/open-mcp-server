package com.open.mcp.server.dubbo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.open.mcp.server.annotation.Tool;
import com.open.mcp.server.annotation.ToolParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DubboMcpAdapter {

    private final DubboGenericService genericService;
    private final DubboServiceDiscovery serviceDiscovery;

    /**
     * Invoke Dubbo service
     *
     * @param interfaceName Interface fully qualified name
     * @param methodName Method name
     * @param version Service version
     * @param params Parameters JSON string
     * @return Invocation result
     */
    @Tool(name = "invokeDubboService", description = "调用Dubbo服务")
    public String invokeDubboService(
            @ToolParam(name = "interfaceName", description = "接口全限定名") String interfaceName,
            @ToolParam(name = "methodName", description = "方法名") String methodName,
            @ToolParam(name = "version", description = "服务版本") String version,
            @ToolParam(name = "params", description = "参数JSON字符串") String params) {
        
        try {
            // Get method information
            Map<String, Object> methodInfo = serviceDiscovery.getServiceMethodInfo(interfaceName);
            Map<String, Object> paramInfo = (Map<String, Object>) methodInfo.get(methodName);
            
            if (paramInfo == null) {
                throw new RuntimeException("方法不存在: " + methodName);
            }

            // Parse parameters
            Class<?>[] parameterTypes = (Class<?>[]) paramInfo.get("parameterTypes");
            String[] paramTypeNames = new String[parameterTypes.length];
            Object[] args = new Object[parameterTypes.length];
            
            JSONObject paramsJson = JSON.parseObject(params);
            
            for (int i = 0; i < parameterTypes.length; i++) {
                paramTypeNames[i] = parameterTypes[i].getName();
                Object paramValue = paramsJson.get("param" + (i + 1));
                args[i] = genericService.convertParam(paramTypeNames[i], paramValue);
            }

            // Invoke service
            return (String) genericService.invoke(interfaceName, methodName, version, paramTypeNames, args);
            
        } catch (Exception e) {
            log.error("Failed to invoke Dubbo service: {}", e.getMessage());
            return JSON.toJSONString(new JSONObject()
                    .fluentPut("success", false)
                    .fluentPut("message", e.getMessage()));
        }
    }

    /**
     * Get service method information
     *
     * @param interfaceName Interface fully qualified name
     * @return Method information
     */
    @Tool(name = "getDubboServiceInfo", description = "获取Dubbo服务信息")
    public String getDubboServiceInfo(
            @ToolParam(name = "interfaceName", description = "接口全限定名") String interfaceName) {
        try {
            Map<String, Object> methodInfo = serviceDiscovery.getServiceMethodInfo(interfaceName);
            return JSON.toJSONString(methodInfo);
        } catch (Exception e) {
            log.error("Failed to get service information: {}", e.getMessage());
            return JSON.toJSONString(new JSONObject()
                    .fluentPut("success", false)
                    .fluentPut("message", e.getMessage()));
        }
    }
} 