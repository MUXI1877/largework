package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.DeviceTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceTelemetryRepository extends JpaRepository<DeviceTelemetry, String> {
  List<DeviceTelemetry> findByDeviceIdAndCollectTimeBetween(String deviceId, LocalDateTime start, LocalDateTime end);
  List<DeviceTelemetry> findByDeviceIdOrderByCollectTimeDesc(String deviceId);
}

