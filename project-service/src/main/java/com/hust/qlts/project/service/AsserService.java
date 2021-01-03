package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.AssetDTO;
import com.hust.qlts.project.dto.DataPage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface AsserService {
     DataPage<AssetDTO> searchAsser(AssetDTO dto);
     byte[] exportExcel(MultipartFile file) throws IOException;

}
