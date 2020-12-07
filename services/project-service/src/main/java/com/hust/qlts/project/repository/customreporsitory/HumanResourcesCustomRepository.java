package com.hust.qlts.project.repository.customreporsitory;

import com.hust.qlts.project.dto.HumanResourcesDTO;
import com.hust.qlts.project.dto.HumanResourcesShowDTO;
import com.hust.qlts.project.dto.DTOSearch;
import common.AppParams;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//ANHTT_IIST
@Repository
public class HumanResourcesCustomRepository {
    @Autowired
    EntityManager em;

    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    //ANHTT_IIST api get danh sach nhan su theo ten hoac code
    public List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto) {
        log.info("-----------------------SQL lay thong tin nhan su theo tung chuc vu-----------------------");
        StringBuilder sql = new StringBuilder();

        sql.append("    select ");
        sql.append("    hr.HUMAN_RESOURCES_ID as humanResourceId ,                          ");
        sql.append("    hr.FULLNAME as fullname ,                                                   ");
        sql.append("    hr.CODE as  code ,                                                   ");
        sql.append("    hr.EMAIL as email                                                   ");
        sql.append("    FROM HUMAN_RESOURCES hr                                             ");
        sql.append("    left join POSITION p on hr.POSITION_ID= p.ID        ");
        sql.append("    where 1=1 and hr.STATUS=1      ");


        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            sql.append("  and (( lower(hr.CODE) LIKE :keySearch ) or ( lower(hr.FULLNAME) LIKE :keySearch ))");
        }
        if (StringUtils.isNotBlank(dto.getType())) {
            sql.append(" and lower(p.CODE) LIKE upper(:parCode)");
        }

        if (CollectionUtils.isNotEmpty(dto.getListHumanResources())) {
            sql.append(" and hr.HUMAN_RESOURCES_ID not in :listHumanResources ");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
        }
        if (StringUtils.isNotBlank(dto.getType())) {
            if (dto.getType() == AppParams.PM) {
                query.setParameter("parCode", AppParams.PM_ID);
            } else if (dto.getType().equals(AppParams.BA_MANAGER)) {
                query.setParameter("parCode", AppParams.BA_MANAGER_ID);
            } else if (dto.getType().equals(AppParams.QA_MANAGER)) {
                query.setParameter("parCode", AppParams.QA_MANAGER_ID);
            } else if (dto.getType().equals(AppParams.TEST_LEAD)) {
                query.setParameter("parCode", AppParams.TEST_LEAD_ID);
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getListHumanResources())) {
            query.setParameter("listHumanResources", dto.getListHumanResources());
        }

        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDto(lstObject);
    }

    //ANHTT_IIST convert object to dto
    public List<HumanResourcesDTO> convertObjectToDto(List<Object[]> lstObject) {
        log.info("------------------convert object to dto------------------");
        List<HumanResourcesDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesDTO resourcesDTO = new HumanResourcesDTO();
                resourcesDTO.setHumanResourceId(((BigInteger) obj[0]).longValue());
                resourcesDTO.setFullName((String) obj[1]);
                resourcesDTO.setUsername((String) obj[2]);
                resourcesDTO.setEmail((String) obj[3]);

                listDto.add(resourcesDTO);
            }
        }

        return listDto;
    }


    //ANHTT_IIST
    // lay tat ca cac quyen cua theo userId
    public List<String> getListPermissionByUserId(Long userId) {


        return null;
    }

    public List<HumanResourcesDTO> getListHumanResourceByProjectID(long id) {

        StringBuilder sql = new StringBuilder();

        sql.append("    SELECT ");
        sql.append("    hr.USERNAME as NAME ,                          ");
        sql.append("    hr.HUMAN_RESOURCES_ID as resourceId                                                    ");
        sql.append("    FROM HUMAN_RESOURCES hr                                             ");
        sql.append("    LEFT JOIN PROJECT_MEMBER pm ON hr.HUMAN_RESOURCES_ID=pm.HUMAN_RESOURCE_ID        ");
        sql.append("    WHERE pm.PROJECT_ID= (:id)       ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("id", id);
        List<Object[]> lstObject = query.getResultList();
        List<HumanResourcesDTO> listDto = new ArrayList<HumanResourcesDTO>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesDTO humanDto = new HumanResourcesDTO();
                humanDto.setUsername((String) obj[0]);
                humanDto.setHumanResourceId(((BigInteger) obj[1]).longValue());
                listDto.add(humanDto);
            }
        }
        return listDto;
    }

    public List<HumanResourcesShowDTO> getlistHumanResources(HumanResourcesShowDTO dto) {
        log.info("------------------------sql get list HumanResources--------------------------");

        StringBuilder sql = new StringBuilder();
        sql.append(" select    ");
        sql.append(" hr.HUMAN_RESOURCES_ID as humanResourceId,         ");
        sql.append(" hr.FULLNAME as name,             ");
        sql.append(" ps.NAME position,            ");
        sql.append(" pr.NAME as part,              ");
        sql.append(" hr.STATUS as active,                 ");
        sql.append(" hr.DATE_GRADUATE as dategraduate,            ");


        sql.append(" hr.EMAIL as email,      ");
        sql.append(" hr.NOTE as note,      ");
        sql.append(" hr.CODE as code,         ");
        sql.append(" hr.PASSWORD as password    ,     ");
        sql.append(" pr.ID as parId");
        sql.append(" from HUMAN_RESOURCES as hr              ");
        sql.append(" LEFT JOIN POSITION as ps on hr.POSITION_ID = ps.ID                ");
        sql.append(" LEFT JOIN PART as pr on hr.PART_ID = pr.ID              ");


        sql.append("  where hr.STATUS != 3 ");
        if (null != dto.getActive()) {
            sql.append(" and hr.STATUS = :active  ");
        }
        if (null != dto.getHumanResourceId()) {
            sql.append(" and( hr.HUMAN_RESOURCES_ID = :humanResourceId )");
        }
        if (null != dto.getPositionId()) {
            sql.append(" and( hr.POSITION_ID = :positionId )");
        }

        if (null != dto.getPartId()) {
            sql.append(" and( hr.PART_ID = :partId )");
        }



        sql.append(" GROUP BY HUMAN_RESOURCES_ID ");
        sql.append(" ORDER BY hr.HUMAN_RESOURCES_ID DESC ");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (null != dto.getActive()) {
            query.setParameter("active", dto.getActive());
            queryCount.setParameter("active", dto.getActive());
        }
        if (null != dto.getHumanResourceId()) {
            query.setParameter("humanResourceId", dto.getHumanResourceId());
            queryCount.setParameter("humanResourceId", dto.getHumanResourceId());
        }
        if (null != dto.getPositionId()) {
            query.setParameter("positionId", dto.getPositionId());
            queryCount.setParameter("positionId", dto.getPositionId());
        }
//        if (null != dto.getExperience()) {
//            query.setParameter("experience", dto.getExperience());
//            queryCount.setParameter("experience", dto.getExperience());
//        }
        if (null != dto.getPartId()) {
            query.setParameter("partId", dto.getPartId());
            queryCount.setParameter("partId", dto.getPartId());
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
    public List<HumanResourcesShowDTO> convertObjectToDtoShow(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<HumanResourcesShowDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesShowDTO humanResourcesDTO = new HumanResourcesShowDTO();
                humanResourcesDTO.setHumanResourceId((BigInteger) obj[0]);
                humanResourcesDTO.setName((String) obj[1]);
                humanResourcesDTO.setPosition((String) obj[2]);
                humanResourcesDTO.setPart((String) obj[3]);
                humanResourcesDTO.setStatus((Integer) obj[4]);
                if ((Integer) obj[4] == 1) {
                    humanResourcesDTO.setStatuss("Đang làm việc");
                } else if ((Integer) obj[4] == 2) {
                    humanResourcesDTO.setStatuss("Đã thôi việc");

                }

                humanResourcesDTO.setDategraduate((Integer) obj[5]);

                humanResourcesDTO.setEmail((String) obj[6]);
                humanResourcesDTO.setNote((String) obj[7]);
                humanResourcesDTO.setCode((String) obj[8]);
                humanResourcesDTO.setPassword((String) obj[9]);
                if(obj[10]!=null){
                    humanResourcesDTO.setPartId(Long.valueOf(String.valueOf(obj[10])));
                }
                humanResourcesDTO.setTyleDto("HUMMER");
                listDto.add(humanResourcesDTO);
            }
        }

        return listDto;
    }

    // get thong tin nhan su
    public List<HumanResourcesDTO> getHumanResources(DTOSearch dto) {
        log.info("-----------------------SQL lay thong tin nhan su theo ma nhan su , ten, hoac email-----------------------");
        StringBuilder sql = new StringBuilder();

        sql.append("    select ");
        sql.append("    hr.HUMAN_RESOURCES_ID as humanResourceId ,                          ");
        sql.append("    hr.CODE as  code ,                                                   ");
        sql.append("    hr.FULLNAME as name ,                                                   ");
        sql.append("    hr.EMAIL as email                                                  ");
        sql.append("    FROM HUMAN_RESOURCES as hr                                             ");
        sql.append("    where 1=1 and hr.STATUS != 3      ");


        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            if (null != dto.getIsSearchByCodeAndFullname() && dto.getIsSearchByCodeAndFullname() == 1L) {
                sql.append("   and (( upper(hr.CODE) LIKE :keySearch ) or ( upper(hr.FULLNAME) LIKE :keySearch )) ");
            } else {
                sql.append("   and (( upper(hr.CODE) LIKE :keySearch ) or ( upper(hr.EMAIL) LIKE :keySearch ) or ( upper(hr.FULLNAME) LIKE :keySearch )) ");
            }
        }


        Query query = em.createNativeQuery(sql.toString());

        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
        }

        List<Object[]> lstObject = query.getResultList();


        return convertHumanResourcesToDto(lstObject);
    }

    //TanNV convert object to dto
    public List<HumanResourcesDTO> convertHumanResourcesToDto(List<Object[]> lstObject) {
        log.info("------------------convert object to dto------------------");
        List<HumanResourcesDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesDTO resourcesDTO = new HumanResourcesDTO();
                resourcesDTO.setHumanResourceId(((BigInteger) obj[0]).longValue());
                resourcesDTO.setCode((String) obj[1]);
                resourcesDTO.setFullName((String) obj[2]);
                resourcesDTO.setEmail((String) obj[3]);
                listDto.add(resourcesDTO);
            }
        }
        return listDto;
    }



    public boolean checkAssociationBeforeDelete(Long id) {


        return true;
    }

}