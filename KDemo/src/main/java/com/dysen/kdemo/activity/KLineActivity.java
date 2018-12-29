package com.dysen.kdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.dysen.kdemo.views.LoadingFrameLayout;
import com.dysen.kdemo.views.MagicIndicatorView;
import com.dysen.kdemo.views.MarketChartData;
import com.dysen.kdemo.views.ObservableScrollView;
import com.dysen.kdemo.views.RateLinearView;
import com.dysen.kdemo.views.TargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bitbank on 2018/4/6.
 */

public class KLineActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private ObservableScrollView mScrollView;
    private RateLinearView linear_rate;
    private MagicIndicatorView mMagicIndicatorView, mMagicIndicatorViewBottom, mklineTagget;
    private TextView cointitle, tv_target, ma, ema, boll;
    private TargetView mTargetView, mMyChartsView;
    private ImageView image_full;
    private RelativeLayout relative_target;

    private List<String> intoTitle = new ArrayList<String>();
    private String exchangeType = "QC", currencyType = "BTC", symbol = "zbbtcqc";
    private String kChartTimeInterval = "3600";                       //图表数据间隔
    private final String kChartDataSize = "1440";                          //图表数据总条数
    private Context mContext;
    List<String> list = new ArrayList<String>();
    private List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
    private ArrayList data_list;
    private Resources res;
    private LinearLayout llSpaceView;
    private boolean is_treeObserver = false;
    private KCapitalFragment kCapitalFragment;
    private KAnalyzeFragment kAnalyzeFragment;
    private KRecordlFragment kRecordlFragment;
    private KIntroductionFragment kIntroductionFragment;
    AutoHeightViewPager vpType;
    private List<Fragment> fragments = new ArrayList<>();
    private int mIndex = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 设置显示的视图
        setContentView(R.layout.activity_kline);
        ButterKnife.bind(this);
        mContext = this;
        res = this.getResources();

        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                initMagicIndicator();

                indexMarketChart(Tools.getDatas(mIndex));
            }
        }, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initView() {
        tvTitle.setText(Tools.getString(R.string.app_name));
        tvBack.setOnClickListener(this);
        ma = (TextView) findViewById(R.id.ma);
        ma.setOnClickListener(this);
        ema = (TextView) findViewById(R.id.ema);
        ema.setOnClickListener(this);
        boll = (TextView) findViewById(R.id.boll);
        boll.setOnClickListener(this);
        image_full = (ImageView) findViewById(R.id.image_full);
        image_full.setOnClickListener(this);
        mTargetView = (TargetView) findViewById(R.id.mTargetView);
        mTargetView.setOnClickListener(this);

        linear_rate = (RateLinearView) findViewById(R.id.linear_rate);
        linear_rate.setOnClickListener(this);
        linear_rate.marketlist();
        mMagicIndicatorView = (MagicIndicatorView) findViewById(R.id.mMagicIndicatorView);
        mMagicIndicatorView.setOnClickListener(this);
        mMagicIndicatorViewBottom = (MagicIndicatorView) findViewById(R.id.magic_indicator);
        mMagicIndicatorViewBottom.setOnClickListener(this);
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

        mklineTagget = (MagicIndicatorView) findViewById(R.id.mklineTagget);
        mklineTagget.setOnClickListener(this);

        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_target.setOnClickListener(this);
        mMyChartsView = (TargetView) findViewById(R.id.my_charts_view);
        mMyChartsView.setOnClickListener(this);

        mTargetView = (TargetView) findViewById(R.id.mTargetView);
        mTargetView.setOnClickListener(this);
        image_full = (ImageView) findViewById(R.id.image_full);
        image_full.setOnClickListener(this);
        relative_target = (RelativeLayout) findViewById(R.id.relative_target);
        relative_target.setOnClickListener(this);

        Intent intent = getIntent();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.image_full:
                showFullScreenActivity(mIndex);
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
            case R.id.my_charts_view:
                relative_target.setVisibility(View.GONE);
                break;
        }
    }

    public void showFullScreenActivity(int index) {
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    //todo index
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

    /**
     * 注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}