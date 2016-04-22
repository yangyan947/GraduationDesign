package com.example.domain.dto;

import com.example.domain.Blog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by SunYi on 2016/4/19/0019.
 */
public class BlogDTO {
    private Long id;
    private Date createTime;
    private String status;
    private String context;
    private Long userId;
    private Set<CommentDTO> comments;

    public BlogDTO(Blog blog) {
        id = blog.getId();
        createTime = blog.getCreateTime();
        status = blog.getStatus();
        context = blog.getContext();
        userId = blog.getUser().getId();
        comments = new HashSet<>();
        comments.addAll(blog.getComments().stream().map(comment -> new CommentDTO(comment)).collect(Collectors.toList()));
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

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
