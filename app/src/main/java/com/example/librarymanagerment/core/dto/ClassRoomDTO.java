package com.example.librarymanagerment.core.dto;

public class ClassRoomDTO {

    private Integer id;

    private String classRoom;

    public ClassRoomDTO() {
    }

    public ClassRoomDTO(Integer id, String classRoom) {
        this.id = id;
        this.classRoom = classRoom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
