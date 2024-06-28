package com.example.librarymanagerment.core.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterDataDTO {
    private List<String> collectionParentBookDTO;
    private List<String> classRoomBookDTO;
    private Map<String, List<String>> dataBook;
    private List<String> collectionParentBookReadingDTO;
    private List<String> classRoomBookReadingDTO;
    private Map<String, List<String>> dataBookReading;

    public MasterDataDTO() {
    }

    public List<String> getCollectionParentBookDTO() {
        return collectionParentBookDTO;
    }

    public void setCollectionParentBookDTO(List<String> collectionParentBookDTO) {
        this.collectionParentBookDTO = collectionParentBookDTO;
    }

    public List<String> getClassRoomBookDTO() {
        return classRoomBookDTO;
    }

    public void setClassRoomBookDTO(List<String> classRoomBookDTO) {
        this.classRoomBookDTO = classRoomBookDTO;
    }

    public Map<String, List<String>> getDataBook() {
        return dataBook;
    }

    public void setDataBook(Map<String, List<String>> dataBook) {
        this.dataBook = dataBook;
    }

    public List<String> getCollectionParentBookReadingDTO() {
        return collectionParentBookReadingDTO;
    }

    public void setCollectionParentBookReadingDTO(List<String> collectionParentBookReadingDTO) {
        this.collectionParentBookReadingDTO = collectionParentBookReadingDTO;
    }

    public List<String> getClassRoomBookReadingDTO() {
        return classRoomBookReadingDTO;
    }

    public void setClassRoomBookReadingDTO(List<String> classRoomBookReadingDTO) {
        this.classRoomBookReadingDTO = classRoomBookReadingDTO;
    }

    public Map<String, List<String>> getDataBookReading() {
        return dataBookReading;
    }

    public void setDataBookReading(Map<String, List<String>> dataBookReading) {
        this.dataBookReading = dataBookReading;
    }
}
