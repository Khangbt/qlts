package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.SupplierDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service(value = "supplierService")

public class SupplierCustomRepository {
    @Autowired
    EntityManager em;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    public List<SupplierDTO> getSupplierSearch(SupplierDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select   ");
        sql.append("  sp.SUPPLIER_ID,  ");
        sql.append("  sp.CODE,  ");
        sql.append("  sp.NAME,  ");
        sql.append("  ap.PAR_NAME,  ");
        sql.append("  hr.FULLNAME,  ");
        sql.append("  sp.PHONENUMBER,  ");
        sql.append("  sp.EMAIL,  ");
        sql.append("  sp.ADDRESS,  ");
        sql.append(" ps.NAME position,            ");
        sql.append("  sp.NOTE,  ");
        sql.append("  sp.WEBSITE  ");

        sql.append(" from SUPPLIER as sp              ");
        sql.append(" LEFT JOIN POSITION as ps on sp.POSITION_ID = ps.ID                ");
        sql.append("  left join APP_PARAMS as ap on sp.GROUP_SUPPLIER_ID = ap.APP_PARAMS_ID  ");
        sql.append("  left join HUMAN_RESOURCES as hr on sp.HUMAN_ID = hr.HUMAN_RESOURCES_ID ");

        sql.append("  where sp.STATUS != 0 ");

        if (StringUtils.isNotBlank(dto.getFullName())) {
            sql.append("  and upper(sp.NAME) like upper(:fullName) " +
                    "or  upper(sp.CODE) like upper(:fullName) ");
        }

        if (null != dto.getHumanResourceId()) {
            sql.append(" and( sp.HUMAN_ID = :humanResourceId )");
        }
        if (null != dto.getPositionId()) {
            sql.append(" and( sp.POSITION_ID = :positionId )");
        }
        if (null != dto.getDepartmentId()) {
            sql.append(" and( sp.DEPARTMENT_ID = :departmentId )");
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            sql.append("  and upper(sp.PHONENUMBER) like upper(:phone)  ");
        }
        if (StringUtils.isNotBlank(dto.getAddress())) {
            sql.append("  and upper(sp.ADDRESS) like upper(:address)  ");
        }

        sql.append(" GROUP BY SUPPLIER_ID ");
        sql.append(" ORDER BY sp.SUPPLIER_ID DESC");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (StringUtils.isNotBlank(dto.getFullName())) {
            query.setParameter("fullName", dto.getFullName());
            queryCount.setParameter("fullName", dto.getFullName());
        }
        if (null != dto.getHumanResourceId()) {
            query.setParameter("humanResourceId", dto.getHumanResourceId());
            queryCount.setParameter("humanResourceId", dto.getHumanResourceId());
        }
        if (null != dto.getPositionId()) {
            query.setParameter("positionId", dto.getPositionId());
            queryCount.setParameter("positionId", dto.getPositionId());
        }
        if (null != dto.getDepartmentId()) {
            query.setParameter("departmentId", dto.getDepartmentId());
            queryCount.setParameter("departmentId", dto.getDepartmentId());
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            query.setParameter("phone", dto.getPhone());
            queryCount.setParameter("phone", dto.getPhone());
        }
        if (StringUtils.isNotBlank(dto.getAddress())) {
            query.setParameter("address", dto.getAddress());
            queryCount.setParameter("address", dto.getAddress());
        }

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }


        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDtoShow(lstObject);
    }

    //TanNV convert object to dto
    public List<SupplierDTO> convertObjectToDtoShow(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<SupplierDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                SupplierDTO supplierDTO = new SupplierDTO();
//                Calendar c = Calendar.getInstance();
//                Integer year = c.get(Calendar.YEAR);

                supplierDTO.setSupplierid((BigInteger) obj[0]);
                supplierDTO.setCode((String) obj[1]);
                supplierDTO.setFullName((String) obj[2]);
                supplierDTO.setGroupSupplier((String) obj[3]);

                supplierDTO.setHumanresources((String) obj[4]);

                supplierDTO.setPhone((String) obj[5]);
                supplierDTO.setEmail((String) obj[6]);
                supplierDTO.setAddress((String) obj[7]);
                supplierDTO.setPosition((String) obj[8]);
                supplierDTO.setNote((String) obj[9]);
                supplierDTO.setWebsite((String) obj[10]);

                listDto.add(supplierDTO);
            }
        }

        return listDto;
    }
    public SupplierDTO findById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select   ");
        sql.append("  sp.SUPPLIER_ID,  ");
        sql.append("  sp.CODE,  ");
        sql.append("  sp.NAME,  ");
        sql.append("  ap.PAR_NAME,  ");
        sql.append("  hr.FULLNAME,  ");
        sql.append("  sp.PHONENUMBER,  ");
        sql.append("  sp.EMAIL,  ");
        sql.append("  sp.ADDRESS,  ");
        sql.append(" ps.NAME position,            ");
        sql.append("  sp.NOTE,  ");
        sql.append("  sp.WEBSITE  ");

        sql.append(" from SUPPLIER as sp              ");
        sql.append(" LEFT JOIN POSITION as ps on sp.POSITION_ID = ps.ID                ");
        sql.append("  left join APP_PARAMS as ap on sp.GROUP_SUPPLIER_ID = ap.APP_PARAMS_ID  ");
        sql.append("  left join HUMAN_RESOURCES as hr on sp.HUMAN_ID = hr.HUMAN_RESOURCES_ID ");

        sql.append("  where sp.STATUS != 0 ");

        if (null != id) {
            sql.append(" and( sp.SUPPLIER_ID = :supplierId )  ");
        }


        sql.append(" GROUP BY SUPPLIER_ID ");
        sql.append(" ORDER BY sp.SUPPLIER_ID DESC");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());


        if (null != id) {
            query.setParameter("supplierId", id);
            queryCount.setParameter("supplierId", id);
        }
        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDtoShow1(lstObject);
    }

    //TanNV convert object to dto
    public SupplierDTO convertObjectToDtoShow1(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<SupplierDTO> listDto = new ArrayList<>();
        SupplierDTO supplierDTO = new SupplierDTO();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
//                Calendar c = Calendar.getInstance();
//                Integer year = c.get(Calendar.YEAR);

                supplierDTO.setSupplierid((BigInteger) obj[0]);
                supplierDTO.setCode((String) obj[1]);
                supplierDTO.setFullName((String) obj[2]);
                supplierDTO.setGroupSupplier((String) obj[3]);

                supplierDTO.setHumanresources((String) obj[4]);

                supplierDTO.setPhone((String) obj[5]);
                supplierDTO.setEmail((String) obj[6]);
                supplierDTO.setAddress((String) obj[7]);
                supplierDTO.setPosition((String) obj[8]);
                supplierDTO.setNote((String) obj[9]);
                supplierDTO.setWebsite((String) obj[10]);
            }
        }

        return supplierDTO;
    }
}
