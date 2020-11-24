package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.*;

import java.util.List;

public interface DeviceGroupService {
   public Object creatDeviceGoup(DeviceGroupDto dto);
   public Object updateDeviceGroup(DeviceGroupDto dto, Integer id);
   public Object deleteDeviceGroup(Integer id);
   public boolean checkCode(String code);
   public DataPage<DeviceGroupDto> searchAsser(DeviceGroupDto dto);
   DeviceGroupFindDto getFindByCode(String code);
   List<DeviceGroupMaxCodeDto> getMaxCode(String code);
   List<DeviceGroupListDto> getList(Integer id);
}
