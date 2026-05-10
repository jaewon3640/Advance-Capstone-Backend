package com.factcheck.dto.response;

import com.factcheck.domain.AnalysisResult;
import com.factcheck.domain.AnalysisSection;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GET /api/v1/articles/{id}/result 응답 DTO
 */
@Getter
public class AnalysisResultResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Long articleId;
    private Long resultId;
    private Integer totalScore;
    private Indicators indicators;
    private BiasInfo bias;
    private SummaryInfo summary;
    private List<SectionInfo> sections;
    private String cleanedText;
    private List<SentenceAnalysisResponse> sentences;
    private LocalDateTime analyzedAt;
    private String originalText;
    private String factRatioSource;
    private Float  sectionBiasScore;
    private String background;
    private CotReasons cotReasons;
    private List<FactCheckResult> factCheckResults;

    public AnalysisResultResponse(AnalysisResult result) {
        this.articleId       = result.getArticle().getId();
        this.resultId        = result.getId();
        this.originalText    = result.getArticle().getOriginalText();
        this.sections        = result.getSections().stream()
                .map(SectionInfo::new)
                .collect(Collectors.toList());
        this.cleanedText     = result.getCleanedText();
        this.factRatioSource = result.getFactRatioSource();
        this.sectionBiasScore = result.getSectionBiasScore();
        this.background      = result.getBackground();
        this.cotReasons         = new CotReasons(result);
        this.factCheckResults   = parseFactCheckResults(result.getFactCheckResults());
        this.totalScore      = result.getTotalScore();
        this.indicators      = new Indicators(result);
        this.bias            = new BiasInfo(result);
        this.summary         = new SummaryInfo(result);
        this.analyzedAt      = result.getAnalyzedAt();
        this.sentences       = result.getSentenceAnalyses().stream()
                .map(SentenceAnalysisResponse::new)
                .collect(Collectors.toList());
    }

    private static List<FactCheckResult> parseFactCheckResults(String json) {
        if (json == null || json.isBlank() || json.equals("[]")) return Collections.emptyList();
        try {
            return MAPPER.readValue(json, new TypeReference<List<FactCheckResult>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Getter
    public static class SectionInfo {
        private String topic;
        private String biasLabel;
        private Double confidence;
        private String reason;
        private List<String> step1BiasedExpressions;
        private List<String> step2NeutralExpressions;
        private String step3Judgment;

        public SectionInfo(AnalysisSection section) {
            this.topic      = section.getTopic();
            this.biasLabel  = section.getBiasLabel();
            this.confidence = section.getConfidence() != null ? section.getConfidence().doubleValue() : null;
            this.reason     = section.getReason();
            this.step3Judgment            = section.getStep3Judgment();
            this.step1BiasedExpressions   = parseStringList(section.getStep1BiasedExpressions());
            this.step2NeutralExpressions  = parseStringList(section.getStep2NeutralExpressions());
        }

        private static List<String> parseStringList(String json) {
            if (json == null || json.isBlank()) return Collections.emptyList();
            try {
                return MAPPER.readValue(json, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }
    }

    @Getter
    public static class Indicators {
        private Float emotionNeutrality;
        private Float factRatio;
        private Float biasScore;

        public Indicators(AnalysisResult result) {
            this.emotionNeutrality  = result.getEmotionNeutrality();
            this.factRatio          = result.getFactRatio();
            this.biasScore          = result.getBiasScore();
        }
    }

    @Getter
    public static class BiasInfo {
        private String biasDirection;
        private String biasLabel;
        private Float  biasConfidence;
        private String biasReason;

        public BiasInfo(AnalysisResult result) {
            this.biasDirection  = result.getBiasDirection();
            this.biasLabel      = result.getBiasLabel();
            this.biasConfidence = result.getBiasConfidence();
            this.biasReason     = result.getBiasReason();
        }
    }

    @Getter
    public static class SummaryInfo {
        private String title;
        private String keyFacts;
        private String keywords;

        public SummaryInfo(AnalysisResult result) {
            this.title    = result.getTitle();
            this.keyFacts = result.getKeyFacts();
            this.keywords = result.getKeywords();
        }
    }

    @Getter
    public static class CotReasons {
        private String factRatio;
        private String emotionNeutrality;

        public CotReasons(AnalysisResult result) {
            this.factRatio         = result.getCotFactRatioReason();
            this.emotionNeutrality = result.getCotEmotionReason();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FactCheckResult {
        @JsonProperty("fact")
        private String fact;

        @JsonProperty("found")
        private Boolean found;

        @JsonProperty("rating")
        private String rating;

        @JsonProperty("score")
        private Double score;

        @JsonProperty("publisher")
        private String publisher;

        @JsonProperty("url")
        private String url;
    }
}
