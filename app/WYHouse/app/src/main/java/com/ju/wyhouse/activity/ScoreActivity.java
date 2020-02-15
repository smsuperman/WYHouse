package com.ju.wyhouse.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.CommonAdapter;
import com.ju.wyhouse.adapter.CommonViewHolder;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.manager.DialogManager;
import com.ju.wyhouse.model.AllScoreModel;
import com.ju.wyhouse.model.ScoreDataModel;
import com.ju.wyhouse.model.ScoreMessageModel;
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

public class ScoreActivity extends BaseBackActivity implements View.OnClickListener {


    //总分和绩点FLAG
    private static final int ITEM_TYPE_ALL_SCORE = 0;
    private static final int ITEM_TYPE_ALL_JIDIAN = 1;
    private TextView score_week_choose;
    private RecyclerView score_recycler;
    private RecyclerView score_all_recycler;
    private CommonAdapter<ScoreDataModel> scoreAdapter;
    private CommonAdapter<AllScoreModel> allScoreAdapter;
    private List<ScoreDataModel> mScoreList = new ArrayList<>();
    private List<AllScoreModel> mAllScoreList = new ArrayList<>();
    private List<String> mWeekList = new ArrayList<>();
    private LoadingView mLoadingView;
    private OptionsPickerView mScorePickerView;
    private String week;
    private String cookie;
    //以下为dialog控件
    private DialogView mCourseScoreDialog;
    private TextView dialog_score_name;
    private TextView dialog_score_pingshi;
    private TextView dialog_score_qizhong;
    private TextView dialog_score_qimo;
    private TextView dialog_score_zongchengji;
    private TextView dialog_score_kechengxingzhi;
    private TextView dialog_score_banjipaiming;
    private TextView dialog_score_zhuanyepaiming;
    private TextView dialog_score_quanxiaopaiming;
    //以下为总分和绩点dialog
    private DialogView mAllCourseScoreDialog;
    private TextView dialog_all_score_zongfen;
    private TextView dialog_all_score_banjipaiming;
    private TextView dialog_all_score_zhuanyepaiming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initData();
        initView();
        initPickerView();
        //首次查询成绩
        queryScore();
        initDialog();
    }


    /**
     * 初始化Dialog
     */
    private void initDialog() {
        mCourseScoreDialog = DialogManager.getInstance().initView(this, R.layout.dialog_score_message);
        dialog_score_name = mCourseScoreDialog.findViewById(R.id.dialog_score_name);
        dialog_score_pingshi = mCourseScoreDialog.findViewById(R.id.dialog_score_pingshi);
        dialog_score_qizhong = mCourseScoreDialog.findViewById(R.id.dialog_score_qizhong);
        dialog_score_qimo = mCourseScoreDialog.findViewById(R.id.dialog_score_qimo);
        dialog_score_zongchengji = mCourseScoreDialog.findViewById(R.id.dialog_score_zongchengji);
        dialog_score_kechengxingzhi = mCourseScoreDialog.findViewById(R.id.dialog_score_kechengxingzhi);
        dialog_score_banjipaiming = mCourseScoreDialog.findViewById(R.id.dialog_score_banjipaiming);
        dialog_score_zhuanyepaiming = mCourseScoreDialog.findViewById(R.id.dialog_score_zhuanyepaiming);
        dialog_score_quanxiaopaiming = mCourseScoreDialog.findViewById(R.id.dialog_score_quanxiaopaiming);

        mAllCourseScoreDialog = DialogManager.getInstance().initView(this, R.layout.dialog_all_score_message);
        dialog_all_score_zongfen = mAllCourseScoreDialog.findViewById(R.id.dialog_all_score_zongfen);
        dialog_all_score_banjipaiming = mAllCourseScoreDialog.findViewById(R.id.dialog_all_score_banjipaiming);
        dialog_all_score_zhuanyepaiming = mAllCourseScoreDialog.findViewById(R.id.dialog_all_score_zhuanyepaiming);
    }


    /**
     * 初始化本地数据，期数
     */
    private void initData() {
        mWeekList.add("2015-2016-1");
        mWeekList.add("2015-2016-2");
        mWeekList.add("2016-2017-1");
        mWeekList.add("2016-2017-2");
        mWeekList.add("2017-2018-1");
        mWeekList.add("2017-2018-2");
        mWeekList.add("2018-2019-1");
        mWeekList.add("2018-2019-2");
        mWeekList.add("2019-2020-1");
//        mWeekList.add("2019-2020-2");
    }

    /**
     * 初始化选择器
     */
    private void initPickerView() {
        mScorePickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                week = mWeekList.get(options1);
                score_week_choose.setText(week);
                queryScore();
            }
        }).setOutSideCancelable(false)
                .build();
        mScorePickerView.setPicker(mWeekList);
        //初次默认选中最后一个
        mScorePickerView.setSelectOptions(mWeekList.size() - 1);
        week = mWeekList.get(mWeekList.size() - 1);
        score_week_choose.setText(week);
    }

    private void initView() {
        //更改标题
        getSupportActionBar().setTitle("成绩");
        score_week_choose = findViewById(R.id.score_week_choose);
        score_week_choose.setOnClickListener(this);
        score_recycler = findViewById(R.id.score_recycler);
        //初始化LoadingView
        mLoadingView = new LoadingView(this);
        mLoadingView.setText("查询成绩中...");
        mLoadingView.show();
        //初始化成绩Recycler
        score_recycler.setLayoutManager(new LinearLayoutManager(this));
        score_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        scoreAdapter = new CommonAdapter<>(mScoreList, new CommonAdapter.OnBindDataListener<ScoreDataModel>() {
            @Override
            public void onBindViewHolder(final ScoreDataModel model, CommonViewHolder viewHolder, int type, final int position) {
                viewHolder.setText(R.id.item_score_course, model.getKcmc());
                viewHolder.setText(R.id.item_score_xuefen, model.getXf());
                viewHolder.setText(R.id.item_score_type, model.getKcsx());
                viewHolder.setText(R.id.item_score_chengji, model.getZcj());
                viewHolder.setText(R.id.item_score_paiming, model.getBjpm());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCourseScore(position);
                    }
                });
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_score;
            }
        });
        score_recycler.setAdapter(scoreAdapter);

        //初始化总分Recycler
        score_all_recycler = findViewById(R.id.score_all_recycler);
        score_all_recycler.setLayoutManager(new LinearLayoutManager(this));
        score_all_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        allScoreAdapter = new CommonAdapter<>(mAllScoreList, new CommonAdapter.OnBindDataListener<AllScoreModel>() {
            @Override
            public void onBindViewHolder(AllScoreModel model, CommonViewHolder viewHolder, int type, int position) {
                if (model.getType() == ITEM_TYPE_ALL_SCORE) {
                    viewHolder.setText(R.id.item_all_score_name, "总分：" + model.getZf() + " " + "排名：" + model.getZfpm().get(0));
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAllScoreMessage(ITEM_TYPE_ALL_SCORE);
                        }
                    });
                } else if (model.getType() == ITEM_TYPE_ALL_JIDIAN) {
                    viewHolder.setText(R.id.item_all_score_name, "平均学分绩点：" + model.getZf() + " " + "排名：" + model.getPjxfjdpm().get(0));
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAllScoreMessage(ITEM_TYPE_ALL_JIDIAN);
                        }
                    });
                }
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_all_score;
            }
        });
        score_all_recycler.setAdapter(allScoreAdapter);

    }


    /**
     * 总分和绩点信息展示
     *
     * @param itemTypePosition
     */
    @SuppressLint("SetTextI18n")
    private void showAllScoreMessage(int itemTypePosition) {
        if (itemTypePosition == ITEM_TYPE_ALL_SCORE) {
            dialog_all_score_zongfen.setText("总分：" + mAllScoreList.get(0).getZf() + "\n" + "(" + mAllScoreList.get(0).getGkcj() + ")");
            dialog_all_score_banjipaiming.setText("班级排名：" + mAllScoreList.get(0).getZfpm().get(0) + "/" + mAllScoreList.get(0).getZfpm().get(1));
            dialog_all_score_zhuanyepaiming.setText("同级专业排名：" + mAllScoreList.get(0).getZfpm().get(1) + "/" +
                    mAllScoreList.get(0).getZfpm().get(2) + "\n" + "(" + mAllScoreList.get(0).getPmbj() + ")");
            mAllCourseScoreDialog.show();
        } else if (itemTypePosition == ITEM_TYPE_ALL_JIDIAN) {
            dialog_all_score_zongfen.setText("平均学分绩点：" + mAllScoreList.get(1).getZf() + "\n" + "(" + mAllScoreList.get(1).getGkxfjd() + ")");
            dialog_all_score_banjipaiming.setText("班级排名：" + mAllScoreList.get(1).getPjxfjdpm().get(0) + "/" + mAllScoreList.get(1).getPjxfjdpm().get(1));
            dialog_all_score_zhuanyepaiming.setText("同级专业排名：" + mAllScoreList.get(1).getPjxfjdpm().get(2) + "/" +
                    mAllScoreList.get(1).getPjxfjdpm().get(3) + "\n" + "(" + mAllScoreList.get(1).getPmbj() + ")");
            mAllCourseScoreDialog.show();
        }
    }


    /**
     * 获取课程详情
     *
     * @param position
     */
    private void showCourseScore(final int position) {

        Request request = NetUrl.getCourseScoreMessageRequest(mScoreList.get(position).getDetailsUrl());
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取课程分数详情json数据成功：" + response);
                //要选中的model数据一起传递
                JsonToCourseMessage(response, position);
                mLoadingView.hide();
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.e("查询课程分数网络请求失败：" + msg);
                ToastUtil.showToast(ScoreActivity.this, "网络超时，请稍后再试");
                mLoadingView.hide();
            }
        });

    }


    /**
     * 解析课程详情
     *
     * @param response
     */
    @SuppressLint("SetTextI18n")
    private void JsonToCourseMessage(String response, int position) {
        JSONObject jsonObject = JSON.parseObject(response);
        SaveAndWriteUtil.saveCookie(jsonObject.getString("cookie"));
        int code = jsonObject.getInteger("code");
        if (code != 0) {
            ToastUtil.showToast(this, "获取成绩详情数据空，请稍后再试或者与管理员联系");
            return;
        }
        JSONObject data = jsonObject.getJSONObject("data");
        ScoreMessageModel scoreMessageModel = JSON.parseObject(data.toJSONString(), new TypeReference<ScoreMessageModel>() {
        });

        //显示Dialog
        dialog_score_name.setText(mScoreList.get(position).getKcmc());
        dialog_score_pingshi.setText("平时：" + scoreMessageModel.getPingshi() + "(" + scoreMessageModel.getPingshibili() + ")");
        dialog_score_qizhong.setText("期中：" + scoreMessageModel.getQizhong() + "(" + scoreMessageModel.getQizhongbili() + ")");
        dialog_score_qimo.setText("期末：" + scoreMessageModel.getQimo() + "(" + scoreMessageModel.getQimobili() + ")");
        dialog_score_zongchengji.setText("总成绩：" + scoreMessageModel.getZong());
        dialog_score_kechengxingzhi.setText("课程性质：" + mScoreList.get(position).getKcxz());
        dialog_score_banjipaiming.setText("班级排名：" + mScoreList.get(position).getBjpm() + "/" + mScoreList.get(position).getBjrs());
        dialog_score_zhuanyepaiming.setText("同级专业排名" + mScoreList.get(position).getNjpm() + "/" + mScoreList.get(position).getNjrs());
        dialog_score_quanxiaopaiming.setText("全校排名" + mScoreList.get(position).getQxpm() + "/" + mScoreList.get(position).getQxrs());
        mCourseScoreDialog.show();
    }


    /**
     * 成绩查找，先查找缓存的，没有再查找教务处的（先直接查找教务处的）
     */
    private void queryScore() {
        //先清除之前数据
        mScoreList.clear();
        Request request = NetUrl.getScoreRequest(week);
        LogUtil.i("查询成绩url:" + request.header("Wsw-Flag"));
        mLoadingView.show();
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取成绩json数据成功：" + response);
                JsonToParse(response);
                mLoadingView.hide();
            }


            @Override
            public void onFailure(String msg) {
                LogUtil.e("查询成绩网络请求失败：" + msg);
                ToastUtil.showToast(ScoreActivity.this, "查询成绩网络请求失败");
                mLoadingView.hide();
            }
        });
    }


    /**
     * 解析json
     *
     * @param response
     */
    private void JsonToParse(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        SaveAndWriteUtil.saveCookie(jsonObject.getString("cookie"));
        int code = jsonObject.getInteger("code");
        if (code != 0) {
            ToastUtil.showToast(this, "获取数据为空，请稍后再试或者与管理员联系");
            return;
        }
        List<ScoreDataModel> mTempScoreList = JSONObject.parseArray(jsonObject.getJSONArray("data").toJSONString(), ScoreDataModel.class);
        mScoreList.addAll(mTempScoreList);
        LogUtil.i("mScoreListSize：" + mScoreList.size());
        scoreAdapter.notifyDataSetChanged();

        //成绩显示完成后继续请求总分和绩点
        questNetAll();
    }


    /**
     * 请求总分和绩点
     */
    private void questNetAll() {
        //先清除之前数据
        mAllScoreList.clear();
        Request request = NetUrl.getAllScoreRequest(week);
        LogUtil.i("获取总分url：" + request.url());
        HttpClient.getInstance().requestToNet(request, new INetCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.i("获取总分绩点json数据成功：" + response);
                JsonToAllScore(response);
            }


            @Override
            public void onFailure(String msg) {
                LogUtil.e("查询总分绩点网络请求失败：" + msg);
                ToastUtil.showToast(ScoreActivity.this, "查询成绩网络请求失败");
            }
        });
    }


    /**
     * 解析总分绩点
     *
     * @param response
     */
    private void JsonToAllScore(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        SaveAndWriteUtil.saveCookie(jsonObject.getString("cookie"));
        int code = jsonObject.getInteger("code");
        if (code != 0) {
            ToastUtil.showToast(this, "获取总分和绩点数据为空，请稍后再试或者与管理员联系");
            return;
        }
        JSONArray data = jsonObject.getJSONArray("data");
        //设置总分Model
        AllScoreModel allScoreModelOne = new AllScoreModel();
        JSONObject objectOne = data.getJSONObject(0);
        List<String> tempOne = JSONObject.parseArray(objectOne.getJSONArray("zfpm").toJSONString(), String.class);
        allScoreModelOne.setZfpm(tempOne);
        allScoreModelOne.setPmbj(objectOne.getString("pmbj"));
        allScoreModelOne.setGkcj(objectOne.getString("gkcj"));
        allScoreModelOne.setZf(objectOne.getString("zf"));
        allScoreModelOne.setType(ITEM_TYPE_ALL_SCORE);
        mAllScoreList.add(allScoreModelOne);
        //设置绩点Model
        AllScoreModel allScoreModelTwo = new AllScoreModel();
        JSONObject objectTwo = data.getJSONObject(1);
        List<String> tempTwo = JSONObject.parseArray(objectTwo.getJSONArray("pjxfjdpm").toJSONString(), String.class);
        allScoreModelTwo.setPjxfjdpm(tempTwo);
        allScoreModelTwo.setPmbj(objectTwo.getString("pmbj"));
        allScoreModelTwo.setGkxfjd(objectTwo.getString("gkxfjd"));
        allScoreModelTwo.setZf(objectTwo.getString("zf"));
        allScoreModelTwo.setType(ITEM_TYPE_ALL_JIDIAN);
        mAllScoreList.add(allScoreModelTwo);
        //刷新数据
        allScoreAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_week_choose:
                mScorePickerView.show();
                break;
            default:
                break;
        }
    }
}
