package com.hust.qlts.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

// danh muc nha cung cap
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SupplierDTO {
    private BigInteger supplierId;// auto-increment
    private BigInteger humanResourceId;// auto-increment
    private String code;  //required unique
    private String fullName;  //required
    private String type;  //required
    private String position; //required
    private String phone; //required
    private String email; //required
    private String fax;
    private String website;
    private String area; //required
    private String address;
    private String note;
    private BigInteger positionId;
    private Integer status;


    private String nameOrCode;
    private String nameHummer;


    private String groupSupplier;
    private String humanresources;
    private Integer page;
    private Integer pageSize;

    private Long totalRecord;
    private Long departmentId;

}
