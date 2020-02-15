package com.ju.wyhouse.model;

/**
 * Author:gaoju
 * Date:2020/1/20 12:26
 * Path:com.ju.wyhouse.model
 * Desc:课程model
 */
public class CourseModel {


    private int id;
    private String jwUser;
    private String name;
    private String jiaoshi;
    private String laoshi;
    private String zhouci;
    private String xnxq;
    private String zhouciStr;
    private int jieci;
    //判断是否有数据,默认false，无数据
    private boolean ishave = false;

    public boolean getIshave() {
        return ishave;
    }

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJwUser() {
        return jwUser;
    }

    public void setJwUser(String jwUser) {
        this.jwUser = jwUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJiaoshi() {
        return jiaoshi;
    }

    public void setJiaoshi(String jiaoshi) {
        this.jiaoshi = jiaoshi;
    }

    public String getLaoshi() {
        return laoshi;
    }

    public void setLaoshi(String laoshi) {
        this.laoshi = laoshi;
    }

    public String getZhouci() {
        return zhouci;
    }

    public void setZhouci(String zhouci) {
        this.zhouci = zhouci;
    }

    public String getXnxq() {
        return xnxq;
    }

    public void setXnxq(String xnxq) {
        this.xnxq = xnxq;
    }

    public String getZhouciStr() {
        return zhouciStr;
    }

    public void setZhouciStr(String zhouciStr) {
        this.zhouciStr = zhouciStr;
    }

    public int getJieci() {
        return jieci;
    }

    public void setJieci(int jieci) {
        this.jieci = jieci;
    }

    public boolean isIshave() {
        return ishave;
    }
}
