package com.hust.qlts.project.service.impl;


import com.hust.qlts.project.repository.jparepository.DeviceToRequestAddRepository;
import com.hust.qlts.project.service.DeviceToRequestAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceToRequestAddServiceImpl implements DeviceToRequestAddService {
    @Autowired
    private DeviceToRequestAddRepository deviceToRequestAddRepository;
}
