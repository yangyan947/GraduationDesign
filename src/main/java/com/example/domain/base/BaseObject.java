package com.example.domain.base;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by SunYi on 2016/2/1/0001.
 * 基础类，包含创建时间和id
 */
@MappedSuperclass
public class BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp()
    @Column(name = "createTime",updatable = false)
    private Date createTime;
    @Column(name = "status")
    private String status = "normal";

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
}
