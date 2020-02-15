package com.ju.wyhouse.model;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/26 22:35
 * Path:com.ju.wyhouse.model
 * Desc:成绩总分和绩点Model
 *
 * */
public class AllScoreModel {

    //0是总分 1是绩点
    private int type;
    private List<String> zfpm;
    private String pmbj;
    private String gkcj;
    private List<String> pjxfjdpm;
    private String zf;
    private String gkxfjd;


    public List<String> getPjxfjdpm() {
        return pjxfjdpm;
    }

    public void setPjxfjdpm(List<String> pjxfjdpm) {
        this.pjxfjdpm = pjxfjdpm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getZfpm() {
        return zfpm;
    }

    public void setZfpm(List<String> zfpm) {
        this.zfpm = zfpm;
    }

    public String getPmbj() {
        return pmbj;
    }

    public void setPmbj(String pmbj) {
        this.pmbj = pmbj;
    }

    public String getGkcj() {
        return gkcj;
    }

    public void setGkcj(String gkcj) {
        this.gkcj = gkcj;
    }


    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getGkxfjd() {
        return gkxfjd;
    }

    public void setGkxfjd(String gkxfjd) {
        this.gkxfjd = gkxfjd;
    }
}
