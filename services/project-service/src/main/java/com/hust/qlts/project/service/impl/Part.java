package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.entity.PartEntity;
import com.hust.qlts.project.repository.jparepository.PartRepository;
import com.hust.qlts.project.service.PartService;
import com.hust.qlts.project.service.mapper.PartnerMapper;
import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.PartnerDTO;
import com.hust.qlts.project.repository.customreporsitory.PartCustomRepository;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "partner")

public class Part implements PartService {
    @Autowired
    private PartRepository partRepository;
    @Autowired
    PartCustomRepository customRepository;
    @Autowired
    PartnerMapper partnerMapper;


    private Logger log = LogManager.getLogger(Part.class);

    @Override
    public DataPage<PartnerDTO> getPagePartSeach(PartnerDTO dto) {
        log.info("-----------------Danh sach nhà cung cấp--------------");

        DataPage<PartnerDTO> data = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setSize(null != dto.getSize() ? dto.getSize().intValue() : 10);



        List<PartnerDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customRepository.getPartSearch(dto))) {
            listProject = customRepository.getPartSearch(dto);
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
    public PartnerDTO create(PartnerDTO partnerDTO) {
        if (partnerDTO.getPartnerID() == null) {
            PartEntity partEntity = new PartEntity();
            partEntity.setCode(partnerDTO.getCode());
            partEntity.setIsActive(partnerDTO.getStatus());
            partEntity.setName(partnerDTO.getPartName());
//            partEntity.setProvinceCode(partnerDTO.getProvinceID());
            partEntity.setNote(partnerDTO.getNote());
            partRepository.save(partEntity);
            return convertEntitytoDTO(partEntity);
        } else {
            Optional<PartEntity> partEntity1 = partRepository.findById(partnerDTO.getPartnerID());
            if(!partEntity1.isPresent()){
                throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
            } else {
                //TODO: Update  nha cung cấp
                PartEntity partEntity=partEntity1.get();
                partEntity.setId(partnerDTO.getPartnerID());
                partEntity.setCode(partnerDTO.getCode());
                partEntity.setName(partnerDTO.getPartName());
                partEntity.setIsActive(partnerDTO.getStatus());
                partEntity.setNote(partnerDTO.getNote());
                partRepository.save(partEntity);
                return convertEntitytoDTO(partEntity);

            }
        }

    }

    @Override
    public PartnerDTO update(PartnerDTO partnerDTO) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        ///check dieu kien xoa
        if(true){
            partRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public PartnerDTO findById(Long Id) {
        if (!partRepository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        PartnerDTO partnerDTO=convertEntitytoDTO(partRepository.findById(Id).get());
        partnerDTO.setPartnerID(partRepository.findById(Id).get().getId());
        return partnerDTO;
    }

    @Override
    public PartnerDTO findByCode(String code) {
        PartEntity partEntity = partRepository.findByCode(code);
        if (null != partEntity) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return partnerMapper.toDto(partEntity);
    }

    public PartnerDTO convertEntitytoDTO(PartEntity partEntity) {
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setCode(partEntity.getCode());
        partnerDTO.setPartName(partEntity.getName());
        partnerDTO.setStatus(partEntity.getIsActive());
        partnerDTO.setNote(partEntity.getNote());
        return partnerDTO;
    }
}
