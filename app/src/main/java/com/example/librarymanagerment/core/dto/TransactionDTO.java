package com.example.librarymanagerment.core.dto;


import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class TransactionDTO {

    private Integer id;

    private List<BookDTO> listBookDTO;

    private String jsonListBook;

    private String studentName;

    private String classRoom;

    private String createDate;

    private String fromDate;

    private String toDate;

    private String staffName;

    private Integer status;

    private Integer studentId;

    public TransactionDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<BookDTO> getListBookDTO() {
        return ObjectMapperUtils.stringToTypeReference(jsonListBook, new TypeReference<List<BookDTO>>() {});
    }

    public void setListBookDTO(List<BookDTO> listBookDTO) {
        this.listBookDTO = listBookDTO;
    }

    public String getJsonListBook() {
        return jsonListBook;
    }

    public void setJsonListBook(String jsonListBook) {
        this.jsonListBook = jsonListBook;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
