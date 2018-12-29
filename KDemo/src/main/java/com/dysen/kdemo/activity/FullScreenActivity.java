package com.dysen.kdemo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dysen.kdemo.R;
import com.dysen.kdemo.adapter.BasePagerAdapter;
import com.dysen.kdemo.kline.KAnalyzeFragment;
import com.dysen.kdemo.kline.KCapitalFragment;
import com.dysen.kdemo.kline.KIntroductionFragment;
import com.dysen.kdemo.kline.KRecordlFragment;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.views.AutoHeightViewPager;
import com.dysen.kdemo.views.MagicIndicatorView;
import com.dysen.kdemo.views.MarketChartData;
import com.dysen.kdemo.views.ObservableScrollView;
import com.dysen.kdemo.views.TargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bitbank on 2017/7/29.
 */
public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {
    // 声明控件对象
    private String exchangeType = "QC", currencyType = "BTC", symbol = "zbbtcusdt";
    private ArrayList data_list;

    private TextView ma;
    private TextView ema;
    private TextView boll;

    private MagicIndicatorView mMagicIndicatorView, mMagicIndicatorViewBottom;
    private MagicIndicatorView mklineTagget;
    private TargetView mMyChartsView;
    private TargetView mTargetView;
    private TextView tv_target;
    private ImageView image_full;
    private RelativeLayout relative_target;

    private List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
    private Resources res;
    private int mIndex = 4;

    AutoHeightViewPager vpType;

    private KCapitalFragment kCapitalFragment;
    private KAnalyzeFragment kAnalyzeFragment;
    private KRecordlFragment kRecordlFragment;
    private KIntroductionFragment kIntroductionFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private ObservableScrollView mScrollView;
    private LinearLayout llSpaceView;
    private boolean is_treeObserver;
    private List<String> intoTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.activity_fullscreen);
        res = this.getResources();
                initView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                initMagicIndicator();
                mMagicIndicatorView.onPageSelected(mIndex);
                indexMarketChart(Tools.getDatas(mIndex));
            }
        }, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        Intent intent = getIntent();
        mIndex = intent.getIntExtra("index", 0);
        ma = (TextView) findViewById(R.id.ma);
        ma.setOnClickListener(this);
        ema = (TextView) findViewById(R.id.ema);
        ema.setOnClickListener(this);
        boll = (TextView) findViewById(R.id.boll);
        boll.setOnClickListener(this);

        mMagicIndicatorView = (MagicIndicatorView) findViewById(R.id.mMagicIndicatorView);
        mklineTagget = (MagicIndicatorView) findViewById(R.id.mklineTagget);
        mMyChartsView = (TargetView) findViewById(R.id.my_charts_view);
        mTargetView = (TargetView) findViewById(R.id.mTargetView);
        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_target.setOnClickListener(this);
        image_full = (ImageView) findViewById(R.id.image_full);
        image_full.setOnClickListener(this);
        relative_target = (RelativeLayout) findViewById(R.id.relative_target);
        relative_target.setOnClickListener(this);
        mMagicIndicatorViewBottom = (MagicIndicatorView) findViewById(R.id.magic_indicator);
        mMagicIndicatorViewBottom.setOnClickListener(this);
        mTargetView.setVOLShow();
        mTargetView.setShowXText(true);
        mTargetView.setShowXAxial(true);
        mTargetView.setTargetView(mMyChartsView);

        mTargetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relative_target.setVisibility(View.GONE);
                return false;
            }
        });

        mMyChartsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relative_target.setVisibility(View.GONE);
                return false;
            }
        });

        mMyChartsView.setShowXAxial(false);
        mMyChartsView.setTargetView(mTargetView);
        mMyChartsView.setShowXText(false);

        vpType = findViewById(R.id.viewPager_type);

        /**
         * 控件停靠
         */
        mScrollView = findViewById(R.id.observableScrollView);
        llSpaceView = findViewById(R.id.space_view);
        mScrollView.add_spaceview(llSpaceView);
        mScrollView.add_stickview(mMagicIndicatorViewBottom);
        mScrollView.addOnGlobalLayoutListener();

        //加载完成以后设置高度
        if (mScrollView.getViewTreeObserver().isAlive()) {
            mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!is_treeObserver) {
                        is_treeObserver = true;
                        mScrollView.setSpaceHight();
                    }
                }
            });
        }
    }

    private void initData() {
        kCapitalFragment = KCapitalFragment.newInstance(this);
        kAnalyzeFragment = KAnalyzeFragment.newInstance(this);
        kRecordlFragment = KRecordlFragment.newInstance(this);
        kIntroductionFragment = KIntroductionFragment.newInstance(this);

        fragments.add(kCapitalFragment);
        fragments.add(kAnalyzeFragment);
        fragments.add(kRecordlFragment);
        fragments.add(kIntroductionFragment);
        vpType.setAdapter(new BasePagerAdapter.FragmentAdapter(getSupportFragmentManager(), fragments, null));
        Tools.setAutoHeight(vpType);
        vpType.setOffscreenPageLimit(fragments.size());
    }

    /**
     * 生成图表显示的数据
     *
     * @param marketChartDatas
     */
    private void createLineChartDataSet(String[][] marketChartDatas) {
        if (marketChartDatas != null && marketChartDatas.length > 0) {
            marketChartDataLists.clear();

            for (int i = 0; i < marketChartDatas.length; i++) {
                String[] data = marketChartDatas[i];
                MarketChartData mMarketChartData = new MarketChartData();
                mMarketChartData.setTime(Long.parseLong(data[0]));
                mMarketChartData.setOpenPrice(Double.parseDouble(data[3]));
                mMarketChartData.setClosePrice(Double.parseDouble(data[4]));
                mMarketChartData.setHighPrice(Double.parseDouble(data[5]));
                mMarketChartData.setLowPrice(Double.parseDouble(data[6]));
                mMarketChartData.setVol(Double.parseDouble(data[7]));
                marketChartDataLists.add(mMarketChartData);
            }
            //更新图表
            try {

                mMyChartsView.setOHLCData(marketChartDataLists);
                mMyChartsView.setLatLine(6);
                mMyChartsView.setKShow();
                mMyChartsView.postInvalidate();

                mTargetView.setLongitudeIsShow(false);
                mTargetView.setOHLCData(marketChartDataLists);

            } catch (Exception e) {
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_full:
                finish();
                break;
            case R.id.tv_target:
                if (relative_target.getVisibility() == View.VISIBLE) {
                    relative_target.setVisibility(View.GONE);
                } else {
                    relative_target.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ma:
                tv_target.setText(R.string.ma);
                relative_target.setVisibility(View.GONE);
                mMyChartsView.setShowMA();
                mMyChartsView.postInvalidate();
                break;
            case R.id.ema:
                tv_target.setText(R.string.ema);
                relative_target.setVisibility(View.GONE);
                mMyChartsView.setShowEMA();
                mMyChartsView.postInvalidate();
                break;
            case R.id.boll:
                tv_target.setText(R.string.boll);
                relative_target.setVisibility(View.GONE);
                mMyChartsView.setShowBOLL();
                mMyChartsView.postInvalidate();
                break;
        }
    }

    private void indexMarketChart(String datas) {
        try {
            JSONObject objects = JsonUtils.getJsonObject(datas, "datas");
            JSONArray chartData = JsonUtils.getJsonArray(objects, "chartData");
            String[][] sChartData = new String[chartData.length()][chartData.optJSONArray(0).length()];
            for (int i = 0; i < chartData.length(); i++) {

                for (int j = 0; j < chartData.optJSONArray(i).length(); j++) {

                    sChartData[i][j] = chartData.optJSONArray(i).optString(j);
                }
            }
            createLineChartDataSet(sChartData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initMagicIndicator() {

        String[] kline_target = res.getStringArray(R.array.kline_target);
        List<String> klineList = new ArrayList<>();
        for (int i = 0; i < kline_target.length; i++) {
            klineList.add(kline_target[i]);
        }
        mMagicIndicatorView.setTitle_list(klineList);
        mMagicIndicatorView.initView();
        mMagicIndicatorView.onPageSelected(4);
        mMagicIndicatorView.setiOnListener(new MagicIndicatorView.IOnListener() {
            @Override
            public void onItem(int index) {
                mIndex = index;
                indexMarketChart(Tools.getDatas(index));
            }
        });

        intoTitle.add(res.getString(R.string.funds));//资金
        intoTitle.add(res.getString(R.string.analyze));//分析
        intoTitle.add(res.getString(R.string.Trading_Record));//成交
        intoTitle.add(res.getString(R.string.introduction));//简介

        mMagicIndicatorViewBottom.setTitle_list(intoTitle);
        mMagicIndicatorViewBottom.setWidth_size(intoTitle.size());
        mMagicIndicatorViewBottom.onPageSelected(0);
        mMagicIndicatorViewBottom.setmViewPager(vpType);
        mMagicIndicatorViewBottom.initView();

        //数据
        data_list = new ArrayList<String>();
        data_list.add("VOL");
        data_list.add("MACD");
        data_list.add("KDJ");

        mklineTagget.setTitle_list(data_list);
        mklineTagget.initView();
        mklineTagget.setiOnListener(new MagicIndicatorView.IOnListener() {
            @Override
            public void onItem(int index) {

                switch (index) {
                    case 0:
                        mTargetView.setVOLShow();
                        break;
                    case 1:
                        mTargetView.setMACDShow();
                        break;
                    case 2:
                        mTargetView.setKDJShow();
                        break;
                }
            }
        });
    }
}
