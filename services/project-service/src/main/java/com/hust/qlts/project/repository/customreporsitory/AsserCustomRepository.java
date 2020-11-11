package com.hust.qlts.project.repository.customreporsitory;


import com.hust.qlts.project.dto.AssetDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class AsserCustomRepository  {
    private final Logger log = LogManager.getLogger(AsserCustomRepository.class);

    @Autowired
    private EntityManager em;

    public List<AssetDTO> searchAsser(AssetDTO dto){
        log.info("---------------------sql get kho nhan su------------------");

        StringBuilder sql = new StringBuilder();
        sql.append("select  ");
        sql.append(" wa.WAREHOUSE_ASSET_ID,   ");
        sql.append(" wa.NAME_ASSET,  ");
        sql.append(" wa.CODE,  ");
        sql.append(" wa.INFORMATION,   ");
        sql.append(" wa.PRICE, ");
        sql.append(" wa.UNIT, ");
        sql.append(" wa.STATUS,   ");
        sql.append(" wa.DAY_INPUT, ");
        sql.append(" wa.ASSER_STATUS, ");
        sql.append(" w.NAME,  ");
        sql.append(" w.ADDRESS ");

        sql.append(" from WAREHOUSE_ASSET  wa ");
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
    private List<AssetDTO> converEntytoDTO(List<Object[]> objects){
        List<AssetDTO> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(objects)) {
            for (Object[] obj : objects) {
                AssetDTO assetDTO = new AssetDTO();
                assetDTO.setAssetID((BigInteger) obj[0]);
                assetDTO.setName((String) obj[1]);
                assetDTO.setCode((String) obj[2]);
                assetDTO.setInformation((String) obj[3]);
                assetDTO.setPrice((String) obj[4]);
                assetDTO.setUnit((String) obj[5]);
                assetDTO.setStatus((String) obj[6]);
                assetDTO.setDayinput((Date) obj[7]);
                assetDTO.setAssetStatus((String) obj[8]);
                assetDTO.setWarehouseName((String) obj[9]);
                assetDTO.setWarehouseAddress((String) obj[10]);

                list.add(assetDTO);
            }
        }

        return list;
    }
}
