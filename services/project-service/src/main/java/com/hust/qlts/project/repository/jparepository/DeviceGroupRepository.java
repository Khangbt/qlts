package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IDeviceGroupMaxCodeDto;
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
    
    String sql2= "select dg.ID as id,dg.CODE   as code,    " +
            "       (select d1.CODE    " +
            "        from device_group as dg1    " +
            "                 left join device as d1 on dg1.ID = d1.EQUIPMENT_GROUP_ID    " +
            "        where dg1.ID = dg.ID    " +
            "        order by d1.CODE DESC    " +
            "        limit 1) as maxCode,    " +
            "       (select COUNT(*)    " +
            "        from device_group as dg1    " +
            "                 inner join device as d1 on dg1.ID = d1.EQUIPMENT_GROUP_ID    " +
            "        where dg1.ID = dg.ID    " +
            "        order by d1.CODE DESC    " +
            "        ) as size    " +
            "   from device_group as dg    " +
            "   where upper(dg.CODE) like upper(:code)    " +
            "   group by dg.CODE    " ;
    @Query(value = sql2,nativeQuery = true)
    List<IDeviceGroupMaxCodeDto> findByMaxCode(String code);

    String sql3="select dg.ID                            as id,  " +
            "       dg.CODE                          as code,  " +
            "       dg.NAME                          as name,  " +
            "       (select d1.UNIT  " +
            "        from device as d1  " +
            "        where d1.EQUIPMENT_GROUP_ID = dg.ID  " +
            "        group by d1.EQUIPMENT_GROUP_ID) as unit,  " +
            "       (select d1.SIZE_UNIT  " +
            "        from device as d1  " +
            "        where d1.EQUIPMENT_GROUP_ID = dg.ID  " +
            "        group by d1.EQUIPMENT_GROUP_ID) as sizeUnit  " +
            "  " +
            "from device_group as dg";
    @Query(value = sql3,nativeQuery = true)
    List<IDeviceGroupMaxCodeDto> getAllCode();

}
