package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.WarehouseDTO;
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

@Service(value = "warehouseService")
public class WarehouseCustomRepository {
    @Autowired
    EntityManager em;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    public List<WarehouseDTO> getWarehouseSearch(WarehouseDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select   ");
        sql.append("  sp.WAREHOUSE_ID,  ");
        sql.append("  sp.CODE,  ");
        sql.append("  sp.NAME,  ");
        sql.append("  sp.NOTE, ");
        sql.append("  sp.ADDRESS, ");
        sql.append("(SELECT name FROM PART where ID  like sp.PAR_ID)  ");

        sql.append(" from WAREHOUSE as sp              ");


        sql.append("  where sp.STATUS = 1 ");

        if("" != dto.getFullName()){
            sql.append(" and (( sp.NAME like :fullName )  or ( sp.CODE like :fullName ))");
        }

        if ("" != dto.getAddress()) {
            sql.append(" and( sp.ADDRESS like :address )");
        }
        if (null != dto.getPartId()){
            sql.append(" and( sp.PAR_ID = :parID )  ");
        }


        sql.append(" GROUP BY WAREHOUSE_ID ");
        sql.append(" ORDER BY sp.WAREHOUSE_ID DESC");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if ("" != dto.getFullName()) {
            query.setParameter("fullName", "%"+dto.getFullName()+"%");
            queryCount.setParameter("fullName", "%"+dto.getFullName()+"%");
        }

        if ("" != dto.getAddress()) {
            query.setParameter("address", "%"+dto.getAddress()+"%");
            queryCount.setParameter("address","%"+ dto.getAddress()+"%");
        }

        if(null != dto.getPartId()){
            query.setParameter("parID",dto.getPartId());
            queryCount.setParameter("parID",dto.getPartId());
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
    public List<WarehouseDTO> convertObjectToDtoShow(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<WarehouseDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                WarehouseDTO dto = new WarehouseDTO();

                dto.setWearhouseID((BigInteger) obj[0]);
                dto.setCode((String) obj[1]);
                dto.setFullName((String) obj[2]);
                dto.setNote((String) obj[3]);
                dto.setAddress((String) obj[4]);
                dto.setParname((String) obj[5]);


                listDto.add(dto);
            }
        }

        return listDto;
    }
}

