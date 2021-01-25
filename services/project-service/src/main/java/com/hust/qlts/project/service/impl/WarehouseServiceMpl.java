package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.IWarePart;
import com.hust.qlts.project.dto.WarehouseDTO;
import com.hust.qlts.project.entity.WarehouseEntity;
import com.hust.qlts.project.repository.customreporsitory.HumanResourcesCustomRepository;
import com.hust.qlts.project.repository.customreporsitory.WarehouseCustomRepository;
import com.hust.qlts.project.repository.jparepository.HistoryRepository;
import com.hust.qlts.project.repository.jparepository.WarehouseRepository;
import com.hust.qlts.project.service.WarehouseService;
import com.hust.qlts.project.service.mapper.WarehouseMapper;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "warehouse")

public class WarehouseServiceMpl implements WarehouseService {
    @Autowired
    EntityManager em;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private WarehouseCustomRepository warehouseCustomRepository;

    @Override
    public DataPage<WarehouseDTO> getPageWarehouseSeach(WarehouseDTO dto) {
        log.info("-----------------Danh sach nhà cung cấp--------------");

        DataPage<WarehouseDTO> data = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setSize(null != dto.getSize() ? dto.getSize().intValue() : 10);


        List<WarehouseDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(warehouseCustomRepository.getWarehouseSearch(dto))) {
            listProject = warehouseCustomRepository.getWarehouseSearch(dto);
            data.setData(listProject);
        }
        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }

    @Override
    public WarehouseDTO create(WarehouseDTO dto) {
        WarehouseEntity warehouseEntity = warehouseRepository.findByCode(dto.getCode());
        if (null != warehouseEntity && dto.getIdWare() == null) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
        } else if (null != warehouseEntity) {
            //TODO: Update  nha cung cấp
            warehouseEntity.setWarehouseID(Long.valueOf(dto.getIdWare()));
            warehouseEntity.setName(dto.getFullName());
            warehouseEntity.setCode(dto.getCode());
            warehouseEntity.setAddress(dto.getAddress());
            warehouseEntity.setNote(dto.getNote());
            warehouseEntity.setParid(dto.getPartId());
            warehouseEntity.setLastModifiedDate(new Date());

        } else if (dto.getIdWare() == null) {
            //TODO: create nha cung cap
            warehouseEntity = new WarehouseEntity();
            warehouseEntity.setName(dto.getFullName());
            warehouseEntity.setCode(dto.getCode());
            warehouseEntity.setAddress(dto.getAddress());
            warehouseEntity.setNote(dto.getNote());
            warehouseEntity.setParid(dto.getPartId());
            warehouseEntity.setStatus(1);
            warehouseEntity.setCreatedDate(new Date());
            warehouseEntity.setLastModifiedDate(new Date());


        }
        warehouseRepository.save(warehouseEntity);
        return convertEntitytoDTO(warehouseEntity);
    }

    @Override
    public WarehouseDTO update(WarehouseDTO dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        if (null != id) {
            WarehouseEntity supplierEntity = warehouseRepository.findByID(id);
            Integer c=warehouseRepository.getDelete(id);
            if(c>0){
                return false;
            }
            if (supplierEntity != null) {
                supplierEntity.setStatus(0);
                warehouseRepository.save(supplierEntity);
                log.info("<--- DELETE SUPPLIER COMPLETE");
                return true;
            } else {
                log.error("<--- CAN'T DELETE SUPPLIER RESOURCES");
                return false;
            }
        }

        return false;
    }

    @Override
    public WarehouseDTO findById(Long Id) {
        if (!warehouseRepository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        return convertEntitytoDTO(warehouseRepository.findById(Id).get());
    }


    @Override
    public WarehouseDTO findByCode(String code) {
        if (null != warehouseRepository.findByCode(code)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return warehouseMapper.toDto(warehouseRepository.findByCode(code));
    }

    @Override
    public List<IWarePart> findByPart(Long idPart) {
        List<IWarePart> list;
        if (null == idPart || idPart == 0) {
            list = warehouseRepository.findListPartAll();
        } else {
            list = warehouseRepository.findListPart(idPart);
        }

        return list;
    }



    public WarehouseDTO convertEntitytoDTO(WarehouseEntity warehouseEntity) {
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setAddress(warehouseEntity.getAddress());
        warehouseDTO.setCode(warehouseEntity.getCode());
        warehouseDTO.setFullName(warehouseEntity.getName());
        warehouseDTO.setNote(warehouseEntity.getNote());
        // warehouseDTO.setID(warehouseEntity.getWarehouseID());
//       warehouseDTO.setProvinceID(warehouseEntity.getProvincecode());
        warehouseDTO.setPartId(warehouseEntity.getParid());
        return warehouseDTO;
    }
    @Override
    public boolean checkCode(String code) {
        if(warehouseRepository.getCodeCheck(code).size()>0){
            return false;
        }
        return true;
    }
}
