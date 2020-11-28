package com.hust.qlts.project.service;


import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceRequestAddDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;

public interface DeviceRequestAddService{

    DataPage<DeviceRequestAddDto> searList(DeviceRequestAddDto dto);
    boolean deleteDevice(Integer id);
    DeviceRequestAddDto getFindByCode(String code);
    DeviceRequestAddDto craet(DeviceRequestAddDto dto);
    DeviceRequestAddDto update(DeviceRequestAddDto dto,Long id);
    DeviceRequestAddDto browserRequest(DeviceRequestAddDto dto,Long id);
    DeviceRequestAddDto cancelRequest(DeviceRequestAddDto dto,Long id);

}
