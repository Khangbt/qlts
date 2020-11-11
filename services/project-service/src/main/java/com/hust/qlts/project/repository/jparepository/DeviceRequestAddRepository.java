package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestAddEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DeviceRequestAddRepository extends JpaRepository<DeviceRequestAddEntity,Long> {
}
