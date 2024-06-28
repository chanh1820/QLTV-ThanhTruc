package com.example.librarymanagerment.core.dto;

public class SettingDTO {

    private String name;

    private Integer idIcon;

    public SettingDTO() {
    }

    public SettingDTO(String name, Integer idIcon) {
        this.name = name;
        this.idIcon = idIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(Integer idIcon) {
        this.idIcon = idIcon;
    }
}
