package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.NotificetionDto;

public interface NotificetionService {
    NotificetionDto getNoticeUser(Long idHummer);
    NotificetionDto getNoticeAdmin(Long idHummer,Long partId);
    NotificetionDto getNoticeAdminAll(Long idHummer);

    Object updateNoticeUser(Long id);
    Object updateNoticeAdmin(Long idHumme,Long id);
    Object updateNoticeAdminAll(Long idHumme,Long id);

}
