package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class CircleDynamicItem implements Serializable {

    private static final long serialVersionUID = -5390711273604509331L;
    /**
     * 文章标题
     */
    private String status_urlname;
    /**
     * 文章链接url
     */
    private String status_url;

    public String getStatus_urlname() {
        return status_urlname;
    }

    public void setStatus_urlname(String status_urlname) {
        this.status_urlname = status_urlname;
    }

    public String getStatus_url() {
        return status_url;
    }

    public void setStatus_url(String status_url) {
        this.status_url = status_url;
    }

    /**
     * 用户对此条状态是否点赞
     */
    private int hit;
    /**
     * 动态ID
     */
    private String idStr;
    /**
     * 动态评论数
     */
    private int status_comment;
    /**
     * 动态内容
     */
    private String status_content;
    /**
     * 喜欢人数
     */
    private int status_favor;
    /**
     * 动态图片
     */
    private List<DynamicImage> status_pics;
    /**
     * 标签
     */
    private String tags;
    /**
     * 门店相关信息
     */
    private PackageMess status_package;

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public PackageMess getStatus_package() {
        return status_package;
    }

    public void setStatus_package(PackageMess status_package) {
        this.status_package = status_package;
    }

    /**
     * 动态用户信息
     */
    private DynamicUser status_user;


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public int getStatus_comment() {
        return status_comment;
    }

    public void setStatus_comment(int status_comment) {
        this.status_comment = status_comment;
    }

    public String getStatus_content() {
        return status_content;
    }

    public void setStatus_content(String status_content) {
        this.status_content = status_content;
    }

    public int getStatus_favor() {
        return status_favor;
    }

    public void setStatus_favor(int status_favor) {
        this.status_favor = status_favor;
    }

    public List<DynamicImage> getStatus_pics() {
        return status_pics;
    }

    public void setStatus_pics(List<DynamicImage> status_pics) {
        this.status_pics = status_pics;
    }

    public DynamicUser getStatus_user() {
        return status_user;
    }

    public void setStatus_user(DynamicUser status_user) {
        this.status_user = status_user;
    }

    public static CircleDynamicItem parse(String result) {
        return JSON.parseObject(result, CircleDynamicItem.class);
    }
}
