package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by xuyong on 16/8/8.
 */
public class ShopLaneList extends BaseList {
    private List<ShopListMess> list;

    public List<ShopListMess> getList() {
        return list;
    }

    public void setList(List<ShopListMess> list) {
        this.list = list;
    }
    public static  ShopLaneList parse(String result){

        try {
            return JSON.parseObject(result, ShopLaneList.class);

        } catch (Exception e) {
            return null;
        }
    }
}
