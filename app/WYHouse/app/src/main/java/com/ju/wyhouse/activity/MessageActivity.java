package com.ju.wyhouse.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ju.wyhouse.R;
import com.ju.wyhouse.adapter.CommonAdapter;
import com.ju.wyhouse.adapter.CommonViewHolder;
import com.ju.wyhouse.base.BaseBackActivity;
import com.ju.wyhouse.db.DbManager;
import com.ju.wyhouse.db.DbMessageData;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseBackActivity {

    private RecyclerView message_rv;
    private List<DbMessageData> mList = new ArrayList<>();
    private CommonAdapter<DbMessageData> messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("通知");
        message_rv = findViewById(R.id.message_rv);
        //初始化Recycler
        message_rv.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<DbMessageData>() {
            @Override
            public void onBindViewHolder(DbMessageData model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setText(R.id.item_message_title, model.getTitle());
                viewHolder.setText(R.id.item_message_content, model.getContent());
            }

            @Override
            public int getLayout(int type) {
                return R.layout.item_message;
            }
        });
        message_rv.setAdapter(messageAdapter);
        //开始查询数据库
        queryLitePal();
    }


    /**
     * 查询历史消息
     */
    private void queryLitePal() {
        List<DbMessageData> allData = DbManager.getInstance().queryAllMessage();
        mList.addAll(allData);
        messageAdapter.notifyDataSetChanged();
    }
}
