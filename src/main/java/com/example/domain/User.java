package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by SunYi on 2016/2/1/0001.
 * 用户类包含用户名和密码，继承自BaseObject
 */
@Entity
@Table(name = "user")
public class User extends BaseObject {
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    private int sex = 2;
    private String imgUrl = "";
    private String resume;
    @ManyToMany
    @JoinTable(name = "user_user", joinColumns = @JoinColumn(name = "attention_user"), inverseJoinColumns = @JoinColumn(name = "follow_user"))
    private List<User> attentionUsers;
    @ManyToMany
    @JoinTable(name = "user_user", joinColumns = @JoinColumn(name = "follow_user"), inverseJoinColumns = @JoinColumn(name = "attention_user"))
    private List<User> followUsers;

    @OneToMany
    private List<Blog> blogs;

    @OneToMany
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "user_point_blog", joinColumns = @JoinColumn(name = "user"), inverseJoinColumns = @JoinColumn(name = "blog"))
    private Set<Blog> pointsBlogs;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getSex() {
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

    public List<User> getAttentionUsers() {
        return attentionUsers;
    }

    public void setAttentionUsers(List<User> attentionUsers) {
        this.attentionUsers = attentionUsers;
    }


    public List<User> getFollowUsers() {
        return followUsers;
    }

    public void setFollowUsers(List<User> followUsers) {
        this.followUsers = followUsers;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public void addBlog(Blog blog) {
        this.blogs.add(blog);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public User update(User user) {
        this.phone = user.getPhone();
        this.nickname = user.getNickname();
        this.imgUrl = user.getImgUrl();
        this.resume = user.getResume();
        return this;
    }

    public Set<Blog> getPointsBlogs() {
        return pointsBlogs;
    }

    public void setPointsBlogs(Set<Blog> pointsBlogs) {
        this.pointsBlogs = pointsBlogs;
    }

//    public List<Comment> getReceiveComments() {
//        return receiveComments;
//    }
//
//    public void setReceiveComments(List<Comment> receiveComments) {
//        this.receiveComments = receiveComments;
//    }
}
