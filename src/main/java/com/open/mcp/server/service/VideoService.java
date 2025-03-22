package com.open.mcp.server.service;

import com.alibaba.fastjson2.JSON;
import com.open.mcp.server.dto.VideoDetailResponse;
import com.open.mcp.server.dto.VideoListResponse;
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
 * 视频服务实现类
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
                log.error("获取视频列表失败，状态码: {}, 错误信息: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "无错误信息");
                return "获取视频列表失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("获取视频列表异常", e);
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
                log.error("获取视频详情失败，状态码: {}, 错误信息: {}", response.code(), response.errorBody() != null ? response.errorBody().string() : "无错误信息");
                return "获取视频详情失败! HTTP错误: " + response.code();
            }
        } catch (Exception e) {
            log.error("获取视频详情异常", e);
            return "获取视频详情失败! 原因:" + e.getMessage();
        }
    }
} 