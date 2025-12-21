package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.DeviceProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceProfileRepository extends JpaRepository<DeviceProfile, String> {
  Optional<DeviceProfile> findBySn(String sn);
  List<DeviceProfile> findByCustomerId(String customerId);
  List<DeviceProfile> findByModel(String model);
}

