package com.hust.qlts.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.common.CoreUtils;
import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.dto.custom.ListDeviceRetuDto;
import com.hust.qlts.project.entity.*;
import com.hust.qlts.project.repository.jparepository.DeviceRequestRetuRepository;
import com.hust.qlts.project.repository.jparepository.DeviceToRequestRetuRepository;
import com.hust.qlts.project.repository.jparepository.HistoryRepository;
import com.hust.qlts.project.repository.jparepository.NotificetionRepository;
import com.hust.qlts.project.service.DeviceRequestRetuService;
import com.hust.qlts.project.service.DeviceService;
import com.hust.qlts.project.service.RequestService;
import com.hust.qlts.project.service.SendMailService;
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
public class DeviceRequestRetuServiceImpl implements DeviceRequestRetuService {
    @Autowired
    private NotificetionRepository notificetionRepository;
    @Autowired
    private DeviceRequestRetuRepository deviceRequestRetuRepository;
    @Autowired
    private DeviceToRequestRetuRepository deviceToRequestRetuRepository;
    @Autowired
    private RequestService requestService;
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private HistoryRepository historyRepository;
    @Override
    public DataPage<DeviceRequestRetuDto> searList(DeviceRequestRetuDto dto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public boolean deleteDevice(Long id) {
        if (!deviceRequestRetuRepository.findById(id).isPresent()) {
            return false;
        }
        DeviceRequestRetuEntitty deviceRequestEntity = deviceRequestRetuRepository.findById(id).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return false;
        }
        if (deviceRequestEntity.getStatus() == 1) {
            deviceRequestRetuRepository.deleteById(id);
            deviceToRequestRetuRepository.deleteByDeviceRequestIdRetu(id);
            return true;
        }
        return false;

    }

    @Override
    public DeviceRequestRetuDto getFindByCode(Long code) {
        DeviceRequestRetuDto dto = new DeviceRequestRetuDto();
        if (!deviceRequestRetuRepository.findById(code).isPresent()) {
            return null;
        }
        IRequestDto entitty = deviceRequestRetuRepository.getIdCustom(code);
        dto.setStatus(entitty.getStatus());
        dto.setId(entitty.getId());
        dto.setApprovedDate(entitty.getApprovedDate());
        dto.setCode(entitty.getCode());
        dto.setCreatDate(entitty.getCreatDate());
        dto.setCreatHummerId(entitty.getCreatHummerId());
        dto.setHandlerHummerId(entitty.getHandlerHummerId());
        dto.setNote(entitty.getNote());
        dto.setReason(entitty.getReason());
        dto.setPartId(entitty.getPartId());
        dto.setNameCreat(entitty.getNamecreat());
        dto.setNameHandler(entitty.getNameHandler());
        List<ListDeviceRetuDto> list = new ArrayList<>();
        List<ICusTomDto> device = deviceToRequestRetuRepository.getListbyidCustom(code);
        for (ICusTomDto retuEntitty : device) {
            ListDeviceRetuDto retuDto = new ListDeviceRetuDto();
            retuDto.setDeviceId(retuEntitty.getId());
            retuDto.setLostDevice(retuEntitty.getLostDevice());
            retuDto.setUnit(retuEntitty.getUnit());
            retuDto.setWarehouseId(retuEntitty.getWarehouseId());

            list.add(retuDto);
        }
        dto.setListDeviceR(list);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestRetuDto craet(DeviceRequestRetuDto dto) {
        DeviceRequestRetuEntitty entitty = new DeviceRequestRetuEntitty();
        String code = "PT" + CoreUtils.castDateToStringByPattern(new Date(), "MMdd") + CoreUtils.castDateToStringByPattern(new Date(), "hhmmss");
        entitty.setCode(code);
        entitty.setCreatHummerId(dto.getCreatHummerId());
        entitty.setCreatDate(new Date());
        entitty.setPartId(dto.getPartId());
        entitty.setNote(dto.getNote());
        entitty.setPartId(dto.getPartId());
        entitty.setStatus(Constants.CHUAXACNHAN);
        entitty.setCreatedDate(new Date());
        entitty.setLastModifiedDate(new Date());
        DeviceRequestRetuEntitty entitty1 = deviceRequestRetuRepository.save(entitty);

        List<DeviceToRequestRetuEntitty> device = new ArrayList<>();
        for (ListDeviceRetuDto retuDto : dto.getListDeviceR()) {
            DeviceToRequestRetuEntitty retuEntitty = new DeviceToRequestRetuEntitty();
            retuEntitty.setDeviceId(retuDto.getDeviceId());
            retuEntitty.setLostDevice(retuDto.getLostDevice());
            retuEntitty.setStatus(Constants.CHUAXACNHAN);
            retuEntitty.setDeviceRequestIdRetu(entitty1.getId());
            retuEntitty.setWarehouseId(retuDto.getWarehouseId());
            device.add(retuEntitty);
        }
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setCode(entitty.getCode());
        requestEntity.setCreatDate(entitty.getCreatDate());
        requestEntity.setCreatHummerId(entitty.getCreatHummerId());
        requestEntity.setIdRequest(entitty.getId());
        requestEntity.setPartId(entitty.getPartId());
        requestEntity.setTyle("PT");
        requestService.creat(requestEntity);
        deviceToRequestRetuRepository.saveAll(device);





        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã tạo phiếu trả  thiết bị" + entitty.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setTyle(Constants.PHIEUTRA);
        notificationEntity.setPartId(dto.getPartId());
        notificetionRepository.save(notificationEntity);

        return (DeviceRequestRetuDto) ConvetSetData.xetData(new DeviceRequestRetuDto(), entitty1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestRetuDto update(DeviceRequestRetuDto dto, Long id) {
        if (!deviceRequestRetuRepository.findById(dto.getId()).isPresent()) {
            return null;
        }

        DeviceRequestRetuEntitty deviceRequestEntity = deviceRequestRetuRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setLastModifiedDate(new Date());
        deviceRequestEntity.setCreatHummerId(dto.getCreatHummerId());
        deviceRequestEntity.setCreatDate(new Date());
        deviceRequestEntity.setPartId(dto.getPartId());
        deviceRequestEntity.setNote(dto.getNote());
        deviceRequestEntity.setPartId(dto.getPartId());
        deviceRequestEntity.setStatus(Constants.CHUAXACNHAN);
        List<DeviceToRequestRetuEntitty> device = deviceToRequestRetuRepository.getListbyid(deviceRequestEntity.getId());
        deviceToRequestRetuRepository.deleteAll(device);
        DeviceRequestRetuEntitty a = deviceRequestRetuRepository.save(deviceRequestEntity);
        List<DeviceToRequestRetuEntitty> list = new ArrayList<>();

        for (ListDeviceRetuDto retuDto : dto.getListDeviceR()) {
            DeviceToRequestRetuEntitty retuEntitty = new DeviceToRequestRetuEntitty();
            retuEntitty.setDeviceId(retuDto.getDeviceId());
            retuEntitty.setLostDevice(retuDto.getLostDevice());
            retuEntitty.setStatus(Constants.CHUAXACNHAN);
            retuEntitty.setDeviceRequestIdRetu(deviceRequestEntity.getId());
            retuEntitty.setWarehouseId(retuDto.getWarehouseId());
            list.add(retuEntitty);
        }
        deviceToRequestRetuRepository.saveAll(list);



        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã sửa phiếu trả  thiết bị" + deviceRequestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setTyle(Constants.PHIEUTRA);
        notificationEntity.setPartId(dto.getPartId());
        notificetionRepository.save(notificationEntity);

        return (DeviceRequestRetuDto) ConvetSetData.xetData(new DeviceRequestRetuDto(), a);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestRetuDto browserRequest(DeviceRequestRetuDto dto, Long id) {
        if (!deviceRequestRetuRepository.findById(dto.getId()).isPresent()) {
            return null;
        }
        DeviceRequestRetuEntitty deviceRequestEntity = deviceRequestRetuRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setStatus(Constants.XACNHAN);
        deviceRequestEntity.setReason(dto.getReason());
        deviceRequestEntity.setHandlerHummerId(dto.getHandlerHummerId());
        deviceRequestEntity.setApprovedDate(new Date());
        deviceRequestEntity.setLastModifiedDate(new Date());
        deviceRequestRetuRepository.save(deviceRequestEntity);


        List<Long> listId = new ArrayList<>();
        for (ListDeviceRetuDto retuDto : dto.getListDeviceR()) {
            listId.add(retuDto.getDeviceId());
        }
        List<DeviceToRequestRetuEntitty> device = deviceToRequestRetuRepository.getListbyid(deviceRequestEntity.getId());

        for (DeviceToRequestRetuEntitty entitty : device) {
            entitty.setStatus(2);
        }
        deviceToRequestRetuRepository.saveAll(device);
        List<DeviceEntity> deviceEntities = deviceService.listSetStatus(listId);
        for (DeviceEntity entity : deviceEntities) {
            Optional<ListDeviceRetuDto> retuDto = dto.getListDeviceR().stream().filter(a
                    -> a.getDeviceId().equals(entity.getId())).findAny();
            if (retuDto.isPresent()) {
                entity.setStatus(Constants.TRONGKHO);
                entity.setLostDevice(retuDto.get().getLostDevice());
                entity.setWarehouseId(retuDto.get().getWarehouseId());
                entity.setLastModifiedDate(new Date());

            }

        }
        deviceService.saveList(deviceEntities);

        sendMailService.sendMailCandRequestRetuGood(deviceRequestEntity);

        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã xác nhận phiếu cầu trả thiết bị  " + deviceRequestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setIdHummerShow(dto.getCreatHummerId());
        notificationEntity.setTyle(Constants.PHIEUTRA);
        notificationEntity.setNote2(dto.getHandlerHummerId());
        notificetionRepository.save(notificationEntity);

        return (DeviceRequestRetuDto) ConvetSetData.xetData(new DeviceRequestRetuDto(), deviceRequestEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestRetuDto cancelRequest(DeviceRequestRetuDto dto, Long id) {
        if (!deviceRequestRetuRepository.findById(dto.getId()).isPresent()) {
            return null;
        }

        DeviceRequestRetuEntitty deviceRequestEntity = deviceRequestRetuRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setHandlerHummerId(dto.getHandlerHummerId());
        deviceRequestEntity.setApprovedDate(new Date());
        deviceRequestEntity.setStatus(Constants.HUY);
        deviceRequestEntity.setReason(dto.getReason());
        deviceRequestEntity.setLastModifiedDate(new Date());
        List<DeviceToRequestRetuEntitty> device = deviceToRequestRetuRepository.getListbyid(deviceRequestEntity.getId());

        for (DeviceToRequestRetuEntitty entitty:device){
            entitty.setStatus(2);
        }

        deviceToRequestRetuRepository.saveAll(device);

        sendMailService.sendMailCandRequestRetuError(deviceRequestEntity);

        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã hủy phiếu cầu trả thiết bị  " + deviceRequestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setIdHummerShow(dto.getCreatHummerId());
        notificationEntity.setTyle(Constants.PHIEUTRA);
        notificationEntity.setNote2(dto.getHandlerHummerId());
        notificetionRepository.save(notificationEntity);

        return (DeviceRequestRetuDto) ConvetSetData.xetData(new DeviceRequestRetuDto(), deviceRequestRetuRepository.save(deviceRequestEntity));
    }


    private void saveHisory(Object oldValue,Object newValus,Long id,Long idHummer){
        HistoryEntity historyEntity=new HistoryEntity();
        historyEntity.setTypeScreen(Constants.REQUESTRETU);
        historyEntity.setUserCreate(idHummer);
        ObjectMapper objectMapper=new ObjectMapper();
        if(oldValue==null){
            historyEntity.setDate(new Date());
            historyEntity.setValueId(id);
            historyEntity.setAction(Constants.TAOMOI);

            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(newValus));

            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
            }
        }else if(newValus==null){
            historyEntity.setDate(new Date());
            historyEntity.setValueId(id);
            historyEntity.setAction(Constants.XOA);
            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(oldValue));

            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
            }
        }else {
            historyEntity.setDate(new Date());
            historyEntity.setValueId(id);
            historyEntity.setAction(Constants.SUA);
            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(newValus));
                historyEntity.setValueOld(objectMapper.writeValueAsString(oldValue));


            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
                historyEntity.setValueOld(null);
            }
        }
        historyRepository.save(historyEntity);
    }
}
