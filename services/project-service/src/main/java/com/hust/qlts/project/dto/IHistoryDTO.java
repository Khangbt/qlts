package com.hust.qlts.project.dto;

import java.util.Date;

/**
 * @author dangnp
 * @created 14/09/2020 - 10:02 AM
 * @project services
 **/
public interface IHistoryDTO {
    Long getHistoryId();

    Long getValueId();

    String getUserCreate();

    Integer getAction();

    Integer getTypeScreen();

    String getContent();

    Date getDate();

    String getUserName();

    String getRole();

    String getValueNew();

    String getValueOld();

    String getGroupPermissionName();

    String getGroupPermissionCode();

    String getGroupPermissionUserName();

    String getProjectName();

    String getRiskNo();
}
