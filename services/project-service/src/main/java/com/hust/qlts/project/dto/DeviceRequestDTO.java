package com.hust.qlts.project.dto;

import com.hust.qlts.project.dto.custom.ListDeviceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestDTO {
    private Long id;
    private String code;
    private Long creatHummerId;
    private Integer status;
    private Date startDateBorrow;
    private Date endDateBorrow;
    private String note;
    private Date creatDate;
    private String reason;
    private Date approvedDate;
    private Long handlerHummerId;
    private Long partId;
    private String nameCreat;
    private String nameHandler;
    private List<ListDeviceDto> listDeviceR;

}
