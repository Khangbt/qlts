package com.hust.qlts.project.dto;

import com.hust.qlts.project.dto.custom.ListDeviceAddDto;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class DeviceRequestAddDto {
    private Long id;
    private String code;
    private Long creatHummerId;
    private Integer status;
    private String note;
    private Date creatDate;
    private String reason;
    private Date approvedDate;
    private Long handlerHummerId;
    private List<ListDeviceAddDto> listDeviceR;
    private Long partId;
}
