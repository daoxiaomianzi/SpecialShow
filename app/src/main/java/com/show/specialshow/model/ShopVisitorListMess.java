package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopVisitorListMess implements Serializable {
    /**
     * 秀客列表数据
     */
    private static final long serialVersionUID = 4360437232472741774L;
    private String distance;//距离
    private String user_introduce;//秀客简介
    private String user_constellation;//秀客星座
    private int user_xz;//星座id
    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    private String shop_name;//门店地址

    public int getUser_xz() {
        return user_xz;
    }

    public void setUser_xz(int user_xz) {
        this.user_xz = user_xz;
    }

    private String user_age;//秀客年龄
    private String user_name;//秀客昵称
    private String user_sex;//秀客性别
    private String user_icon;//秀客头像
    private String user_id;//秀客id
    private int attention;//秀客是否被关注

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUser_introduce() {
        return user_introduce;
    }

    public void setUser_introduce(String user_introduce) {
        this.user_introduce = user_introduce;
    }

    public String getUser_constellation() {
        return user_constellation;
    }

    public void setUser_constellation(String user_constellation) {
        this.user_constellation = user_constellation;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public static List<ShopVisitorListMess> parse(String result) {
        try {
            return JSON.parseArray(result, ShopVisitorListMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
