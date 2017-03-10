package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyong on 2016/12/9.
 */

public class ConditionMess implements Serializable {
    private int key;
    private String name;

    public ConditionMess() {
    }

    public ConditionMess(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<ConditionMess> parse(String result) {
        try {
            return JSON.parseArray(result, ConditionMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
