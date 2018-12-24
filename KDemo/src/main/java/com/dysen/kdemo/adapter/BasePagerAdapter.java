package com.dysen.kdemo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.vip.zb.adapters
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/16 - 下午6:59
 * @info
 */

public class BasePagerAdapter {
    public static class ViewAdapter extends PagerAdapter {
        private List<View> views = new ArrayList<>();//数据源

        public void setDatas(List<View> items) {
            views.clear();
            views.addAll(items);
            notifyDataSetChanged();
        }

        public ViewAdapter() {
        }

        public ViewAdapter(List<View> views) {
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

    public static class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private String[] mTitles;


        public void setDatas(List<Fragment> items) {
            fragments.clear();
            fragments.addAll(items);
            notifyDataSetChanged();
        }

        public FragmentAdapter(FragmentManager fm, String ...titles) {
            super(fm);
            this.mTitles = titles;
        }

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (mTitles == null) {
                return "";
            }
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}