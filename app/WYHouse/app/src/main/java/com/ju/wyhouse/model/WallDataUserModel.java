package com.ju.wyhouse.model;

import java.io.Serializable;

/**
 * Author:gaoju
 * Date:2020/2/6 17:58
 * Path:com.ju.wyhouse.model
 * Desc:墙用户数据
 */
public class WallDataUserModel implements Serializable {


    private String id;
    private String image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
