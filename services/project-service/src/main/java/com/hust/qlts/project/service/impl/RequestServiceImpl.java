package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.entity.RequestEntity;
import com.hust.qlts.project.repository.jparepository.RequestRepository;
import com.hust.qlts.project.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Override
    public RequestEntity creat(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity);
    }

    @Override
    public RequestEntity update(RequestEntity requestEntity) {
        return requestRepository.save(requestEntity);
    }
}
