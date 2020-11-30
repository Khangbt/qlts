package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRepository extends JpaRepository<DeviceRequestEntity,Long> {
    String sql="delete from device_request as drr where drr.ID=:id";
//    @Modifying
//    @Query(value = sql)
    void deleteById(Long id);
}
