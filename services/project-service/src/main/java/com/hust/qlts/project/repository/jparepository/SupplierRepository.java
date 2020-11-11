package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    @Query(value = "select * from SUPPLIER where CODE=?1 and STATUS=1", nativeQuery = true)
    SupplierEntity findByCode(String code);

    @Query(value = "select * from SUPPLIER where SUPPLIER_ID=?1 and STATUS=1", nativeQuery = true)
    SupplierEntity findByID(Long id);


    String sql = "select  "
            + "  sp.SUPPLIER_ID,  "
            + "  sp.CODE,  "
            + "  sp.NAME,  "
            + "  ap.PAR_NAME,  "
            + "  hr.FULLNAME,  "
            + "  sp.PHONENUMBER,  "
            + "  sp.EMAIL,  "
            + "  sp.ADDRESS,  "
            + " ps.NAME position,            "
            + "  sp.NOTE,  "
            + "  sp.WEBSITE  "

            + " from SUPPLIER as sp              "
            + " LEFT JOIN POSITION as ps on sp.POSITION_ID = ps.ID                "
            + "  left join APP_PARAMS as ap on sp.GROUP_SUPPLIER_ID = ap.APP_PARAMS_ID  "
            + "  left join HUMAN_RESOURCES as hr on sp.HUMAN_ID = hr.HUMAN_RESOURCES_ID "

            + "  where sp.STATUS != 0  and( sp.SUPPLIER_ID = :supplierId )"
        +" GROUP BY SUPPLIER_ID "
        +" ORDER BY sp.SUPPLIER_ID DESC ";
    @Query(value = sql, nativeQuery = true)
    SupplierEntity findById1(Long supplierId);

}
