package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyong on 16/8/9.
 */
public class MessageNoticeMess implements Serializable{
    private  String content;//消息内容
    private String idstr;//消息id
    private String status;//消息状态
    private String time;//消息时间
    private String title;//消息标题
    private String uid;//用户id

    public void setContent(String content) {
        this.content = content;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getIdstr() {
        return idstr;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }
    public static List<MessageNoticeMess> parse(String result){
        try {
            return JSON.parseArray(result, MessageNoticeMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
