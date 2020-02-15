package com.ju.wyhouse.db;

import org.litepal.crud.LitePalSupport;

/**
 * Author:gaoju
 * Date:2020/1/19 20:56
 * Path:com.ju.wyhouse.db
 * Desc:
 */
public class DbMessageData extends LitePalSupport {


    //消息id
    private String messageId;
    //消息标题
    private String title;
    //消息内容
    private String content;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
