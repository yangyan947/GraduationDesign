package com.example.domain;

import com.example.domain.base.BaseObject;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private String imgUrl = "/static/images/test/head.jpg";
    private String resume = "他很懒什么都没有留下";
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_user", joinColumns = @JoinColumn(name = "attention_user"), inverseJoinColumns = @JoinColumn(name = "follow_user"))
    @OrderBy(value = "createTime DESC")
    private Set<User> attentionUsers;
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_user", joinColumns = @JoinColumn(name = "follow_user"), inverseJoinColumns = @JoinColumn(name = "attention_user"))
    @OrderBy(value = "createTime DESC")
    private Set<User> followUsers;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy(value = "createTime DESC")
    private List<Blog> blogs;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy(value = "createTime DESC")
    private List<Comment> comments;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_point_blog", joinColumns = @JoinColumn(name = "pointsUser"), inverseJoinColumns = @JoinColumn(name = "pointsBlog"))
//    @OrderBy(value = "createTime DESC")
//    private Set<Blog> pointsBlogs;

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

    public int getSexInt() {
        return sex;
    }


    public void setSex(int sex) {
        this.sex = sex;
    }

    public Set<User> getAttentionUsers() {
        for (User user : attentionUsers) {
            if (user.getStatus() == "freeze") {
                attentionUsers.remove(user);
            }
        }
        return attentionUsers;
    }

    public void setAttentionUsers(Set<User> attentionUsers) {
        this.attentionUsers = attentionUsers;
    }


    public Set<User> getFollowUsers() {
        for (User user : followUsers) {
            if (user.getStatus() == "freeze") {
                followUsers.remove(user);
            }
        }
        return followUsers;
    }

    public void setFollowUsers(Set<User> followUsers) {
        this.followUsers = followUsers;
    }

    public List<Blog> getBlogs() {
        for (Blog blog : blogs) {
            if (blog.getStatus() == "freeze") {
                blogs.remove(blog);
            }
        }
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public void addBlog(Blog blog) {
        this.blogs.add(blog);
    }

    public List<Comment> getComments() {
        for (Comment comment : comments) {
            if (comment.getStatus() == "freeze") {
                comments.remove(comment);
            }
        }
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
        this.resume = user.getResume();
        return this;
    }

//    public Set<Blog> getPointsBlogs() {
//        return pointsBlogs;
//    }
//
//    public void setPointsBlogs(Set<Blog> pointsBlogs) {
//        this.pointsBlogs = pointsBlogs;
//    }

    //    public List<Comment> getReceiveComments() {
//        return receiveComments;
//    }
//
//    public void setReceiveComments(List<Comment> receiveComments) {
//        this.receiveComments = receiveComments;
//    }
    public String getStatusZn() {
        if (getStatus().equals("freeze")) {
            return "冻结";
        } else if (getStatus().equals("normal")) {
            return "正常";
        } else if (getStatus().equals("hot")) {
            return "热门";
        } else if (getStatus().equals("danger")) {
            return "危险";
        }
        return "状态异常";
    }

//    public boolean isPoint(Long blogId) {
//        List<Long> blogIdList = getPointsBlogs().stream().map(Blog::getId).collect(Collectors.toList());
//        return blogIdList.contains(blogId);
//    }
    public boolean isOwner(Long blogId) {
        List<Long> blogIdList = getBlogs().stream().map(Blog::getId).collect(Collectors.toList());
        return blogIdList.contains(blogId);
    }

    public boolean isAttend(Long userId) {
        List<Long> userIdList = getAttentionUsers().stream().map(User::getId).collect(Collectors.toList());
        return userIdList.contains(userId);
    }
    public String relationship(Long userId) {
        List<Long> attentionUsersList = getAttentionUsers().stream().map(User::getId).collect(Collectors.toList());
        List<Long> followUsersList = getFollowUsers().stream().map(User::getId).collect(Collectors.toList());

        if (attentionUsersList.contains(userId)) {
            if (followUsersList.contains(userId)) {
                return "互相关注";
            } else {
                return "已关注ta";
            }
        }else{
            if (followUsersList.contains(userId)) {
                return "已关注你";
            } else {
                return "未关注";
            }
        }
    }
}
