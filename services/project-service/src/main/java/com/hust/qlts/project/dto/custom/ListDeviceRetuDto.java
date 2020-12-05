package com.hust.qlts.project.dto.custom;

import lombok.Data;

import java.util.Date;

@Data
public class ListDeviceRetuDto {
    private Long deviceId;
    private Integer unit;
    private Integer lostDevice;
    private Long warehouseId;
    private Date dateBorrow;
}
