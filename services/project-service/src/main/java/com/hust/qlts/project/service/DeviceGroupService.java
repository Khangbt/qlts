package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.AssetDTO;
import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.request.DeviceGroupReqDto;

public interface DeviceGroupService {
   public Object creatDeviceGoup(DeviceGroupReqDto dto);
   public Object updateDeviceGroup(DeviceGroupReqDto dto,Integer id);
   public Object deleteDeviceGroup(Integer id);
   public boolean checkCode(String code);
   public DataPage<DeviceGroupReqDto> searchAsser(DeviceGroupReqDto dto);
}
