package com.show.specialshow.model;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShowVisitorList extends BaseList {
    /**
     *
     */
    private static final long serialVersionUID = 3236200017432762003L;
    private List<ShopVisitorListMess> list;
    private List<ConditionMess> filter;
    private List<ConditionMess> stafftype;
    private List<ConditionMess> ordering;
    public List<ConditionMess> getFilter() {
        return filter;
    }

    public void setFilter(List<ConditionMess> filter) {
        this.filter = filter;
    }

    public List<ConditionMess> getStafftype() {
        return stafftype;
    }

    public void setStafftype(List<ConditionMess> stafftype) {
        this.stafftype = stafftype;
    }

    public List<ConditionMess> getOrdering() {
        return ordering;
    }

    public void setOrdering(List<ConditionMess> ordering) {
        this.ordering = ordering;
    }

    public List<ShopVisitorListMess> getList() {
        return list;
    }

    public void setList(List<ShopVisitorListMess> list) {
        this.list = list;
    }

    public static ShowVisitorList parse(String result) {
        try {
            return JSON.parseObject(result, ShowVisitorList.class);

        } catch (Exception e) {
            return null;
        }
    }
}
