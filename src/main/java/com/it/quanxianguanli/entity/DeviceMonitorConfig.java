package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "device_monitor_config")
public class DeviceMonitorConfig extends BaseEntity {

  @Column(name = "device_id", length = 36)
  private String deviceId;

  @Column(name = "metric_key", nullable = false, length = 50)
  private String metricKey;

  @Column(name = "metric_name", length = 100)
  private String metricName;

  @Column(length = 20)
  private String unit;

  @Column(name = "min_value", precision = 18, scale = 4)
  private BigDecimal minValue;

  @Column(name = "max_value", precision = 18, scale = 4)
  private BigDecimal maxValue;

  @Column(name = "warning_min", precision = 18, scale = 4)
  private BigDecimal warningMin;

  @Column(name = "warning_max", precision = 18, scale = 4)
  private BigDecimal warningMax;

  @Column(name = "alarm_min", precision = 18, scale = 4)
  private BigDecimal alarmMin;

  @Column(name = "alarm_max", precision = 18, scale = 4)
  private BigDecimal alarmMax;

  @Column(name = "is_enabled")
  private Boolean isEnabled;

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

  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public BigDecimal getMinValue() {
    return minValue;
  }

  public void setMinValue(BigDecimal minValue) {
    this.minValue = minValue;
  }

  public BigDecimal getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  public BigDecimal getWarningMin() {
    return warningMin;
  }

  public void setWarningMin(BigDecimal warningMin) {
    this.warningMin = warningMin;
  }

  public BigDecimal getWarningMax() {
    return warningMax;
  }

  public void setWarningMax(BigDecimal warningMax) {
    this.warningMax = warningMax;
  }

  public BigDecimal getAlarmMin() {
    return alarmMin;
  }

  public void setAlarmMin(BigDecimal alarmMin) {
    this.alarmMin = alarmMin;
  }

  public BigDecimal getAlarmMax() {
    return alarmMax;
  }

  public void setAlarmMax(BigDecimal alarmMax) {
    this.alarmMax = alarmMax;
  }

  public Boolean getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
}

