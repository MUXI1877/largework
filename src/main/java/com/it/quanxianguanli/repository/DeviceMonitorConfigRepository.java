package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.DeviceMonitorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMonitorConfigRepository extends JpaRepository<DeviceMonitorConfig, String> {
  List<DeviceMonitorConfig> findByDeviceId(String deviceId);
  List<DeviceMonitorConfig> findByDeviceIdIsNull();
}

