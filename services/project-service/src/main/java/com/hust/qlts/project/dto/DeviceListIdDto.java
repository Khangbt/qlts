package com.hust.qlts.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceListIdDto {
    private Long id;
    private String code;
    private String name;
    private List<String> listDevice;
    private List<Xet> listXet;

    @Data
    public static class Xet {
        private Long id;
        private String code;

    }
}