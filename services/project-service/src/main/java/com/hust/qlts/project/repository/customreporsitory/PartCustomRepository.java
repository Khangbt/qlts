package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.PartnerDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@Service(value = "partService")

public class PartCustomRepository {
    @Autowired
    EntityManager em;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    public List<PartnerDTO> getPartSearch(PartnerDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select   ");
        sql.append("  sp.ID,  ");
        sql.append("  sp.CODE,  ");
        sql.append("  sp.NAME,  ");
        sql.append("  sp.NOTE, ");
        sql.append("  sp.STATUS, ");
        sql.append("(SELECT COUNT(*) AS NumberOfProducts FROM human_resources where PART_ID = sp.ID)  ");

        sql.append(" from PART as sp              ");


        sql.append("  where sp.STATUS != '' ");

        if("" != dto.getPartName()){
            sql.append(" and(( sp.NAME like :codeorname ) or (sp.CODE like  :codeorname))");
        }

        if ("" != dto.getStatus()) {
            sql.append(" and( sp.STATUS = :status )");
        }


        sql.append(" GROUP BY ID ");
        sql.append(" ORDER BY sp.ID DESC");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if ("" != dto.getPartName()) {
            query.setParameter("codeorname","%"+ dto.getPartName()+"%");
            queryCount.setParameter("codeorname","%"+ dto.getPartName()+"%");
        }

        if ("" != dto.getStatus()) {
            query.setParameter("status", dto.getStatus());
            queryCount.setParameter("status", dto.getStatus());
        }





        if (dto.getPage() != null && dto.getSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getSize().intValue());
            query.setMaxResults(dto.getSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }


        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDtoShow(lstObject);
    }

    //TanNV convert object to dto
    public List<PartnerDTO> convertObjectToDtoShow(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<PartnerDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                PartnerDTO partnerDTO = new PartnerDTO();
                Calendar c = Calendar.getInstance();
                Integer year = c.get(Calendar.YEAR);
                partnerDTO.setId((BigInteger) obj[0]);
                partnerDTO.setCode((String) obj[1]);
                partnerDTO.setPartName((String) obj[2]);
                partnerDTO.setNote((String) obj[3]);
                partnerDTO.setStatus((String) obj[4]);
                partnerDTO.setCountHM((BigInteger) obj[5]);
                listDto.add(partnerDTO);
            }
        }

        return listDto;
    }
}
