package com.hust.qlts.project.entity.excel;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceExcel {
    private Integer stt;
    private String code;
    private Date dateAdd;
    private String codeGroup;
    private String status;
    private String partName;
    private String note;
    private Integer lostDevice;
    private String unitName;
    private String unit;
    private Integer sizeUnit;
    private String supplierName;
    private String wareHouseName;
    private String wareHouseAdd;

    private String nameHummerUse;
    private String local;
    private String name;
    private String specifications;


}
