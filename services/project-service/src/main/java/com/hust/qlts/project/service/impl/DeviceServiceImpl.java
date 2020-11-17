package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.repository.customreporsitory.DeviceCustomRepository;
import com.hust.qlts.project.repository.jparepository.DeviceRepository;
import com.hust.qlts.project.service.DeviceService;
import common.ConvetSetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCustomRepository deviceCustomRepository;

    @Override
    public boolean saveList(List<DeviceEntity> list) {
        deviceRepository.saveAll(list);
        return true;
    }

    @Override
    public DataPage<DeviceDto> searList(DeviceDto dto) {
        DataPage<DeviceDto> dtoDataPage = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        List<DeviceDto> list = new ArrayList<>();
        try {
            list = deviceCustomRepository.search(dto);
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
        public DeviceDto update(DeviceDto dto,Long id) {
            if (!deviceRepository.findById(id).isPresent()){
                return null;
            }
            DeviceEntity entity=deviceRepository.findById(id).get();
            entity= (DeviceEntity) ConvetSetData.xetData(entity,dto);
            assert entity != null;
            deviceRepository.save(entity);
        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(),deviceRepository.save(entity));
    }

    @Override
    public boolean deleteDevice(Integer id) {
        ///check dieedeuf kiên xóa
        return false;
    }

    @Override
    public DeviceFindDto getFindByCode(String code) {
        return deviceCustomRepository.getFindByCode(code);
    }
}
