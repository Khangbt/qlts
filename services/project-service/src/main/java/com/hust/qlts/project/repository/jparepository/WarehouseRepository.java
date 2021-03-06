package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IWarePart;
import com.hust.qlts.project.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository  extends JpaRepository<WarehouseEntity,Long> {
    String sql="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p ORDER BY p.CREATE_DATE DESC";
    @Query(value = sql, nativeQuery = true, countQuery = sql)
    List<WarehouseEntity> getPart();
    String sql1="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p  WHERE p.NAME = :toUpperCase" ;
    @Query(value = sql1,nativeQuery = true)
    List<WarehouseEntity> getName(String toUpperCase);

    @Query(value = "select * from WAREHOUSE where CODE=?1 and STATUS= 1 ", nativeQuery = true)
    WarehouseEntity findByCode(String code);

    @Query(value = "select * from WAREHOUSE where WAREHOUSE_ID=?1 and STATUS=1", nativeQuery = true)
    WarehouseEntity findByID(Long id);

    String sql2="SELECT wh.WAREHOUSE_ID as id,wh.NAME as name,wh.CODE as code from  warehouse  as  wh where  wh.PAR_ID=:id and wh.STATUS=1";
    @Query(value = sql2,nativeQuery = true)
    List<IWarePart> findListPart(Long id);
    String sql3="SELECT wh.WAREHOUSE_ID as id,wh.NAME as name,wh.CODE as code from  warehouse  as  wh where wh.STATUS=1";
    @Query(value = sql3,nativeQuery = true)
    List<IWarePart> findListPartAll();


    String sql4="SELECT *  " +
            "from warehouse as w  " +
            "where upper(w.CODE)=upper(:code)";
    @Query(value = sql4,nativeQuery = true)
    List<WarehouseEntity> getCodeCheck(String code);


        String sql13="  select count(*)\n   " +
            "   from warehouse as w\n   " +
            "         left join device as d\n   " +
            "   on w.WAREHOUSE_ID=d.WAREHOUSE_ID where w.WAREHOUSE_ID=:idhouse and d.EXIST=1    " ;

    @Query(value = sql13,nativeQuery = true)
    Integer getDelete(Long idhouse);
}
