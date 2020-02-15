package com.ju.wyhouse.db;

import com.ju.wyhouse.model.UserModel;
import com.ju.wyhouse.utils.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/19 23:23
 * Path:com.ju.wyhouse.db
 * Desc:数据库管理类
 */
public class DbManager {


    private static volatile DbManager mInstance;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (mInstance == null) {
            synchronized (DbManager.class) {
                if (mInstance == null) {
                    mInstance = new DbManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 查询全部推送消息
     *
     * @return
     */
    public List<DbMessageData> queryAllMessage() {
        return (List<DbMessageData>) baseQuery(DbMessageData.class);
    }


    /**
     * 保存推送消息
     *
     * @param messageId
     * @param title
     * @param content
     */
    public void saveMessage(String messageId, String title, String content) {
        DbMessageData dbMessageData = new DbMessageData();
        dbMessageData.setMessageId(messageId);
        dbMessageData.setTitle(title);
        dbMessageData.setContent(content);
        baseSave(dbMessageData);
    }


    /**
     * 保存个人信息
     *
     * @param userModel
     */
    public void saveMeUser(UserModel userModel) {
        DbUserData dbUserData = new DbUserData();
        dbUserData.setUserId(userModel.getData().getId());
        dbUserData.setImage(userModel.getData().getImage());
        dbUserData.setJwBanji(userModel.getData().getJwBanji());
        dbUserData.setName(userModel.getData().getName());
        dbUserData.setJwUser(userModel.getData().getJwUser());
        dbUserData.setJwName(userModel.getData().getJwName());
        baseSave(dbUserData);
    }

    /**
     * 更新个人信息
     */
    public void updateMeUser(UserModel userModel) {
        DbUserData dbUserData = queryMeUser();
        if (dbUserData != null) {
            dbUserData.setUserId(userModel.getData().getId());
            dbUserData.setImage(userModel.getData().getImage());
            dbUserData.setJwBanji(userModel.getData().getJwBanji());
            dbUserData.setName(userModel.getData().getName());
            dbUserData.setJwUser(userModel.getData().getJwUser());
            dbUserData.setJwName(userModel.getData().getJwName());
            baseSave(dbUserData);
        }
    }

    /**
     * 删除个人信息
     */
    public void removeMeUser() {
        DbUserData dbUserData = queryMeUser();
        if (dbUserData != null) {
            dbUserData.delete();
        }
    }

    /**
     * 查询个人信息
     *
     * @return
     */
    public DbUserData queryMeUser() {
        List<DbUserData> dbUserDataList = (List<DbUserData>) baseQuery(DbUserData.class);
        LogUtil.i("数据库缓存用户数量：" + dbUserDataList.size());
        if (dbUserDataList.size() > 0) {
            return dbUserDataList.get(0);
        }
        return null;
    }


    /**
     * 更新头像
     */
    public void updateMeImage(String imageUrl) {
        DbUserData dbUserData = queryMeUser();
        if (dbUserData != null) {
            dbUserData.setImage(imageUrl);
            baseSave(dbUserData);
        }
    }


    /**
     * 更新昵称
     */
    public void updateMeNickName(String nickName) {
        DbUserData dbUserData = queryMeUser();
        if (dbUserData != null) {
            dbUserData.setName(nickName);
            baseSave(dbUserData);
        }
    }

    /**
     * 查询基类
     *
     * @return
     */
    private List<? extends LitePalSupport> baseQuery(Class cls) {
        return LitePal.findAll(cls);
    }


    /**
     * 保存基类
     *
     * @param support
     */
    private void baseSave(LitePalSupport support) {
        support.save();
    }


}
