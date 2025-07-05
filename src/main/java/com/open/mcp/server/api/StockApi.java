package com.open.mcp.server.api;

import com.open.mcp.server.api.dto.StockRequest;
import com.open.mcp.server.api.dto.StockResponse;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * API接口
 */
public interface StockApi {

    @GET("hs")
    Call<StockResponse> getStock(
        @Header("Cookie") String cookie,
        @Query("key") String key,
        @Query("gid") String gid,
        @Query("type") String type
    );
}
