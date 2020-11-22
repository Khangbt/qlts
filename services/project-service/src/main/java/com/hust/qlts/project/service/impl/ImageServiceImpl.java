package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.entity.ImageEntity;
import com.hust.qlts.project.repository.jparepository.ImageRepository;
import com.hust.qlts.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public ImageEntity creat(ImageEntity imageEntity) {
        return imageRepository.save(imageEntity);
    }

    @Override
    public ImageEntity update(ImageEntity imageEntity) {
        return imageRepository.save(imageEntity);
    }
}
