package com.hust.qlts.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDTO {
    private Long historyId;
    private Long valueId;
    private Long userCreate;
    private Integer action;
    private Integer typeScreen;
    private String content;
    private Date date;
    private String userName;
    private String role;
    private String valueNew;
    private String valueOld;
    private String groupPermissionName;
    private String groupPermissionUserName;
    private String projectName;
    private String rolePermission;
}
