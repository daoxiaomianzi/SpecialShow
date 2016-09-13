package com.show.specialshow.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuyong on 16/8/11.
 */
public class BannerMess implements Serializable {
    private String ImagePath;//图片路径
    private String Title;//
    private String Url;//点击的跳转url

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
    public static List<BannerMess> parse(String result){
        try {
            return JSON.parseArray(result, BannerMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
