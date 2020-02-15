package com.ju.wyhouse.model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/24 21:32
 * Path:com.ju.wyhouse.model
 * Desc: 全部课程model
 */
public class AllCourseModel implements IPickerViewData {


    /**
     * name : 学院
     * area : [1学院,2学院,3学院]
     */

    private String name;
    private List<String> schoolList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<String> schoolList) {
        this.schoolList = schoolList;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
