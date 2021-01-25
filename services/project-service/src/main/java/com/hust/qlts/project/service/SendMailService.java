package com.hust.qlts.project.service;

import com.hust.qlts.project.entity.DeviceRequestAddEntity;
import com.hust.qlts.project.entity.DeviceRequestEntity;
import com.hust.qlts.project.entity.DeviceRequestRetuEntitty;

public interface SendMailService {
    void  sendMailCandRequestGood(DeviceRequestEntity entity);

    void  sendMailCandRequestError(DeviceRequestEntity entity);


    void  sendMailCandRequestAddGood(DeviceRequestAddEntity entity);

    void  sendMailCandRequestAddError(DeviceRequestAddEntity entity);


    void  sendMailCandRequestRetuGood(DeviceRequestRetuEntitty entity);

    void  sendMailCandRequestRetuError(DeviceRequestRetuEntitty entity);
}
