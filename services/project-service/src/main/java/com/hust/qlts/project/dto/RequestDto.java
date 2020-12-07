package com.hust.qlts.project.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestDto {
    private Long id;
    private String code;
    private String tyle;
    private Long requestId;
    private Long creatHummerId;
    private Long partId;
    private Date creatDate;
    private Date handDate;
    private Integer status;
    private Long handlerHummerId;
    private String note;
    private String reason;
    private String nameCreat;
    private String handlerName;


    private Integer page;
    private Integer pageSize;

    private Long totalRecord;
    private String tyleDto;
}
