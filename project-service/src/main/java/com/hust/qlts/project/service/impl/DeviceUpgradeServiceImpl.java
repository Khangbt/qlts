package com.hust.qlts.project.service.impl;


import com.hust.qlts.project.repository.jparepository.DeviceUpgradeRepository;
import com.hust.qlts.project.service.DeviceUpgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceUpgradeServiceImpl implements DeviceUpgradeService {
    @Autowired
    private DeviceUpgradeRepository deviceUpgradeRepository;

}
