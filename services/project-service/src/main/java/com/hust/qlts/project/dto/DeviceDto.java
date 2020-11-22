package com.hust.qlts.project.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private Long id;
    private String code;
    private String name;
    private String tyle;
    private String specifications;
    private String note;

    private Long supplierId;
    private Long partId;
    private String location;
    private Long warehouseID;
    private String hummer;
    private Integer status;
    private String partName;
    private Integer sizeUnit;
    private String unit;
    private Integer lostDevice;


    private Integer page;
    private Integer pageSize;

    private Long totalRecord;
}
