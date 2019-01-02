package com.dysen.test.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dysen.common_library.adapter.recycler.SuperRecyclerAdapter;
import com.dysen.common_library.adapter.recycler.SuperRecyclerHolder;
import com.dysen.common_library.utils.Tools;
import com.dysen.common_library.views.WaveSideBarView;
import com.dysen.test.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetContactsActivity extends AppCompatActivity {

    @BindView(R.id.rcl_contacts)
    RecyclerView rclContacts;
    @BindView(R.id.sv_search)
    SearchView svSearch;
    @BindView(R.id.side_bar)
    WaveSideBarView sideBar;

    private Activity aty;
    private Context mContext;
    private List<PhoneBean> mPhoneBean;
    private List<PhoneBean> mPhoneShow = new ArrayList<>();
    private SuperRecyclerAdapter<PhoneBean> adapter;
    private PhoneUtil phoneUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contacts);
        ButterKnife.bind(this);
        check();
    }

    private void initViews() {
        aty = this;
        mContext = this;
        phoneUtil = new PhoneUtil(this);
        mPhoneBean = phoneUtil.getPhone();
        adapter = new SuperRecyclerAdapter<PhoneBean>(this) {
            @Override
            public void convert(SuperRecyclerHolder holder, final PhoneBean bean, int layoutType, int position) {

                //是否显示分组标题
                if (position == 0 || !mPhoneBean.get(position - 1).getHeaderWord().equals(bean.getHeaderWord())) {
                    Tools.setVisible(holder.getViewById(R.id.tv_group));
                    holder.setText(R.id.tv_group, bean.getHeaderWord());
                } else {
                    Tools.setGone(holder.getViewById(R.id.tv_group));
                }
                holder.setText(R.id.tv_name, bean.getName());
                holder.setText(R.id.tv_num, bean.getTelPhone());
                //是否加载默认图片
                if (bean.getPhotoId() > 0) {//加载图像并以圆角显示
                    Glide.with(mContext).load(phoneUtil.getContactsImg(aty, bean.getContactId())).apply(RequestOptions.circleCropTransform()).into((ImageView) holder.getViewById(R.id.iv_pic));
                } else
                    holder.setImageResource(R.id.iv_pic, R.mipmap.icon_user_img);

                holder.getViewById(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,bean.getName()+"\n"+bean.getTelPhone(), Toast.LENGTH_LONG);
                        //todo
                    }
                });
            }

            @Override
            public int getLayoutAsViewType(PhoneBean bean, int position) {
                return R.layout.layout_contacts_item;
            }
        };
        rclContacts.setLayoutManager(Tools.setManager1(this, LinearLayoutManager.VERTICAL));
        rclContacts.setAdapter(adapter);
        initData();
    }

    private void initData() {
        adapter.setDatas(mPhoneBean);
        sideBar.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i = 0; i < mPhoneBean.size(); i++) {
                    if (mPhoneBean.get(i).getHeaderWord().equals(letter)) {
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
                    for (PhoneBean model : mPhoneBean) {
                        String str = PinYinUtils.getPinyin(model.getName());
                        if (str.startsWith(newText) || model.getName().startsWith(newText)) {
                            mPhoneShow.add(model);
                        }
                    }
                    phoneUtil.sortList(mPhoneShow);
                    adapter.setDatas(mPhoneShow);
                } else {
                    adapter.setDatas(mPhoneBean);
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
            initViews();
        } else {
            return;
        }
    }

}
