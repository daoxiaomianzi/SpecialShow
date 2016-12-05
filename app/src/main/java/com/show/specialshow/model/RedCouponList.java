package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by xuyong on 2016/12/5.
 */

public class RedCouponList extends BaseList {
    private List<RedCoupon> list;

    public List<RedCoupon> getList() {
        return list;
    }

    public void setList(List<RedCoupon> list) {
        this.list = list;
    }

    public static RedCouponList parse(String result) {

        try {
            return JSON.parseObject(result, RedCouponList.class);

        } catch (Exception e) {
            return null;
        }
    }
}
