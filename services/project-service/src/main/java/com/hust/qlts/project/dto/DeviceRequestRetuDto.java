package com.hust.qlts.project.dto;


import com.hust.qlts.project.dto.custom.ListDeviceDto;
import com.hust.qlts.project.dto.custom.ListDeviceRetuDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestRetuDto {
    private Long id;
    private String code;
    private Long creatHummerId;
    private Integer status;
    private String note;
    private Date creatDate;
    private String reason;
    private Date approvedDate;
    private Long handlerHummerId;
    private Long partId;
    private String nameCreat;
    private String nameHandler;
    private List<ListDeviceRetuDto> listDeviceR;

}
