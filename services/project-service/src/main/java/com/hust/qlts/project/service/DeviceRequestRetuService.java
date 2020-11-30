package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceRequestAddDto;
import com.hust.qlts.project.dto.DeviceRequestRetuDto;
import org.springframework.stereotype.Service;


public interface DeviceRequestRetuService {
    DataPage<DeviceRequestRetuDto> searList(DeviceRequestRetuDto dto);
    boolean deleteDevice(Long id);
    DeviceRequestRetuDto getFindByCode(Long code);
    DeviceRequestRetuDto craet(DeviceRequestRetuDto dto);
    DeviceRequestRetuDto update(DeviceRequestRetuDto dto,Long id);
    DeviceRequestRetuDto browserRequest(DeviceRequestRetuDto dto,Long id);
    DeviceRequestRetuDto cancelRequest(DeviceRequestRetuDto dto,Long id);
}
