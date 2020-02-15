package com.ju.wyhouse.db;

import org.litepal.crud.LitePalSupport;

/**
 * Author:gaoju
 * Date:2020/2/9 18:50
 * Path:com.ju.wyhouse.db
 * Desc:个人用户存储
 */
public class DbUserData extends LitePalSupport {


    private int userId;
    private String name;
    private String image;
    private String jwUser;
    private String jwName;
    private String jwBanji;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJwUser() {
        return jwUser;
    }

    public void setJwUser(String jwUser) {
        this.jwUser = jwUser;
    }

    public String getJwName() {
        return jwName;
    }

    public void setJwName(String jwName) {
        this.jwName = jwName;
    }

    public String getJwBanji() {
        return jwBanji;
    }

    public void setJwBanji(String jwBanji) {
        this.jwBanji = jwBanji;
    }
}
