package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_telemetry")
public class DeviceTelemetry extends BaseEntity {

  @Column(name = "device_id", nullable = false, length = 36)
  private String deviceId;

  @Column(name = "metric_key", nullable = false, length = 50)
  private String metricKey;

  @Column(name = "metric_value", precision = 18, scale = 4)
  private BigDecimal metricValue;

  @Column(name = "collect_time", nullable = false)
  private LocalDateTime collectTime;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getMetricKey() {
    return metricKey;
  }

  public void setMetricKey(String metricKey) {
    this.metricKey = metricKey;
  }

  public BigDecimal getMetricValue() {
    return metricValue;
  }

  public void setMetricValue(BigDecimal metricValue) {
    this.metricValue = metricValue;
  }

  public LocalDateTime getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(LocalDateTime collectTime) {
    this.collectTime = collectTime;
  }
}

