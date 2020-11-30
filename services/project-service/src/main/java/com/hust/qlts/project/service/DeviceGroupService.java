package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceGroupEntity;

import java.util.List;

public interface DeviceGroupService {
    Object creatDeviceGoup(DeviceGroupDto dto);

    Object updateDeviceGroup(DeviceGroupDto dto, Integer id);

    Object deleteDeviceGroup(Integer id);

    boolean checkCode(String code);

    DataPage<DeviceGroupDto> searchAsser(DeviceGroupDto dto);

    DeviceGroupFindDto getFindByCode(Long code);

    List<DeviceGroupMaxCodeDto> getMaxCode(String code);

    List<DeviceGroupListDto> getList(Integer id);

    List<IDeviceGroupMaxCodeDto> listAll();

    List<ICusTomDto> getAllListId(List<Long> id);

    List<DeviceGroupEntity> getAllListLong(List<Long> id);

    List<DeviceGroupEntity> saveList(List<DeviceGroupEntity> list);
}
