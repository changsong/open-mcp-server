package com.open.mcp.server.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoListResponse {
    private String reason;
    private Result result;
    private Integer error_code;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Result {
        private String stat;
        private List<VideoData> data;
        private String page;
        private String pageSize;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VideoData {
        private String uniquekey;
        private String title;
        private String date;
        private String category;
        private String author_name;
        private String url;
        private String thumbnail_pic_s;
        private String duration;
        private String play_count;
        private String like_count;
        private String comment_count;
    }
} 