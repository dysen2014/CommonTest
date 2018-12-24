package com.dysen.kdemo.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.adapter.BasePagerAdapter;
import com.dysen.kdemo.entity.ChartData;
import com.dysen.kdemo.kline.KAnalyzeFragment;
import com.dysen.kdemo.kline.KCapitalFragment;
import com.dysen.kdemo.kline.KIntroductionFragment;
import com.dysen.kdemo.kline.KRecordlFragment;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.views.AutoHeightViewPager;
import com.dysen.kdemo.views.MagicIndicatorView;
import com.dysen.kdemo.views.MarketChartData;
import com.dysen.kdemo.views.ObservableScrollView;
import com.dysen.kdemo.views.RateLinearView;
import com.dysen.kdemo.views.TargetView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bitbank on 2018/4/6.
 */

public class KLineActivity extends AppCompatActivity implements View.OnClickListener {

    private ObservableScrollView mScrollView;
    private RateLinearView linear_rate;
    private LinearLayout linear_buy_sell;
    private MagicIndicatorView mMagicIndicatorView, mMagicIndicatorViewBottom, mklineTagget;
    private TextView cointitle, tv_target, ma, ema, boll;
    private TargetView mTargetView, mMyChartsView;
    private Button buy, sell;
    private ImageView image_full;
    private RelativeLayout relative_target;

    private List<String> intoTitle = new ArrayList<String>();
    private String exchangeType = "QC", currencyType = "BTC", symbol = "zbbtcqc";
    private String kChartTimeInterval = "3600";                       //图表数据间隔
    private final String kChartDataSize = "1440";                          //图表数据总条数
    //定时器
    Timer timer1 = null;  //刷新定时器
    Timer timer2 = null;  //刷新定时器
    private final int dataRefreshTime1 = 2 * 1000;                         //数据刷新间隔
    private final int dataRefreshTime2 = 60 * 1000;                         //数据刷新间隔
    private Context mContext;
    List<String> list = new ArrayList<String>();
    private List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
    private ArrayList data_list;
    private Resources res;
    private boolean isCollected = false;
    private LinearLayout llSpaceView;
    private boolean is_treeObserver = false;
    private KCapitalFragment kCapitalFragment;
    private KAnalyzeFragment kAnalyzeFragment;
    private KRecordlFragment kRecordlFragment;
    private KIntroductionFragment kIntroductionFragment;
    AutoHeightViewPager vpType;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tintManager.setStatusBarTintResource(R.color.klinebg);//通知栏所需颜色

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 设置显示的视图
        setContentView(R.layout.activity_kline);
        mContext = this;
        res = this.getResources();

        initView();
        initMagicIndicator();
        initData();
        initGroup();

        /** 注册广播 */
        receiveBroadViewData = new ReceiveBroadViewData();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vip.zb.activity.KLineActivity");    //只有持有相同的action的接受者才能接收此广播
        this.registerReceiver(receiveBroadViewData, filter);
    }

    private void initData() {
        kCapitalFragment = KCapitalFragment.newInstance(this);
        kAnalyzeFragment = KAnalyzeFragment.newInstance(this);
        kRecordlFragment = KRecordlFragment.newInstance(this);
        kIntroductionFragment = KIntroductionFragment.newInstance(this);

        //加载币种资料数据
        kIntroductionFragment.init(exchangeType, currencyType);
        //加载K线数据
        kCapitalFragment.init(exchangeType, currencyType);

        fragments.add(kCapitalFragment);
        fragments.add(kAnalyzeFragment);
        fragments.add(kRecordlFragment);
        fragments.add(kIntroductionFragment);
        vpType.setAdapter(new BasePagerAdapter.FragmentAdapter(getSupportFragmentManager(), fragments, null));
        Tools.setAutoHeight(vpType);
        vpType.setOffscreenPageLimit(fragments.size());
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    private void initView() {

        ma = (TextView) findViewById(R.id.ma);
        ma.setOnClickListener(this);
        ema = (TextView) findViewById(R.id.ema);
        ema.setOnClickListener(this);
        boll = (TextView) findViewById(R.id.boll);
        boll.setOnClickListener(this);

        mTargetView = (TargetView) findViewById(R.id.mTargetView);
        mTargetView.setOnClickListener(this);

        linear_rate = (RateLinearView) findViewById(R.id.linear_rate);
        linear_rate.setOnClickListener(this);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initGroup() {

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

                switch (index) {
                    case 0:
                        kChartTimeInterval = 1 * 60 + "";
                        break;
                    case 1:
                        kChartTimeInterval = 5 * 60 + "";
                        break;
                    case 2:
                        kChartTimeInterval = 15 * 60 + "";
                        break;
                    case 3:
                        kChartTimeInterval = 30 * 60 + "";
                        break;
                    case 4:
                        kChartTimeInterval = 60 * 60 + "";
                        break;
                    case 5:
                        kChartTimeInterval = 2 * 60 * 60 + "";
                        break;
                    case 6:
                        kChartTimeInterval = 4 * 60 * 60 + "";
                        break;
                    case 7:
                        kChartTimeInterval = 6 * 60 * 60 + "";
                        break;
                    case 8:
                        kChartTimeInterval = 12 * 60 * 60 + "";
                        break;
                    case 9:
                        kChartTimeInterval = 24 * 60 * 60 + "";
                        break;
                    case 10:
                        kChartTimeInterval = 3 * 24 * 60 * 60 + "";
                        break;
                    case 11:
                        kChartTimeInterval = 7 * 24 * 60 * 60 + "";
                        break;
                }
                indexMarketChart();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_full:
//                UIHelper.showFullScreenActivity(this, symbol);
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

    //todo index
    private void indexMarketChart() {
        String datas = Tools.getString(R.string.analyze_data);
        ChartData chartData1 = null;
        if (chartData1 != null && chartData1.getChartData().length > 0) {
            createLineChartDataSet(chartData1.getChartData());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
//        startTimer();
//        keyboard.getfunds();
//        mScrollView.scrollTo(0, (int) move_range);
    }

    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 1) {//2S
                linear_rate.marketlist();
                kAnalyzeFragment.init(exchangeType, currencyType);
                kRecordlFragment.init(exchangeType, currencyType);
            } else if (msg.what == 2) {//60S
                indexMarketChart();
                kCapitalFragment.init(exchangeType, currencyType);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timer1 == null) {
            //Log.i("KD_startTimer", "KD_startTimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer1 = new Timer();
            TransTimerTask task1 = new TransTimerTask(handlerOfTrans, 1);
            timer1.schedule(task1, 0, dataRefreshTime1);
        }
        if (timer2 == null) {
            //Log.i("KD_startTimer", "KD_startTimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer2 = new Timer();
            TransTimerTask task2 = new TransTimerTask(handlerOfTrans, 2);
            timer2.schedule(task2, 0, dataRefreshTime2);
        }
    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timer1 != null) {
            //Log.i("KD_stoptimer", "KD_stoptimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer1.cancel();
            timer1 = null;
        }
        if (timer2 != null) {
            //Log.i("KD_stoptimer", "KD_stoptimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer2.cancel();
            timer2 = null;
        }
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {
        private int code;
        private Handler mHandler;

        public TransTimerTask() {

        }

        public TransTimerTask(Handler handler, int code) {
            this.mHandler = handler;
            this.code = code;
        }

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = code;
            this.mHandler.sendMessage(message);
        }
    }

    private void initMagicIndicator() {

        intoTitle.add(res.getString(R.string.funds));//资金
        intoTitle.add(res.getString(R.string.analyze));//分析
        intoTitle.add(res.getString(R.string.Trading_Record));//成交
        intoTitle.add(res.getString(R.string.introduction));//简介
        //TODO 隐藏评论
//        intoTitle.add(res.getString(R.string.comment));//评论

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

    public void setMagicIndicatorViewBottomIndex() {
        mMagicIndicatorViewBottom.onPageSelected(3);
        //mScrollView.fullScroll(ScrollView.FOCyUS_DOWN);//滚动到底部
        int y = (int) mMagicIndicatorViewBottom.getY();
        mScrollView.scrollTo(0, y);
    }

    private ReceiveBroadViewData receiveBroadViewData;

    class ReceiveBroadViewData extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            marketChartDataLists.clear();
            indexMarketChart();
            linear_rate.marketlist();

            kCapitalFragment.init(exchangeType, currencyType);
            kAnalyzeFragment.init(exchangeType, currencyType);
            kRecordlFragment.init(exchangeType, currencyType);
            kIntroductionFragment.init(exchangeType, currencyType);
        }
    }

    /**
     * 注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}