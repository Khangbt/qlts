package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceService {
     boolean saveList(List<DeviceEntity> list);
     DataPage<DeviceDto> searList(DeviceDto dto);
     DeviceDto update(DeviceDto dto,Long id);
     boolean deleteDevice(Integer id);
     DeviceFindDto getFindByCode(String code);
     DeviceDto craet(DeviceDto dto);
     String getMaxCode(Long code);
     boolean updateListStatus(List<String> code);
     List<DeviceEntity> listSetStatus(List<Long> list);

}
