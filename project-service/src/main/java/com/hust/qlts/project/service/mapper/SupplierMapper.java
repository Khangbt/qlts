package com.hust.qlts.project.service.mapper;

import com.hust.qlts.project.dto.SupplierDTO;
import com.hust.qlts.project.entity.SupplierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends EntityMapper<SupplierDTO, SupplierEntity> {
}
