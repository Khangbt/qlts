package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.HistoryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HistoryCustomRepository {

    @Autowired
    EntityManager em;

    /*ducvm*/
    public List<HistoryDTO> getHumanHistory() {

        return null;
    }
    public List<HistoryDTO> getHumanHistoryById(Long hrId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT h.DATE,p.CODE as position,hr.CODE as code,h.ACTION,h.VALUE_OLD, h.VALUE_NEW from HISTORY h left join HUMAN_RESOURCES hr on h.USER_CREATE=hr.HUMAN_RESOURCES_ID left join POSITION p on hr.POSITION_ID= p.ID" +
                " WHERE TYPE_SCREEN=4 And ACTION=4 and h.VALUE_ID=(:id) ORDER BY h.DATE desc");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("id", hrId);
        List<Object[]> list = query.getResultList();
        List<HistoryDTO> listDto = new ArrayList<HistoryDTO>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object[] obj : list) {
                HistoryDTO historyDTO = new HistoryDTO();
                historyDTO.setDate((Date) obj[0]);
                historyDTO.setRole((String) obj[1]);
                historyDTO.setUserName((String) obj[2]);
                historyDTO.setAction((Integer) obj[3]);
                historyDTO.setValueOld((String) obj[4]);
                historyDTO.setValueNew((String) obj[5]);
                listDto.add(historyDTO);
            }
            return listDto;
        }
        return null;
    }


    /*end duc*/








    //TungHT
    public List<HistoryDTO> getCustomerHistory() {

        return null;
    }


}
