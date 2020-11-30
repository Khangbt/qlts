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


    private Integer page;
    private Integer pageSize;

    private Long totalRecord;
}
