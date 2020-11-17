package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceGroupDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.entity.DeviceGroupEntity;
import com.hust.qlts.project.repository.customreporsitory.DeviceGroupCustomRepository;
import com.hust.qlts.project.repository.jparepository.DeviceGroupRepository;
import com.hust.qlts.project.service.DeviceGroupService;
import com.hust.qlts.project.service.DeviceService;
import common.Constants;
import common.ConvetSetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceGroupCustomRepository deviceGroupCustomRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object creatDeviceGoup(DeviceGroupDto dto) {
        DeviceGroupEntity entity = (DeviceGroupEntity) ConvetSetData.xetData(new DeviceGroupEntity(), dto);
        assert entity != null;
        DeviceGroupEntity entity1 = deviceGroupRepository.save(entity);
        List<DeviceEntity> list = new ArrayList<>();
        for (int i = 0; i < dto.getSizeId(); i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCode(creatCode(i, dto.getCode()));
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setPartId(dto.getPartId());
            deviceEntity.setSupplierId(dto.getSupplierId());
            deviceEntity.setIdEquipmentGroup(entity1.getId());
            deviceEntity.setWarehouseID(dto.getWarehouseID());
            deviceEntity.setStatus(Constants.TOT);
            list.add(deviceEntity);

        }

        deviceService.saveList(list);
        return ConvetSetData.xetData(dto, entity1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateDeviceGroup(DeviceGroupDto dto, Integer id) {
        if (!deviceGroupRepository.findById(Long.valueOf(id)).isPresent()) {
            return null;
        }
        DeviceGroupEntity entity = deviceGroupRepository.findById(Long.valueOf(id)).get();
        List<DeviceEntity> list = new ArrayList<>();
        for (int i = entity.getSizeId(); i < (dto.getSizeId()); i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCode(creatCode(i, dto.getCode()));
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setPartId(dto.getPartId());
            deviceEntity.setSupplierId(dto.getSupplierId());
            deviceEntity.setIdEquipmentGroup(entity.getId());
            deviceEntity.setWarehouseID(dto.getWarehouseID());
            deviceEntity.setStatus(Constants.TOT);
            list.add(deviceEntity);

        }
        DeviceGroupEntity entity1 = deviceGroupRepository.save((DeviceGroupEntity) Objects.requireNonNull(ConvetSetData.xetData(entity, dto)));
        deviceService.saveList(list);
        return entity1;
    }

    @Override
    public Object deleteDeviceGroup(Integer id) {
        ////////check dieu kien
        if (true) {
            deviceGroupRepository.deleteById(Long.valueOf(id));
            return "ok";
        }
        return null;
    }

    @Override
    public boolean checkCode(String code) {
        if (deviceGroupRepository.findByCodeCustom(code).size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public DataPage<DeviceGroupDto> searchAsser(DeviceGroupDto dto) {
        DataPage<DeviceGroupDto> dtoDataPage = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        List<DeviceGroupDto> list = new ArrayList<>();
        try {
            list = deviceGroupCustomRepository.search(dto);
            dtoDataPage.setData(list);

        } catch (Exception e) {
            throw e;
        }
        dtoDataPage.setPageIndex(dto.getPage());
        dtoDataPage.setPageSize(dto.getPageSize());
        dtoDataPage.setDataCount(dto.getTotalRecord());
        dtoDataPage.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (dtoDataPage.getDataCount() % dtoDataPage.getPageSize() != 0) {
            dtoDataPage.setPageCount(dtoDataPage.getPageCount() + 1);
        }
        return dtoDataPage;

    }

    @Override
    public DeviceGroupFindDto getFindByCode(String code) {
        return deviceGroupCustomRepository.findByCode(code);
    }

    private String creatCode(int id, String code) {
        StringBuilder codeData = new StringBuilder();
        codeData.append(code);
        for (int i = String.valueOf(id).length(); i < 4; i++) {
            codeData.append("0");
        }
        codeData.append(id);

        return codeData.toString();
    }
}
