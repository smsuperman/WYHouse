package com.ju.wyhouse.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.model.CourseModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;
import com.ju.wyhouse.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * 全部课程界面
 */
public class AllCourseActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView all_class_choose;
    private TextView all_week_choose;
    private LinearLayout all_course_ll_one_two;
    private LinearLayout all_course_ll_three_four;
    private LinearLayout all_course_ll_five_six;
    private LinearLayout all_course_ll_seven_eight;
    private LinearLayout all_course_ll_nine_ten;
    private LoadingView mLoadingView;
    private OptionsPickerView mClassPickerView;
    private OptionsPickerView mWeekPickerView;
    private List<String> allWeekList = new ArrayList<>();
    private List<String> allSchoolList = new ArrayList<>();
    private List<List<String>> allClassList = new ArrayList<>();
    private String week;
    private String classRoom;
    private List<CourseModel> courseList = new ArrayList<>();
    private DialogView mDialogView;
    private TextView dialog_course_name;
    private TextView dialog_course_zhouci;
    private TextView dialog_course_teacher;
    private TextView dialog_course_classroom;

    private int[] colorArray = {R.color.course_one,
            R.color.course_two,
            R.color.course_three,
            R.color.course_four,
            R.color.course_five,
            R.color.course_six,
            R.color.course_seven};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_course);
        initView();
        initPickerView();
        initNet();
    }


    /**
     * 获取全校课表列表
     */
    private void initNet() {
        Request allClassRequest = NetUrl.getAllClassListRequest();
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(allClassRequest, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取班级列表json数据成功：" + response);
                mLoadingView.hide();
                jsonToAllClass(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("查询课程表网络获取失败：" + msg);
                mLoadingView.hide();
                ToastUtil.showToast(AllCourseActivity.this, "查询课程表网络获取失败");
            }


            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(AllCourseActivity.this, "网络超时，请稍后再试");
            }
        });
    }

    /**
     * 解析班级列表，并且设置到选择器上
     *
     * @param response
     */
    private void jsonToAllClass(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");
        if (code != 0) {
            ToastUtil.showToast(this, "数据获取失败，请稍后再试或者联系管理员");
            return;
        }
        //开始获取
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray weekListData = data.getJSONArray("xnxq");
        //获取所有学期，第一级，不联动
        for (Object week : weekListData) {
            allWeekList.add((String) week);
        }
        LogUtil.i("AllWeek:" + allWeekList.size());
        //获取所有学院数据，第二级
        JSONArray allSchoolData = data.getJSONArray("xueyuan");
        for (Object school : allSchoolData) {
            allSchoolList.add((String) school);
        }
        //获取所有班级数据，第三级
        JSONArray allClassData = data.getJSONArray("class");
        for (int i = 0; i < allClassData.size(); i++) {
            //临时list
            List<String> tempList = new ArrayList<>();
            JSONArray array = allClassData.getJSONArray(i);
            for (Object room : array) {
                tempList.add((String) room);
            }
            allClassList.add(tempList);
        }
        //设置选择器数据
        mWeekPickerView.setPicker(allWeekList);
        mClassPickerView.setPicker(allSchoolList, allClassList);

    }


    /**
     * 初始化选择器
     */
    private void initPickerView() {

        //学期选择器
        mWeekPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                week = allWeekList.get(options1);
                LogUtil.i("学期选择:" + week);
                all_week_choose.setText(week);
                questToCourse();
            }
        }).setOutSideCancelable(false)
                .build();

        //班级选择器
        mClassPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                classRoom = allClassList.get(options1).get(options2);
                LogUtil.i("班级选择:" + classRoom);
                all_class_choose.setText(allSchoolList.get(options1) + " " + allClassList.get(options1).get(options2));
                questToCourse();
            }
        }).setOutSideCancelable(false)//点击背的地方不消失
                .build();

    }

    /**
     * 开始正式进行网络请求
     */
    public void questToCourse() {
        Request classCourseRequest = NetUrl.getAllClassCourseRequest(week, classRoom);

        mLoadingView.show();
        HttpClient.getInstance().requestToNet(classCourseRequest, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取班级课表数据成功：" + response);
                mLoadingView.hide();
                removeAllLinerLayout();
                jsonToAllCourse(response);
            }

            @Override
            public void onFailure(String msg) {
                mLoadingView.hide();
                ToastUtil.showToast(AllCourseActivity.this, msg);
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(AllCourseActivity.this, "网络超时，请稍后再试");
            }
        });
    }


    /**
     * 解析Json数据
     *
     * @param response
     */
    private void jsonToAllCourse(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getInteger("code");
        if (code != 0) {
            ToastUtil.showToast(this, "获取数据为空，请稍后再试或者与管理员联系");
            return;
        }

        //保存Cookie
        String cookie = jsonObject.getString("cookie");
        SaveAndWriteUtil.saveCookie(cookie);

        //放入前先把之前存放的数据清空
        courseList.clear();

        JSONArray data = jsonObject.getJSONArray("data");
        //开始解析
        for (int i = 0; i < data.size(); i++) {
            CourseModel courseModel = new CourseModel();
            JSONArray array = data.getJSONArray(i);
            if (array.size() == 0) {
                //为0代表课程为空
                courseModel.setIshave(false);
                courseList.add(courseModel);
            } else {
                //有数据进行设置
                courseModel.setIshave(true);
                //去第0个，一个array就一个课程
                JSONObject kebiaoData = array.getJSONObject(0);
                courseModel.setId(kebiaoData.getInteger("id"));
                courseModel.setName(kebiaoData.getString("name"));
                courseModel.setJiaoshi(kebiaoData.getString("jiaoshi"));
                courseModel.setLaoshi(kebiaoData.getString("laoshi"));
                courseModel.setZhouci(kebiaoData.getString("zhouci"));
                courseModel.setJieci(kebiaoData.getInteger("jieci"));
                courseList.add(courseModel);
            }
        }
        LogUtil.i("课程List:" + courseList.size());

        //解析完毕开始加载到LinearLayout上面
        showCourse();
    }

    /**
     * 展示课程数据
     */
    private void showCourse() {
        for (int i = 0; i < courseList.size(); i++) {
            //判断数据是否为空
            //算出是第几行，为0则是第一行,首先判断是第几行除以
            int hang = i / 7;
            //动态创建TextView
            TextView child = new TextView(this);
            child.setWidth(148);
            child.setHeight(400);
            child.setTextSize(10);
            child.setTextColor(Color.WHITE);
            CourseModel courseModel = courseList.get(i);
            if (courseModel.getIshave()) {
                //有课
                child.setText(courseModel.getJiaoshi() + "@" + courseModel.getName() + "@" + courseModel.getLaoshi() + "@" + courseModel.getJieci() + "@" + courseModel.getZhouci());
                //产生随机数来配置背景颜色
                int index = (int) (Math.random() * colorArray.length);
                child.setBackgroundResource(colorArray[index]);
            } else {
                child.setBackgroundColor(Color.WHITE);
            }
            //把position传递给点击事件
            final int position = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogMessage(position);
                }
            });
            switch (hang) {
                case 0:
                    all_course_ll_one_two.addView(child);
                    break;
                case 1:
                    all_course_ll_three_four.addView(child);
                    break;
                case 2:
                    all_course_ll_five_six.addView(child);
                    break;
                case 3:
                    all_course_ll_seven_eight.addView(child);
                    break;
                case 4:
                    all_course_ll_nine_ten.addView(child);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 删除所有view
     */
    private void removeAllLinerLayout() {
        all_course_ll_one_two.removeAllViews();
        all_course_ll_three_four.removeAllViews();
        all_course_ll_five_six.removeAllViews();
        all_course_ll_seven_eight.removeAllViews();
        all_course_ll_nine_ten.removeAllViews();
    }


    private void initView() {
        //更改标题
        getSupportActionBar().setTitle("全校课表");
        //初始化dialog
        mDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_course, Gravity.CENTER);
        dialog_course_name = mDialogView.findViewById(R.id.dialog_course_name);
        dialog_course_zhouci = mDialogView.findViewById(R.id.dialog_course_zhouci);
        dialog_course_teacher = mDialogView.findViewById(R.id.dialog_course_teacher);
        dialog_course_classroom = mDialogView.findViewById(R.id.dialog_course_classroom);
        //初始化LoadingView
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("查询课表中...");

        all_class_choose = findViewById(R.id.all_class_choose);
        all_week_choose = findViewById(R.id.all_week_choose);
        all_course_ll_one_two = findViewById(R.id.all_course_ll_one_two);
        all_course_ll_three_four = findViewById(R.id.all_course_ll_three_four);
        all_course_ll_five_six = findViewById(R.id.all_course_ll_five_six);
        all_course_ll_seven_eight = findViewById(R.id.all_course_ll_seven_eight);
        all_course_ll_nine_ten = findViewById(R.id.all_course_ll_nine_ten);
        all_class_choose.setOnClickListener(this);
        all_week_choose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_class_choose:
                mClassPickerView.show();
                break;
            case R.id.all_week_choose:
                mWeekPickerView.show();
                break;
            default:
                break;
        }
    }

    /**
     * 展示课程详细信息
     *
     * @param position
     */
    private void showDialogMessage(int position) {
        CourseModel courseModel = courseList.get(position);
        dialog_course_name.setText("课程：" + courseModel.getName());
        dialog_course_zhouci.setText("周次：" + courseModel.getZhouci());
        dialog_course_teacher.setText("老师：" + courseModel.getLaoshi());
        dialog_course_classroom.setText("教室：" + courseModel.getJiaoshi());
        mDialogView.show();
    }

}
