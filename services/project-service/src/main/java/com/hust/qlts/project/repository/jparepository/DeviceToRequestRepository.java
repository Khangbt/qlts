package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceToRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceToRequestRepository extends JpaRepository<DeviceToRequestEntity,Long> {
    String sql="select * from device_to_request as dtr where  dtr.DEVICE_REQUEST_ID=:id";
    @Query(value = sql,nativeQuery = true)
    List<DeviceToRequestEntity> getListAll(Long id);
}
