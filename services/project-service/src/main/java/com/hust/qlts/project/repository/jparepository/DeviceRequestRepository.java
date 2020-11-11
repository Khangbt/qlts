package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRepository extends JpaRepository<DeviceRequestEntity,Long> {
}
