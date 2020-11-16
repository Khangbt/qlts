package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceService {
     boolean saveList(List<DeviceEntity> list);
     DataPage<DeviceDto> searList(DeviceDto dto);
     DeviceDto update(DeviceDto dto,Long id);
     boolean deleteDevice(Integer id);
}
