package com.hust.qlts.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.common.CoreUtils;
import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.dto.IListDeviceToReDto;
import com.hust.qlts.project.dto.IRequestDto;
import com.hust.qlts.project.dto.custom.ListDeviceDto;
import com.hust.qlts.project.entity.*;
import com.hust.qlts.project.repository.jparepository.DeviceRequestRepository;
import com.hust.qlts.project.repository.jparepository.DeviceToRequestRepository;
import com.hust.qlts.project.repository.jparepository.HistoryRepository;
import com.hust.qlts.project.repository.jparepository.NotificetionRepository;
import com.hust.qlts.project.service.DeviceRequestService;
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
import java.util.stream.Collectors;

@Service
public class DeviceRequestServiceImpl implements DeviceRequestService {
    @Autowired
    private DeviceRequestRepository deviceRequestRepository;
    @Autowired
    private RequestService requestService;
    @Autowired
    private DeviceToRequestRepository deviceToRequestRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private NotificetionRepository notificetionRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public DataPage<DeviceRequestDTO> searList(DeviceRequestDTO dto) {
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDevice(Long id) {

        if (!deviceRequestRepository.findById(id).isPresent()) {
            return false;
        }

        DeviceRequestEntity deviceRequestEntity = deviceRequestRepository.findById(id).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return false;
        }
        if (deviceRequestEntity.getStatus() == 1) {
            deviceRequestRepository.deleteById(id);
            deviceToRequestRepository.deleteByDeviceRequestId(id);
            return true;
        }
        return false;
    }

    @Override
    public DeviceRequestDTO getFindByCode(Long code) {
        DeviceRequestDTO dto = new DeviceRequestDTO();
        if (!deviceRequestRepository.findById(code).isPresent()) {
            return null;
        }
        IRequestDto entity = deviceRequestRepository.getIdCustom(code);
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setApprovedDate(entity.getApprovedDate());
        System.out.println(entity.getCreatDate());
        dto.setCreatDate(entity.getCreatDate());
        dto.setCreatHummerId(entity.getCreatHummerId());
        dto.setHandlerHummerId(entity.getHandlerHummerId());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        dto.setEndDateBorrow(entity.getEndDateBorrow());
        dto.setStartDateBorrow(entity.getStartDateBorrow());
        dto.setNote(entity.getNote());
        dto.setPartId(entity.getPartId());
        dto.setNameCreat(entity.getNamecreat());
        dto.setNameHandler(entity.getNameHandler());
        List<ListDeviceDto> list = new ArrayList<>();

        List<IListDeviceToReDto> device = deviceToRequestRepository.getListAllIdCustom(code);
        for (IListDeviceToReDto entity1 : device) {
            ListDeviceDto dto1 = new ListDeviceDto();
            dto1.setIdGroup(entity1.getGroupId());
            dto1.setQuantity(entity1.getSize());
            dto1.setUnit(entity1.getUnit());
            String s = entity1.getListLongId();
            if (s != null) {
                String[] strings = s.split(",");
                List<Long> longList = new ArrayList<>();
                for (String a1 : strings) {
                    longList.add(Long.valueOf(a1));
                }
                dto1.setListCode(longList);
            }


            list.add(dto1);
        }
        dto.setListDeviceR(list);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestDTO craet(DeviceRequestDTO dto) {
        RequestEntity requestEntity = new RequestEntity();
        DeviceRequestEntity deviceRequestEntity = new DeviceRequestEntity();
        deviceRequestEntity.setCreatDate(new Date());
        String code = "PYCM" + CoreUtils.castDateToStringByPattern(new Date(), "MMdd") + CoreUtils.castDateToStringByPattern(new Date(), "hhmmss");
        deviceRequestEntity.setCode(code);
        deviceRequestEntity.setCreatHummerId(dto.getCreatHummerId());
        deviceRequestEntity.setEndDateBorrow(dto.getEndDateBorrow());
        deviceRequestEntity.setStartDateBorrow(dto.getStartDateBorrow());
        deviceRequestEntity.setNote(dto.getNote());
        deviceRequestEntity.setPartId(dto.getPartId());
        deviceRequestEntity.setCreatedDate(new Date());
        deviceRequestEntity.setLastModifiedDate(new Date());
        deviceRequestEntity.setStatus(Constants.CHUAXACNHAN);
        DeviceRequestEntity entity = deviceRequestRepository.save(deviceRequestEntity);
        List<DeviceToRequestEntity> list = new ArrayList<>();
        for (ListDeviceDto deviceDto : dto.getListDeviceR()) {
            for (int i = 0; i < deviceDto.getQuantity(); i++) {
                DeviceToRequestEntity device = new DeviceToRequestEntity();
                device.setDeviceGroupId(deviceDto.getIdGroup());
                device.setDeviceRequestId(entity.getId());
                device.setStatus(Long.valueOf(Constants.CHUAMUON));
                list.add(device);
            }

        }
        requestEntity.setCode(entity.getCode());
        requestEntity.setCreatDate(entity.getCreatDate());
        requestEntity.setCreatHummerId(entity.getCreatHummerId());
        requestEntity.setIdRequest(entity.getId());
        requestEntity.setPartId(entity.getPartId());
        requestEntity.setTyle("PYCM");
        deviceToRequestRepository.saveAll(list);

        requestService.creat(requestEntity);


        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã tạo phiếu cầu mượng thiết bị " + entity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setTyle(Constants.PHIEUMUON);
        notificationEntity.setPartId(dto.getPartId());
        notificetionRepository.save(notificationEntity);


        return (DeviceRequestDTO) ConvetSetData.xetData(new DeviceRequestDTO(), entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceRequestDTO update(DeviceRequestDTO dto, Long id) {
        if (!deviceRequestRepository.findById(dto.getId()).isPresent()) {
            return null;
        }

        DeviceRequestEntity deviceRequestEntity = deviceRequestRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setLastModifiedDate(new Date());
        deviceRequestEntity.setEndDateBorrow(dto.getEndDateBorrow());
        deviceRequestEntity.setStartDateBorrow(dto.getStartDateBorrow());
        deviceRequestEntity.setPartId(dto.getPartId());
        deviceRequestEntity.setNote(dto.getNote());
        DeviceRequestEntity requestEntity = deviceRequestRepository.save(deviceRequestEntity);
        List<DeviceToRequestEntity> listAll = deviceToRequestRepository.getListAll(dto.getId());
        deviceToRequestRepository.deleteAll(listAll);
        List<DeviceToRequestEntity> deviceToRequestEntities = new ArrayList<>();
        for (ListDeviceDto deviceDto : dto.getListDeviceR()) {
            for (int i = 0; i < deviceDto.getQuantity(); i++) {
                DeviceToRequestEntity device = new DeviceToRequestEntity();
                device.setDeviceGroupId(deviceDto.getIdGroup());
                device.setDeviceRequestId(deviceRequestEntity.getId());
                device.setStatus(Long.valueOf(Constants.CHUAMUON));
                deviceToRequestEntities.add(device);
            }

        }
        deviceToRequestRepository.saveAll(deviceToRequestEntities);

        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã cập nhập phiếu cầu mượng thiết bị  " + deviceRequestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setPartId(dto.getPartId());
        notificationEntity.setTyle(Constants.PHIEUMUON);
        notificetionRepository.save(notificationEntity);

        return (DeviceRequestDTO) ConvetSetData.xetData(new DeviceRequestDTO(), requestEntity);
    }

    @Override
    public DeviceRequestDTO browserRequest(DeviceRequestDTO dto, Long id) {
        List<Long> list = new ArrayList<>();
        if (!deviceRequestRepository.findById(id).isPresent()) {
            return null;
        }
        DeviceRequestEntity requestEntity = deviceRequestRepository.findById(id).get();
        if (requestEntity.getStatus().equals(Constants.XACNHAN) || requestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        List<DeviceToRequestEntity> listAll = deviceToRequestRepository.getListAll(dto.getId());
        for (ListDeviceDto deviceDto : dto.getListDeviceR()) {
            list.addAll(deviceDto.getListCode());
            List<DeviceToRequestEntity> device = listAll.stream().filter(deviceToRequestEntity ->
                    deviceToRequestEntity.getDeviceGroupId().equals(deviceDto.getIdGroup())).collect(Collectors.toList());
            for (int i = 0; i < device.size(); i++) {
                if (i < deviceDto.getListCode().size()) {
                    device.get(i).setStatus(Long.valueOf(Constants.DANGMUON));
                    device.get(i).setDeviceId(deviceDto.getListCode().get(i));
                }
            }
        }

        List<DeviceEntity> deviceEntities = deviceService.listSetStatus(list);
        for (DeviceEntity entity : deviceEntities) {
            entity.setUseHummerId(dto.getCreatHummerId());
            entity.setStatus(Constants.DANGSUDUNG);
            entity.setLastModifiedDate(new Date());
        }
        requestEntity.setLastModifiedDate(new Date());
        requestEntity.setReason(dto.getReason());
        requestEntity.setStatus(Constants.XACNHAN);
        requestEntity.setHandlerHummerId(dto.getHandlerHummerId());
        requestEntity.setApprovedDate(new Date());
        deviceToRequestRepository.saveAll(listAll);
        deviceRequestRepository.save(requestEntity);
        deviceService.saveList(deviceEntities);


        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã xác nhận yêu cầu  phiếu cầu mượn thiết bị mã phiếu " + requestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setIdHummerShow(dto.getCreatHummerId());
        notificationEntity.setNote2(dto.getHandlerHummerId());
        notificationEntity.setTyle(Constants.PHIEUMUON);
        notificetionRepository.save(notificationEntity);



        return (DeviceRequestDTO) ConvetSetData.xetData(new DeviceRequestDTO(), deviceRequestRepository.save(requestEntity));
//        return null;
    }

    @Override
    public DeviceRequestDTO cancelRequest(DeviceRequestDTO dto, Long id) {
        if (!deviceRequestRepository.findById(dto.getId()).isPresent()) {
            return null;
        }

        DeviceRequestEntity deviceRequestEntity = deviceRequestRepository.findById(dto.getId()).get();
        if (deviceRequestEntity.getStatus().equals(Constants.XACNHAN) || deviceRequestEntity.getStatus().equals(Constants.HUY)) {
            return null;
        }
        deviceRequestEntity.setHandlerHummerId(dto.getHandlerHummerId());
        deviceRequestEntity.setApprovedDate(new Date());
        deviceRequestEntity.setStatus(Constants.HUY);
        deviceRequestEntity.setReason(dto.getReason());

        NotificetionEntity notificationEntity = new NotificetionEntity();
        notificationEntity.setConten(" đã hủy phiếu cầu mượng thiết bị trong mã phiếu" + deviceRequestEntity.getCode());
        notificationEntity.setNote1(dto.getCreatHummerId());
        notificationEntity.setIdHummerShow(dto.getCreatHummerId());
        notificationEntity.setNote2(dto.getHandlerHummerId());
        notificationEntity.setTyle(Constants.PHIEUMUON);
        notificetionRepository.save(notificationEntity);


        return (DeviceRequestDTO) ConvetSetData.xetData(new DeviceRequestDTO(), deviceRequestRepository.save(deviceRequestEntity));
    }

    @Override
    public IRequestDto getIdList(Long id) {
        return null;
    }

    private void saveHisory(Object oldValue, Object newValus, Long id, Long idHummer) {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setTypeScreen(Constants.REQUEST);
        historyEntity.setUserCreate(idHummer);
        ObjectMapper objectMapper = new ObjectMapper();
        if (oldValue == null) {
            historyEntity.setDate(new Date());
            historyEntity.setValueId(id);
            historyEntity.setAction(Constants.TAOMOI);

            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(newValus));

            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
            }
        } else if (newValus == null) {
            historyEntity.setDate(new Date());
            historyEntity.setValueId(id);
            historyEntity.setAction(Constants.XOA);
            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(oldValue));

            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
            }
        } else {
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
