package com.open.mcp.server.config;

import com.open.mcp.server.api.NewsApi;
import com.open.mcp.server.api.VideoApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassDescription:
 * @Author: song.chang
 * @Created: 2025/3/12 16:41
 */

@Configuration
public class Retrofit2Config {

    @Value("${juhe.news.baseUrl}")
    private String baseUrl;

    @Bean
    public NewsApi newsApi() {
        return createApi(NewsApi.class);
    }

    @Bean
    public VideoApi videoApi() {
        return createApi(VideoApi.class);
    }

    private <T> T createApi(Class<T> apiClass) {
        // Create logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttpClient and add logging interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(apiClass);
    }
}
