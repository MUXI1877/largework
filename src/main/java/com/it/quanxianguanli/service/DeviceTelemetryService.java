package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.DeviceTelemetry;
import com.it.quanxianguanli.repository.DeviceTelemetryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceTelemetryService {

  @Autowired
  private DeviceTelemetryRepository deviceTelemetryRepository;

  public List<DeviceTelemetry> findAll() {
    return deviceTelemetryRepository.findAll();
  }

  public DeviceTelemetry findById(String id) {
    return deviceTelemetryRepository.findById(id).orElse(null);
  }

  public List<DeviceTelemetry> findByDeviceId(String deviceId) {
    return deviceTelemetryRepository.findByDeviceIdOrderByCollectTimeDesc(deviceId);
  }

  public List<DeviceTelemetry> findByDeviceIdAndTimeRange(String deviceId, LocalDateTime start, LocalDateTime end) {
    return deviceTelemetryRepository.findByDeviceIdAndCollectTimeBetween(deviceId, start, end);
  }

  @Transactional
  public DeviceTelemetry save(DeviceTelemetry telemetry) {
    return deviceTelemetryRepository.save(telemetry);
  }

  @Transactional
  public void deleteById(String id) {
    deviceTelemetryRepository.deleteById(id);
  }
}

