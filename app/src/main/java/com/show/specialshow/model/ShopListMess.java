package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ShopListMess implements Serializable {

    /**
     * 秀坊列表数据
     */
    private static final long serialVersionUID = 6083981045076101997L;
    private String tags;//标签
    private String needCard;//蹭卡
    private String showCard;//秀卡
    private String hot;//人气
    private String shop_id;//商户id
    private String shop_uid;//商户店家用户id
    private String title;//商户名
    private String distance;//距离
    private String pic_urls;//图片地址
    private int notice;//是否收藏
    private String address;//店铺地址
    private String xf;//人均消费

    public String getShop_uid() {
        return shop_uid;
    }

    public void setShop_uid(String shop_uid) {
        this.shop_uid = shop_uid;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNeedCard() {
        return needCard;
    }

    public void setNeedCard(String needCard) {
        this.needCard = needCard;
    }

    public String getShowCard() {
        return showCard;
    }

    public void setShowCard(String showCard) {
        this.showCard = showCard;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(String pic_urls) {
        this.pic_urls = pic_urls;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public static List<ShopListMess> parse(String result) {
        try {
            return JSON.parseArray(result, ShopListMess.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static ShopListMess getparse(String result) {
        try {
            return JSON.parseObject(result, ShopListMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
