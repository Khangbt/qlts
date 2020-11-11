package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceGroupRepository extends JpaRepository<DeviceGroupEntity,Long> {
}
