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
    private Integer page;
    private String parname;
    private Integer size;
    private Long totalRecord;
    private Long departmentId;
}
