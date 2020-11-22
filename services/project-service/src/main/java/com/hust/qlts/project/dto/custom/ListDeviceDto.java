package com.hust.qlts.project.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDeviceDto{
    private Long id;
    private Long deviceRequestId;
    private Long deviceId;
    private Long status;
    private Long deviceGroupId;
    private Long size;
}
