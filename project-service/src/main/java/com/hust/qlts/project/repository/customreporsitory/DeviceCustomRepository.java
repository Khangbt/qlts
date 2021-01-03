package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.entity.excel.DeviceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DeviceCustomRepository {
    @Autowired
    private EntityManager em;

    public List<DeviceDto> search(DeviceDto dto) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select d.DEVICE_ID,    " +
                "       dg.NAME,    " +
                "       d.CODE,    d.PART_ID, " +
                "       d.STATUS,    " +
                "       d.UNIT,    " +
                "       d.SIZE_UNIT,    " +
                "       d.LOST_DEVICE,    " +
                "       d.USE_HUMMER_ID,    " +
                "       d.SPECIFICATIONS,    " +
                "       concat(dg.NOTE,d.NOTE)  as note,    " +
                "       dg.NOTE as groupNote,    " +
                "       d.NOTE as deviceNote,   ");
        sql.append(" (select hr1.FULLNAME from human_resources as hr1 " +
                "where hr1.HUMAN_RESOURCES_ID = d.USE_HUMMER_ID) as hu,    ");
        sql.append("   (select group_concat(dr3.CODE)    " +
                "        from device_request as dr3    " +
                "                 left join device_to_request as dtr3 on dr3.ID = dtr3.DEVICE_REQUEST_ID    " +
                "        where dtr3.DEVICE_ID = d.DEVICE_ID)         as codeRe, ");
        sql.append(" (select group_concat(dr4.STATUS)    " +
                "        from device_request as dr4    " +
                "                 left join device_to_request as dtr4 on dr4.ID = dtr4.DEVICE_REQUEST_ID    " +
                "        where dtr4.DEVICE_ID = d.DEVICE_ID)              as reStaus,  ");
        sql.append("  (select group_concat(dr5.CODE)    " +
                "        from device_request as dr5    " +
                "                 left join device_to_request as dtr5 on dr5.ID = dtr5.DEVICE_REQUEST_ID    " +
                "        where dtr5.DEVICE_ID = d.DEVICE_ID    " +
                "          and dr5.STATUS = 2)            as reRresent , ");
        sql.append(" d.WAREHOUSE_ID," +
                "       d.SUPPLIER_ID ,  ");
        sql.append("  (select p6.NAME from part as p6 where p6.ID = d.PART_ID)       as partName,  " +
                "       (select s6.NAME from  supplier as s6 where s6.SUPPLIER_ID=d.SUPPLIER_ID) as supperName,  " +
                "       (select w6.NAME from warehouse as w6 where w6.WAREHOUSE_ID=d.WAREHOUSE_ID) as wartName  ");
        sql.append(" , d.EQUIPMENT_GROUP_ID   ");
        sql.append("from device as d    " +
                "         join device_group as dg on d.EQUIPMENT_GROUP_ID = dg.ID    " +
                "         left join device_to_request as dtr on dtr.DEVICE_ID = d.DEVICE_ID    " +
                "         left join device_request as dr on dr.ID = dtr.DEVICE_REQUEST_ID    " +
                "where d.EXIST = true  ");
        if (null != dto.getNameOrCode()) {
            sql.append("   and (upper(dg.CODE) like upper(:codeOrName) or upper(dg.NAME) like upper(:codeOrName)) ");
        }
        if (null != dto.getUseHummerId()) {
            sql.append("    and d.USE_HUMMER_ID=:id     ");
        }
        if (null != dto.getWarehouseId()) {
            sql.append(" and d.WAREHOUSE_ID=:warehoueId  ");
        }
        if (null != dto.getPartId()) {
            sql.append("  and d.PART_ID=:partId ");
        }
        if (null != dto.getSupplierId()) {
            sql.append(" and d.SUPPLIER_ID=:supplierId");
        }
        if (null != dto.getStatus()) {
            sql.append(" and d.STATUS=:status");
        }
        if (null != dto.getReQuest()) {
            sql.append("    and dr.CODE=:reQ   ");
        }
        sql.append("    group by d.DEVICE_ID    ");
        sql.append( "order by d.LAST_MODIFIED_DATE desc ");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());
        if (null != dto.getNameOrCode()) {
            query.setParameter("codeOrName", "%" + dto.getNameOrCode() + "%");
            queryCount.setParameter("codeOrName", "%" + dto.getNameOrCode() + "%");
        }
        if (null != dto.getUseHummerId()) {
            query.setParameter("id", dto.getUseHummerId());
            queryCount.setParameter("id", dto.getUseHummerId());
        }
        if (null != dto.getSupplierId()) {
            query.setParameter("supplierId", dto.getSupplierId());
            queryCount.setParameter("supplierId", dto.getSupplierId());

        }
        if (null != dto.getWarehouseId()) {
            query.setParameter("warehoueId", dto.getWarehouseId());
            queryCount.setParameter("warehoueId", dto.getWarehouseId());

        }
        if (null != dto.getStatus()) {
            query.setParameter("status", dto.getStatus());
            queryCount.setParameter("status", dto.getStatus());
        }
        if (null != dto.getPartId()) {
            query.setParameter("partId", dto.getPartId());
            queryCount.setParameter("partId", dto.getPartId());

        }
        if (null != dto.getReQuest()) {
            query.setParameter("reQ", dto.getReQuest());
            queryCount.setParameter("reQ", dto.getReQuest());

        }

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
            if (o[3] != null) {
                dto.setPartId(Long.valueOf(String.valueOf((o[3]))));
            }

            dto.setStatus((Integer) o[4]);
            dto.setUnit((Integer) o[5]);
            dto.setSizeUnit((Integer) o[6]);
            dto.setLostDevice((Integer) o[7]);
            if (o[8] != null) {
                dto.setUseHummerId(Long.valueOf(String.valueOf((o[8]))));
            }
            dto.setSpecifications((String) o[9]);
            dto.setNote((String) o[10]);
            dto.setNodeGroup((String) o[11]);
            dto.setNodeDevice((String) o[12]);
            dto.setUseHummerName((String) o[13]);
            String codelisst = (String) o[14];
            String status = (String) o[15];
            dto.setReQuest((String) o[16]);
            if (o[17] != null) {
                dto.setWarehouseId(Long.valueOf(String.valueOf((o[17]))));
            }
            if (o[18] != null) {
                dto.setSupplierId(Long.valueOf(String.valueOf((o[18]))));
            }

            dto.setPartName((String) o[19]);
            dto.setSupperName((String) o[20]);
            dto.setWareHouseName((String) o[21]);
            if (o[22] != null) {
                dto.setIdEquipmentGroup(Long.valueOf(String.valueOf((o[22]))));
            }
            dto.setTyleDto("DEVICE");
            list.add(dto);
        }


        return list;
    }

    public DeviceFindDto getFindByCode(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select d.DEVICE_ID, dg.NAME, d.CODE, d.PART_ID, d.SPECIFICATIONS, d.LOCATION, ");
        sql.append("    d.NOTE,p.NAME as name1,d.STATUS  ");
        sql.append("    from device as d    ");
        sql.append("         left join device_group as dg on d.EQUIPMENT_GROUP_ID = dg.ID   ");
        sql.append("        left join part as p on d.PART_ID=p.ID  ");
        sql.append("    where d.CODE=:code  and d.EXIST = true");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("code", code);

        List<Object[]> objectList = query.getResultList();
        if (objectList.size() == 0) {
            return null;
        }
        return conventDtoFind(objectList.get(0));

    }

    private DeviceFindDto conventDtoFind(Object[] o) {
        DeviceFindDto dto = new DeviceFindDto();
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

    public void setListDeviceStasus(List<String> list) {

    }

    public List<DeviceExcel> getExcal(DeviceDto dto) {

        StringBuffer sql = new StringBuffer();
        sql.append(" select d.DEVICE_ID,  " +
                "       dg.NAME,  " +
                "       d.CODE,  " +
                "       d.STATUS,  " +
                "       d.SPECIFICATIONS,  " +
                "       d.UNIT,  " +
                "       d.SIZE_UNIT,  " +
                "       d.LOST_DEVICE,  " +
                "       d.DATE_ADD,  " +
                "       (select w6.NAME from warehouse as w6 where w6.WAREHOUSE_ID = d.WAREHOUSE_ID)                     as wartName,  " +
                "       (select w6.ADDRESS from warehouse as w6 where w6.WAREHOUSE_ID = d.WAREHOUSE_ID)                     as addWart,  ");
        sql.append("  d.USE_HUMMER_ID,  " +
                "       concat(dg.NOTE, d.NOTE)                                                                          as note,  " +
                "       dg.NOTE                                                                                          as groupNote,  " +
                "       d.NOTE                                                                                           as deviceNote, ");
        sql.append("  (select hr1.FULLNAME from human_resources as hr1 where hr1.HUMAN_RESOURCES_ID = d.USE_HUMMER_ID) as hu, ");
        sql.append(" (select group_concat(dr3.CODE)  " +
                "        from device_request as dr3  " +
                "                 left join device_to_request as dtr3 on dr3.ID = dtr3.DEVICE_REQUEST_ID  " +
                "        where dtr3.DEVICE_ID = d.DEVICE_ID)                                                             as codeRe,  ");
        sql.append("    (select group_concat(dr4.STATUS)  " +
                "        from device_request as dr4  " +
                "                 left join device_to_request as dtr4 on dr4.ID = dtr4.DEVICE_REQUEST_ID  " +
                "        where dtr4.DEVICE_ID = d.DEVICE_ID)                                                             as reStaus, ");
        sql.append("(select group_concat(dr5.CODE)  " +
                "        from device_request as dr5  " +
                "                 left join device_to_request as dtr5 on dr5.ID = dtr5.DEVICE_REQUEST_ID  " +
                "        where dtr5.DEVICE_ID = d.DEVICE_ID  " +
                "          and dr5.STATUS = 2)                                                                           as reRresent,  " +
                "       (select p6.NAME from part as p6 where p6.ID = d.PART_ID)                                         as partName,  " +
                "       (select s6.NAME from supplier as s6 where s6.SUPPLIER_ID = d.SUPPLIER_ID)                        as supperName,  " +
                "       d.EQUIPMENT_GROUP_ID  ");
        sql.append("from device as d    " +
                "         join device_group as dg on d.EQUIPMENT_GROUP_ID = dg.ID    " +
                "         left join device_to_request as dtr on dtr.DEVICE_ID = d.DEVICE_ID    " +
                "         left join device_request as dr on dr.ID = dtr.DEVICE_REQUEST_ID    " +
                "where d.EXIST = true  ");
        if (null != dto.getNameOrCode()) {
            sql.append("   and (upper(dg.CODE) like upper(:codeOrName) or upper(dg.NAME) like upper(:codeOrName)) ");
        }
        if (null != dto.getUseHummerId()) {
            sql.append("    and d.USE_HUMMER_ID=:id     ");
        }
        if (null != dto.getWarehouseId()) {
            sql.append(" and d.WAREHOUSE_ID=:warehoueId  ");
        }
        if (null != dto.getPartId()) {
            sql.append("  and d.PART_ID=:partId ");
        }
        if (null != dto.getSupplierId()) {
            sql.append(" and d.SUPPLIER_ID=:supplierId");
        }
        if (null != dto.getStatus()) {
            sql.append(" and d.STATUS=:status");
        }
        if (null != dto.getReQuest()) {
            sql.append("    and dr.CODE=:reQ   ");
        }
        sql.append("    group by d.DEVICE_ID    ");
        Query query = em.createNativeQuery(sql.toString());
        if (null != dto.getNameOrCode()) {
            query.setParameter("codeOrName", "%" + dto.getNameOrCode() + "%");
        }
        if (null != dto.getUseHummerId()) {
            query.setParameter("id", dto.getUseHummerId());
        }
        if (null != dto.getSupplierId()) {
            query.setParameter("supplierId", dto.getSupplierId());

        }
        if (null != dto.getWarehouseId()) {
            query.setParameter("warehoueId", dto.getWarehouseId());
            ;

        }
        if (null != dto.getStatus()) {
            query.setParameter("status", dto.getStatus());
        }
        if (null != dto.getPartId()) {
            query.setParameter("partId", dto.getPartId());

        }
        if (null != dto.getReQuest()) {
            query.setParameter("reQ", dto.getReQuest());

        }


        List<Object[]> conventDtoExcel = query.getResultList();


        return conventDtoExcel(conventDtoExcel);
    }

    private List<DeviceExcel> conventDtoExcel(List<Object[]> objects) {
        List<DeviceExcel> list = new ArrayList<>();
        int i=1;
        for (Object[] o:objects){
            DeviceExcel deviceExcel=new DeviceExcel();
            deviceExcel.setStt(i);
            deviceExcel.setName((String) o[1]);
            deviceExcel.setCode((String) o[2]);
            if(o[3]!=null){
                if ((Integer) o[3]==1){
                    deviceExcel.setStatus("Đang trong Kho");
                }else {
                    deviceExcel.setStatus("Đang sử dụng");
                }
            }
            deviceExcel.setSpecifications((String) o[4]);
            if(o[5]!=null){
               switch ((Integer) o[5]){
                   case 1:
                       deviceExcel.setUnit("Lô");
                       break;
                   case 2:
                       deviceExcel.setUnit("Cái");
                       break;
                   case 3:
                       deviceExcel.setUnit("Chiếc");
                       break;

               }
            }
            deviceExcel.setLostDevice((Integer) o[7]);
            deviceExcel.setDateAdd((Date) o[8]);
            deviceExcel.setWareHouseName((String) o[9]);
            deviceExcel.setWareHouseAdd((String) o[10]);

            i++;
            list.add(deviceExcel);
        }

        return list;
    }


}
