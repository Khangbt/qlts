package com.hust.qlts.project.service.mapper;

import com.hust.qlts.project.dto.PartnerDTO;
import com.hust.qlts.project.entity.PartEntity;
import org.mapstruct.Mapper;

/**
 * @author dangnp
 * @created 22/09/2020 - 11:49 AM
 * @project services
 **/
@Mapper(componentModel = "spring")
public interface PartnerMapper extends EntityMapper<PartnerDTO, PartEntity> {

}
