package com.hust.qlts.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.DeviceEntity;
import com.hust.qlts.project.entity.DeviceGroupEntity;
import com.hust.qlts.project.entity.HistoryEntity;
import com.hust.qlts.project.entity.excel.DeviceExcel;
import com.hust.qlts.project.repository.customreporsitory.DeviceCustomRepository;
import com.hust.qlts.project.repository.jparepository.DeviceGroupRepository;
import com.hust.qlts.project.repository.jparepository.DeviceRepository;
import com.hust.qlts.project.repository.jparepository.HistoryRepository;
import com.hust.qlts.project.service.DeviceService;
import common.Constants;
import common.ConvetSetData;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final Logger log = LogManager.getLogger(HumanResourcesServiceImpl.class);
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCustomRepository deviceCustomRepository;

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Autowired
    private HistoryRepository historyRepository;


    private ObjectMapper objectMapper=new ObjectMapper();
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
        entity.setLastModifiedDate(new Date());
        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), deviceRepository.save(entity));

    }

    @Override
    public boolean deleteDevice(Long id) {
        if(!deviceRepository.findById(id).isPresent()){
            return false;
        }
        DeviceEntity entity=deviceRepository.findById(id).get();
        if(entity.getStatus()==2){
            return false;
        }
        entity.setExist(false);
        deviceRepository.save(entity);
        return true;
    }

    @Override
    public DeviceFindDto getFindByCode(String code) {
        return deviceCustomRepository.getFindByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceDto craet(DeviceDto dto) {
        if (dto.isCheck()) {
            DeviceEntity deviceEntity = (DeviceEntity) ConvetSetData.xetData(new DeviceEntity(), dto);
            assert deviceEntity != null;
            deviceEntity.setExist(true);
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setCreatedDate(new Date());
            deviceEntity.setLastModifiedDate(new Date());
            DeviceEntity deviceE=deviceRepository.save(deviceEntity);
            HistoryEntity historyEntity=new HistoryEntity();
            historyEntity.setDate(new Date());
            try {
                historyEntity.setValueNew(objectMapper.writeValueAsString(deviceE));
            } catch (JsonProcessingException e) {
                historyEntity.setValueNew(null);
            }
            return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), deviceE);
        }
        if (!deviceGroupRepository.findById(dto.getIdEquipmentGroup()).isPresent()) {
            return null;
        }
        DeviceGroupEntity groupEntity = deviceGroupRepository.findById(dto.getIdEquipmentGroup()).get();
        List<DeviceEntity> list = new ArrayList<>();
        for (int i = 0; i < dto.getSize(); i++) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setCode(creatCode(i + groupEntity.getSizeId(), groupEntity.getCode()));
            deviceEntity.setDateAdd(new Date());
            deviceEntity.setPartId(dto.getPartId());
            deviceEntity.setSupplierId(dto.getSupplierId());
            deviceEntity.setIdEquipmentGroup(dto.getIdEquipmentGroup());
            deviceEntity.setWarehouseId(dto.getWarehouseId());
            deviceEntity.setStatus(Constants.TRONGKHO);
            deviceEntity.setUnit(dto.getUnit());
            deviceEntity.setSizeUnit(dto.getSizeUnit());
            deviceEntity.setNote(dto.getNote());
            deviceEntity.setSpecifications(groupEntity.getSpecifications());
            deviceEntity.setLostDevice(100);
            deviceEntity.setExist(true);
            deviceEntity.setCreatedDate(new Date());
            deviceEntity.setLastModifiedDate(new Date());
            deviceEntity.setSeri(dto.getSeri());
            deviceEntity.setPrice(dto.getPrice());
            list.add(deviceEntity);
        }
        int z = groupEntity.getSizeId();
        groupEntity.setSizeId(z + dto.getSize());
        deviceRepository.saveAll(list);
        groupEntity.setLastModifiedDate(new Date());
        DeviceGroupEntity entity = deviceGroupRepository.saveAndFlush(groupEntity);

        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), dto);
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
            System.out.println(dto.getId() + dto.getName());
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
        if (deviceRepository.getByCodeCustom(code).size() > 0) {
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

    @Override
    public byte[] exportXel(DeviceDto dto) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(new File(getFileFromURL("/templates/import/Danh_Sach_Tai_San.xlsx")));
            log.info("có fieldataa");
        } catch (FileNotFoundException e) {

            try {
                String file1="/templates/import/Danh_Sach_Tai_San.xlsx";
                in= new ClassPathResource(file1).getInputStream();
                log.info("dường dẫn 2");

            } catch (IOException ioException) {
                ioException.printStackTrace();
                log.info("Khong có file data");

                return null;
            }

        }
        HashedMap beans = new HashedMap();
        List<DeviceExcel> deviceExcels = deviceCustomRepository.getExcal(dto);
        beans.put("deviceExcels", deviceExcels);
        try {
            XLSTransformer transformer = new XLSTransformer();
            org.apache.poi.ss.usermodel.Workbook workBook = transformer.transformXLS(in, beans);
            workBook.write(out);
            byte[] bytes = out.toByteArray();
            workBook.close();
            in.close();
            out.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<DeviceEntity> getListyCode(Long id) {
        return deviceRepository.listDeviceByCode(id);
    }

    @Override
    public DeviceDto lock(Long dto) {
        if(!deviceRepository.findById(dto).isPresent()){
            return null;
        }
        DeviceEntity entity=deviceRepository.findById(dto).get();
        entity.setStatus(3);
        entity.setLastModifiedDate(new Date());
        return (DeviceDto) ConvetSetData.xetData(new DeviceDto(), deviceRepository.save(entity));
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

    private String getFileFromURL(String path) {
        URL url = this.getClass().getResource(path);
        assert url != null;
        return url.getPath();
    }

    private void saveHisory(Object oldValue,Object newValus,Long id,Long idHummer){
        HistoryEntity historyEntity=new HistoryEntity();
        historyEntity.setTypeScreen(Constants.DEVICE);
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
