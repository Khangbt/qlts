package com.hust.qlts.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDTO {
    private Integer idWare;
    private BigInteger wearhouseID;
    private String fullName;
    private String code;
    private String note;
    private String address;
    private Long partId;
    private Long area_id;
    private String provinceID;
    private String groupSupplier;
    private Integer page;
    private String provincecode;
    private String humanresources;
    private String parname;
    private Integer pageSize;
    private Long totalRecord;
    private Long departmentId;
}
