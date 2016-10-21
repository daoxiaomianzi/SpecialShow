package com.show.specialshow.model;

import java.io.Serializable;

/**
 * Created by xuyong on 2016/10/17.
 */

public class TeShowActivitiesMess implements Serializable {
    private String post_content;
    private  int post_id;
    private  String post_title;
    private String post_slogan;
    private int post_expense;
    private String post_smeta;
    private String post_excerpt;

    private boolean post_isprogress;
    private String post_active_time;
    private String post_place;
    private int isEnter;//1:已报名，0：未报名

    public int getIsEnter() {
        return isEnter;
    }

    public void setIsEnter(int isEnter) {
        this.isEnter = isEnter;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_slogan() {
        return post_slogan;
    }

    public void setPost_slogan(String post_slogan) {
        this.post_slogan = post_slogan;
    }

    public int getPost_expense() {
        return post_expense;
    }

    public void setPost_expense(int post_expense) {
        this.post_expense = post_expense;
    }

    public String getPost_smeta() {
        return post_smeta;
    }

    public void setPost_smeta(String post_smeta) {
        this.post_smeta = post_smeta;
    }

    public boolean isPost_isprogress() {
        return post_isprogress;
    }

    public void setPost_isprogress(boolean post_isprogress) {
        this.post_isprogress = post_isprogress;
    }

    public String getPost_active_time() {
        return post_active_time;
    }

    public void setPost_active_time(String post_active_time) {
        this.post_active_time = post_active_time;
    }

    public String getPost_place() {
        return post_place;
    }

    public void setPost_place(String post_place) {
        this.post_place = post_place;
    }
}
