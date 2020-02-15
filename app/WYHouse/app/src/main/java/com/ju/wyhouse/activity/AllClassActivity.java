package com.ju.wyhouse.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.CommonAdapter;
import com.ju.wyhouse.adapter.CommonViewHolder;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.model.ClassroomModel;
import com.ju.wyhouse.net.HttpClient;
import com.ju.wyhouse.net.INetCallback;
import com.ju.wyhouse.net.NetUrl;
import com.ju.wyhouse.utils.DateUtil;
import com.ju.wyhouse.utils.LogUtil;
import com.ju.wyhouse.utils.SaveAndWriteUtil;
import com.ju.wyhouse.manager.SpManager;
import com.ju.wyhouse.utils.ToastUtil;
import com.ju.wyhouse.view.LoadingView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

public class AllClassActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView allclass_build_choose;
    private TextView allclass_date_choose;
    private RecyclerView allclass_list;
    private List<String> mainBuildList = new ArrayList<>();
    private List<List<String>> buildList = new ArrayList<>();
    private OptionsPickerView mBuildPickerView;
    private TimePickerView mTimePickerView;
    private LoadingView mLoadingView;
    private String nowDate;
    private String mainBuild;
    private String build;
    //第一次进入查询flag
    private boolean isFirst = true;
    private CommonAdapter<ClassroomModel> classroomAdapter;
    private List<ClassroomModel> mClassroomList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_class);
        initBuildData();
        initView();
        initPickerView();
        queryAllClass();
    }


    /**
     * 进行网络查询课程
     */
    private void queryAllClass() {
        //每次查询前先清空之前的数据
        mClassroomList.clear();
        //首次进入设置为当前的日期，地址默认主校区，瑞樟楼
        if (isFirst) {
            isFirst = false;
            //设置默认大楼
            mainBuild = mainBuildList.get(0);
            build = buildList.get(0).get(0);
            mBuildPickerView.setSelectOptions(0, 0);
            allclass_build_choose.setText(mainBuild + " " + build);
            //设置默认时间
//            nowDate = "2020-02-19";
            nowDate = DateUtil.getDateNow();
            mTimePickerView.setDate(DateUtil.getPickerViewDate());
            allclass_date_choose.setText(nowDate);
        }
        //开始获取url
        Request request = NetUrl.getAllClassRoomRequest(mainBuild, build, nowDate);
        LogUtil.i("url：" + request.url());
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取空教室json数据成功：" + response);
                mLoadingView.hide();
                jsonToAllClassRoom(response);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("查询空教室网络获取失败：" + msg);
                mLoadingView.hide();
                ToastUtil.showToast(AllClassActivity.this, "查询空教室网络获取失败");
            }
        });
    }


    /**
     * 解析Json数据
     *
     * @param response
     */
    private void jsonToAllClassRoom(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        //保存cookie
        String cookie = jsonObject.getString("cookie");
        SaveAndWriteUtil.saveCookie(cookie);
        int code = jsonObject.getInteger("code");
        //等于0才有数据，不等于0一律return
        if (code != 0) {
            return;
        }
        //开始解析数据
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            ClassroomModel classroomModel = new ClassroomModel();
            JSONObject dataObject = data.getJSONObject(i);
            classroomModel.setName(dataObject.getString("name"));
            //json转换成list
            List<Integer> list = JSONObject.parseArray(dataObject.getJSONArray("status").toJSONString(), Integer.class);
            classroomModel.setStatusList(list);
            mClassroomList.add(classroomModel);
        }
        //加载完毕刷新数据
        classroomAdapter.notifyDataSetChanged();
    }


    /**
     * 初始化选择器
     */
    private void initPickerView() {
        //楼层选择器
        mBuildPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mainBuild = mainBuildList.get(options1);
                build = buildList.get(options1).get(options2);
                allclass_build_choose.setText(mainBuild + " " + build);
                mLoadingView.show();
                queryAllClass();
            }
        }).setOutSideCancelable(false)
                .build();
        mBuildPickerView.setPicker(mainBuildList, buildList);

        //时间选择器
        mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                nowDate = DateUtil.getTime(date);
                allclass_date_choose.setText(nowDate);
                mLoadingView.show();
                queryAllClass();
            }
        }).setOutSideCancelable(false)
                .build();


    }

    private void initView() {
        //更改标题
        getSupportActionBar().setTitle("空教室");
        //初始化LoadingView
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("查询空教室中...");

        allclass_build_choose = findViewById(R.id.allclass_build_choose);
        allclass_date_choose = findViewById(R.id.allclass_date_choose);
        allclass_build_choose.setOnClickListener(this);
        allclass_date_choose.setOnClickListener(this);
        allclass_list = findViewById(R.id.allclass_list);
        //初始化recyclerView
        allclass_list.setLayoutManager(new LinearLayoutManager(this));
        //初始化adapter
        classroomAdapter = new CommonAdapter<>(mClassroomList, new CommonAdapter.OnBindDataListener<ClassroomModel>() {
            @Override
            public void onBindViewHolder(ClassroomModel model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setText(R.id.item_class_room_name, model.getName());
                List<Integer> tempList = model.getStatusList();
                //设置教室状态
                viewHolder.setImageResource(R.id.item_scoreclass_room_one_two, tempList.get(0) == 1 ? R.drawable.classroom_not_null : R.drawable.classroom_null);
                viewHolder.setImageResource(R.id.item_class_room_three_four, tempList.get(1) == 1 ? R.drawable.classroom_not_null : R.drawable.classroom_null);
                viewHolder.setImageResource(R.id.item_class_room_five_six, tempList.get(2) == 1 ? R.drawable.classroom_not_null : R.drawable.classroom_null);
                viewHolder.setImageResource(R.id.item_class_room_seven_eight, tempList.get(3) == 1 ? R.drawable.classroom_not_null : R.drawable.classroom_null);
                viewHolder.setImageResource(R.id.item_class_room_nine_ten, tempList.get(4) == 1 ? R.drawable.classroom_not_null : R.drawable.classroom_null);
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_all_class_room;
            }
        });
        allclass_list.setAdapter(classroomAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allclass_build_choose:
                mBuildPickerView.show();
                break;
            case R.id.allclass_date_choose:
                mTimePickerView.show();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化楼层数据，本地添加
     */
    private void initBuildData() {
        //第一级
        mainBuildList.add("主校区");
        mainBuildList.add("桃源校区");
        //第二级
        List<String> listOne = new ArrayList<>();
        listOne.add("瑞樟1");
        listOne.add("瑞樟2");
        listOne.add("瑞樟3");
        listOne.add("瑞樟4");
        listOne.add("瑞樟5");
        listOne.add("瑞樟6");
        listOne.add("瑞樟7");
        listOne.add("瑞樟8");
        listOne.add("瑞樟9");
        listOne.add("瑞樟10");
        listOne.add("瑞樟11");
        listOne.add("宋明中心");
        listOne.add("同文1");
        listOne.add("同文2");
        listOne.add("同文3");
        listOne.add("同文4");
        listOne.add("同文5");
        listOne.add("同文6");
        listOne.add("体育馆");
        listOne.add("屏山10A");
        listOne.add("屏山10B");
        listOne.add("屏山10C");
        listOne.add("屏山10D");
        listOne.add("屏山10E");
        listOne.add("屏山2(太阳楼)");
        listOne.add("屏山3(实训中心)");
        listOne.add("屏山7");
        listOne.add("屏山8");
        listOne.add("屏山9");
        listOne.add("屏山12");
        buildList.add(listOne);
        //第二级
        List<String> listTwo = new ArrayList<>();
        listTwo.add("桃源旧楼");
        listTwo.add("桃源楼");
        listTwo.add("天祥楼");
        buildList.add(listTwo);
    }


}
