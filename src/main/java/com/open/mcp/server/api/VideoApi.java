package com.open.mcp.server.api;

import com.open.mcp.server.api.dto.VideoDetailResponse;
import com.open.mcp.server.api.dto.VideoListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoApi {

    /**
     * 获取视频列表
     *
     * @param key API密钥
     * @param type 视频类型
     * @param page 页码
     * @param pageSize 每页数量
     * @param isFilter 是否只返回有内容的视频
     * @return 视频列表响应
     */
    @GET("video/index")
    Call<VideoListResponse> getVideoList(
            @Query("key") String key,
            @Query("type") String type,
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize,
            @Query("is_filter") Integer isFilter
    );

    /**
     * 获取视频详情
     *
     * @param key API密钥
     * @param uniquekey 视频ID
     * @return 视频详情响应
     */
    @GET("video/content")
    Call<VideoDetailResponse> getVideoDetail(
            @Query("key") String key,
            @Query("uniquekey") String uniquekey
    );
} 