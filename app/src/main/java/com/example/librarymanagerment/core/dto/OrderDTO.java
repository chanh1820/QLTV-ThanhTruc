package com.example.librarymanagerment.core.dto;

import java.io.Serializable;

public class OrderDTO implements Serializable {

    private Integer id;

    private String userName;

    private String booksData;

    private String studentName;

    private String classRoom;

    private String createDate;

    private Integer status;

    public OrderDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBooksData() {
        return booksData;
    }

    public void setBooksData(String booksData) {
        this.booksData = booksData;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
