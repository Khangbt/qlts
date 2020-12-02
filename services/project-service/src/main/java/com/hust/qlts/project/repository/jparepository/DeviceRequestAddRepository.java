package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IRequestDto;
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
    
    String sql3="select dr.ID                                             as id,      " +
            "       dr.CODE                                           as code,      " +
            "       dr.STATUS                                         as status,      " +
            "       dr.PART_ID                                        as partId,      " +
            "       dr.CREAT_DATE                                     as creatDate,      " +
            "       dr.NOTE                                           as note,      " +
            "       dr.CREAT_HUMMER_ID                                as creatHummerId,      " +
            "       dr.PROCESSING_DATE                                as approvedDate,      " +
            "       dr.HANDLER_HUMMER_ID                              as handlerHummerId,      " +
            "       (select h.FULLNAME      " +
            "        from human_resources as h      " +
            "        where h.HUMAN_RESOURCES_ID      " +
            "                  = dr.HANDLER_HUMMER_ID)                as nameHandler,      " +
            "       (select h.FULLNAME      " +
            "        from human_resources as h      " +
            "        where h.HUMAN_RESOURCES_ID = dr.CREAT_HUMMER_ID) as nameCreat,      " +
            "       dr.REASON                                         as reason      " +
            "from device_request_add as dr      " +
            "where dr.ID = :id";
    @Query(value = sql3, nativeQuery = true)
    IRequestDto getIdCustom(Long id);

}
