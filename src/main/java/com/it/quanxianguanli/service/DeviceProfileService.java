package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.DeviceProfile;
import com.it.quanxianguanli.repository.DeviceProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceProfileService {

  @Autowired
  private DeviceProfileRepository deviceProfileRepository;

  public List<DeviceProfile> findAll() {
    return deviceProfileRepository.findAll();
  }

  public DeviceProfile findBySn(String sn) {
    return deviceProfileRepository.findBySn(sn).orElse(null);
  }

  public List<DeviceProfile> findByCustomerId(String customerId) {
    return deviceProfileRepository.findByCustomerId(customerId);
  }

  public List<DeviceProfile> findByModel(String model) {
    return deviceProfileRepository.findByModel(model);
  }

  public DeviceProfile findById(String id) {
    return deviceProfileRepository.findById(id).orElse(null);
  }

  @Transactional
  public DeviceProfile save(DeviceProfile deviceProfile) {
    return deviceProfileRepository.save(deviceProfile);
  }

  @Transactional
  public void deleteById(String id) {
    deviceProfileRepository.deleteById(id);
  }
}

