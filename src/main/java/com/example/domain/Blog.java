package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Entity
public class Blog extends BaseObject {
    private String context;
    @ManyToOne
    private User user;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_point_blog", joinColumns = @JoinColumn(name = "blog"), inverseJoinColumns = @JoinColumn(name = "user"))
    private Set<User> pointsUsers;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getPoints() {
        if (pointsUsers != null)
            return pointsUsers.size();
        else
            return 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getPointsUsers() {
        return pointsUsers;
    }

    public void setPointsUsers(Set<User> pointsUsers) {
        this.pointsUsers = pointsUsers;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public String getStatusZn() {
        if (getStatus().equals("freeze")) {
            return "冻结";
        } else if (getStatus().equals("normal")) {
            return "正常";
        } else if (getStatus().equals("hot")) {
            return "热门";
        }
        return "状态异常";
    }
}
