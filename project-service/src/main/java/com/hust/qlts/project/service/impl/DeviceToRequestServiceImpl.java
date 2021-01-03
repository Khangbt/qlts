package com.hust.qlts.project.service.impl;


import com.hust.qlts.project.repository.jparepository.DeviceToRequestRepository;
import com.hust.qlts.project.service.DeviceToRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceToRequestServiceImpl implements DeviceToRequestService {
    @Autowired
    private DeviceToRequestRepository deviceToRequestRepository;
}
