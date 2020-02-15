package com.ju.wyhouse.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ju.wyhouse.entity.Constants;

/**
 * Author:gaoju
 * Date:2020/1/18 16:47
 * Path:com.ju.wyhouse.utils
 * Desc: 数据库封装
 */
public class SpManager {

    /**
     * key - values 存储方式
     * 它的存储路径：data/data/packageName/shared_prefs/sp_name.xml
     */

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private volatile static SpManager mInstance;


    private SpManager() {
    }


    public static SpManager getInstance() {
        if (mInstance == null) {
            synchronized (SpManager.class) {
                if (mInstance == null) {
                    mInstance = new SpManager();
                }
            }
        }
        return mInstance;
    }

    public void initSp(Context context){
        sp = context.getSharedPreferences(Constants.SP_NAME,Context.MODE_PRIVATE);
        editor =sp.edit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defValues) {
        return sp.getInt(key, defValues);
    }

    public void putString(String key, String values) {
        editor.putString(key, values);
        editor.commit();
    }

    public String getString(String key, String defValues) {
        return sp.getString(key, defValues);
    }

    public void putBoolean(String key, boolean values) {
        editor.putBoolean(key, values);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValues) {
        return sp.getBoolean(key, defValues);
    }

    public void deleteKey(String key) {
        editor.remove(key);
        editor.commit();
    }
}
