package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.Entity;

/**
 * Created by SunYi on 2016/4/19/0019.
 */
@Entity
public class Admin extends BaseObject {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}