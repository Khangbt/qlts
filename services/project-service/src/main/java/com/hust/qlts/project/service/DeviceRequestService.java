package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceRequestService{
    DataPage<DeviceRequestDTO> searList(DeviceRequestDTO dto);
    DeviceDto update(DeviceRequestDTO dto,Long id);
    boolean deleteDevice(Integer id);
    DeviceRequestDTO getFindByCode(String code);
    DeviceRequestDTO craet(DeviceRequestDTO dto);
}
