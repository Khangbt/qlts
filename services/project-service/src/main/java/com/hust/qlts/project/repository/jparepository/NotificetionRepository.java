package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.INotifition;
import com.hust.qlts.project.entity.ImageEntity;
import com.hust.qlts.project.entity.NotificetionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificetionRepository extends JpaRepository<NotificetionEntity, Long> {


    String sql1="select n.ID                                                                               as id,    " +
            "       n.CONTEN                                                                           as conten,    " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE1) as nameCreat,    " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE2) as nameHand,    " +
            "       n.STATUS                                                                           as status    " +
            "from notificetion as n    " +
            "where n.ID_HUMMER_SHOW = :idHummer    " +
            "  and n.PARTID is null order by n.ID desc";
    @Query(value = sql1,nativeQuery = true)
    List<INotifition> getNotideUser(Long idHummer);
    String sql2="select n.ID                                                                               as id,   " +
            "       n.CONTEN                                                                           as conten,   " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE1) as nameCreat,   " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE2) as nameHand,   " +
            "       n.STATUS                                                                           as status,   " +
            "       n.TYLE as tyle,   " +
            "       n.LIST_SHOW as listShow,   " +
            "       n.NOTE1 as note1,   " +
            "       n.NOTE2 as note2   " +
            "from notificetion as n   " +
            "where (n.PARTID = :partId   " +
            "   or n.ID_HUMMER_SHOW = :idHummer)   " +
            "  order by n.ID desc ";

    @Query(value = sql2,nativeQuery = true)
    List<INotifition> getNotideAdmin(Long partId,Long idHummer);
    
    
    String sql3="select n.ID                                                                               as id,  " +
            "       n.CONTEN                                                                           as conten,  " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE1) as nameCreat,  " +
            "       (select h.FULLNAME from human_resources as h where h.HUMAN_RESOURCES_ID = n.NOTE2) as nameHand,  " +
            "       n.STATUS                                                                           as status,  " +
            "       n.TYLE                                                                             as tyle,  " +
            "       n.LIST_SHOW                                                                        as listShow,  " +
            "       n.NOTE1                                                                            as note1,  " +
            "       n.NOTE2                                                                            as note2  " +
            "from notificetion as n  " +
            "where n.ID_HUMMER_SHOW = :idHummer  " +
            "   or n.PARTID = -1  " +
            "order by n.ID desc";
    @Query(value = sql3,nativeQuery = true)
    List<INotifition> getNotideAdminAll(Long idHummer);

}
