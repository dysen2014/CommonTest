package com.dysen.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dysen.common_library.base.BaseActivity;
import com.dysen.common_library.bean.CommonBean;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.base_recycler_adapter.SuperRecyclerAdapter;
import com.dysen.common_library.utils.base_recycler_adapter.SuperRecyclerHolder;

import net.lucode.hackware.magicindicator.views.MagicIndicatorView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoActivity extends BaseActivity {

    @BindView(R.id.magic_indicator)
    MagicIndicatorView magicIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedinstancestate) {
        super.onCreate(savedinstancestate);
        baseSetContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        magicInit();
    }

    @Override
    public void setListener() {

    }

    List<String> titleLists = Arrays.asList(new String[]{"页面1", "页面2", "页面3", "页面4", "页面5", "页面6", "页面7"});

    private void magicInit() {
//        magicIndicator.setWidth_size(titleLists.size());
        magicIndicator.setTitle_list(titleLists);
        magicIndicator.initView();
        magicIndicator.setIndicator_type(1);
        magicIndicator.setBackgroundColor(Color.parseColor("#e70101"));
//        magicIndicator.setText_selected_color(Color.parseColor("#e70101"));
//        magicIndicator.setSelected_color(Color.parseColor("#ff4801"));
        magicIndicator.onPageSelected(2);
        magicIndicator.setiOnListener(new MagicIndicatorView.IOnListener() {
            String tip = null;

            @Override
            public void onItem(int index) {
                switch (index) {
                    case 0:
                        tip = titleLists.get(index);
                        break;
                    case 1:
                        tip = "";
                        break;
                    case 2:
                        tip = "";
                        break;
                    case 3:
                        tip = "";
                        break;
                }
                UIHelper.ToastMessage(DemoActivity.this, "你选择了-->>" + titleLists.get(index));
            }
        });
    }
}
