package com.example.librarymanagerment.core.dto;

public class BookLinkDTO {

    private Integer id;

    private String link;

    private String displayName;

    private String imageFile;

    private String collectionCode;

    public BookLinkDTO() {
    }

    public BookLinkDTO(Integer id, String link, String displayName, String imageFile, String collectionCode) {
        this.id = id;
        this.link = link;
        this.displayName = displayName;
        this.imageFile = imageFile;
        this.collectionCode = collectionCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
}
