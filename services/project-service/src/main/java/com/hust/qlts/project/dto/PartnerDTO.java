package com.hust.qlts.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDTO {
    private BigInteger id;

    private Long partnerID;

    private String code;

    private String customerPmName;

    private String customerEmail;

    private String amName;

    private String amEmail;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private String status;

    private BigInteger countHM;

    private String partname;

    private String provinceID;


    private String amPhone;

    private String customerPmPhone;
    private String note;

    private String prrovinceCode;

    private Integer page;
    private String groupSupplier;
    private String humanresources;

    private Integer pageSize;
    private Long totalRecord;
    private Long departmentId;
}
