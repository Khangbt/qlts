package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceGroupRepository extends JpaRepository<DeviceGroupEntity,Long> {

    String sql="select * from device_group as dg where upper(dg.CODE)= upper(:code)";
    @Query(value = sql,nativeQuery = true)
    List<DeviceGroupEntity> findByCodeCustom(String code);
}
