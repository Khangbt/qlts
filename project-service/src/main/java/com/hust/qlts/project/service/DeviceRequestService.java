package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceEntity;

import java.util.List;

public interface DeviceRequestService{
    DataPage<DeviceRequestDTO> searList(DeviceRequestDTO dto);
    boolean deleteDevice(Long id);
    DeviceRequestDTO getFindByCode(Long code);
    DeviceRequestDTO craet(DeviceRequestDTO dto);
    DeviceRequestDTO update(DeviceRequestDTO dto,Long id);
    DeviceRequestDTO browserRequest(DeviceRequestDTO dto,Long id);
    DeviceRequestDTO cancelRequest(DeviceRequestDTO dto,Long id);
    IRequestDto getIdList(Long id);
}
