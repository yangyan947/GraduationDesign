package com.tianm.domain;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by SunYi on 2016/3/24/0024.
 */

@Entity
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class User implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String email;

    private String name;
    private String password;

    // ==============
    // PUBLIC METHODS
    // ==============
    public User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
} // class User

