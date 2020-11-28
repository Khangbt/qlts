package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceToRequestAddEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceToRequestAddRepository  extends JpaRepository<DeviceToRequestAddEntity,Long> {
  String sql="select * from device_to_request_add as dtr where  dtr.DEVICE_REQUEST_ADD_ID=:id";
    @Query(value = sql,nativeQuery = true)
    List<DeviceToRequestAddEntity> getListAll(Long id);
}
