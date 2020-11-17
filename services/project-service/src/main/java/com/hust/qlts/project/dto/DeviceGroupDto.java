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

    private Long supplierId;
    private String partId;
    private String location;
    private Long warehouseID;


    private Integer page;
    private Integer pageSize;

    private Long totalRecord;


}
