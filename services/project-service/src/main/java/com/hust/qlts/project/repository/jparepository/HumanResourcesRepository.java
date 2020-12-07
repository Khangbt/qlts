package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.ICusTomDto;
import com.hust.qlts.project.entity.HumanResourcesEntity;
import com.hust.qlts.project.dto.IPartDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanResourcesRepository extends JpaRepository<HumanResourcesEntity, Long> {
    HumanResourcesEntity findByUsername(String username);

    List<HumanResourcesEntity> findByStatus(Integer status);

    //ducvm
    @Query(value = "select * from HUMAN_RESOURCES where CODE=?1 and STATUS=1", nativeQuery = true)
    HumanResourcesEntity findByCode(String code);

    @Query(value = "select * from HUMAN_RESOURCES where EMAIL=?1 and STATUS=1", nativeQuery = true)
    List<HumanResourcesEntity> findByEmail(String email);

    String sql5="SELECT *  " +
            "FROM human_resources HR  " +
            "WHERE HR.email = :email";
    @Query(value = sql5,nativeQuery = true)
    HumanResourcesEntity findByEmail2(String email);

    /*end duc */
    //TanNV
    HumanResourcesEntity findByHumanResourceId(Long id);

    @Query(value = "select * from HUMAN_RESOURCES where EMAIL=?1 and STATUS !=3", nativeQuery = true)
    List<HumanResourcesEntity> findByEmail1(String email);

    /*
     *@author ThaoLC - IIST
     *created on 9/9/2020
     */


    // TanNV
    String sql1 = "SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p  ";

    @Query(value = sql1, nativeQuery = true)
    List<IPartDTO> getNameorCode(String toUpperCase, String i);

    @Query("SELECT h FROM HumanResourcesEntity h WHERE h.positionId = ?2 AND LOWER(h.email) LIKE CONCAT('%',?1,'%')")
    List<HumanResourcesEntity> findAllByUsernameOrGmailAndPosition(String usernameOrGmail, Long positionId);


    @Query("SELECT h FROM HumanResourcesEntity h WHERE (LOWER(h.username) LIKE CONCAT('%',?1,'%') OR (LOWER(h.email) LIKE CONCAT('%',?1,'%')) )")
    List<HumanResourcesEntity> findAllByUsernameOrGmail(String usernameOrGmail);

    @Query("SELECT h FROM HumanResourcesEntity h WHERE h.positionId = ?1 AND h.status = 1")
    List<HumanResourcesEntity> findAllHumanResourcesByPositionId(Long positionId);

    String sql9 = "select hr.CODE as code, hr.HUMAN_RESOURCES_ID as id, hr.FULLNAME as name,  " +
            "       (select p.NAME from part as p where p.ID=hr.PART_ID ) as partName  " +
            "from human_resources as hr where  hr.STATUS=1";

    @Query(value = sql9, nativeQuery = true)
    List<ICusTomDto> getcoe();
}
