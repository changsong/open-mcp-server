package com.open.mcp.server.api.service;

import com.alibaba.fastjson2.JSON;
import com.open.mcp.server.api.VideoApi;
import com.open.mcp.server.api.dto.VideoDetailResponse;
import com.open.mcp.server.api.dto.VideoListResponse;
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
 * Video service implementation class
 */
@Service
public class VideoService {
    private static final Logger log = LoggerFactory.getLogger(VideoService.class);

    @Resource
    private VideoApi videoApi;

    @Value("${juhe.video.apikey}")
    private String apiKey;

    @Tool(description = "获取视频列表")
    public String getVideoList(
            @ToolParam(description = "视频类型：hot(热门),funny(搞笑),music(音乐),movie(电影),tv(电视剧),tech(科技),game(游戏),sports(体育)") String type,
            @ToolParam(description = "页码，默认1") Integer page,
            @ToolParam(description = "每页数量，默认30") Integer pageSize,
            @ToolParam(description = "是否只返回有内容的视频，1是，默认0") Integer isFilter
    ) {
        try {
            Call<VideoListResponse> call = videoApi.getVideoList(
                    apiKey,
                    type,
                    page,
                    pageSize,
                    isFilter
            );

            Response<VideoListResponse> response = call.execute();
            if (response.isSuccessful()) {
                return JSON.toJSONString(response.body());
            } else {
                log.error("Failed to get video list, status code: {}, error message: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "No error message");
                return "获取视频列表失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting video list", e);
            return "获取视频列表失败! 原因:" + e.getMessage();
        }
    }

    @Tool(description = "获取视频详情")
    public String getVideoDetail(@ToolParam(description = "视频ID") String uniquekey) {
        try {
            Call<VideoDetailResponse> call = videoApi.getVideoDetail(apiKey, uniquekey);

            Response<VideoDetailResponse> response = call.execute();
            if (response.isSuccessful()) {
                return JSON.toJSONString(response.body());
            } else {
                log.error("Failed to get video detail, status code: {}, error message: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "No error message");
                return "获取视频详情失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting video detail", e);
            return "获取视频详情失败! 原因:" + e.getMessage();
        }
    }
} 