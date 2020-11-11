package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.repository.jparepository.DeviceRepository;
import com.hust.qlts.project.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
}
