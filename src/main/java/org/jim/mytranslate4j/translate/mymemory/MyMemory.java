package org.jim.mytranslate4j.translate.mymemory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jim
 */
@Data
@NoArgsConstructor
public class MyMemory {
    private ResponseData responseData;

    private Boolean quotaFinished;

    private String mtLangSupported;

    private String responseDetails;

    private int responseStatus;

    private String responderId;

    @JsonProperty("exception_code")
    private String exceptionCode;

    private List<Match> matches;

    @Data
    @NoArgsConstructor
    public static class ResponseData {
        private String translatedText;
        private double match;
    }

    @Data
    @NoArgsConstructor
    public static class Match {
        private int id;
        private String segment;
        private String translation;
        private String source;
        private String target;
        private int quality;
        private String reference;

        @JsonProperty("usage-count")
        private int usageCount;
        private String subject;
        @JsonProperty("created-by")
        private String createdBy;
        @JsonProperty("last-updated-by")
        private String lastUpdatedBy;
        @JsonProperty("create-date")
        private String createDate;
        @JsonProperty("last-update-date")
        private String lastUpdateDate;
        private double match;
        private String model;
    }
}
