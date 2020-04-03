package com.developer.enjad.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String id;
    private String name;
    private String password;
    private String phone;
    private int user_type;

    public UserModel() {
    }

    public UserModel(String id, String name, String password, String phone, int user_type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}
