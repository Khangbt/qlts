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

    @Query(value = "select * from PART where CODE=?1 and STATUS= 'ACTIVE'", nativeQuery = true)
    PartEntity findByCode(String code);
}
