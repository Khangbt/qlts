package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.DeviceRequestAddEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface DeviceRequestAddRepository extends JpaRepository<DeviceRequestAddEntity, Long> {
    String sql = "delete from device_request_add as drr where drr.ID=:id";

    //    @Modifying
//    @Query(value = sql)
    void deleteById(Long id);
}
