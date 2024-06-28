package com.example.librarymanagerment.core.sco;

public class VideoSCO {

    private String classRoom;

    private String category;

    private String searchName;

    public VideoSCO(String classRoom, String category, String searchName) {
        this.classRoom = classRoom;
        this.category = category;
        this.searchName = searchName;
    }

    public VideoSCO() {
        this.classRoom = "";
        this.category = "";
        this.searchName = "";
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
