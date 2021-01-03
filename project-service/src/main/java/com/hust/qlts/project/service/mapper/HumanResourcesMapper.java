package com.hust.qlts.project.service.mapper;

import com.hust.qlts.project.dto.HumanResourcesDTO;
import com.hust.qlts.project.entity.HumanResourcesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HumanResourcesMapper extends EntityMapper<HumanResourcesDTO, HumanResourcesEntity> {
}
