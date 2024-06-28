package com.example.librarymanagerment.core.dto;

public class BookDTO {

    private Integer id;

    private String code;

    private String name;

    private String locate;

    private Integer quantity;

    private String collectionCode;

    private String collectionParent;

    private String classRoom;

    private String imageFile;

    private String imageBase64;
    private String description;


    public BookDTO(Integer id, String code, String name, String description, String locate, Integer quantity, String collectionCode) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.locate = locate;
        this.quantity = quantity;
        this.collectionCode = collectionCode;
    }

    public BookDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCollectionCode() {
        if(collectionCode==null){
            return "";
        }
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getCollectionParent() {
        return collectionParent;
    }

    public void setCollectionParent(String collectionParent) {
        this.collectionParent = collectionParent;
    }
}
