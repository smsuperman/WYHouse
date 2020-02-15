package com.ju.wyhouse.model;

import java.io.Serializable;
import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/28 21:40
 * Path:com.ju.wyhouse.model
 * Desc:墙数据
 */
public class WallDataModel implements Serializable {


    //id
    private int id;
    //1是匿名，0不是 匿名字段
    private int anonymous;
    //发送的用户
    private List<WallDataUserModel> user;
    //内容
    private String content;
    //背景颜色
    private int backgroundColor;
    //匿名头像颜色
    private int avatarColor;
    //背景图片
    private String backgroundImage;
    //点赞数量
    private int likeNumber;
    //是否点赞过，有值就是点赞过
    private int likeId;
    //评论数量
    private int messageNumber;
    //发表时间
    private String createTime;


    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public List<WallDataUserModel> getUser() {
        return user;
    }

    public void setUser(List<WallDataUserModel> user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(int avatarColor) {
        this.avatarColor = avatarColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }
}
