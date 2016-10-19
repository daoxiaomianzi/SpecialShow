package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by xuyong on 2016/10/19.
 */

public class TeShowActivitiesList extends BaseList {
    private List<TeShowActivitiesMess> list;

    public List<TeShowActivitiesMess> getList() {
        return list;
    }

    public void setList(List<TeShowActivitiesMess> list) {
        this.list = list;
    }
    public static TeShowActivitiesList parse(String result) {
        try {
            return JSON.parseObject(result, TeShowActivitiesList.class);

        } catch (Exception e) {
            return null;
        }
    }
}
