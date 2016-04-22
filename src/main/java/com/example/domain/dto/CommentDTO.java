package com.example.domain.dto;

import com.example.domain.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

/**
 * Created by SunYi on 2016/4/19/0019.
 */
public class CommentDTO {
    private Long id;
    private Date createTime;
    private String status;
    private String context;
    private Long userId;
    private Long blogId;

    public CommentDTO(Comment comment) {
        id = comment.getId();
        createTime = comment.getCreateTime();
        status = comment.getStatus();
        context = comment.getContext();
        userId = comment.getUser().getId();
        blogId = comment.getBlog().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
