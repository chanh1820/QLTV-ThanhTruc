package com.example.librarymanagerment.core.dto;

public class StudentDTO {

    private Integer id;

    private String studentCode;
    private String studentName;

    private String classRoom;

    private Integer borrowedFlag;

    public StudentDTO() {
    }

    public StudentDTO(Integer id,String studentName, String classRoom) {
        this.id = id;

        this.studentName = studentName;
        this.classRoom = classRoom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBorrowedFlag() {
        return borrowedFlag;
    }

    public void setBorrowedFlag(Integer borrowedFlag) {
        this.borrowedFlag = borrowedFlag;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public StudentDTO(Integer id, String studentName, String classRoom, Integer borrowedFlag) {
        this.id = id;
        this.studentName = studentName;
        this.classRoom = classRoom;
        this.borrowedFlag = borrowedFlag;
    }
}
