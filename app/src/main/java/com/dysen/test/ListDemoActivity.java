package com.dysen.test;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.LinearLayoutManager;

import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.utils.FormatUtil;
import com.dysen.common_library.views.WaveSideBarView;
import com.dysen.recyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDemoActivity extends BaseActivity {

    @BindView(R.id.rcl_list)
    PullLoadMoreRecyclerView rclList;
    @BindView(R.id.side_bar)
    WaveSideBarView sideBar;

    SuperRecyclerAdapter<String> mAdapter;
    WaveSideBarView.OnSelectIndexItemListener onSelectIndexItemListener;
    private List datas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_list_demo);
        ButterKnife.bind(this);
        initAdapter();
        initViews();
        initData();
    }

    private void initData() {
        sideBar.setIndexItems(getStringArray());
        sideBar.setOnSelectIndexItemListener(onSelectIndexItemListener);
        mAdapter.setDatas(datas);
    }

    private String[] getStringArray() {

        String[] items = new String[200];
        for (int i = 0; i < 200; i++) {
            items[i] = String.valueOf(i+1);
            datas.add(String.valueOf(i+1));
        }
        return items;
    }

    private void initAdapter() {
        mAdapter = new SuperRecyclerAdapter<String>(this) {
            @Override
            public void convert(SuperRecyclerHolder holder, String s, int layoutType, int position) {
                holder.setText(R.id.tv_name, s);
            }

            @Override
            public int getLayoutAsViewType(String s, int position) {
                return R.layout.layout_common_btn_v_item;
            }
        };
    }

    private void initViews() {
        sText(tvTitle, "列表测试");
        rclList.setLinearLayout();
        rclList.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        onSelectIndexItemListener = new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i = 0; i < datas.size(); i++) {
                    if (i == (FormatUtil.isNumeric(letter)?Integer.valueOf(letter):0)-1){
                        ((LinearLayoutManager) rclList.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        };
    }
}
