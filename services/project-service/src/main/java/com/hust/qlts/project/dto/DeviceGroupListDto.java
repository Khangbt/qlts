package com.hust.qlts.project.dto;

import lombok.Data;

@Data
public class DeviceGroupListDto {
    private Long id;
    private String code;
    private String name;
    private Integer size;
    private Integer sizeWarhous;
    private Integer unit;
    private Integer sizeUnit;
}
