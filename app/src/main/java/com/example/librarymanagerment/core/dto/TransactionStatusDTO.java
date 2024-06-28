package com.example.librarymanagerment.core.dto;

public class TransactionStatusDTO {
    private int id;

    private Integer statusCode;

    private String name;

    public TransactionStatusDTO() {
    }

    public TransactionStatusDTO(int id, Integer statusCode, String name) {
        this.id = id;
        this.statusCode = statusCode;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
