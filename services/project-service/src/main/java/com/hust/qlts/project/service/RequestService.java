package com.hust.qlts.project.service;

import com.hust.qlts.project.entity.ImageEntity;
import com.hust.qlts.project.entity.RequestEntity;

public interface RequestService {
    RequestEntity creat(RequestEntity requestEntity);
    RequestEntity update(RequestEntity requestEntity);
}
