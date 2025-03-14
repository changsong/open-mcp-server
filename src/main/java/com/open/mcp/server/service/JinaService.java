package com.open.mcp.server.service;

import com.alibaba.fastjson2.JSON;
import com.open.mcp.server.dto.JinaReadAndFetchContentResponse;
import com.open.mcp.server.dto.JinaSearchAndGetSERPResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @ClassDescription:
 * @Author: song.chang
 * @Created: 2025/3/12 16:37
 */
@Service
public class JinaService {

    @Resource
    private IJinaReaderApi jinaReaderApi;

    @Tool(description = "read a URL and fetch its content")
    public String readAndFetchContent(@ToolParam(description = "The target URL to fetch content from") String url) {
        Call<JinaReadAndFetchContentResponse> jinaReadAndFetchContentResponseCall = jinaReaderApi.readAndFetchContent(url);
        Response<JinaReadAndFetchContentResponse> response = null;
        try {
            response = jinaReadAndFetchContentResponseCall.execute();
        } catch (Exception e) {
            return "execute fail! reason:" + e.getMessage();
        }
        return (response.isSuccessful() ? JSON.toJSONString(response.body()) : "execute fail!");
    }


    @Tool(description = "search the web and get SERP")
    public String searchAndGetSERP(@ToolParam(description = "The keyword to search on the web") String keyword) {
        Call<JinaSearchAndGetSERPResponse> jinaSearchAndGetSERPResponseCall = jinaReaderApi.searchAndGetSERP(keyword, 10, 1);
        Response<JinaSearchAndGetSERPResponse> response = null;
        try {
            response = jinaSearchAndGetSERPResponseCall.execute();
        } catch (Exception e) {
            return "execute fail! reason:" + e.getMessage();
        }
        return (response.isSuccessful() ? JSON.toJSONString(response.body()) : "execute fail!");
    }

}
