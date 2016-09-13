package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by xuyong on 16/8/10.
 */
public class UserNumMess implements Serializable {
    private String attentionNum;//关注数
    private String fansNum;//粉丝数
    private String friendNum;//好友数

    public String getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(String attentionNum) {
        this.attentionNum = attentionNum;
    }

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }
    public static UserNumMess parse(String result){
        try {
            return JSON.parseObject(result, UserNumMess.class);
        } catch (Exception e) {
            return null;
        }
    }

}
