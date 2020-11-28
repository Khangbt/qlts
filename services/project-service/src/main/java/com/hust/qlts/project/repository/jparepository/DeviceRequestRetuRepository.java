package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestRetuEntitty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRetuRepository extends JpaRepository<DeviceRequestRetuEntitty, Long> {
}
