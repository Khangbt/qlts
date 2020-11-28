package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceToRequestRetuEntitty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceToRequestRetuRepository extends JpaRepository<DeviceToRequestRetuEntitty,Long> {
}
