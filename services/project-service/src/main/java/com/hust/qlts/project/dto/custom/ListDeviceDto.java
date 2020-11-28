package com.hust.qlts.project.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDeviceDto{
    private Long idGroup;
    private Long size;
    private Long unit;
    private Long id;
    private Integer quantity;
    private List<Long> listCode;
}
