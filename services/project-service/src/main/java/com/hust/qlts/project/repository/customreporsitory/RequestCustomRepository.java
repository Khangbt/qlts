package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.RequestDto;
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
        sql.append("select r.ID, r.ID_REQUEST, r.TYLE, r.CODE, r.CREART_HUMMER_ID, r.PART_ID,r.CREART_DATE  ");
        sql.append("from request as r");


        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());


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
            if(o[0]!=null){
                dto.setId((Long.valueOf( o[0].toString())));
            }
            dto.setId((Long.valueOf( o[0].toString())));
            if(o[1]!=null){
                dto.setRequestId(Long.valueOf(o[1].toString()));
            }
            dto.setTyle((String) o[2]);
            dto.setCode((String) o[3]);
            if(o[4]!=null){
                dto.setCreatHummerId(Long.valueOf( o[4].toString()));

            }
            if(o[5]!=null){
                dto.setPartId(Long.valueOf( o[5].toString()));
            }
            dto.setCreatDate((Date) o[6]);
            dtos.add(dto);

        }

        return dtos;
    }
}
