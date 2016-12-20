package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

public class MessageResult {
    private int success;
    private String data;
    private String message;
    private String taglist;

    public String getTaglist() {
        return taglist;
    }

    public void setTaglist(String taglist) {
        this.taglist = taglist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static MessageResult parse(String result) {
        try {
            return JSON.parseObject(result, MessageResult.class);
        } catch (Exception e) {
            return null;
        }
    }
}
