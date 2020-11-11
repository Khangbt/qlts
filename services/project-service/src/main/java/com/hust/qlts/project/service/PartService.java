package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.PartnerDTO;


public interface PartService {
    DataPage<PartnerDTO> getPagePartSeach(PartnerDTO dto);
    PartnerDTO create(PartnerDTO partnerDTO);
    PartnerDTO update(PartnerDTO partnerDTO);
    Boolean delete (Long id);
    PartnerDTO findById(Long Id);
    PartnerDTO findByCode(String code);
}
