package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Entity
public class Blog extends BaseObject {
    private String context;

    @ManyToOne
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "blog")
    @OrderBy(value = "createTime")
    private Set<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @OrderBy(value = "createTime DESC")
    private Set<User> pointsUsers;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        context = context.replaceAll("http://localhost:8080", "");
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
        Iterator<Comment> it = comments.iterator();
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getStatus().equals("freeze")) {
                it.remove();
            }
        }
        return comments;
    }

    public void setComments(Set<Comment> comments) {
//        for (Comment comment : comments) {
//            if (comment.getStatus().equals("freeze")) {
//                comments.remove(comment);
//            }
//        }
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
    public boolean isHot() {
        return getStatus().equals("hot");
    }
    public boolean isPoint(Long userId) {
        List<Long> userIdList = getPointsUsers().stream().map(User::getId).collect(Collectors.toList());
        return userIdList.contains(userId);
    }
}
