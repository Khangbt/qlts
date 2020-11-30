package com.hust.qlts.project.service.impl;


import com.hust.qlts.project.common.CoreUtils;
import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceRequestAddDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.dto.ICusTomDto;
import com.hust.qlts.project.dto.custom.ListDeviceAddDto;
import com.hust.qlts.project.entity.*;
import com.hust.qlts.project.repository.jparepository.DeviceRequestAddRepository;
import com.hust.qlts.project.repository.jparepository.DeviceToRequestAddRepository;
import com.hust.qlts.project.service.DeviceGroupService;
import com.hust.qlts.project.service.DeviceRequestAddService;
import com.hust.qlts.project.service.DeviceService;
import com.hust.qlts.project.service.RequestService;
import common.Constants;
import common.ConvetSetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceRequestAddServiceImpl implements DeviceRequestAddService {
    @Autowired
    private DeviceRequestAddRepository deviceRequestAddRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceToRequestAddRepository deviceToRequestAddRepository;
    @Autowired
    private RequestService requestService;
    @Autowired
    private DeviceGroupService deviceGroupService;

    @Override
    public DataPage<DeviceRequestAddDto> searList(DeviceRequestAddDto dto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDevice(Long id) {
        if (!deviceRequestAddRepository.findById(id).isPresent()) {
            return false;
        }
        DeviceRequestAddEntity deviceRequestEntity = deviceRequestAddRepository.findById(id).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return false;
        }
        if(deviceRequestEntity.getStatus()==1){
        deviceRequestAddRepository.deleteById(id);
        deviceToRequestAddRepository.deleteByDeviceRequestAddId(id);

        return true;
        }
        return false;
    }

    @Override
    public DeviceRequestAddDto getFindByCode(Long code) {
        DeviceRequestAddDto dto=new DeviceRequestAddDto();
        if(!deviceRequestAddRepository.findById(code).isPresent()){
            return null;
        }
        DeviceRequestAddEntity entity=deviceRequestAddRepository.findById(code).get();
        dto.setId(entity.getId());
        dto.setApprovedDate(entity.getProcessingDate());
        dto.setCode(entity.getCode());
        dto.setCreatDate(entity.getCreatDate());
        dto.setCreatHummerId(entity.getCreatHummerId());
        dto.setHandlerHummerId(entity.getHandlerHummerId());
        dto.setNote(entity.getNote());
        dto.setReason(entity.getReason());
        dto.setPartId(entity.getPartId());
        dto.setStatus(entity.getStatus());
        List<DeviceToRequestAddEntity> device=deviceToRequestAddRepository.getListAll(code);
        List<Long> listId=new ArrayList<>();;
        for (DeviceToRequestAddEntity addEntity:device){
            listId.add(addEntity.getDeviceGroup());
        }

        List<ICusTomDto> deviceGroupEntities=deviceGroupService.getAllListId(listId);
        List<ListDeviceAddDto> deviceAddDtos=new ArrayList<>();
        for (DeviceToRequestAddEntity addEntity:device){
            ListDeviceAddDto addDto=new ListDeviceAddDto();
            addDto.setId(addEntity.getId());
            addDto.setIdGroup(addEntity.getDeviceGroup());
            addDto.setPrice(addEntity.getPrice());
            addDto.setSize(addEntity.getSize());
            addDto.setSupplierId(addEntity.getSupplierId());
            if(deviceGroupEntities.stream().
                    filter(a -> a.getId().equals(addDto.getIdGroup())).findFirst().isPresent()){
                ICusTomDto groupEntity = deviceGroupEntities.stream().
                        filter(a -> a.getId().equals(addDto.getIdGroup())).findFirst().get();
                addDto.setSizeUnit(Long.valueOf(groupEntity.getSizeUnit()));
                addDto.setUnit(Long.valueOf(groupEntity.getUnit()));
            }

            deviceAddDtos.add(addDto);
        }
        dto.setListDeviceR(deviceAddDtos);



        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public DeviceRequestAddDto craet(DeviceRequestAddDto dto) {
        DeviceRequestAddEntity deviceRequestAddEntity = new DeviceRequestAddEntity();
        deviceRequestAddEntity.setCreatDate(new Date());
        String code = "YCM" + CoreUtils.castDateToStringByPattern(new Date(), "yyMMdd");
        deviceRequestAddEntity.setCode(code);
        deviceRequestAddEntity.setStatus(1);
        deviceRequestAddEntity.setHandlerHummerId(dto.getCreatHummerId());
        deviceRequestAddEntity.setNote(dto.getNote());
        deviceRequestAddEntity.setPartId(dto.getPartId());


        DeviceRequestAddEntity entity = deviceRequestAddRepository.save(deviceRequestAddEntity);
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setCode(entity.getCode());
        requestEntity.setCreatDate(entity.getCreatDate());
        requestEntity.setCreatHummerId(entity.getCreatHummerId());
        requestEntity.setIdRequest(entity.getId());
        requestEntity.setPartId(entity.getPartId());
        requestEntity.setTyle("YCM");
        requestService.creat(requestEntity);
        List<DeviceToRequestAddEntity> addEntityList = new ArrayList<>();


        for (ListDeviceAddDto addDto : dto.getListDeviceR()) {
            DeviceToRequestAddEntity device = new DeviceToRequestAddEntity();
            device.setDeviceRequestAddId(entity.getId());
            device.setDeviceGroup(addDto.getIdGroup());
            device.setPrice(addDto.getPrice());
            device.setStatus(Constants.CHUAXACNHAN);
            device.setSize(addDto.getSize());
            device.setSupplierId(addDto.getSupplierId());
            addEntityList.add(device);
        }
        deviceToRequestAddRepository.saveAll(addEntityList);
        return (DeviceRequestAddDto) ConvetSetData.xetData(new DeviceRequestAddDto(), entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestAddDto update(DeviceRequestAddDto dto, Long id) {
        if (!deviceRequestAddRepository.findById(dto.getId()).isPresent()) {
            return null;
        }
        DeviceRequestAddEntity deviceRequestEntity = deviceRequestAddRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setPartId(dto.getPartId());
        deviceRequestEntity.setNote(dto.getNote());
        DeviceRequestAddEntity addEntity = deviceRequestAddRepository.save(deviceRequestEntity);
        List<DeviceToRequestAddEntity> requestAddEntityList = deviceToRequestAddRepository.getListAll(id);
        deviceToRequestAddRepository.deleteAll(requestAddEntityList);
        List<DeviceToRequestAddEntity> addEntityList = new ArrayList<>();
        for (ListDeviceAddDto addDto : dto.getListDeviceR()) {
            DeviceToRequestAddEntity device = new DeviceToRequestAddEntity();
            device.setDeviceRequestAddId(addEntity.getId());
            device.setDeviceGroup(addDto.getIdGroup());
            device.setPrice(addDto.getPrice());
            device.setStatus(Constants.CHUAXACNHAN);
            device.setSize(addDto.getSize());
            device.setSupplierId(addDto.getSupplierId());
            addEntityList.add(device);
        }
        deviceToRequestAddRepository.saveAll(addEntityList);
        return (DeviceRequestAddDto) ConvetSetData.xetData(new DeviceRequestAddDto(), addEntity);
    }

    @Override
    public DeviceRequestAddDto browserRequest(DeviceRequestAddDto dto, Long id) {
        if (!deviceRequestAddRepository.findById(id).isPresent()) {
            return null;
        }
        DeviceRequestAddEntity requestEntity = deviceRequestAddRepository.findById(id).get();
        if (requestEntity.getStatus().equals(Constants.XACNHAN) || requestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        List<DeviceToRequestAddEntity> listAll = deviceToRequestAddRepository.getListAll(dto.getId());
        List<Long> strings = new ArrayList<>();
        for (DeviceToRequestAddEntity device : listAll) {
            strings.add(device.getDeviceGroup());
        }
        List<ICusTomDto> list = deviceGroupService.getAllListId(strings);
        List<DeviceGroupEntity> entityList = deviceGroupService.getAllListLong(strings);
        List<DeviceEntity> deviceEntities = new ArrayList<>();
        List<DeviceGroupEntity> deviceGroupEntities = new ArrayList<>();
        for (DeviceToRequestAddEntity device : listAll) {
            Optional<ICusTomDto> tomDto = list.stream().filter(a -> a.getId().equals(device.getDeviceGroup())).findFirst();
            Optional<DeviceGroupEntity> groupEntity = entityList.stream().filter(a -> a.getId().equals(device.getDeviceGroup())).findFirst();
            if (tomDto.isPresent() && groupEntity.isPresent()) {
                for (int i = 0; i < device.getSize(); i++) {
                    DeviceEntity entity = new DeviceEntity();
                    entity.setRequestAddId(requestEntity.getId());
                    entity.setUnit(tomDto.get().getUnit());
                    entity.setSizeUnit(tomDto.get().getSizeUnit());
                    entity.setPartId(dto.getPartId());
                    entity.setIdEquipmentGroup(device.getDeviceGroup());
                    entity.setDateAdd(new Date());
                    entity.setExist(true);
                    entity.setLostDevice(100);
                    entity.setSupplierId(device.getSupplierId());
                    entity.setCode(creatCode(groupEntity.get().getSizeId() + i, groupEntity.get().getCode()));
                    entity.setStatus(Constants.TRONGKHO);
                    entity.setWarehouseId(device.getWarehouseId());
                    deviceEntities.add(entity);

                }
                groupEntity.get().setSizeId(tomDto.get().getSizeId() + Integer.parseInt(String.valueOf(device.getSize())));
                deviceGroupEntities.add(groupEntity.get());
            }


        }
        requestEntity.setStatus(Constants.XACNHAN);
        requestEntity.setReason(dto.getReason());
        requestEntity.setHandlerHummerId(dto.getHandlerHummerId());
        requestEntity.setProcessingDate(new Date());

//        deviceRequestAddRepository.save(requestEntity);
//        deviceService.saveList(deviceEntities);
//        deviceGroupService.saveList(deviceGroupEntities);
//        return (DeviceRequestAddDto) ConvetSetData.xetData(new DeviceRequestAddDto(), deviceRequestAddRepository.save(requestEntity));

        return null;
    }

    @Override
    public DeviceRequestAddDto cancelRequest(DeviceRequestAddDto dto, Long id) {
        if (!deviceRequestAddRepository.findById(dto.getId()).isPresent()) {
            return null;
        }
        DeviceRequestAddEntity deviceRequestEntity = deviceRequestAddRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN)) {
            return null;
        }
        deviceRequestEntity.setStatus(Constants.HUY);
        deviceRequestEntity.setReason(dto.getReason());
        return (DeviceRequestAddDto) ConvetSetData.xetData(new DeviceRequestAddDto(), deviceRequestAddRepository.save(deviceRequestEntity));
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
