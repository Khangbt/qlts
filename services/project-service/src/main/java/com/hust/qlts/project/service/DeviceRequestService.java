package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceRequestService{
    DataPage<DeviceRequestDTO> searList(DeviceRequestDTO dto);
    boolean deleteDevice(Integer id);
    DeviceRequestDTO getFindByCode(String code);
    DeviceRequestDTO craet(DeviceRequestDTO dto);
    DeviceRequestDTO update(DeviceRequestDTO dto,Long id);
    DeviceRequestDTO browserRequest(DeviceRequestDTO dto,Long id);
    DeviceRequestDTO cancelRequest(DeviceRequestDTO dto,Long id);

}
