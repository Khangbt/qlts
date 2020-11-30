package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.RequestDto;
import com.hust.qlts.project.entity.RequestEntity;
import com.hust.qlts.project.repository.customreporsitory.RequestCustomRepository;
import com.hust.qlts.project.repository.jparepository.RequestRepository;
import com.hust.qlts.project.service.DeviceRequestAddService;
import com.hust.qlts.project.service.DeviceRequestRetuService;
import com.hust.qlts.project.service.DeviceRequestService;
import com.hust.qlts.project.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private DeviceRequestRetuService deviceRequestRetuService;
    @Autowired
    private DeviceRequestAddService deviceRequestAddService;
    @Autowired
    private DeviceRequestService deviceRequestService;

    @Autowired
    private RequestCustomRepository requestCustomRepository;

    @Override
    public RequestEntity creat(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity);
    }

    @Override
    public RequestEntity update(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity);
    }

    @Override
    public boolean delete(Long id) {
        if (!requestRepository.findById(id).isPresent()) {
            return false;
        }
        boolean check = false;
        RequestEntity requestEntity = requestRepository.findById(id).get();
        if (requestEntity.getTyle().equals("PT")) {
            check = deviceRequestRetuService.deleteDevice(requestEntity.getIdRequest());
        }
        if (requestEntity.getTyle().equals("YCM")) {
            check = deviceRequestAddService.deleteDevice(requestEntity.getIdRequest());
        }
        if (requestEntity.getTyle().equals("PYCM")) {
            check = deviceRequestService.deleteDevice(requestEntity.getIdRequest());
        }
        if(check){
            requestRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public DataPage<RequestDto> sreah(RequestDto dto) {
        DataPage<RequestDto> dtoDataPage = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        List<RequestDto> list = new ArrayList<>();
        try {
            list = requestCustomRepository.getSrearh(dto);
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
}
