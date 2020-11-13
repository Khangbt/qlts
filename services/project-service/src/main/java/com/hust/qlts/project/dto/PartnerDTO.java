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

    private String status;

    private BigInteger countHM;

    private String partName;

    private String provinceID;

    private String note;
    private Integer page;
    private Integer size;
    private Long totalRecord;
    private Long departmentId;
}
