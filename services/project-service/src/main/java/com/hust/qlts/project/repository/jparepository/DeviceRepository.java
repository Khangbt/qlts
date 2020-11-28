package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    String sql = "select d.CODE from device as d where d.EQUIPMENT_GROUP_ID=:id order by CODE DESC limit 1";

    @Query(value = sql, nativeQuery = true)
    String getMaxCode(Long id);

    String sql2="select * from device as d where d.DEVICE_ID in :list";
    @Query(value = sql2, nativeQuery = true)
    List<DeviceEntity> getListCode(List<Long> list);
}
