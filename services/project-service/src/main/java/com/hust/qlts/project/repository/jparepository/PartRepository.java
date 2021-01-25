package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IPartDTO;
import com.hust.qlts.project.entity.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<PartEntity,Long> {

    String sql="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p ORDER BY p.CREATE_DATE DESC";
    @Query(value = sql, nativeQuery = true, countQuery = sql)
    List<IPartDTO> getPart();
    String sql1="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p  WHERE p.NAME = :toUpperCase" ;
    @Query(value = sql1,nativeQuery = true)
    List<IPartDTO> getName(String toUpperCase);
    String sql2="select * from PART where CODE=?1 and STATUS= 'ACTIVE'";
    @Query(value =sql2, nativeQuery = true)
    PartEntity findByCode(String code);

    String sql3="select count(*) from part as p left join human_resources as h on p.ID=h.PART_ID where p.ID=:part and h.STATUS=3";
    @Query(value =sql3, nativeQuery = true)
    Integer getDateTeHu(Long part);

    String sql4="select count(*)\n  " +
            "   from part as p\n" +
            "         left join device as h on p.ID = h.PART_ID\n   " +
            "   where p.ID = :part and  h.EXIST=1";
    @Query(value =sql4, nativeQuery = true)
    Integer getDateTeDe(Long part);
}
