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
    private String nodeDevice;
    private String nodeGroup;
    private Long supplierId;
    private Long partId;
    private String location;
    private Long warehouseId;
    private String hummer;
    private Integer status;
    private String partName;
    private Integer sizeUnit;
    private Integer unit;
    private Integer lostDevice;
    private Long useHummerId;
    private String useHummerName;
    private String reQuest;
    private String supperName;
    private String wareHouseName;
    private String handlerHummer;
    private Long idEquipmentGroup;
    private String codeDeviceRequest;
    private String nameOrCode;
    private Integer page;
    private Integer pageSize;
    private Integer size;
    private boolean check;
    private Long totalRecord;
    private String tyleDto;
}
