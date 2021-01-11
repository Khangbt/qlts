package com.hust.qlts.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumanResourcesShowDTO {

    private BigInteger humanResourceId;

    private String code;

    private String Name;

    private String Password;

    private String phone;
    private Date dateOfBirth;
    private String username;

    private String parcode;

    private Long centerId;

    private Integer active;

    private String position;

    private Integer positionId;

    private Long partId;

    private String part;

//    private String deparment;

    private Integer status;

    private String statuss;

//    private String major;

    private Integer[] lstMajorId;

//    private Date daterecruitment;

    private Integer dategraduate;

//    private Integer experience;

//    private Integer datemajor;

//    private Integer majorexperience;

//    private String projectparticipate;

    private String email;

    private String note;

    private Integer page;

    private Integer pageSize;

    private Long totalRecord;
    private Long departmentId;
    private String password;
    private String tyleDto;
    private Integer isNew;

}
