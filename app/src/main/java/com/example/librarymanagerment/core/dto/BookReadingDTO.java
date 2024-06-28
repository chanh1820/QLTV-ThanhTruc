package com.example.librarymanagerment.core.dto;

import android.content.Intent;

public class BookReadingDTO {
    Integer id;
    private String code;

    private String name;

    private String type;

    private String resource;

    private String imageFile;

    private String collectionCode;

    private String classRoom;

    public BookReadingDTO() {
    }

    public BookReadingDTO(Integer id, String code, String name, String type, String resource, String imageFile) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.imageFile = imageFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
