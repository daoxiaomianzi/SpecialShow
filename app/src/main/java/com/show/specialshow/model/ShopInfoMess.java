package com.show.specialshow.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ShopInfoMess implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3673078459692332653L;
    private String shop_info_time;//商户营业时间
    private String shop_info_city;//商户地址地区
    private double shop_info_lon;//经度
    private double shop_info_lat;//纬度

    private String shop_info_address;//商户地址
    private String shop_info_phoneNum;//商户电话号码

    public double getShop_info_lon() {
        return shop_info_lon;
    }

    public void setShop_info_lon(double shop_info_lon) {
        this.shop_info_lon = shop_info_lon;
    }

    public double getShop_info_lat() {
        return shop_info_lat;
    }

    public void setShop_info_lat(double shop_info_lat) {
        this.shop_info_lat = shop_info_lat;
    }

    public String getShop_info_city() {
        return shop_info_city;
    }

    public void setShop_info_city(String shop_info_city) {
        this.shop_info_city = shop_info_city;
    }


    public String getShop_info_time() {
        return shop_info_time;
    }

    public void setShop_info_time(String shop_info_time) {
        this.shop_info_time = shop_info_time;
    }

    public String getShop_info_address() {
        return shop_info_address;
    }

    public void setShop_info_address(String shop_info_address) {
        this.shop_info_address = shop_info_address;
    }

    public String getShop_info_phoneNum() {
        return shop_info_phoneNum;
    }

    public void setShop_info_phoneNum(String shop_info_phoneNum) {
        this.shop_info_phoneNum = shop_info_phoneNum;
    }

    public static ShopInfoMess parse(String result) {
        try {
            return JSON.parseObject(result, ShopInfoMess.class);
        } catch (Exception e) {
            return null;
        }
    }

}
