package com.hust.qlts.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.entity.DeviceGroupEntity;
import com.hust.qlts.project.entity.HistoryEntity;
import com.hust.qlts.project.repository.customreporsitory.DeviceGroupCustomRepository;
import com.hust.qlts.project.repository.jparepository.DeviceGroupRepository;
import com.hust.qlts.project.repository.jparepository.HistoryRepository;
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
    private HistoryRepository historyRepository;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceGroupCustomRepository deviceGroupCustomRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object creatDeviceGoup(DeviceGroupDto dto) {
        DeviceGroupEntity entity = (DeviceGroupEntity) ConvetSetData.xetData(new DeviceGroupEntity(), dto);
        assert entity != null;
        entity.setLastModifiedDate(new Date());
        entity.setCreatedDate(new Date());
        DeviceGroupEntity entity1 = deviceGroupRepository.save(entity);
        List<DeviceEntity> list = new ArrayList<>();
        for (int i = 0; i < dto.getSizeId(); i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCode(creatCode(i, dto.getCode()));
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setPartId(dto.getPartId());
            deviceEntity.setSupplierId(dto.getSupplierId());
            deviceEntity.setIdEquipmentGroup(entity1.getId());
            deviceEntity.setWarehouseId(dto.getWarehouseId());
            deviceEntity.setStatus(Constants.TRONGKHO);
            deviceEntity.setUnit(dto.getUnit());
            deviceEntity.setSizeUnit(dto.getSizeUnit());
            deviceEntity.setNote(dto.getNote());
            deviceEntity.setSpecifications(dto.getSpecifications());
            deviceEntity.setLostDevice(100);
            deviceEntity.setExist(true);
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
        entity.setLastModifiedDate(new Date());
        List<DeviceEntity> list = new ArrayList<>();
        for (int i = entity.getSizeId(); i < (dto.getSizeId()); i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCode(creatCode(i, dto.getCode()));
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setPartId(dto.getPartId());
            deviceEntity.setSupplierId(dto.getSupplierId());
            deviceEntity.setIdEquipmentGroup(Long.valueOf(id));
            deviceEntity.setWarehouseId(dto.getWarehouseId());
            deviceEntity.setStatus(Constants.TRONGKHO);
            deviceEntity.setUnit(dto.getUnit());
            deviceEntity.setSizeUnit(dto.getSizeUnit());
            deviceEntity.setSpecifications(dto.getSpecifications());
            deviceEntity.setLostDevice(100);
            deviceEntity.setNote(dto.getNote());
            deviceEntity.setExist(true);
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
    public DeviceGroupFindDto getFindByCode(Long code) {
        return deviceGroupCustomRepository.findByCode(code);
    }
    @Override
    public DeviceGroupFindDto getFindByCodeCustom(String code) {
        return deviceGroupCustomRepository.findByCodeCustom(code);
    }
    @Override
    public List<DeviceGroupMaxCodeDto> getMaxCode(String code) {
        if (null == code || code.equals("")) {
            code = "%%";
            return convent(deviceGroupRepository.findByMaxCode(code));
        }
        code = "%" + code + "%";
        return convent(deviceGroupRepository.findByMaxCode(code));
    }

    @Override
    public List<DeviceGroupListDto> getList(Integer id) {
        return deviceGroupCustomRepository.getList(id);
    }

    @Override
    public List<IDeviceGroupMaxCodeDto> listAll() {
        return deviceGroupRepository.getAllCode();
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

    private List<DeviceGroupMaxCodeDto> convent(List<IDeviceGroupMaxCodeDto> dtos) {
        List<DeviceGroupMaxCodeDto> list = new ArrayList<>();
        for (IDeviceGroupMaxCodeDto dto : dtos) {
            DeviceGroupMaxCodeDto codeDto = new DeviceGroupMaxCodeDto();
            codeDto.setCode(dto.getCode());
            codeDto.setId(dto.getId());
            codeDto.setMaxCode(dto.getMaxCode());
            codeDto.setSize(dto.getSize());
            if (dto.getMaxCode() != null) {
                String data = dto.getMaxCode();
                codeDto.setNextMaxCode(creatCode(Integer.parseInt(String.copyValueOf(data.toCharArray(), data.length() - 4, 4)+1), dto.getCode()));
            }
            list.add(codeDto);
        }
        return list;
    }
    @Override
    public List<ICusTomDto> getAllListId(List<Long> id) {
        return deviceGroupRepository.getAllXet(id);
    }

    @Override
    public List<DeviceGroupEntity> getAllListLong(List<Long> id) {
        return deviceGroupRepository.getListAllId(id);
    }

    @Override
    public List<DeviceGroupEntity> saveList(List<DeviceGroupEntity> list) {
        return deviceGroupRepository.saveAll(list);
    }

    private void saveHisory(Object oldValue,Object newValus,Long id,Long idHummer){
        HistoryEntity historyEntity=new HistoryEntity();
        historyEntity.setTypeScreen(Constants.DEVICEGROUP);
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
