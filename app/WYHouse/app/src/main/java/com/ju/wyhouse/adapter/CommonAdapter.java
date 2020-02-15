package com.ju.wyhouse.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.module.LoadMoreModule;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/11 21:19
 * Desc: 万能适配器
 */
public class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>{

    private List<T> mList;
    private OnBindDataListener<T> onBindDataListener;
    private OnMoreBindDataListener<T> onMoreBindDataListener;

    public CommonAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindDataListener;
    }

    public CommonAdapter(List<T> mList, OnMoreBindDataListener<T> onMoreBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onMoreBindDataListener;
        this.onMoreBindDataListener = onMoreBindDataListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (onMoreBindDataListener != null) {
            return onMoreBindDataListener.getItemType(position);
        }
        return 0;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = onBindDataListener.getLayout(viewType);
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(parent, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        onBindDataListener.onBindViewHolder(mList.get(position), holder, getItemViewType(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnBindDataListener<T> {

        //绑定数据
        void onBindViewHolder(T model, CommonViewHolder viewHolder, int type, int position);

        int getLayout(int type);
    }


    //绑定多类型的数据
    public interface OnMoreBindDataListener<T> extends OnBindDataListener<T> {

        int getItemType(int position);
    }
}