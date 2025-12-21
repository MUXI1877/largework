package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.DeviceUnit;
import com.it.quanxianguanli.repository.DeviceUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceUnitService {

    @Autowired
    private DeviceUnitRepository deviceUnitRepository;

    public List<DeviceUnit> findAll() {
        return deviceUnitRepository.findAll();
    }

    public DeviceUnit findById(String id) {
        return deviceUnitRepository.findById(id).orElse(null);
    }

    @Transactional
    public DeviceUnit save(DeviceUnit deviceUnit) {
        return deviceUnitRepository.save(deviceUnit);
    }

    @Transactional
    public void deleteById(String id) {
        deviceUnitRepository.deleteById(id);
    }
}

