package com.factcheck.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analysis_results")
@Getter
@NoArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESULT_ID")
    private Long id;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "emotion_neutrality")
    private Float emotionNeutrality;

    @Column(name = "fact_ratio")
    private Float factRatio;

    @Column(name = "bias_score")
    private Float biasScore;

    @Column(name = "bias_direction", length = 20)
    private String biasDirection;

    @Column(name = "bias_label", length = 30)
    private String biasLabel;

    @Column(name = "bias_confidence")
    private Float biasConfidence;

    @Column(name = "bias_reason", columnDefinition = "TEXT")
    private String biasReason;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Column(name = "key_facts", columnDefinition = "JSON")
    private String keyFacts;

    @Column(name = "keywords", columnDefinition = "JSON")
    private String keywords;

    @Column(name = "cleaned_text", columnDefinition = "TEXT")
    private String cleanedText;

    @Column(name = "fact_ratio_source", length = 10)
    private String factRatioSource;

    @Column(name = "section_bias_score")
    private Float sectionBiasScore;

    @Column(name = "background", columnDefinition = "TEXT")
    private String background;

    @Column(name = "cot_emotion_reason", columnDefinition = "TEXT")
    private String cotEmotionReason;

    @Column(name = "cot_fact_ratio_reason", columnDefinition = "TEXT")
    private String cotFactRatioReason;

    @Column(name = "fact_check_results", columnDefinition = "JSON")
    private String factCheckResults;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID", unique = true)
    private Article article;

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SentenceAnalysis> sentenceAnalyses = new ArrayList<>();

    @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnalysisSection> sections = new ArrayList<>();

    @Builder
    public AnalysisResult(Integer totalScore, Float emotionNeutrality, Float factRatio,
                          Float biasScore,
                          String biasDirection, String biasLabel, Float biasConfidence, String biasReason,
                          String title,
                          String keyFacts, String keywords, String cleanedText,
                          String factRatioSource, Float sectionBiasScore,
                          String background, String cotEmotionReason, String cotFactRatioReason,
                          String factCheckResults, Article article) {
        this.totalScore = totalScore;
        this.emotionNeutrality = emotionNeutrality;
        this.factRatio = factRatio;
        this.biasScore = biasScore;
        this.biasDirection  = biasDirection;
        this.biasLabel      = biasLabel;
        this.biasConfidence = biasConfidence;
        this.biasReason     = biasReason;
        this.title = title;
        this.keyFacts  = keyFacts;
        this.keywords  = keywords;
        this.cleanedText = cleanedText;
        this.factRatioSource = factRatioSource;
        this.sectionBiasScore = sectionBiasScore;
        this.background = background;
        this.cotEmotionReason    = cotEmotionReason;
        this.cotFactRatioReason  = cotFactRatioReason;
        this.factCheckResults    = factCheckResults;
        this.article = article;
        this.analyzedAt = LocalDateTime.now();
    }
}
