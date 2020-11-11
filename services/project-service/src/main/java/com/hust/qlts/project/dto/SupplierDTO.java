package com.hust.qlts.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

// danh muc nha cung cap
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SupplierDTO {
    private BigInteger supplierid;// auto-increment
    private BigInteger groupSupplierid;// auto-increment
    private BigInteger humanResourceId;// auto-increment
    private String code;  //required unique
    private String fullName;  //required
    private String type;  //required
    private String contactTo; //required
    private String position; //required
    private String phone; //required
    private String email; //required
    private String fax;
    private String website;
    private String area; //required
    private BigInteger province;
    private BigInteger distric;
    private String address;
    private String desciprtion;
    private String note;
    private BigInteger positionId;
    private Integer status;
    private Integer page;
    private String groupSupplier;
    private String humanresources;

    private Integer pageSize;

    private Long totalRecord;
    private Long departmentId;
    private String password;

    public BigInteger getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(BigInteger supplierid) {
        this.supplierid = supplierid;
    }

    public BigInteger getId() {
        return groupSupplierid;
    }

    public void setId(BigInteger id) {
        this.groupSupplierid = id;
    }

    public BigInteger getHumanResourceId() {
        return humanResourceId;
    }

    public void setHumanResourceId(BigInteger humanResourceId) {
        this.humanResourceId = humanResourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public BigInteger getProvince() {
        return province;
    }

    public void setProvince(BigInteger province) {
        this.province = province;
    }

    public BigInteger getDistric() {
        return distric;
    }

    public void setDistric(BigInteger distric) {
        this.distric = distric;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesciprtion() {
        return desciprtion;
    }

    public void setDesciprtion(String desciprtion) {
        this.desciprtion = desciprtion;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigInteger getPositionId() {
        return positionId;
    }

    public void setPositionId(BigInteger positionId) {
        this.positionId = positionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
