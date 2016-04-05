package com.tianm.service.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by SunYi on 2016/3/25/0025.
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Message implements Serializable {
    private boolean isSuccess = true;
    private String reason = "success";
    private Object others;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Message(boolean isSuccess, String reason) {
        this.isSuccess = isSuccess;
        this.reason = reason;
    }

    public Message() {
    }


    public Message(boolean isSuccess, String reason, Object others) {
        this.others = others;
        this.isSuccess = isSuccess;
        this.reason = reason;
    }

    public Object getOthers() {
        return others;
    }

    public void setOthers(Object others) {
        this.others = others;
    }
}
