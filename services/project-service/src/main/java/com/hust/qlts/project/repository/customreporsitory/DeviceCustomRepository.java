package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceCustomRepository {
    @Autowired
    private EntityManager em;

    public List<DeviceDto> search(DeviceDto dto) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select d.DEVICE_ID, dg.NAME, d.CODE, d.PART_ID, dg.SPECIFICATIONS, d.LOCATION, ");
        sql.append("    d.NOTE,p.NAME as name1,d.STATUS  ");
        sql.append("    from device as d    ");
        sql.append("         left join device_group as dg on d.EQUIPMENT_GROUP_ID = dg.ID   ");
        sql.append("        left join part as p on d.PART_ID=p.ID  ");

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

    private List<DeviceDto> converEntytoDTO(List<Object[]> objects) {
        List<DeviceDto> list = new ArrayList<>();
        for (Object[] o : objects) {
            DeviceDto dto = new DeviceDto();
            dto.setId(Long.valueOf(String.valueOf((o[0]))));
            dto.setName((String) o[1]);
            dto.setCode((String) o[2]);
            dto.setPartId(Long.valueOf(String.valueOf((o[3]))));
            dto.setSpecifications((String) o[4]);
            dto.setLocation((String) o[5]);
            dto.setNote((String) o[6]);
            dto.setPartName((String) o[7]);
            dto.setStatus((Integer) o[8]);
            list.add(dto);
        }


        return list;
    }
    public DeviceFindDto getFindByCode(String code){
        StringBuffer sql=new StringBuffer();
        sql.append(" select d.DEVICE_ID, dg.NAME, d.CODE, d.PART_ID, dg.SPECIFICATIONS, d.LOCATION, ");
        sql.append("    d.NOTE,p.NAME as name1,d.STATUS  ");
        sql.append("    from device as d    ");
        sql.append("         left join device_group as dg on d.EQUIPMENT_GROUP_ID = dg.ID   ");
        sql.append("        left join part as p on d.PART_ID=p.ID  ");
        sql.append("    where d.CODE=:code  ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("code", code);

        List<Object[]> objectList = query.getResultList();
        if(objectList==null){
            return null;
        }
        return conventDtoFind(objectList.get(0)) ;

    }
    private DeviceFindDto conventDtoFind(Object[] o){
        DeviceFindDto dto=new DeviceFindDto();
        dto.setId(Long.valueOf(String.valueOf((o[0]))));
        dto.setName((String) o[1]);
        dto.setCode((String) o[2]);
        dto.setPartId(Long.valueOf(String.valueOf((o[3]))));
        dto.setSpecifications((String) o[4]);
        dto.setLocation((String) o[5]);
        dto.setNote((String) o[6]);
        dto.setPartName((String) o[7]);
        dto.setStatus((Integer) o[8]);
        return dto;
    }
}
