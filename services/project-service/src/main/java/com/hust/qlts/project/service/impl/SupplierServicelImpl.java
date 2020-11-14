package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.SupplierDTO;
import com.hust.qlts.project.entity.SupplierEntity;
import com.hust.qlts.project.repository.customreporsitory.SupplierCustomRepository;
import com.hust.qlts.project.repository.jparepository.HumanResourcesRepository;
import com.hust.qlts.project.repository.jparepository.SupplierRepository;
import com.hust.qlts.project.service.SupplierService;
import com.hust.qlts.project.service.mapper.SupplierMapper;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service(value = "supplier")
public class SupplierServicelImpl implements SupplierService {
    private final Logger log = LogManager.getLogger(HumanResourcesServiceImpl.class);
    @Autowired
    private SupplierCustomRepository customRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private HumanResourcesRepository humanResourcesRepository;

    @Override
    public DataPage<SupplierDTO> getPageSupplierSeach(SupplierDTO dto) {

        log.info("-----------------Danh sach nhà cung cấp--------------");

        DataPage<SupplierDTO> data = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        //    String s = String.valueOf(dto.getActive());
//        if (!s.equals("null")) {
//            if (dto.getActive() == 0)
//                dto.setActive(null);
//        }


        List<SupplierDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customRepository.getSupplierSearch(dto))) {
            listProject = customRepository.getSupplierSearch(dto);
            data.setData(listProject);
        }
        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getPageSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) {
        SupplierEntity supplierEntity = supplierRepository.findByCode(supplierDTO.getCode());
        if (null == supplierEntity && supplierDTO.getSupplierId() != null) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
        } else if (null != supplierEntity) {
            //TODO: Update  nha cung cấp
//            supplierEntity.setGroupSupplierid(supplierDTO.getId());
            supplierEntity.setCode(supplierDTO.getCode());
            supplierEntity.setName(supplierDTO.getFullName());
            supplierEntity.setFax(supplierDTO.getFax());
            supplierEntity.setAddress(supplierDTO.getAddress());
            supplierEntity.setEmail(supplierDTO.getEmail());
            supplierEntity.setWebsite(supplierDTO.getWebsite());
            supplierEntity.setNote(supplierDTO.getNote());
            supplierEntity.setPhoneNumber(supplierDTO.getPhone());
            supplierEntity.setHumanContact(supplierDTO.getNameHummer());
            supplierEntity.setStatus(1);
        } else if (supplierDTO.getSupplierId() == null) {
            //TODO: create nha cung cap
            supplierEntity = convertDTOtoEntity(supplierDTO);
            supplierEntity.setFax(supplierDTO.getFax());
            supplierEntity.setHumanContact(supplierDTO.getNameHummer());
            supplierEntity.setStatus(1);


        }
            supplierRepository.save(supplierEntity);

        return convertEntitytoDTO(supplierEntity);
    }


    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) {
        SupplierEntity hrResourcesEntity = convertDTOtoEntity(supplierDTO);
        hrResourcesEntity = supplierRepository.save(hrResourcesEntity);
        return convertEntitytoDTO(hrResourcesEntity);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("-----------------Xoa nhà cung cấp---------------");
        if (id != null) {
            SupplierEntity supplierEntity = supplierRepository.findByID(id);
            if (supplierEntity != null) {
                supplierEntity.setStatus(0);
                supplierRepository.save(supplierEntity);
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public SupplierDTO findById(Long Id) {
        if (!supplierRepository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        return convertEntitytoDTO(supplierRepository.findById(Id).get());
    }

    @Override
    public SupplierDTO findByCode(String code) {
        if (null != supplierRepository.findByCode(code)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return supplierMapper.toDto(supplierRepository.findByCode(code));
    }

    public SupplierEntity convertDTOtoEntity(SupplierDTO supplierDTO) {
        SupplierEntity supplierEntity = new SupplierEntity();
//        supplierEntity.setGroupSupplierid(supplierDTO.getId());
        supplierEntity.setCode(supplierDTO.getCode());
        supplierEntity.setName(supplierDTO.getFullName());
//        supplierEntity.setHuman_id(supplierDTO.getHumanResourceId());
        supplierEntity.setAddress(supplierDTO.getAddress());
        supplierEntity.setEmail(supplierDTO.getEmail());
//        supplierEntity.setPosition(supplierDTO.getPositionId());
        supplierEntity.setWebsite(supplierDTO.getWebsite());
        supplierEntity.setNote(supplierDTO.getNote());
        supplierEntity.setPhoneNumber(supplierDTO.getPhone());
        supplierEntity.setStatus(1);
        return supplierEntity;
    }

    public SupplierDTO convertEntitytoDTO(SupplierEntity supplierEntity) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setFax(supplierEntity.getFax());
        supplierDTO.setNameHummer(supplierEntity.getHumanContact());

        supplierDTO.setFullName(supplierEntity.getName());
        supplierDTO.setCode(supplierEntity.getCode());
        supplierDTO.setAddress(supplierEntity.getAddress());
        supplierDTO.setEmail(supplierEntity.getEmail());
        supplierDTO.setWebsite(supplierEntity.getWebsite());
        supplierDTO.setNote(supplierEntity.getNote());
        supplierDTO.setPhone(supplierEntity.getPhoneNumber());
        supplierDTO.setSupplierId(BigInteger.valueOf(supplierEntity.getSupplierId()));
        return supplierDTO;
    }

}
