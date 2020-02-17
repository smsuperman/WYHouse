package com.ju.wyhouse.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ju.wyhouse.R;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.model.CourseModel;
import com.ju.wyhouse.model.MeCourseModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.DataUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.DialogView;
import com.ju.wyhouse.view.LoadingView;


import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class CourseActivity extends BaseBackActivity implements View.OnClickListener {


    private TextView course_choose;
    private List<String> weekList = new ArrayList<>();
    private List<String> termList = new ArrayList<>();
    private OptionsPickerView mOptionsPickerView;
    private MeCourseModel meCourseModel;
    private int week;
    private String term;
    private LinearLayout course_ll_one_two;
    private LinearLayout course_ll_three_four;
    private LinearLayout course_ll_five_six;
    private LinearLayout course_ll_seven_eight;
    private LinearLayout course_ll_nine_ten;
    private int[] colorCourseArray;
    private LoadingView mLoadingView;
    //第一次进行查询
    private DialogView mDialogView;
    private TextView dialog_course_name;
    private TextView dialog_course_zhouci;
    private TextView dialog_course_teacher;
    private TextView dialog_course_classroom;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        initData();
        initView();
        //开始请求数据，学习默认为最新的学期
        initNetToCourse();
    }


    /**
     * 初始化相关数据
     */
    private void initData() {
        //更改标题
        getSupportActionBar().setTitle("课表");
        colorCourseArray = DataUtil.getColorCourseArray();
        termList.addAll(gettermData());
        weekList.addAll(getWeekData());
        //默认获取最新学期
        term = termList.get(termList.size() - 1);
    }

    private void initView() {

        course_choose = findViewById(R.id.course_choose);
        course_choose.setOnClickListener(this);
        //初始化dialog
        mDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_course, Gravity.CENTER);
        dialog_course_name = mDialogView.findViewById(R.id.dialog_course_name);
        dialog_course_zhouci = mDialogView.findViewById(R.id.dialog_course_zhouci);
        dialog_course_teacher = mDialogView.findViewById(R.id.dialog_course_teacher);
        dialog_course_classroom = mDialogView.findViewById(R.id.dialog_course_classroom);

        //初始化选择器
        mOptionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //设置学期内容
                course_choose.setText(makeTitle(options1, options2));
                term = termList.get(options1);
                initNetToCourse();
            }
        }).setOutSideCancelable(false)//点击背的地方不消失
                .build();//创建

        //默认加载本地数据，后期改成网络请求获取学期和周数
        mOptionsPickerView.setNPicker(termList, weekList, null);
        //初始化所有lin
        course_ll_one_two = findViewById(R.id.course_ll_one_two);
        course_ll_three_four = findViewById(R.id.course_ll_three_four);
        course_ll_five_six = findViewById(R.id.course_ll_five_six);
        course_ll_seven_eight = findViewById(R.id.course_ll_seven_eight);
        course_ll_nine_ten = findViewById(R.id.course_ll_nine_ten);
        //初始化LoadingView
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("查询课表中...");
    }


    /**
     * 拼接字符
     *
     * @return
     */
    private String makeTitle(int options1, int options2) {
        //拼接字符
        String content;
        //设置到文本
        term = termList.get(options1);
        content = term + " " + weekList.get(options2);
        //设置拼接文本
        //获取要请求的周次，因为是从0开始算，所以+1
        week = options2 + 1;
        return content;
    }


    /**
     * 开始网络请求获取课程表数据
     */
    private void initNetToCourse() {
        //先清空所有LinearLayout的view
        removeAllLinerLayout();
        Request request = NetUrl.getCourseRequest(term);
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                mLoadingView.hide();
                LogUtil.i("获取课程表json数据成功：" + response);
                parsingJsonCourse(response);
            }

            @Override
            public void onFailure(String msg) {
                mLoadingView.hide();
                ToastUtil.showToast(CourseActivity.this, msg);
            }

            @Override
            public void onError(String errMsg) {
                LogUtil.e("errMsg:" + errMsg);
                ToastUtil.showToast(CourseActivity.this, "网络超时，请稍后再试");
            }
        });

    }

    /**
     * 进行解析
     *
     * @param response
     */
    private void parsingJsonCourse(String response) {
        meCourseModel = JSON.parseObject(response, new TypeReference<MeCourseModel>() {
        });

        if (meCourseModel == null) {
            return;
        }
        if (meCourseModel.getCode() == 0) {
            SaveAndWriteUtil.saveCookie(meCourseModel.getCookie());
            //第一次初始化设置当前周次,周次从0开始，所以-1
            if (isFirst) {
                isFirst = false;
                mOptionsPickerView.setSelectOptions(termList.size() - 1, meCourseModel.getData().getZhouci() - 1);
                course_choose.setText(makeTitle(termList.size() - 1, meCourseModel.getData().getZhouci() - 1));
            }
            //解析完毕开始展示课程
            showCourse();
        } else {
            ToastUtil.showToast(CourseActivity.this, "获取课程表数据失败，请稍后再试");
        }
    }


    /**
     * 展示课程数据
     */
    private void showCourse() {
        //循环34次，总共34个格子
        for (int i = 0; i <= 34; i++) {
            if (meCourseModel.getData().getTab().get(String.valueOf(i)) == null) {
                //添加空格子
                addTextView(i, null);
                continue;
            }
            if (meCourseModel.getData().getTab().get(String.valueOf(i)).get(week) != -1) {
                int courseIndex = meCourseModel.getData().getTab().get(String.valueOf(i)).get(week);
                addTextView(i, meCourseModel.getData().getKecheng().get(courseIndex));
            } else {
                //添加空格子
                addTextView(i, null);
            }
        }

    }


    /**
     * 添加格子
     *
     * @param index
     * @param courseBean
     */
    public void addTextView(int index, MeCourseModel.DataBean.KechengBean courseBean) {

        //算出是第几行，为0则是第一行,首先判断是第几行除以
        int hang = index / 7;
        switch (hang) {
            case 0:
                course_ll_one_two.addView(createTextView(courseBean));
                break;
            case 1:
                course_ll_three_four.addView(createTextView(courseBean));
                break;
            case 2:
                course_ll_five_six.addView(createTextView(courseBean));
                break;
            case 3:
                course_ll_seven_eight.addView(createTextView(courseBean));
                break;
            case 4:
                course_ll_nine_ten.addView(createTextView(courseBean));
                break;
            default:
                break;
        }
    }


    //创建课程
    public TextView createTextView(MeCourseModel.DataBean.KechengBean courseBean) {
        //动态创建TextView
        TextView child = new TextView(this);
        child.setWidth(148);
        child.setHeight(400);
        child.setTextSize(10);
        child.setTextColor(Color.WHITE);
        if (courseBean != null) {
            child.setText(courseBean.getJiaoshi() + "@" + courseBean.getName() + "@" + courseBean.getLaoshi());
            //产生随机数来配置背景颜色
            int index = (int) (Math.random() * colorCourseArray.length);
            child.setBackgroundResource(colorCourseArray[index]);
            //设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogMessage(courseBean);
                }
            });
        }
        return child;
    }

    /**
     * 展示课程详细信息
     */
    private void showDialogMessage(MeCourseModel.DataBean.KechengBean courseBean) {
        dialog_course_name.setText("课程：" + courseBean.getName());
        dialog_course_zhouci.setText("周次：" + courseBean.getZhouci());
        dialog_course_teacher.setText("老师：" + courseBean.getLaoshi());
        dialog_course_classroom.setText("教室：" + courseBean.getJiaoshi());
        mDialogView.show();
    }


    /**
     * 删除所有view
     */
    private void removeAllLinerLayout() {
        course_ll_one_two.removeAllViews();
        course_ll_three_four.removeAllViews();
        course_ll_five_six.removeAllViews();
        course_ll_seven_eight.removeAllViews();
        course_ll_nine_ten.removeAllViews();
    }

    private List<String> getWeekData() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add("第" + i + "周");
        }
        return list;
    }

    /**
     * 本地数据
     *
     * @return
     */
    private List<String> gettermData() {
        List<String> list = new ArrayList<>();
        list.add("2016-2017-1");
        list.add("2016-2017-2");
        list.add("2017-2018-1");
        list.add("2017-2018-2");
        list.add("2018-2019-1");
        list.add("2018-2019-2");
        list.add("2019-2020-1");
        list.add("2019-2020-2");
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.course_choose:
                mOptionsPickerView.show();
                break;
            default:
                break;
        }
    }


}
