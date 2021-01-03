package com.hust.qlts.project.dto;

import lombok.Data;

import java.util.Date;


@Data
public class HumanResourcesImportDTO {
    private int stt;
    private String code;
    private String fullName;
    private String position;//chuc vu
    private String part; // bo pham
    private String department;//phong ban
    private String major; //chuyen mon
    private String dateRecuitment;//ngay tuyen dung
    private String dateGraduate;//thoi gian tot nghiep
    private String dateMajor;//ngay bat dau chuyen mom
    private String projectParticipate;
    private String email;
    private String node;
    private String staus;
}
