package com.example.librarymanagerment.core.version;

public class VersionDTO {

    private Integer id;
    private String versionCode;
    private String versionIndex;

    public VersionDTO() {
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(String versionIndex) {
        this.versionIndex = versionIndex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
