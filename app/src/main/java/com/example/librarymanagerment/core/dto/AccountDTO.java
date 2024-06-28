package com.example.librarymanagerment.core.dto;

public class AccountDTO {

    private String userName;

    private String passWord;

    private  String displayName;

    private  String role;

    private String classRoom;

    private  String flagLogin;

    public AccountDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getFlagLogin() {
        return flagLogin;
    }

    public void setFlagLogin(String flagLogin) {
        this.flagLogin = flagLogin;
    }
}
