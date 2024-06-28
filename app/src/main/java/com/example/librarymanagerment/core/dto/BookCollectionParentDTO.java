package com.example.librarymanagerment.core.dto;

import java.util.Collection;
import java.util.List;

public class BookCollectionParentDTO {

    private Integer id;

    private String name;


    private List<BookCollectionDTO> bookCollectionDTOList;
    public BookCollectionParentDTO() {
    }

    public BookCollectionParentDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookCollectionDTO> getBookCollectionDTOList() {
        return bookCollectionDTOList;
    }

    public void setBookCollectionDTOList(List<BookCollectionDTO> bookCollectionDTOList) {
        this.bookCollectionDTOList = bookCollectionDTOList;
    }
}
