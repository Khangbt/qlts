package com.hust.qlts.project.service.impl;


import com.hust.qlts.project.repository.jparepository.DeviceRequestAddRepository;
import com.hust.qlts.project.service.DeviceRequestAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceRequestAddServiceImpl implements DeviceRequestAddService {
    @Autowired
    private DeviceRequestAddRepository deviceRequestAddRepository;
}
