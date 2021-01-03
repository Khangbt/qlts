package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.ICusTomDto;
import com.hust.qlts.project.entity.DeviceToRequestRetuEntitty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceToRequestRetuRepository extends JpaRepository<DeviceToRequestRetuEntitty, Long> {
    String sql = "select * from device_to_request_retu as dtrr  where dtrr.DEVICE_REQUEST_ID_RETU=:id";

    @Query(value = sql, nativeQuery = true)
    List<DeviceToRequestRetuEntitty> getListbyid(Long id);

    String sql2 = "select d.DEVICE_ID       as id,   " +
            "       d.CODE            as code,   " +
            "       dtrr.LOST_DEVICE  as lostDevice,   " +
            "       d.UNIT            as unit,   " +
            "       dtrr.WAREHOUSE_ID as warehouseId   " +
            "from device_to_request_retu as dtrr   " +
            "         left join device as d on dtrr.DEVICE_ID = d.DEVICE_ID   " +
            "where dtrr.DEVICE_REQUEST_ID_RETU = :id";

    @Query(value = sql2, nativeQuery = true)
    List<ICusTomDto> getListbyidCustom(Long id);

    String sql1 = "delete from device_to_request_retu as drr where drr.DEVICE_REQUEST_ID_RETU=:id";

    //    @Modifying
//    @Query(value = sql1)
    Object deleteByDeviceRequestIdRetu(Long id);
}
