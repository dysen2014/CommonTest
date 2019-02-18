package com.dysen.xdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.utils.StatusBarUtil;
import com.dysen.common_library.utils.Tools;
import com.dysen.recyclerview.PullLoadMoreRecyclerView;
import com.dysen.xdemo.dialog.XDialogActivity;
import com.dysen.xdemo.title_bar.XTitleBarActivity;
import com.dysen.xdemo.toast.XToastActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends XBaseActivity {

    @BindView(R.id.rcl_menu)
    PullLoadMoreRecyclerView rclMenu;
    private XAdapter mAdapter;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseSetContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setStatusColor(R.color.text_color_green);
        initViews();
        initDatas();
    }

    public void initDatas() {
        mAdapter.setDatas(getDatas());
    }

    public void initViews() {
        baseSetText(tvTitle, "Xdemo");
        rclMenu.setLinearLayout();
        mAdapter = new XAdapter(this);
        rclMenu.setAdapter(mAdapter);
    }

    public List<String> getDatas() {
        datas = Arrays.asList(this.getResources().getStringArray(R.array.menu_list));

        return datas;
    }

    @Override
    public void setListener() {

    }

    public class XAdapter extends SuperRecyclerAdapter<String> {

        public XAdapter(Context mCtx) {
            super(mCtx);
        }

        @Override
        public void convert(SuperRecyclerHolder holder, String s, int layoutType, final int position) {
            holder.setText(R.id.tv_name, s);
            if (position == 0)
                Tools.setGone(holder.getViewById(R.id.view_line));
            holder.setOnItemClickListenner(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            transAty(XDialogActivity.class);
                            break;
                        case 1:
                            transAty(XToastActivity.class);
                            break;
                        case 2:
                            transAty(XTitleBarActivity.class);
                            break;
                        case 3:
                            break;
                    }
                }
            });
        }

        @Override
        public int getLayoutAsViewType(String s, int position) {
            return R.layout.layout_common_btn_v_item;
        }
    }
}
