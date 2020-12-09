package com.hust.qlts.project.entity.excel;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceExcel {
    private String code;
    private Date dateAdd;
    private String codeGroup;
    private Integer status;
    private String partName;
    private String note;
    private Integer lostDevice;
    private String unitName;
    private Integer unit;
    private Integer sizeUnit;
    private String supplierName;
    private String wareHouseName;
    private String nameHummerUse;

}
