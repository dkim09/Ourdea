package com.ourdea.ourdea.dto;

public class UserDto {

    String email;

    String password;

    String gcmId;

    String name;

    public UserDto(String email, String password, String gcmId, String name) {
        this.email = email;
        this.password = password;
        this.gcmId = gcmId;
        this.name = name;
    }

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
