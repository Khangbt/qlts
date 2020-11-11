package com.hust.qlts.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {

    private BigInteger assetID;
    private String name;
    private String code;
    private String information;
    private String price;
    private String unit;
    private String status;
    private Date dayinput;
    private String assetStatus;
    private Long warehouseID;

    private String warehouseName;
    private String warehouseAddress;
    private Integer page;

    private Integer pageSize;

    private Long totalRecord;
    private Long departmentId;
}
