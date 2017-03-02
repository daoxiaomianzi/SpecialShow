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
    private int couponNum;//优惠劵数量
    private int appointmentNum;//预约数量
    private int isMerchant;//返回2说明此用户同时是商户，1为普通用户
    private int userBiaoshi;//返回2说明此用户同时为手艺人，1为普通用户
    private int isAgent;//返回1说明此用户可以邀请别人成为代理，0不是

    public int getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(int isAgent) {
        this.isAgent = isAgent;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public int getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(int appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public int getUserBiaoshi() {
        return userBiaoshi;
    }

    public void setUserBiaoshi(int userBiaoshi) {
        this.userBiaoshi = userBiaoshi;
    }

    public int getIsMerchant() {
        return isMerchant;
    }

    public void setIsMerchant(int isMerchant) {
        this.isMerchant = isMerchant;
    }

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

    public static UserNumMess parse(String result) {
        try {
            return JSON.parseObject(result, UserNumMess.class);
        } catch (Exception e) {
            return null;
        }
    }

}
