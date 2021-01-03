package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.RequestDto;
import com.hust.qlts.project.entity.ImageEntity;
import com.hust.qlts.project.entity.RequestEntity;

import java.util.List;

public interface RequestService {
    RequestEntity creat(RequestEntity requestEntity);
    RequestEntity update(RequestEntity requestEntity);
    boolean delete(Long id);
    DataPage<RequestDto> sreah(RequestDto dto);
}
