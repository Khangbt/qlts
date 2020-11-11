package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.repository.jparepository.DeviceGroupRepository;
import com.hust.qlts.project.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
}
