package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.repository.customreporsitory.DeviceCustomRepository;
import com.hust.qlts.project.repository.jparepository.DeviceRepository;
import com.hust.qlts.project.service.DeviceService;
import common.ConvetSetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public DeviceDto update(DeviceDto dto, Long id) {
        if (!deviceRepository.findById(id).isPresent()) {
            return null;
        }
        DeviceEntity entity = deviceRepository.findById(id).get();
        entity = (DeviceEntity) ConvetSetData.xetData(entity, dto);
        assert entity != null;
        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), deviceRepository.save(entity));

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

    @Override
    public DeviceDto craet(DeviceDto dto) {
        DeviceEntity deviceEntity = (DeviceEntity) ConvetSetData.xetData(new DeviceEntity(), dto);
        assert deviceEntity != null;
        deviceEntity.setExist(true);
        deviceEntity.setDateAdd(new Date());
        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), deviceRepository.save(deviceEntity));
    }

    @Override
    public String getMaxCode(Long id) {
        return deviceRepository.getMaxCode(id);
    }

    @Override
    public boolean updateListStatus(List<String> code) {
        return false;
    }

    @Override
    public List<DeviceEntity> listSetStatus(List<Long> list) {
        String a = list.toString().replace('[', '(');
        String b = a.replace(']', ')');
        return deviceRepository.getListCode(list);
    }

    @Override
    public List<DeviceListIdDto> getList(Long Request, Long partId) {
        List<Long> longs = deviceRepository.getAllLog(Request);
        List<ICusTomDto> tomDtos = deviceRepository.listDevice(longs, partId);
        List<DeviceListIdDto> idDtos = new ArrayList<>();
        for (ICusTomDto dto : tomDtos) {
            DeviceListIdDto idDto = new DeviceListIdDto();
            idDto.setId(dto.getId());
            idDto.setCode(dto.getCode());
            idDto.setName(dto.getName());
            String s = dto.getListName();
            List<String> lists = Arrays.asList(s.split(","));
            List<DeviceListIdDto.Xet> xets = new ArrayList<>();
            for (String s1 : lists) {
                DeviceListIdDto.Xet xet = new DeviceListIdDto.Xet();
                String[] strings = s1.split("===");
                xet.setCode(strings[0]);
                xet.setId(Long.valueOf(strings[1]));
                xets.add(xet);
            }
            idDto.setListDevice(lists);
            idDto.setListXet(xets);
            idDtos.add(idDto);
        }
        return idDtos;
    }

    @Override
    public List<ICusTomDto> getListIdHumme(Long id) {
        return deviceRepository.listByID(id);
    }

    @Override
    public List<ICusTomDto> getIdHummeReti(Long idHummer, Long partId) {
        return deviceRepository.listDeviceRetu(idHummer, partId);
    }

    @Override
    public List<DeviceListIdDto> getListReturn(Long Request) {
        List<ICusTomDto> tomDtos = deviceRepository.listReturnRequest(Request);
        List<DeviceListIdDto> idDtos = new ArrayList<>();
        for (ICusTomDto dto : tomDtos) {
            DeviceListIdDto idDto = new DeviceListIdDto();
            idDto.setId(dto.getId());
            idDto.setCode(dto.getCode());
            idDto.setName(dto.getName());
            System.out.println(dto.getId()+dto.getName());
            String[] lists = dto.getListName().split(",");
            List<DeviceListIdDto.Xet> xets = new ArrayList<>();
            for (String s1 : lists) {
                DeviceListIdDto.Xet xet = new DeviceListIdDto.Xet();
                String[] strings = s1.split("===");
                xet.setCode(strings[0]);
                xet.setId(Long.valueOf(strings[1]));
                xets.add(xet);
            }

            idDto.setListXet(xets);
            idDtos.add(idDto);
        }

        return idDtos;
    }

    @Override
    public boolean checkCode(String code) {
        if(deviceRepository.getByCodeCustom(code).size()>0){
            return false;
        }
        return true;
    }

    @Override
    public List<ICusTomDto> getListReturnbor(Long id) {
        return deviceRepository.listRequestRetuBorw(id);
    }

    @Override
    public List<ICusTomDto> getIdHummeRetiByReque(Long idHummer, Long partId, Long idReque) {
        return deviceRepository.listDeviceRetuById(idHummer, partId, idReque);
    }

    @Override
    public List<ICusTomDto> getIdHummeRetiByIdStaue(Long idReque) {
        return deviceRepository.listDeviceRetuGetStaus(idReque);
    }

}
