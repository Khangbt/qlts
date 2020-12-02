package com.hust.qlts.project.dto;

import java.util.Date;

public interface IRequestDto {
    Long getId();
    String getCode();
    Long getPartId();
    Date getCreatDate();
    String getNote();
    Long getCreatHummerId();
    Date getApprovedDate();
    Long getHandlerHummerId();
    String getNameHandler();
    String getNamecreat();
    Date getEndDateBorrow();
    Date getStartDateBorrow();
    String getReason();
    Integer getStatus();
}
