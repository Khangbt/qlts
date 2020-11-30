package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.ICusTomDto;
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

    String sql3="   select dg.ID as id,dg.CODE as code,dg.NAME as name, group_concat(d.CODE) as listName  " +
            "from device_group as dg  " +
            "         left join device as d on dg.ID = d.EQUIPMENT_GROUP_ID  " +
            "where d.EXIST = true and d.STATUS=1 and dg.ID in :id  " +
            "group by dg.ID  " ;
    @Query(value = sql3, nativeQuery = true)

    List<ICusTomDto> listDevice(List<Long> id);

String sql4="select d.DEVICE_ID as id, d.CODE as code, d.LOST_DEVICE as lostDevice, d.LOCATION as localion  " +
        "from device as d  " +
        "where d.STATUS = 2  " +
        "  and d.USE_HUMMER_ID = :id";
    @Query(value = sql4, nativeQuery = true)

    List<ICusTomDto> listByID(Long id);

}
