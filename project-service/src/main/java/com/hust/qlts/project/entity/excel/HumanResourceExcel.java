package com.hust.qlts.project.entity.excel;


import com.hust.qlts.project.common.Constants;
import com.hust.qlts.project.common.annotation.Import;
import com.hust.qlts.project.common.annotation.SheetImportSerializable;
import lombok.Data;

@Data
@SheetImportSerializable(sheetDataIndex = 0, totalCols = 13, startRow = 1)
public class HumanResourceExcel {

    @Import(type = Constants.EXECEL_STYLE.String, header = "STT", index = 0)
    private String index;
    /**
     * Mã nhân sự
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Mã số nhân sự(*)", index = 1)
    private String employeeCode;
    /**
     * Tên nhân sự
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Họ và tên(*)", index = 2)
    private String employeeName;

    /**
     * Chức vụ
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Chức danh(*)", index = 3)
    private String position;

    /**
     * Bộ phận
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Bộ phận(*)", index = 4)
    private String part;

    /**
     * Phòng ban
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Phòng ban(*)", index = 5)
    private String deparment;

    /**
     * chuyên môn
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Chuyên môn(*)", index = 6)
    private String major;

    /**
     * ngày tuyển dung
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Ngày tuyển dụng(*)", index = 7)
    private String daterecruitment;
    /**
     * Thời gian tốt nghiệp
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Thời gian tốt nghiệp", index = 8)
    private String dategraduate;

    /**
     * Thời gian bắt đầu làm chuyên môn
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Thời gian bắt đầu làm chuyên môn", index = 9)
    private String datemajor;

    /**
     * Dự án đã tham gia
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Dự án đã tham gia", index = 10)
    private String projectparticipate;

    /**
     * Email
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Chuyên môn(*)", index = 11)
    private String email;

    /**
     * Ghi chú
     */
    @Import(type = Constants.EXECEL_STYLE.String, header = "Ghi chú", index = 12)
    private String note;


    private boolean status = true;

    private boolean duplicatePartnerCode = true;

    private boolean inserted = false;

}
