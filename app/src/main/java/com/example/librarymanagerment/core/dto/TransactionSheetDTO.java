package com.example.librarymanagerment.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonValue;

public class TransactionSheetDTO {

    private Integer id ;

    private String listBookValue;

    private String studentName;

    private String classRoom;

    private String createdDate;

    private String fromDate;

    private String toDate;

    private Integer fromDateValue;

    private Integer toDateValue;

    private Integer status;

    private Integer deleteFlag;

    private String staffName;

    private Integer studentId;

    public TransactionSheetDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getListBookValue() {
        return listBookValue;
    }

    public void setListBookValue(String listBookValue) {
        this.listBookValue = listBookValue;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getFromDateValue() {
        return fromDateValue;
    }

    public void setFromDateValue(Integer fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public Integer getToDateValue() {
        return toDateValue;
    }

    public void setToDateValue(Integer toDateValue) {
        this.toDateValue = toDateValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
