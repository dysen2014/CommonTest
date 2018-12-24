package com.dysen.common_library.common.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @package com.vip.zb.adapters
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/16 - 下午6:59
 * @info
 */

public class BaseViewPagerAdapter extends PagerAdapter {
    private List<View> views;//数据源

    public BaseViewPagerAdapter(List<View> views) {
        this.views = views;
    }

    //数据源的数目
    @Override
    public int getCount() {
        return views.size();
    }
    //view是否由对象产生，官方写arg0==arg1即可
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //对应页卡添加上数据
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }
    //销毁一个页卡(即ViewPager的一个item)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views.get(position);
        container.removeView(view);
    }
}