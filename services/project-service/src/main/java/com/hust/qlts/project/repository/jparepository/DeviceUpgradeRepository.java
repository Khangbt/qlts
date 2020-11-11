package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceUpgradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceUpgradeRepository extends JpaRepository<DeviceUpgradeEntity,Long> {
}
