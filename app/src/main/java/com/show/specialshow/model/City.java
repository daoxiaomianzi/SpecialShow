package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable{
    public String name;
    public String pinyi;

    public City(String name, String pinyi) {
        super();
        this.name = name;
        this.pinyi = pinyi;
    }

    public City() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyi() {
        return pinyi;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }
    public static List<City> parse(String result){
        try {
            return JSON.parseArray(result, City.class);
        } catch (Exception e) {
            return null;
        }
    }
}
