package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.DeviceGroupDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
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
        sql.append(" dg.TYLE , ");
        sql.append(" (select count(*)  " +
                "        from device_group as dg1  " +
                "                 join device as d1 on d1.EQUIPMENT_GROUP_ID = dg1.ID  " +
                "        where dg1.ID = dg.ID ");
        if (null != dto.getPartId()) {
            sql.append("  and d1.PART_ID=:partId ");
        }
        sql.append("    ) as size, ");

        sql.append(" ( select group_concat(DISTINCT  p.NAME)  " +
                "        from device_group as dg2  " +
                "                 join device as d2 on d2.EQUIPMENT_GROUP_ID = dg2.ID  " +
                "                 left join part as p on d2.PART_ID = p.ID  " +
                "        where dg2.ID = dg.ID  ");
        if (null != dto.getPartId()) {
            sql.append("  and d2.PART_ID=:partId ");
        }
        sql.append("   group by dg2.ID  ) as pard , ");
        sql.append(" (select count(*)  " +
                "        from device as d3  " +
                "        where d3.STATUS = 1  " +
                "          and d3.EQUIPMENT_GROUP_ID = dg.ID    ");

        if (null != dto.getPartId()) {
            sql.append("  and d3.PART_ID=:partId ");
        }
        sql.append("group by d3.EQUIPMENT_GROUP_ID) as p,   ");
        sql.append("(   select group_concat(distinct s4.NAME)  " +
                "           from device as d4  " +
                "                    left join warehouse as s4 on d4.WAREHOUSE_ID = s4.WAREHOUSE_ID  " +
                "           where d4.STATUS = 1  " +
                "             and d4.EQUIPMENT_GROUP_ID = dg.ID");

        if (null != dto.getPartId()) {
            sql.append("   and d4.PART_ID = :partId    ");
        }
        sql.append(")     as p1 ,");

        sql.append(" (select group_concat(distinct s5.NAME)  " +
                "        from device as d5  " +
                "                 left join supplier as s5 on d5.SUPPLIER_ID = s5.SUPPLIER_ID  " +
                "        where d5.EQUIPMENT_GROUP_ID = dg.ID ");
        if (null != dto.getPartId()) {
            sql.append("   and d5.PART_ID = :partId    ");
        }
        sql.append("  )     as p2 ");
        sql.append(" from device_group as dg ");
        sql.append(" join device as d on d.EQUIPMENT_GROUP_ID = dg.ID ");
        sql.append("    where 1=1 ");
        if (null != dto.getPartId()) {
            sql.append("  and d.PART_ID=:partId ");
        }
        sql.append("    group by dg.ID ");

        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());
        if (null != dto.getPartId()) {
            query.setParameter("partId", dto.getPartId());
            queryCount.setParameter("partId", dto.getPartId());

        }
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
            dto.setSize(Integer.valueOf(String.valueOf(o[7])));
            dto.setPartName((String) o[8]);
            dto.setSizeWareHouse(Integer.valueOf(String.valueOf(o[9])));
            dto.setWarehouseName((String) o[10]);
            dto.setSupperName((String) o[11]);
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
        if (objectList == null) {
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
