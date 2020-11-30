package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IListDeviceToReDto;
import com.hust.qlts.project.entity.DeviceToRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceToRequestRepository extends JpaRepository<DeviceToRequestEntity,Long> {
    String sql="select * from device_to_request as dtr where  dtr.DEVICE_REQUEST_ID=:id";
    @Query(value = sql,nativeQuery = true)
    List<DeviceToRequestEntity> getListAll(Long id);

    String sql1="delete from device_to_request as drr where drr.DEVICE_REQUEST_ID=:id";
//    @Modifying
//    @Query(value = sql1)
    void deleteByDeviceRequestId(Long id);


    String sql2="select dtr.DEVICE_GROUP_ID as groupId,  " +
            "       dtr.DEVICE_REQUEST_ID as deviceRequest,  " +
            "       (count(*))                                                                                            as size,  " +
            "       group_concat(dtr.DEVICE_ID)                                                                           as listLongId,  " +
            "       (select d1.UNIT from device as d1 where d1.EQUIPMENT_GROUP_ID = dg.ID group by d1.EQUIPMENT_GROUP_ID) as unit,  " +
            "       (select d1.SIZE_UNIT from device as d1 where d1.EQUIPMENT_GROUP_ID = dg.ID group by d1.EQUIPMENT_GROUP_ID) as unitSize  " +
            "from device_to_request as dtr  " +
            "         join device_group as dg on dtr.DEVICE_GROUP_ID = dg.ID  " +
            "where dtr.DEVICE_REQUEST_ID = :id  " +
            "group by dtr.DEVICE_GROUP_ID  ";

    @Query(value = sql2,nativeQuery = true)
    List<IListDeviceToReDto> getListAllIdCustom(Long id);
}
