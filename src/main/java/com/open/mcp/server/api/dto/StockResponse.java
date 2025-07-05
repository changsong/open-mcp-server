package com.open.mcp.server.api.dto;

import lombok.Data;
import java.util.List;

/**
 * Response DTO
 */
@Data
public class StockResponse {
    private String resultcode;
    private String reason;
    private List<Result> result;

    @Data
    public static class Result {
        private StockData data;

        @Data
        public static class StockData {
            private String buyFive;
            private String buyFivePri;
            private String buyFour;
            private String buyFourPri;
            private String buyOne;
            private String buyOnePri;
            private String buyThree;
            private String buyThreePri;
            private String buyTwo;
            private String buyTwoPri;
            private String competitivePri;
            private String date;
            private String gid;
            private String increPer;
            private String increase;
            private String name;
            private String nowPri;
            private String reservePri;
            private String sellFive;
            private String sellFivePri;
            private String sellFour;
            private String sellFourPri;
            private String sellOne;
            private String sellOnePri;
            private String sellThree;
            private String sellThreePri;
            private String sellTwo;
            private String sellTwoPri;
            private String time;
            private String todayMax;
            private String todayMin;
            private String todayStartPri;
            private String traAmount;
            private String traNumber;
            private String yestodEndPri;
        }

        private Dapandata dapandata;

        @Data
        public static class Dapandata {
            private String dot;
            private String name;
            private String nowPic;
            private String rate;
            private String traAmount;
            private String traNumber;
        }

        private Gopicture gopicture;

        @Data
        public static class Gopicture {
            private String minurl;
            private String dayurl;
            private String weekurl;
            private String monthurl;
        }

    }

    private Integer error_code;
}
