package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expert_analysis")
public class ExpertAnalysis extends BaseEntity {

  @Column(name = "device_id", nullable = false, length = 36)
  private String deviceId;

  @Column(name = "analysis_type", nullable = false, length = 50)
  private String analysisType;

  @Column(name = "analysis_result", columnDefinition = "TEXT")
  private String analysisResult;

  @Column(precision = 5, scale = 2)
  private BigDecimal score;

  @Column(length = 500)
  private String conclusion;

  @Column(columnDefinition = "TEXT")
  private String suggestions;

  @Column(name = "analysis_time", nullable = false)
  private LocalDateTime analysisTime;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getAnalysisType() {
    return analysisType;
  }

  public void setAnalysisType(String analysisType) {
    this.analysisType = analysisType;
  }

  public String getAnalysisResult() {
    return analysisResult;
  }

  public void setAnalysisResult(String analysisResult) {
    this.analysisResult = analysisResult;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public String getConclusion() {
    return conclusion;
  }

  public void setConclusion(String conclusion) {
    this.conclusion = conclusion;
  }

  public String getSuggestions() {
    return suggestions;
  }

  public void setSuggestions(String suggestions) {
    this.suggestions = suggestions;
  }

  public LocalDateTime getAnalysisTime() {
    return analysisTime;
  }

  public void setAnalysisTime(LocalDateTime analysisTime) {
    this.analysisTime = analysisTime;
  }
}

