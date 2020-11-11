package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.repository.jparepository.DeviceRequestRepository;
import com.hust.qlts.project.service.DeviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceRequestServiceImpl implements DeviceRequestService {
    @Autowired
    private DeviceRequestRepository deviceRequestRepository;
}
