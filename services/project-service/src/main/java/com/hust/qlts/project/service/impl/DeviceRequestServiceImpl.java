package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.dto.custom.ListDeviceDto;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.entity.DeviceRequestEntity;
import com.hust.qlts.project.entity.DeviceToRequestEntity;
import com.hust.qlts.project.repository.jparepository.DeviceRequestRepository;
import com.hust.qlts.project.repository.jparepository.DeviceToRequestRepository;
import com.hust.qlts.project.service.DeviceRequestService;
import com.hust.qlts.project.service.DeviceService;
import com.hust.qlts.project.service.DeviceToRequestService;
import common.Constants;
import common.ConvetSetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeviceRequestServiceImpl implements DeviceRequestService {
    @Autowired
    private DeviceRequestRepository deviceRequestRepository;
    @Autowired
    private RequestServiceImpl requestService;
    @Autowired
    private DeviceToRequestRepository deviceToRequestRepository;
    @Autowired
    private DeviceService deviceService;

    @Override
    public DataPage<DeviceRequestDTO> searList(DeviceRequestDTO dto) {
        return null;
    }

    @Override
    public DeviceDto update(DeviceRequestDTO dto, Long id) {
        return null;
    }

    @Override
    public boolean deleteDevice(Integer id) {
        return false;
    }

    @Override
    public DeviceRequestDTO getFindByCode(String code) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestDTO craet(DeviceRequestDTO dto) {
        DeviceRequestEntity requestEntity=new DeviceRequestEntity();
        requestEntity.setCreatDate(new Date());
        requestEntity.setCreatHummerId(dto.getCreatHummerId());
        requestEntity.setEndDateBorrow(dto.getEndDateBorrow());
        requestEntity.setStartDateBorrow(dto.getStartDateBorrow());
        requestEntity.setNote(dto.getNote());
        requestEntity.setStatus(Constants.CHUAXACNHAN);
        DeviceRequestEntity entity=deviceRequestRepository.save(requestEntity);
        List<DeviceToRequestEntity> list=new ArrayList<>();
        for (ListDeviceDto deviceDto:dto.getDeviceDtos()){
            for (int i = 0; i <deviceDto.getSize() ; i++) {
                DeviceToRequestEntity device=new DeviceToRequestEntity();
                device.setDeviceGroupId(deviceDto.getDeviceGroupId());
                device.setDeviceRequestId(entity.getId());
                device.setStatus(Long.valueOf(Constants.CHUAMUON));
                list.add(device);
            }

        }
        deviceToRequestRepository.saveAll(list);
        return (DeviceRequestDTO) ConvetSetData.xetData(new DeviceRequestDTO(),entity);
    }
}
