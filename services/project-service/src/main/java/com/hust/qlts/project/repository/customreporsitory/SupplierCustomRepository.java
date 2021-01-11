package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.SupplierDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository

public class SupplierCustomRepository {
    @Autowired
    EntityManager em;
    @Value("${valueDB}")
    private String valueDb;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    public List<SupplierDTO> getSupplierSearch(SupplierDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select   ");
        sql.append("  sp.SUPPLIER_ID,  ");
        sql.append("  sp.CODE,  ");
        sql.append("  sp.NAME,  ");
        sql.append("  sp.HUMAN_CONTACT,  ");
        sql.append("  sp.PHONENUMBER,  ");
        sql.append("  sp.EMAIL,  ");
        sql.append("  sp.ADDRESS,  ");
        sql.append("  sp.NOTE,  ");
        sql.append("  sp.WEBSITE,  ");
        sql.append("sp.FAX ");
//        sql.append(" from SUPPLIER as sp              ");


        if(valueDb=="1"){
        sql.append(" from SUPPLIER as sp              ");
        }else {
        sql.append(" from supplier as sp              ");

        }
        sql.append("  where sp.STATUS != 0 ");

        if (StringUtils.isNotBlank(dto.getFullName())) {
            sql.append("  and (upper(sp.NAME) like upper(:fullName) " +
                    "or  upper(sp.CODE) like upper(:fullName)) ");
        }
        if(StringUtils.isNotBlank(dto.getNameOrCode())){
            sql.append(" and( upper(sp.HUMAN_CONTACT) like upper(:nameHummer )) ");
        }

        if (StringUtils.isNotBlank(dto.getPhone())) {
            sql.append("  and(upper(sp.PHONENUMBER) like upper(:phone) ) ");
        }
        if (StringUtils.isNotBlank(dto.getAddress())) {
            sql.append("  and (upper(sp.ADDRESS) like upper(:address) ) ");
        }

        sql.append(" GROUP BY SUPPLIER_ID ");
        sql.append( "order by sp.LAST_MODIFIED_DATE desc ");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (StringUtils.isNotBlank(dto.getFullName())) {
            query.setParameter("fullName","%"+ dto.getFullName()+"%");
            queryCount.setParameter("fullName", "%"+dto.getFullName()+"%");
        }

        if (StringUtils.isNotBlank(dto.getPhone())) {
            query.setParameter("phone", "%"+dto.getPhone()+"%");
            queryCount.setParameter("phone", "%"+dto.getPhone()+"%");
        }
        if (StringUtils.isNotBlank(dto.getAddress())) {
            query.setParameter("address", "%"+dto.getAddress()+"%");
            queryCount.setParameter("address", "%"+dto.getAddress()+"%");
        }
        if (StringUtils.isNotBlank(dto.getNameOrCode())) {
            query.setParameter("nameHummer", "%%"+dto.getNameOrCode()+"%");
            queryCount.setParameter("nameHummer", "%"+dto.getNameOrCode()+"%");
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

                supplierDTO.setSupplierId((BigInteger) obj[0]);
                supplierDTO.setCode((String) obj[1]);
                supplierDTO.setFullName((String) obj[2]);

                supplierDTO.setNameHummer((String) obj[3]);

                supplierDTO.setPhone((String) obj[4]);
                supplierDTO.setEmail((String) obj[5]);
                supplierDTO.setAddress((String) obj[6]);
                supplierDTO.setNote((String) obj[7]);
                supplierDTO.setWebsite((String) obj[8]);
                supplierDTO.setFax((String) obj[9]);
                supplierDTO.setTyleDto("SUPPLIER");
                listDto.add(supplierDTO);
            }
        }

        return listDto;
    }

}
