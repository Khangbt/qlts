package com.hust.qlts.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificetionDto {
    private int sum;
    private int sizeSum;
    private List<DataNotifice> list;
    @Data
    public static class DataNotifice{
        private Long id;
        private boolean status;
        private String note;
    }
}
