package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Entity
public class Comment extends BaseObject {
    @Column(nullable = false)
    private String context;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Blog blog;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public User getReceiveUser() {
//        return receiveUser;
//    }
//
//    public void setReceiveUser(User receiveUser) {
//        this.receiveUser = receiveUser;
//    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getStatusZn() {
        if (getStatus().equals("freeze")) {
            return "冻结";
        } else if (getStatus().equals("normal")) {
            return "正常";
        }
        return "状态异常";
    }
}
