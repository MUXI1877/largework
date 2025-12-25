package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.DeviceMonitorConfig;
import com.it.quanxianguanli.repository.DeviceMonitorConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceMonitorConfigService {

  @Autowired
  private DeviceMonitorConfigRepository deviceMonitorConfigRepository;

  public List<DeviceMonitorConfig> findAll() {
    return deviceMonitorConfigRepository.findAll();
  }

  public DeviceMonitorConfig findById(String id) {
    return deviceMonitorConfigRepository.findById(id).orElse(null);
  }

  public List<DeviceMonitorConfig> findByDeviceId(String deviceId) {
    if (deviceId == null) {
      return deviceMonitorConfigRepository.findByDeviceIdIsNull();
    }
    return deviceMonitorConfigRepository.findByDeviceId(deviceId);
  }

  @Transactional
  public DeviceMonitorConfig save(DeviceMonitorConfig config) {
    return deviceMonitorConfigRepository.save(config);
  }

  @Transactional
  public void deleteById(String id) {
    deviceMonitorConfigRepository.deleteById(id);
  }
}

