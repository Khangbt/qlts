package com.hust.qlts.project.dto;

import lombok.Data;

@Data

public class DeviceGroupMaxCodeDto {
    private Long id;
    private String code;
    private Integer size;
    private String maxCode;
    private String nextMaxCode;
}
