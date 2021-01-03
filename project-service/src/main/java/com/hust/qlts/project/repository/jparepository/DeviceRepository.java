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

    String sql2 = "select * from device as d where d.DEVICE_ID in :list";

    @Query(value = sql2, nativeQuery = true)
    List<DeviceEntity> getListCode(List<Long> list);

    String sql3 = "   select dg.ID as id,dg.CODE as code,dg.NAME as name, group_concat(concat(d.CODE,\"===\",d.DEVICE_ID)) as listName  " +
            "from device_group as dg  " +
            "         left join device as d on dg.ID = d.EQUIPMENT_GROUP_ID  " +
            "where d.EXIST = true and d.STATUS=1 and dg.ID in :id and d.PART_ID =:partId  " +
            "group by dg.ID  ";

    @Query(value = sql3, nativeQuery = true)
    List<ICusTomDto> listDevice(List<Long> id, Long partId);

    String sql4 = "select d.DEVICE_ID as id, d.CODE as code, d.LOST_DEVICE as lostDevice, d.LOCATION as localion  " +
            "from device as d  " +
            "where d.STATUS = 2  " +
            "  and d.USE_HUMMER_ID = :id";

    @Query(value = sql4, nativeQuery = true)
    List<ICusTomDto> listByID(Long id);

    String sql55 = "  select dtr.DEVICE_GROUP_ID" +
            "   from device_request as dr" +
            "         left join device_to_request as dtr on dr.ID=dtr.DEVICE_REQUEST_ID" +
            "   where dr.ID=:idRequest" +
            "   group by DEVICE_GROUP_ID";

    @Query(value = sql55, nativeQuery = true)
    List<Long> getAllLog(Long idRequest);

    String sql5 = "select d.DEVICE_ID as id,d.SIZE_UNIT as sizeUnit,    " +
            "                   d.UNIT as unit,d.LOST_DEVICE as lostDevice, " +
            "                   d.CODE as code,d.LOCATION as location   " +
            "            from device as d   left join device_to_request_retu as dtrr on d.DEVICE_ID=dtrr.DEVICE_ID  " +
            "                                left join device_request_retu as drr on dtrr.DEVICE_REQUEST_ID_RETU=drr.ID " +
            "            where d.USE_HUMMER_ID = :idHummer and d.PART_ID=:partId    " +
            "              and d.EXIST = true and d.STATUS = 2  " +
            "                 and   (drr.STATUS=2 or drr.STATUS IS null) ";

    String sql5Custom="select d.DEVICE_ID as id,d.SIZE_UNIT as sizeUnit,  " +
            "                               d.UNIT as unit,d.LOST_DEVICE as lostDevice,  " +
            "                               d.CODE as code,d.LOCATION as location,drr.ID,drr.STATUS,SUM(dtrr.STATUS)  " +
            "                        from device as d  left  join device_to_request_retu as dtrr on d.DEVICE_ID=dtrr.DEVICE_ID  " +
            "                                            left join device_request_retu as drr on dtrr.DEVICE_REQUEST_ID_RETU=drr.ID  " +
            "                        where d.USE_HUMMER_ID = :idHummer and d.PART_ID=:partId  " +
            "                          and d.EXIST = true and d.STATUS = 2  " +
            "group by d.DEVICE_ID  " +
            "having SUM(dtrr.STATUS)%2=0 or SUM(dtrr.STATUS) IS null";

    @Query(value = sql5Custom, nativeQuery = true)
    List<ICusTomDto> listDeviceRetu(Long idHummer, Long partId);


    String sql6 = "   select dg.ID as id,dg.CODE as code,dg.NAME as name, group_concat(concat(d.CODE,\"===\",d.DEVICE_ID)) as listName " +
            "   from device_to_request as dtr  " +
            "         left join device as d  " +
            "                   on d.DEVICE_ID = dtr.DEVICE_ID  " +
            "         left join device_group as dg on dg.ID = d.EQUIPMENT_GROUP_ID  " +
            "where dtr.DEVICE_REQUEST_ID = :id  " +
            "  " +
            "group by d.EQUIPMENT_GROUP_ID";

    @Query(value = sql6, nativeQuery = true)
    List<ICusTomDto> listReturnRequest(Long id);

    String sql7="select d.DEVICE_ID as id,d.CODE as code  " +
            "from device_request_retu as drr  " +
            "         left join device_to_request_retu as dtrr on drr.ID = dtrr.DEVICE_REQUEST_ID_RETU  " +
            "        left join device as d on d.DEVICE_ID=dtrr.DEVICE_ID  " +
            "where drr.ID=:id";
    @Query(value = sql7, nativeQuery = true)
    List<ICusTomDto> listRequestRetuBorw(Long id);

    String sql8="select * from device as dg where upper(dg.CODE)= upper(:code)";
    @Query(value = sql8, nativeQuery = true)
    List<DeviceEntity> getByCodeCustom(String code);


    String sql10="select d.DEVICE_ID as id,d.SIZE_UNIT as sizeUnit,  " +
            "                   d.UNIT as unit,d.LOST_DEVICE as lostDevice,  " +
            "                   d.CODE as code,d.LOCATION as location    " +
            "            from device as d   left join device_to_request_retu as dtrr on d.DEVICE_ID=dtrr.DEVICE_ID  " +
            "                                left join device_request_retu as drr on dtrr.DEVICE_REQUEST_ID_RETU=drr.ID  " +
            "            where d.USE_HUMMER_ID = :idHummer and d.PART_ID=:partId  " +
            "              and d.EXIST = true and d.STATUS = 2  " +
            "                and (drr.ID=:idRequest or drr.ID IS NULL)";
    
    String sql10Custom=" select d.DEVICE_ID as id,d.SIZE_UNIT as sizeUnit,       " +
            "                                d.UNIT as unit,d.LOST_DEVICE as lostDevice,       " +
            "                                d.CODE as code,d.LOCATION as location,d.STATUS       " +
            "                         from device as d   left join device_to_request_retu as dtrr on d.DEVICE_ID=dtrr.DEVICE_ID       " +
            "                                             left join device_request_retu as drr on dtrr.DEVICE_REQUEST_ID_RETU=drr.ID       " +
            "                         where d.USE_HUMMER_ID = :idHummer and d.PART_ID=:partId       " +
            "                           and d.EXIST = true and d.STATUS = 2       " +
            "                             and ( (drr.ID=:idRequest or drr.ID IS NULL) or       " +
            "                         (select SUM(dtrr1.STATUS) from device as d1  left join device_to_request_retu as dtrr1 on d1.DEVICE_ID=dtrr1.DEVICE_ID       " +
            "                         where d1.DEVICE_ID=d.DEVICE_ID and d1.STATUS=2       " +
            "                         group by d1.DEVICE_ID  " +
            "                             )%2=0)  " +
            "group by d.CODE  ";
    @Query(value = sql10Custom, nativeQuery = true)
    List<ICusTomDto> listDeviceRetuById(Long idHummer, Long partId,Long  idRequest);


    String sql11="   " +
            "select d.DEVICE_ID as id,d.SIZE_UNIT as sizeUnit,   " +
            "       d.UNIT as unit,d.LOST_DEVICE as lostDevice,   " +
            "       d.CODE as code,d.LOCATION as location   " +
            "from device as d   left join device_to_request_retu as dtrr on d.DEVICE_ID=dtrr.DEVICE_ID   " +
            "                   left join device_request_retu as drr on dtrr.DEVICE_REQUEST_ID_RETU=drr.ID   " +
            "where  d.EXIST = true   " +
            "  and (drr.ID=:idRequest )";
    @Query(value = sql11,nativeQuery = true)
    List<ICusTomDto> listDeviceRetuGetStaus(Long  idRequest);


    String sql12="select * from device  as d where d.EXIST=true and d.EQUIPMENT_GROUP_ID=:id";
    @Query(value = sql12,nativeQuery = true)
    List<DeviceEntity> listDeviceByCode(Long  id);
}
