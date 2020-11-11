package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DeviceRepository  extends JpaRepository<DeviceEntity,Long> {
}
