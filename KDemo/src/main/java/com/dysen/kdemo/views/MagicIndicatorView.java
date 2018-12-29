package com.dysen.kdemo.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.dysen.kdemo.R;
import com.dysen.kdemo.utils.DeviceUtil;
import com.dysen.kdemo.entity.KLineItemName;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ScaleTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MagicIndicatorView extends LinearLayout {
	private Context mContext;
	//背景颜色
	private int background_color;
	//没有选中
	private int normal_color;
	//字体没有选中
	private int text_normal_color;
	//字体选中
	private int text_selected_color;


	//已经选中
	private int selected_color;
	//屏幕宽度/N
	private float width_size;
	//字体大小
	private float text_size=16;
	//1线条，2椭圆
	private int indicator_type=1;
	private ViewPager mViewPager=null;
	//指示器
	private MagicIndicator magicIndicator;
	private CommonNavigator commonNavigator;
	private List<String> title_list = new ArrayList<String>();
	private List<KLineItemName> title_list2 = new ArrayList<KLineItemName>();
	private IOnListener iOnListener=null;
	private float fontScale=1;

	public void setIndicator_type(int indicator_type) {
		this.indicator_type = indicator_type;
	}
	public MagicIndicatorView(Context context) {
		super(context);
	}

	public MagicIndicatorView(Context context, AttributeSet attrs) {
		super(context,attrs);
		mContext=context;
		View view = LayoutInflater.from(context).inflate(R.layout.magic_indicator, this, true);
		magicIndicator=view.findViewById(R.id.magicindicator);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MagicIndicator);
		background_color=typedArray.getColor(R.styleable.MagicIndicator_background_color, Color.WHITE);
		normal_color=typedArray.getColor(R.styleable.MagicIndicator_normal_color, Color.GRAY);
		selected_color=typedArray.getColor(R.styleable.MagicIndicator_selected_color, Color.BLACK);

		text_normal_color=typedArray.getColor(R.styleable.MagicIndicator_text_normal_color, Color.GRAY);
		text_selected_color=typedArray.getColor(R.styleable.MagicIndicator_text_selected_color, Color.BLACK);

		width_size=typedArray.getFloat(R.styleable.MagicIndicator_width_size,2);
		text_size=typedArray.getDimension(R.styleable.MagicIndicator_text_size, 50);// 默认为10dp
		indicator_type=typedArray.getInteger(R.styleable.MagicIndicator_indicator_type,1);

		fontScale = context.getResources().getDisplayMetrics().scaledDensity;

		typedArray.recycle();
	}


	public void setmViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}
	public void setTitle_list(List<String> title_list) {
		this.title_list = title_list;
	}
	public void setTitle_list2(List<KLineItemName> title_list) {
		this.title_list2 = title_list;
	}

	public void setiOnListener(IOnListener iOnListener) {
		this.iOnListener = iOnListener;
	}

	public void setWidth_size(float width_size) {
		this.width_size = width_size;
	}

	public void setText_selected_color(int text_selected_color) {
		this.text_selected_color = text_selected_color;
	}

	public void setSelected_color(int selected_color) {
		this.selected_color = selected_color;
	}

	public MagicIndicator getMagicIndicator() {
		return magicIndicator;
	}

	public void initView() {
		initMagicIndicator();
	}
	public void notifyDataSetChanged() {
		if(commonNavigator!=null){
			commonNavigator.notifyDataSetChanged();
		}
	}
	public void onPageSelected(int index) {
		if(commonNavigator!=null){
			commonNavigator.onPageSelected(index);
			commonNavigator.notifyDataSetChanged();
		}
		if (iOnListener != null) {
			iOnListener.onItem(index);
		}
	}
	private String getTitle(int index){
		String title="";
		if(title_list!=null&&title_list.size()>0){
			title=title_list.get(index);
		}else if(title_list2!=null&&title_list2.size()>0){
			title=title_list2.get(index).getName();
		}
		return title;
	}
	private int getTitleCount(){
		int titleCount=0;
		if(title_list!=null&&title_list.size()>0){
			titleCount=title_list.size();
		}else if(title_list2!=null&&title_list2.size()>0){
			titleCount=title_list2.size();
		}
		return titleCount;
	}
	public void initMagicIndicator() {
		magicIndicator.setBackgroundColor(background_color);
		commonNavigator = new CommonNavigator(mContext);
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return getTitleCount();
			}
			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context, 0.95f);
				if(width_size>0){
					simplePagerTitleView.setMinWidth((int) (DeviceUtil.getWidth((Activity) mContext) /width_size));
				}
				simplePagerTitleView.setTextSize(text_size/fontScale);
				simplePagerTitleView.setText(getTitle(index));
				simplePagerTitleView.setNormalColor(text_normal_color);
				simplePagerTitleView.setSelectedColor(text_selected_color);
				simplePagerTitleView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							if (iOnListener != null) {
								commonNavigator.onPageSelected(index);
								commonNavigator.notifyDataSetChanged();
								iOnListener.onItem(index);
							}
						}catch (Exception e){

						}
						try {
							if (getTitleCount() > index) {
								if(mViewPager!=null){
									mViewPager.setCurrentItem(index);
								}
							}
						} catch (Exception e) {
							if (getTitleCount() > 0) {
								if(mViewPager!=null){
									mViewPager.setCurrentItem(0);
								}
							}

						}
					}
				});
				return simplePagerTitleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				IPagerIndicator indicator_ls;
				if(indicator_type==1){
					LinePagerIndicator indicator = new LinePagerIndicator(context);
					indicator.setLineHeight(5f);
					indicator.setColors(selected_color);

					indicator.setStartInterpolator(new AccelerateInterpolator());
					indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
					indicator_ls=indicator;
				}else{
					WrapPagerIndicator indicator = new WrapPagerIndicator(context);
					indicator.setFillColor(selected_color);
					indicator_ls=indicator;
				}
				return indicator_ls;
			}
		});
		magicIndicator.setNavigator(commonNavigator);
		if (mViewPager!=null) {
			ViewPagerHelper.bind(magicIndicator, mViewPager);
		}

	}
	public List getTitleList(){
		return title_list;
	}
	public List getTitleList2(){
		return title_list2;
	}
	public interface IOnListener {
		void onItem(int index);
	}
}
