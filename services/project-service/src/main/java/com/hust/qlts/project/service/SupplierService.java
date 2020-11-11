package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.SupplierDTO;
import com.hust.qlts.project.dto.DataPage;

public interface SupplierService {
    DataPage<SupplierDTO> getPageSupplierSeach(SupplierDTO dto);

    SupplierDTO create(SupplierDTO supplierDTO);

    SupplierDTO update(SupplierDTO supplierDTO);

    Boolean delete(Long id);

    SupplierDTO findById(Long Id);

    SupplierDTO findByCode(String code);
}
