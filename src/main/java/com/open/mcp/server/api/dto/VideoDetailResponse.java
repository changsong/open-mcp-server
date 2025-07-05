package com.open.mcp.server.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoDetailResponse {
    private String reason;
    private Result result;
    private Integer error_code;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Result {
        private String uniquekey;
        private Detail detail;
        private String content;
        private String video_url;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {
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