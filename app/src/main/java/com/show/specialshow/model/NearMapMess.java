package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyong on 16/8/12.
 */
public class NearMapMess implements Serializable {
    private String address;//地址
    private String title;//标题
    private double latitude;//纬度
    private double longitude;//经度
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String shop_id;//商户id

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
    public static List<NearMapMess> parse(String result){
        try {
            return JSON.parseArray(result, NearMapMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
