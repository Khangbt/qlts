package com.hust.qlts.project.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumanResourcesDTO {

    private Long humanResourceId;

    private String code;

    private String fullName;

    private String email;

//    private Long departmentId;

    private Long positionId;

    private Integer status;

    private String note;

//    private Long majorId;

    private Long partId;

//    private Date dateRecruitment;

    private Integer dateGraduate;

//    private Integer dateMajor;

//    private String projectPaticipate;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private String username;

    private String password;

    private Integer isNew;

    private String verifyKey;

    private String newPassword;

    private String newPasswordConfirm;

    private List<String> lstPermission;

    private String role;

//    private Double resourcesUsed;

//    private String sgtFixedBy;

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    private Integer roleId;
}
