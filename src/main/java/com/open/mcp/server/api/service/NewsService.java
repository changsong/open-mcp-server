package com.open.mcp.server.api.service;

import com.alibaba.fastjson2.JSON;
import com.open.mcp.server.api.NewsApi;
import com.open.mcp.server.api.dto.NewsDetailResponse;
import com.open.mcp.server.api.dto.NewsListResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

/**
 * News service implementation class
 */
@Service
public class NewsService {
    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    @Resource
    private NewsApi newsApi;

    @Value("${juhe.news.apikey}")
    private String apiKey;

    @Tool(description = "获取新闻列表")
    public String getNewsList(
            @ToolParam(description = "新闻类型：top(推荐),guone(图片),yule(娱乐),tiyu(体育),junshi(军事),keji(科技),caijing(财经),youxi(游戏),qiche(汽车),jiankang(健康)") String type,
            @ToolParam(description = "页码，默认1") Integer page,
            @ToolParam(description = "每页数量，默认30") Integer pageSize,
            @ToolParam(description = "是否只返回有内容的新闻，1是，默认0") Integer isFilter
    ) {
        try {
            Call<NewsListResponse> call = newsApi.getNewsList(
                    apiKey,
                    type,
                    page,
                    pageSize,
                    isFilter
            );

            Response<NewsListResponse> response = call.execute();
            if (response.isSuccessful()) {
                return JSON.toJSONString(response.body());
            } else {
                log.error("Failed to get news list, status code: {}, error message: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "No error message");
                return "获取新闻列表失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting news list", e);
            return "获取新闻列表失败! 原因:" + e.getMessage();
        }
    }

    @Tool(description = "获取新闻详情")
    public String getNewsDetail(@ToolParam(description = "新闻ID") String uniquekey) {
        try {
            Call<NewsDetailResponse> call = newsApi.getNewsDetail(apiKey, uniquekey);

            Response<NewsDetailResponse> response = call.execute();
            if (response.isSuccessful()) {
                return JSON.toJSONString(response.body());
            } else {
                log.error("Failed to get news detail, status code: {}, error message: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "No error message");
                return "获取新闻详情失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting news detail", e);
            return "获取新闻详情失败! 原因:" + e.getMessage();
        }
    }
} 