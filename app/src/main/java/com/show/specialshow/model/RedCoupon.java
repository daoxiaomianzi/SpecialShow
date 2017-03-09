package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class RedCoupon implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6616660573955027239L;

    private long enddate;
    private int is_use;
    private double num;
    private String red_id;
    private int type;
    private double use_money;//使用起点
    private String use_space;
    private String use_service;
    private String use_merchant;
    private String use_merchant_id;//优惠劵使用店铺

    public String getUse_merchant_id() {
        return use_merchant_id;
    }

    public void setUse_merchant_id(String use_merchant_id) {
        this.use_merchant_id = use_merchant_id;
    }

    public String getUse_service() {
        return use_service;
    }

    public void setUse_service(String use_service) {
        this.use_service = use_service;
    }

    public String getUse_space() {
        return use_space;
    }

    public void setUse_space(String use_space) {
        this.use_space = use_space;
    }

    public String getUse_merchant() {
        return use_merchant;
    }

    public void setUse_merchant(String use_merchant) {
        this.use_merchant = use_merchant;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public int getIs_use() {
        return is_use;
    }

    public void setIs_use(int is_use) {
        this.is_use = is_use;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }

    public double getUse_money() {
        return use_money;
    }

    public void setUse_money(double use_money) {
        this.use_money = use_money;
    }

    public static RedCoupon parse(String result) {
        try {
            return JSON.parseObject(result, RedCoupon.class);
        } catch (Exception e) {
            return null;
        }
    }
}
