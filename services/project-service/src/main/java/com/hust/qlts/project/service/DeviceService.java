package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceService {
     boolean saveList(List<DeviceEntity> list);
     DataPage<DeviceDto> searList(DeviceDto dto);
     DeviceDto update(DeviceDto dto,Long id);
     boolean deleteDevice(Long id);
     DeviceFindDto getFindByCode(String code);
     DeviceDto craet(DeviceDto dto);
     String getMaxCode(Long code);
     boolean updateListStatus(List<String> code);
     List<DeviceEntity> listSetStatus(List<Long> list);

     List<DeviceListIdDto> getList(Long Request,Long partId);

     List<ICusTomDto> getListIdHumme(Long id);


     List<ICusTomDto> getIdHummeReti(Long idHummer,Long partId);
     List<DeviceListIdDto> getListReturn(Long Request);

     boolean checkCode(String code);
     List<ICusTomDto> getListReturnbor(Long id);
     List<ICusTomDto> getIdHummeRetiByReque(Long idHummer,Long partId,Long idReque);
     List<ICusTomDto> getIdHummeRetiByIdStaue(Long idReque);
     byte[] exportXel(DeviceDto dto);

     List<DeviceEntity> getListyCode(Long id);
     DeviceDto lock(Long dto);

}
