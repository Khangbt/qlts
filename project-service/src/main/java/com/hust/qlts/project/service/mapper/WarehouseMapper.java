package com.hust.qlts.project.service.mapper;

import com.hust.qlts.project.dto.WarehouseDTO;
import com.hust.qlts.project.entity.WarehouseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, WarehouseEntity> {
}
