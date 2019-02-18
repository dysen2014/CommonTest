package com.dysen.test.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.utils.CallAndSMS;
import com.dysen.common_library.utils.ToolStatus;
import com.dysen.common_library.utils.Tools;
import com.dysen.common_library.views.CustomPopWindow;
import com.dysen.common_library.views.WaveSideBarView;
import com.dysen.recyclerview.PullLoadMoreRecyclerView;
import com.dysen.test.R;
import com.zhy.changeskin.SkinManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetContactsActivity extends BaseActivity {

    @BindView(R.id.rcl_contacts)
    PullLoadMoreRecyclerView rclContacts;
    @BindView(R.id.sv_search)
    SearchView svSearch;
    @BindView(R.id.side_bar)
    WaveSideBarView sideBar;

    private Activity aty;
    private Context mContext;
    private List<ContactsBean> mContactsBean;
    private List<ContactsBean> mPhoneShow = new ArrayList<>();
    private SuperRecyclerAdapter<ContactsBean> adapter;
    private ContactsUtil contactsUtil;
    private CustomPopWindow customPopWindow;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().register(this);
        baseSetContentView(R.layout.activity_get_contacts);
        ButterKnife.bind(this);
//        setStatusColor(R.color.color_green);
        setStatusBG(R.mipmap.default_bg);

        sideBar.setTextSize(13);
        check();
    }

    String suffix = "";

    private void showMenu(View v) {
        View contentView = Tools.getView(this, R.layout.layout_common_recyclerview); //创建并显示popWindow

        customPopWindow = CustomPopWindow.newInstance(this, contentView, v);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(Tools.setManager1(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new SuperRecyclerAdapter<String>(this,
                Arrays.asList(this.getResources().getStringArray(R.array.menu))) {
            @Override
            public void convert(SuperRecyclerHolder holder, String name, int layoutType, final int position) {
                if (position == 0)
                    Tools.setGone(holder.getViewById(R.id.view_line));
                holder.setText(R.id.tv_name, name);
                holder.setOnItemClickListenner(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customPopWindow.getPopupWindow().isShowing())
                            customPopWindow.dissmiss();
                        switch (position) {
                            case 0:
                                suffix = "green";
                                break;
                            case 1:
                                suffix = "red";
                                break;
                            case 2:
                                suffix = "night";
                                break;
                            case 3:
                                break;
                        }
                        SkinManager.getInstance().removeAnySkin();
                        if (position != 3)
                            SkinManager.getInstance().changeSkin(suffix);
                    }
                });
            }

            @Override
            public int getLayoutAsViewType(String name, int position) {
                return R.layout.layout_common_btn_v_item;
            }
        });


        customPopWindow.showAsDropDown(v, 50, 50, Gravity.BOTTOM);
        customPopWindow.getPopupWindow().update();
    }

    private void initViews() {
        aty = this;
        mContext = this;
        tvTitle.setText("通讯录测试");
        tvMenu.setText("Menu");
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        contactsUtil = new ContactsUtil(this);
        mContactsBean = contactsUtil.getContactsData();
        adapter = new SuperRecyclerAdapter<ContactsBean>(this) {
            @Override
            public void convert(SuperRecyclerHolder holder, final ContactsBean bean, int layoutType, int position) {

                //是否显示分组标题
                if (position == 0 || !mContactsBean.get(position - 1).getHeaderWord().equals(bean.getHeaderWord())) {
                    Tools.setVisible(holder.getViewById(R.id.tv_group));
                    holder.setText(R.id.tv_group, bean.getHeaderWord().toUpperCase());
                } else {
                    Tools.setGone(holder.getViewById(R.id.tv_group));
                }
                holder.setText(R.id.tv_name, bean.getName());
                holder.setText(R.id.tv_num, bean.getTelPhone());
                holder.setText(R.id.tv_pic, bean.getHeaderWord().toUpperCase());
                //是否加载默认图片
                if (bean.getPhotoId() > 0) {//加载图像并以圆角显示
                    Glide.with(mContext).load(contactsUtil.getContactsImg(aty, bean.getContactId())).apply(RequestOptions.circleCropTransform()).into((ImageView) holder.getViewById(R.id.iv_pic));
                } else
                    holder.setImageResource(R.id.iv_pic, R.mipmap.icon_user_img);

                holder.getViewById(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(mContext,bean.getName()+"\n"+bean.getTelPhone(), Toast.LENGTH_LONG).show();
                        //todo
                        doCall(bean.getTelPhone());
                    }
                });
            }

            @Override
            public int getLayoutAsViewType(ContactsBean bean, int position) {
                return R.layout.layout_contacts_item;
            }
        };
//        rclContacts.setLayoutManager(Tools.setManager1(this, LinearLayoutManager.VERTICAL));
        rclContacts.setLinearLayout();
        rclContacts.setAdapter(adapter);
        initData();
    }

    private void doCall(String telPhone) {
//        Tools.showConfirmDialog(this, telPhone);
        Tools.toast(telPhone);

        CallAndSMS.call(this, telPhone);
    }

    private void initData() {
        adapter.setDatas(mContactsBean);
        rclContacts.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearData();
                        initViews();
                        rclContacts.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mContactsBean.size()>= 10) {
                            rclContacts.setFooterViewText("没有更多数据！！！");
                            Tools.setGone(rclContacts.loadMoreProgressBar);
                        }else {
                            Tools.setVisible(rclContacts.loadMoreProgressBar);
                            rclContacts.setFooterViewText("正在加载更多数据！！！");
                        }
                        rclContacts.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        });
        sideBar.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i = 0; i < mContactsBean.size(); i++) {
                    if (mContactsBean.get(i).getHeaderWord().equals(letter)) {
                        ((LinearLayoutManager) rclContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        // 搜索按钮相关
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    newText = newText.toUpperCase();
                    mPhoneShow.clear();
                    for (ContactsBean model : mContactsBean) {
                        String str = PinYinUtils.getPinyin(model.getName());
                        if (str.startsWith(newText) || model.getName().startsWith(newText)) {
                            mPhoneShow.add(model);
                        }
                    }
                    contactsUtil.sortList(mPhoneShow);
                    adapter.setDatas(mPhoneShow);
                } else {
                    adapter.setDatas(mContactsBean);
                }
                return false;
            }
        });
    }

    /**
     * 检查权限
     */
    private void check() {
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(GetContactsActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 201);
        } else {
            initViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 201) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS))
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initViews();
                } else {
                    return;
                }
        } else {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }

    @Override
    public void setListener() {

    }
}
