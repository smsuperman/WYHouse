package com.ju.wyhouse.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ju.wyhouse.R;
import com.ju.wyhouse.activity.AllClassActivity;
import com.ju.wyhouse.activity.AllCourseActivity;
import com.ju.wyhouse.activity.CourseActivity;
import com.ju.wyhouse.activity.ScoreActivity;
import com.ju.wyhouse.activity.WallActivity;
import com.ju.wyhouse.adapter.BannerImageAdapter;
import com.ju.wyhouse.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    private LinearLayout lin_score_layout;
    private LinearLayout lin_course_layout;
    private LinearLayout lin_pingjiao_layout;
    private LinearLayout lin_allcourse_layout;
    private LinearLayout lin_allclass_layout;
    private LinearLayout lin_wall_layout;
    private Banner frag_main_banner;
    private List<Integer> mBannerList = new ArrayList<>();
    private BannerImageAdapter mBannerImageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        frag_main_banner = view.findViewById(R.id.frag_main_banner);
        lin_score_layout = view.findViewById(R.id.lin_score_layout);
        lin_score_layout.setOnClickListener(this);
        lin_course_layout = view.findViewById(R.id.lin_course_layout);
        lin_course_layout.setOnClickListener(this);
        lin_pingjiao_layout = view.findViewById(R.id.lin_pingjiao_layout);
        lin_pingjiao_layout.setOnClickListener(this);
        lin_allcourse_layout = view.findViewById(R.id.lin_allcourse_layout);
        lin_allcourse_layout.setOnClickListener(this);
        lin_allclass_layout = view.findViewById(R.id.lin_allclass_layout);
        lin_allclass_layout.setOnClickListener(this);
        lin_wall_layout = view.findViewById(R.id.lin_wall_layout);
        lin_wall_layout.setOnClickListener(this);

        initBanner();
    }

    /**
     * 初始化Banner
     */
    private void initBanner() {
        mBannerList.add(R.drawable.viewpage_fly);
        mBannerImageAdapter = new BannerImageAdapter(mBannerList, getActivity());
        frag_main_banner.setAdapter(mBannerImageAdapter)
                .setIndicator(new CircleIndicator(getActivity()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_score_layout:
                //成绩
                startActivity(new Intent(getActivity(), ScoreActivity.class));
                break;
            case R.id.lin_course_layout:
                //课表
                startActivity(new Intent(getActivity(), CourseActivity.class));
                break;
            case R.id.lin_pingjiao_layout:
                //评教
                ToastUtil.showToast(getActivity(), "评教功能尚未开放");
                break;
            case R.id.lin_allcourse_layout:
                //全校课表
                startActivity(new Intent(getActivity(), AllCourseActivity.class));
                break;
            case R.id.lin_allclass_layout:
                //空教室
                startActivity(new Intent(getActivity(), AllClassActivity.class));
                break;
            case R.id.lin_wall_layout:
                startActivity(new Intent(getActivity(), WallActivity.class));
                break;
            default:
                break;
        }
    }
}
