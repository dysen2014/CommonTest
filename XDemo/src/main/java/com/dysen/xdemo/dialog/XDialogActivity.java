package com.dysen.xdemo.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.utils.Tools;
import com.dysen.dialog.OnListener;
import com.dysen.dialog.OnToastLifecycle;
import com.dysen.dialog.XDialog;
import com.dysen.dialog.draggable.SpringDraggable;
import com.dysen.permissions.OnPermission;
import com.dysen.permissions.Permission;
import com.dysen.permissions.XXPermissions;
import com.dysen.recyclerview.PullLoadMoreRecyclerView;
import com.dysen.title_bar.TitleBar;
import com.dysen.xdemo.MainActivity;
import com.dysen.xdemo.R;
import com.dysen.xdemo.XBaseActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XDialogActivity extends XBaseActivity {

    @BindView(R.id.rcl_menu)
    PullLoadMoreRecyclerView rclMenu;

    private XAdapter mAdapter;
    private List<String> datas;
    private XDialog xDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        initDatas();
    }
    public List<String> getDatas() {
        datas = Arrays.asList(this.getResources().getStringArray(R.array.dialog_menu));

        return datas;
    }

    public void initDatas() {
        mAdapter.setDatas(getDatas());
    }

    public void initViews() {

        sText(tvTitle, "Xdialog");
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
                    }
                }
            });
        }

        @Override
        public int getLayoutAsViewType(String s, int position) {
            return R.layout.layout_common_btn_v_item;
        }
    }

    public void show1(View v) {
        new XDialog(this)
                .setDuration(3000)
                .setView(R.layout.dialog_hint)
                .setAnimStyle(android.R.style.Animation_Translucent)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                .setText(android.R.id.message, "这个动画是不是很骚")
                .show();
    }

    public void show2(View v) {
        new XDialog(this)
                .setDuration(1000)
                .setView(R.layout.dialog_hint)
                .setAnimStyle(android.R.style.Animation_Activity)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                .setText(android.R.id.message, "一秒后消失")
                .show();
    }

    public void show3(View v) {
        new XDialog(this)
                .setDuration(3000)
                .setView(R.layout.dialog_hint)
                .setAnimStyle(android.R.style.Animation_Dialog)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_warning)
                .setText(android.R.id.message, "是不是感觉很牛逼")
                .setOnToastLifecycle(new OnToastLifecycle() {

                    @Override
                    public void onShow(XDialog toast) {
                        Snackbar.make(getWindow().getDecorView(), "XDialog 显示了", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismiss(XDialog toast) {
                        Snackbar.make(getWindow().getDecorView(), "XDialog 消失了", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public void show4(View v) {
        new XDialog(this)
                .setView(R.layout.dialog_hint)
                .setAnimStyle(android.R.style.Animation_Translucent)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                .setText(android.R.id.message, "点我点我点我")
                .setOnClickListener(android.R.id.message, new OnListener.OnClickListener<TextView>() {

                    @Override
                    public void onClick(final XDialog toast, TextView view) {
                        view.setText("那么听话啊");
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 1000);
                    }
                })
                .show();
    }

    public void show5(View v) {
        new XDialog(this)
                .setView(R.layout.dialog_hint)
                .setAnimStyle(android.R.style.Animation_Translucent)
                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                .setText(android.R.id.message, "点我消失")
                .setDraggable() // 设置成可拖拽的
                .setOnClickListener(android.R.id.message, new OnListener.OnClickListener<TextView>() {

                    @Override
                    public void onClick(XDialog toast, TextView view) {
                        toast.cancel();
                    }
                })
                .show();
    }

    public void show6(View v) {
        XXPermissions.with(this)
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (xDialog != null)
                            xDialog.cancel();
                        xDialog = new XDialog(getApplication());
                        xDialog // 传入 Application 表示这个是一个全局的 Toast
                                .setView(R.layout.dialog_phone)
                                .setDraggable(new SpringDraggable()) // 设置指定的拖拽规则
                                .setOnClickListener(R.id.iv_toast_call_phone, new OnListener.OnClickListener<ImageView>() {

                                    @Override
                                    public void onClick(final XDialog toast, ImageView view) {
                                        // 点击后跳转到拨打电话界面
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setOnLongClickListener(R.id.iv_toast_call_phone, new OnListener.OnLongClickListener<ImageView>() {
                                    @Override
                                    public void onLongClick(XDialog toast, ImageView view) {
                                        toast.cancel();
                                    }
                                });
                        xDialog.show();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        new XDialog(XDialogActivity.this)
                                .setDuration(1000)
                                .setView(R.layout.dialog_hint)
                                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                                .setText(android.R.id.message, "请先授予悬浮窗权限")
                                .show();
                    }
                });
    }
}
