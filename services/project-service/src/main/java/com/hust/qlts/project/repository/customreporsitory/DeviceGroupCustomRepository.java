package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.DeviceGroupDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceGroupCustomRepository {
    @Autowired
    private EntityManager em;

    public List<DeviceGroupDto> search(DeviceGroupDto dto) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT dg.ID,  ");
        sql.append(" dg.CODE, dg.NAME, dg.SIZE_ID, dg.NOTE, dg.SPECIFICATIONS,  ");
        sql.append(" dg.TYLE ");
        sql.append(" from device_group as dg ");


        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());
        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }
        List<Object[]> objectList = query.getResultList();
        return converEntytoDTO(objectList);
    }

    private List<DeviceGroupDto> converEntytoDTO(List<Object[]> objects) {
        List<DeviceGroupDto> list = new ArrayList<>();
        for (Object[] o : objects) {
            DeviceGroupDto dto = new DeviceGroupDto();
            dto.setId(Long.valueOf(String.valueOf((o[0]))));
            dto.setCode((String) o[1]);
            dto.setName((String) o[2]);
            dto.setSizeId((Integer) o[3]);
            dto.setNote((String) o[4]);
            dto.setSpecifications((String) o[5]);
            dto.setTyle((String) o[6]);
            list.add(dto);
        }


        return list;
    }

    public DeviceGroupFindDto findByCode(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT dg.ID,  ");
        sql.append(" dg.CODE, dg.NAME, dg.SIZE_ID, dg.NOTE, dg.SPECIFICATIONS,  ");
        sql.append(" dg.TYLE ");
        sql.append(" from device_group as dg ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("code", code);
        List<Object[]> objectList = query.getResultList();
        if(objectList==null){
            return null;
        }

        return conventDtoFind(objectList.get(0));


    }

    private DeviceGroupFindDto conventDtoFind(Object[] o) {
        DeviceGroupFindDto dto = new DeviceGroupFindDto();
        dto.setId(Long.valueOf(String.valueOf((o[0]))));
        dto.setCode((String) o[1]);
        dto.setName((String) o[2]);
        dto.setSizeId((Integer) o[3]);
        dto.setNote((String) o[4]);
        dto.setSpecifications((String) o[5]);
        dto.setTyle((String) o[6]);

        return dto;
    }
}
