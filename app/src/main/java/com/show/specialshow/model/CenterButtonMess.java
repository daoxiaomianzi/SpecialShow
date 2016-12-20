package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijuan on 2016/9/12.
 */
public class CenterButtonMess implements Serializable {
    private String tag_ico;
    private String tag_ico_small;
    private String tag;
    private int tag_id;

    public String getTag_ico() {
        return tag_ico;
    }

    public void setTag_ico(String tag_ico) {
        this.tag_ico = tag_ico;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_ico_small() {
        return tag_ico_small;
    }

    public void setTag_ico_small(String tag_ico_small) {
        this.tag_ico_small = tag_ico_small;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public static List<CenterButtonMess> parse(String result) {
        try {
            return JSON.parseArray(result, CenterButtonMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}