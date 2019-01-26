package com.dysen.xdemo.toast;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.utils.Tools;
import com.dysen.recyclerview.PullLoadMoreRecyclerView;
import com.dysen.title_bar.TitleBar;
import com.dysen.toast.ToastUtils;
import com.dysen.toast.style.ToastAlipayStyle;
import com.dysen.toast.style.ToastBlackStyle;
import com.dysen.toast.style.ToastQQStyle;
import com.dysen.toast.style.ToastWhiteStyle;
import com.dysen.xdemo.MainActivity;
import com.dysen.xdemo.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XToastActivity extends MainActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcl_menu)
    PullLoadMoreRecyclerView rclMenu;

    private XAdapter mAdapter;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        initDatas();
    }
    public List<String> getDatas() {
        datas = Arrays.asList(this.getResources().getStringArray(R.array.toast_menu));

        return datas;
    }

    @Override
    public void initDatas() {
        super.initDatas();
        mAdapter.setDatas(getDatas());
    }

    @Override
    public void initViews() {
        super.initViews();
        rclMenu.setLinearLayout();
        mAdapter = new XAdapter(this);
        rclMenu.setAdapter(mAdapter);
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
                            show1(v);
                            break;
                        case 1:
                            show2(v);
                            break;
                        case 2:
                            show3(v);
                            break;
                        case 3:
                            show4(v);
                            break;
                        case 4:
                            show5(v);
                            break;
                        case 5:
                            show6(v);
                            break;
                        case 6:
                            show7(v);
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

    public void show1(final View v) {
        ToastUtils.show("我是一个普通的吐司");
    }

    public void show2(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show("我是子线程中弹出的吐司");
            }
        }).start();
    }

    public void show3(View v) {
        ToastUtils.initStyle(new ToastWhiteStyle());
        ToastUtils.show("动态切换吐司样式成功");
    }

    public void show4(View v) {
        ToastUtils.initStyle(new ToastBlackStyle());
        ToastUtils.show("动态切换吐司样式成功");
    }

    public void show5(View v) {
        ToastUtils.initStyle(new ToastQQStyle());
        ToastUtils.show("QQ那种还不简单，分分钟的事");
    }

    public void show6(View v) {
        ToastUtils.initStyle(new ToastAlipayStyle());
        ToastUtils.show("支付宝那种还不简单，分分钟的事");
    }

    public void show7(View v) {
        // ToastUtils.setView(View.inflate(getApplication(), R.layout.toast_custom_view, null));
        ToastUtils.setView(R.layout.toast_custom_view);
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.show("我是自定义Toast");
    }

}
