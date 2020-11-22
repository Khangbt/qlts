package com.hust.qlts.project.dto;


import lombok.Data;

@Data
public class DeviceGroupDto {

    private Long id;
    private String code;
    private String name;
    private String tyle;
    private Integer sizeId;
    private String specifications;
    private String note;
    private Integer size;
    private Long supplierId;
    private Long partId;
    private String location;
    private Long warehouseID;
    private String partName;
    private Integer sizeUnit;
    private String unit;
    private Integer lostDevice;
    private String supperName;
    private String warehouseName;
    private Integer sizeWareHouse;
    private Integer page;
    private Integer pageSize;

    private Long totalRecord;


}
