package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.AssetDTO;
import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.repository.customreporsitory.AsserCustomRepository;
import com.hust.qlts.project.service.AsserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service(value = "asserService")
public class AsserServiceImpl implements AsserService {

    @Autowired
    private AsserCustomRepository asserCustomRepository;


    @Override
    public DataPage<AssetDTO> searchAsser(AssetDTO dto) {
        DataPage<AssetDTO> dtoDataPage = new DataPage<>();

        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        List<AssetDTO> list = new ArrayList<>();
        try {
            list = asserCustomRepository.searchAsser(dto);
            dtoDataPage.setData(list);

        }catch (Exception e){
            throw e;
        }
        dtoDataPage.setPageIndex(dto.getPage());
        dtoDataPage.setPageSize(dto.getPageSize());
        dtoDataPage.setDataCount(dto.getTotalRecord());
        dtoDataPage.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (dtoDataPage.getDataCount() % dtoDataPage.getPageSize() != 0) {
            dtoDataPage.setPageCount(dtoDataPage.getPageCount() + 1);
        }
        return dtoDataPage;
    }

    @Override
    public byte[] exportExcel(MultipartFile file)  {
        return new byte[0];
    }
}
