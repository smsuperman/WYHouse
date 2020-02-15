package com.ju.wyhouse.model;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/26 19:31
 * Path:com.ju.wyhouse.model
 * Desc: 空教室model
 */
public class ClassroomModel {

    private String name;
    private List<Integer> statusList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }
}
