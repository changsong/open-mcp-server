package com.open.mcp.server.config;

import com.open.mcp.server.api.NewsApi;
import com.open.mcp.server.api.StockApi;
import com.open.mcp.server.api.VideoApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Value("${juhe.news.baseUrl}")
    private String baseUrl;

    @Bean
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Bean
    public Retrofit stockRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://web.juhe.cn/finance/stock/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public StockApi stockApi(Retrofit stockRetrofit) {
        return stockRetrofit.create(StockApi.class);
    }

    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public NewsApi newsApi(Retrofit retrofit) {
        return retrofit.create(NewsApi.class);
    }

    @Bean
    public VideoApi videoApi(Retrofit retrofit) {
        return retrofit.create(VideoApi.class);
    }
} 