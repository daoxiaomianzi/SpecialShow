package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by admin on 2016/8/4.
 */
public class InviteRecord implements Serializable{
    private String nickname;//昵称
    private String score;//奖励
    private String create_time;//时间

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    public static InviteRecord parse(String result) {
        try {
            return JSON.parseObject(result, InviteRecord.class);
        } catch (Exception e) {
            return null;
        }
    }
}
