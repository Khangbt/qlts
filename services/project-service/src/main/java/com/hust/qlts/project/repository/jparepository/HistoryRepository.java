package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IHistoryDTO;
import com.hust.qlts.project.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

    List<HistoryEntity> findAllByValueIdAndTypeScreen(Long id, int i);

    @Query(value = "SELECT H.HISTORY_ID as historyId, HRR.USERNAME as userCreate, H.ACTION as action, H.TYPE_SCREEN as typeScreen, H.DATE as date, G.NAME as groupPermissionName, HR.CODE as groupPermissionUserName, GP.CODE as role, G.CODE as groupPermissionCode " +
            "FROM HISTORY H INNER JOIN GROUPPERMISSION_USER GU ON H.VALUE_ID = GU.ID " +
            "INNER JOIN GROUPPERMISSION G ON G.ID = GU.GROUP_PERMISSION_ID " +
            "INNER JOIN HUMAN_RESOURCES HR ON HR.HUMAN_RESOURCES_ID = GU.USER_ID " +
            "INNER JOIN HUMAN_RESOURCES HRR ON HRR.HUMAN_RESOURCES_ID = H.USER_CREATE " +
            "INNER JOIN GROUPPERMISSION_USER GPU ON H.USER_CREATE = GPU.USER_ID " +
            "INNER JOIN GROUPPERMISSION GP ON GP.ID = GPU.GROUP_PERMISSION_ID " +
            "WHERE H.HISTORY_ID = ?1",
            nativeQuery = true)
    IHistoryDTO findHistoryPermissionUser(Long id);

    @Query(value = "SELECT H.HISTORY_ID as historyId, HR.USERNAME as userCreate, H.ACTION as action, H.TYPE_SCREEN as typeScreen, H.DATE as date, G.NAME as groupPermissionName, GP.CODE as role, G.CODE as groupPermissionCode " +
            "FROM HISTORY H INNER JOIN GROUPPERMISSION G ON H.VALUE_ID = G.ID " +
            "INNER JOIN HUMAN_RESOURCES HR ON HR.HUMAN_RESOURCES_ID = H.USER_CREATE " +
            "INNER JOIN GROUPPERMISSION_USER GPU ON H.USER_CREATE = GPU.USER_ID " +
            "INNER JOIN GROUPPERMISSION GP ON GP.ID = GPU.GROUP_PERMISSION_ID " +
            "WHERE H.HISTORY_ID = ?1",
            nativeQuery = true)
    IHistoryDTO findHistoryGroupPermission(Long id);

    @Query(value = "SELECT * FROM HISTORY h WHERE h.TYPE_SCREEN = 8 OR h.TYPE_SCREEN = 6 ORDER BY h.DATE DESC", nativeQuery = true)
    List<HistoryEntity> findAllHistoryGroupPermission();

    @Query(value = "SELECT H.HISTORY_ID as historyId, HR.USERNAME as userCreate, H.ACTION as action, H.TYPE_SCREEN as typeScreen, H.DATE as date, P.NAME as projectName, R.RISK_NO as riskNo, GP.CODE as role " +
            " FROM HISTORY H INNER JOIN RISK R ON H.VALUE_ID = R.RISK_ID " +
            "INNER JOIN PROJECT P ON P.PROJECT_ID = R.PROJECT_ID " +
            "INNER JOIN HUMAN_RESOURCES HR ON HR.HUMAN_RESOURCES_ID = H.USER_CREATE " +
            "INNER JOIN GROUPPERMISSION_USER GPU ON H.USER_CREATE = GPU.USER_ID " +
            "INNER JOIN GROUPPERMISSION GP ON GP.ID = GPU.GROUP_PERMISSION_ID " +
            "WHERE H.TYPE_SCREEN = 5 ORDER BY H.DATE DESC", nativeQuery = true)
    List<IHistoryDTO> findHistoryRisk();

    
}
