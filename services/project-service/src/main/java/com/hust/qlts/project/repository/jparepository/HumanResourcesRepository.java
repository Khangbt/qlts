package com.hust.qlts.project.repository.jparepository;

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
    @Query(value="select * from HUMAN_RESOURCES where CODE=?1 and STATUS=1",nativeQuery = true)
    HumanResourcesEntity findByCode(String code);

    @Query(value="select * from HUMAN_RESOURCES where EMAIL=?1 and STATUS=1",nativeQuery = true)
    List<HumanResourcesEntity> findByEmail(String email);

    @Query("SELECT HR FROM HumanResourcesEntity HR WHERE HR.email=?1")
    HumanResourcesEntity findByEmail2(String email);

    /*end duc */
    //TanNV
    HumanResourcesEntity findByHumanResourceId(Long id);
    @Query(value="select * from HUMAN_RESOURCES where EMAIL=?1 and STATUS !=3",nativeQuery = true)
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
}
