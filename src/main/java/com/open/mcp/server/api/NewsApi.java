package com.open.mcp.server.api;

import com.open.mcp.server.api.dto.NewsDetailResponse;
import com.open.mcp.server.api.dto.NewsListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    /**
     * 获取新闻列表
     *
     * @param key API密钥
     * @param type 新闻类型
     * @param page 页码
     * @param pageSize 每页数量
     * @param isFilter 是否只返回有内容的新闻
     * @return 新闻列表响应
     */
    @GET("toutiao/index")
    Call<NewsListResponse> getNewsList(
            @Query("key") String key,
            @Query("type") String type,
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize,
            @Query("is_filter") Integer isFilter
    );

    /**
     * 获取新闻详情
     *
     * @param key API密钥
     * @param uniquekey 新闻ID
     * @return 新闻详情响应
     */
    @GET("toutiao/content")
    Call<NewsDetailResponse> getNewsDetail(
            @Query("key") String key,
            @Query("uniquekey") String uniquekey
    );
} 