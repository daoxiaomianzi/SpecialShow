package com.show.specialshow.model;

import java.io.Serializable;

/**
 * Created by xuyong on 2016/10/17.
 */

public class TeShowActivitiesMess implements Serializable {
    public long id;
    public String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
