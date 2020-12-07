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
    private Long warehouseId;
    private String partName;
    private Integer sizeUnit;
    private Integer unit;
    private Integer lostDevice;
    private String supperName;
    private String warehouseName;
    private Integer sizeWareHouse;
    private String nameOrCode;
    private Integer page;
    private Integer pageSize;
    private String tyleDto;
    private Long totalRecord;


}
