package com.hust.qlts.project.service.mapper;

import com.hust.qlts.project.dto.HistoryDTO;
import com.hust.qlts.project.entity.HistoryEntity;
import org.mapstruct.Mapper;

/**
 * @author dangnp
 * @created 14/09/2020 - 9:59 AM
 * @project services
 **/
@Mapper(componentModel = "spring")
public interface HistoryMapper extends EntityMapper<HistoryDTO, HistoryEntity> {
}
