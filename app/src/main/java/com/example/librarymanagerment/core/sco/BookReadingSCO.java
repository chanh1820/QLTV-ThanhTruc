package com.example.librarymanagerment.core.sco;

public class BookReadingSCO {
    private String collectionParent;

    private String classRoom;

    private String searchName;

    public BookReadingSCO() {
        this.collectionParent = "";
        this.classRoom = "";
        this.searchName = "";
    }

    public BookReadingSCO(String collectionParent, String classRoom, String searchName) {
        this.collectionParent = collectionParent;
        this.classRoom = classRoom;
        this.searchName = searchName;
    }

    public String getCollectionParent() {
        return collectionParent;
    }

    public void setCollectionParent(String collectionParent) {
        this.collectionParent = collectionParent;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
