package com.ju.wyhouse.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author:gaoju
 * Date:2020/2/8 21:19
 * Path:com.ju.wyhouse.view
 * Desc:自定义LinearLayoutManager
 */
public class RecyclerViewLinearLayoutManager extends LinearLayoutManager {

    public RecyclerViewLinearLayoutManager(Context context) {
        super(context);
    }

    public RecyclerViewLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RecyclerViewLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
