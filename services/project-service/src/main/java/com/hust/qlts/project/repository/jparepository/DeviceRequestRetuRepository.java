package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestRetuEntitty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRequestRetuRepository extends JpaRepository<DeviceRequestRetuEntitty, Long> {
    String sql="delete from device_request_retu as drr where drr.ID=:id";
//    @Modifying
//    @Query(value = sql)
    void deleteById(Long id);

}
