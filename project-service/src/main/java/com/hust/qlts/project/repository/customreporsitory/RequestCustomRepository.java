package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.RequestDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class RequestCustomRepository {
    @Autowired
    EntityManager em;

    public List<RequestDto> getSrearh(RequestDto dto) {
        StringBuffer sql = new StringBuffer();


        sql.append("select r.ID,   " +
                "       r.ID_REQUEST,   " +
                "       r.TYLE,   " +
                "       r.CODE,   " +
                "       r.CREART_HUMMER_ID,   ");
        sql.append("(case" +
                "            when r.TYLE = \"YCM\" then (select dra.PART_ID" +
                "                                      from device_request_add as dra" +
                "                                      where dra.ID = r.ID_REQUEST)" +
                "            when r.TYLE = \"PYCM\" then (select dra.PART_ID from device_request as dra where dra.ID = r.ID_REQUEST)" +
                "            ELSE (select dra.PART_ID" +
                "                  from device_request_retu as dra" +
                "                  where dra.ID = r.ID_REQUEST) end)   part, ");
        sql.append("       r.CREART_DATE,   " +
                "       ");
        sql.append("  (case   " +
                "            when r.TYLE = \"YCM\" then (select dra.PROCESSING_DATE   " +
                "                                      from device_request_add as dra   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select dra.APPROVED_DATE   " +
                "                                       from device_request as dra   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select dra.APPROVED_DATE   " +
                "                  from device_request_retu as dra   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as handDate,");
        sql.append("    (case   " +
                "            when r.TYLE = \"YCM\" then (select dra.STATUS   " +
                "                                      from device_request_add as dra   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select dra.STATUS   " +
                "                                       from device_request as dra   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select dra.STATUS   " +
                "                  from device_request_retu as dra   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as status,");
        sql.append("  (case   " +
                "            when r.TYLE = \"YCM\" then (select dra.HANDLER_HUMMER_ID   " +
                "                                      from device_request_add as dra   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select dra.HANDLER_HUMMER_ID   " +
                "                                       from device_request as dra   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select dra.HANDLER_HUMMER_ID   " +
                "                  from device_request_retu as dra   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as handlerHummerId, ");
        sql.append("  (case   " +
                "            when r.TYLE = \"YCM\" then (select dra.NOTE   " +
                "                                      from device_request_add as dra   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select dra.NOTE   " +
                "                                       from device_request as dra   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select dra.NOTE   " +
                "                  from device_request_retu as dra   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as note,");
        sql.append("    (case   " +
                "            when r.TYLE = \"YCM\" then (select dra.REASON   " +
                "                                      from device_request_add as dra   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select dra.REASON   " +
                "                                       from device_request as dra   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select dra.REASON   " +
                "                  from device_request_retu as dra   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as reason, ");
        sql.append("   (case   " +
                "            when r.TYLE = \"YCM\" then (select hr.FULLNAME   " +
                "                                      from device_request_add as dra   " +
                "                                               left join human_resources as hr on dra.CREAT_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select hr.FULLNAME   " +
                "                                       from device_request as dra   " +
                "                                                left join human_resources as hr on dra.CREAT_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select hr.FULLNAME   " +
                "                  from device_request_retu as dra   " +
                "                           left join human_resources as hr on dra.CREAT_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as nameCreat,  ");
        sql.append("     (case   " +
                "            when r.TYLE = \"YCM\" then (select hr.FULLNAME   " +
                "                                      from device_request_add as dra   " +
                "                                               left join human_resources as hr on dra.HANDLER_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                                      where dra.ID = r.ID_REQUEST)   " +
                "            when r.TYLE = \"PYCM\" then (select hr.FULLNAME   " +
                "                                       from device_request as dra   " +
                "                                                left join human_resources as hr on dra.HANDLER_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                                       where dra.ID = r.ID_REQUEST)   " +
                "            ELSE (select hr.FULLNAME   " +
                "                  from device_request_retu as dra   " +
                "                           left join human_resources as hr on dra.HANDLER_HUMMER_ID = hr.HUMAN_RESOURCES_ID   " +
                "                  where dra.ID = r.ID_REQUEST)   " +
                "           end   " +
                "           ) as hander ,");

        sql.append("   (select p.NAME from part as p where p.ID= (case" +
                "                        when r.TYLE = \"YCM\" then (select dra.PART_ID" +
                "                           from device_request_add as dra" +
                "                        where dra.ID = r.ID_REQUEST)" +
                "                            when r.TYLE = \"PYCM\" then (select dra.PART_ID from device_request as dra where dra.ID = r.ID_REQUEST)" +
                "                                  ELSE (select dra.PART_ID" +
                "                                   from device_request_retu as dra" +
                "                                 where dra.ID = r.ID_REQUEST) end)) as partName ");
        sql.append("    from request as r  where 1=1 ");

        if (dto.getStatus() != null) {
            sql.append("    and (case    " +
                    "           when r.TYLE = \"YCM\" then (select dra.STATUS    " +
                    "                                     from device_request_add as dra    " +
                    "                                     where dra.ID = r.ID_REQUEST)    " +
                    "           when r.TYLE = \"PYCM\" then (select dra.STATUS    " +
                    "                                      from device_request as dra    " +
                    "                                      where dra.ID = r.ID_REQUEST)    " +
                    "           ELSE (select dra.STATUS    " +
                    "                 from device_request_retu as dra    " +
                    "                 where dra.ID = r.ID_REQUEST)    " +
                    "    end    " +
                    "          ) =:status   ");
        }
        if (dto.getCreatHummerId() != null) {
            sql.append("    and r.CREART_HUMMER_ID=:creatHummerId   ");
        }
        if (dto.getTyle() != null) {
            sql.append("    and r.TYLE=:tyle  ");
        }
        if (StringUtils.isNotBlank(dto.getCode())) {
            sql.append("    and r.CODE like  upper(:code)  ");
        }
        if(dto.getPartId()!=null){
            sql.append(" and  (case  " +
                    "            when r.TYLE = \"YCM\" then (select dra.PART_ID  " +
                    "                                      from device_request_add as dra  " +
                    "                                      where dra.ID = r.ID_REQUEST)  " +
                    "            when r.TYLE = \"PYCM\" then (select dra.PART_ID from device_request as dra where dra.ID = r.ID_REQUEST)  " +
                    "            ELSE (select dra.PART_ID  " +
                    "                  from device_request_retu as dra  " +
                    "                  where dra.ID = r.ID_REQUEST) end) =:partId   ");
        }
        sql.append("order by (case " +
                "              when r.TYLE = \"YCM\" then (select dra.LAST_MODIFIED_DATE " +
                "                                        from device_request_add as dra " +
                "                                        where dra.ID = r.ID_REQUEST) " +
                "              when r.TYLE = \"PYCM\" then (select dra.LAST_MODIFIED_DATE " +
                "                                         from device_request as dra " +
                "                                         where dra.ID = r.ID_REQUEST) " +
                "              ELSE (select dra.LAST_MODIFIED_DATE " +
                "                    from device_request_retu as dra " +
                "                    where dra.ID = r.ID_REQUEST) end) desc ");
        
        
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());
        if (dto.getStatus() != null) {
            query.setParameter("status", dto.getStatus());
            queryCount.setParameter("status", dto.getStatus());
        }
        if (dto.getCreatHummerId() != null) {
            query.setParameter("creatHummerId", dto.getCreatHummerId());
            queryCount.setParameter("creatHummerId", dto.getCreatHummerId());
        }
        if (dto.getTyle() != null) {
            query.setParameter("tyle", dto.getTyle());
            queryCount.setParameter("tyle", dto.getTyle());
        }
        if (StringUtils.isNotBlank(dto.getCode())) {
            query.setParameter("code", "%" + dto.getCode().toUpperCase() + "%");
            queryCount.setParameter("code", "%" + dto.getCode().toUpperCase() + "%");
        }
        if (dto.getPartId()!=null) {
            query.setParameter("partId", dto.getPartId());
            queryCount.setParameter("partId", dto.getPartId());
        }
        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }
        List<Object[]> objectList = query.getResultList();

        return convent(objectList);
    }

    public List<RequestDto> convent(List<Object[]> objects) {
        List<RequestDto> dtos = new ArrayList<>();
        for (Object[] o : objects) {
            RequestDto dto = new RequestDto();
            if (o[0] != null) {
                dto.setId((Long.valueOf(o[0].toString())));
            }
            dto.setId((Long.valueOf(o[0].toString())));
            if (o[1] != null) {
                dto.setRequestId(Long.valueOf(o[1].toString()));
            }
            dto.setTyle((String) o[2]);
            dto.setCode((String) o[3]);
            if (o[4] != null) {
                dto.setCreatHummerId(Long.valueOf(o[4].toString()));

            }
            if (o[5] != null) {
                dto.setPartId(Long.valueOf(o[5].toString()));
            }
            dto.setCreatDate((Date) o[6]);
            if (o[7] != null) {
                dto.setHandDate((Date) o[7]);
            }
            dto.setStatus((Integer) o[8]);
            if (o[9] != null) {
                dto.setHandlerHummerId(Long.valueOf(o[9].toString()));
            }
            dto.setNote((String) o[10]);
            dto.setReason((String) o[11]);
            dto.setNameCreat((String) o[12]);
            dto.setHandlerName((String) o[13]);
            dto.setPartName((String) o[14]);
            dto.setTyleDto("REQUEST");
            dtos.add(dto);

        }

        return dtos;
    }
}
