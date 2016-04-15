package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SunYi on 2016/2/1/0001.
 * 用户类包含用户名和密码，继承自BaseObject
 */
@Entity
@Table(name = "user")
public class User extends BaseObject {
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private int sex;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String  getSex() {
        if (sex == 0) {
            return "女";
        } else if (sex == 1) {
            return "男";
        }
        return "保密";
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
