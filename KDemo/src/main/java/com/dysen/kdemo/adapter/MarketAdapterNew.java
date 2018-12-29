package com.dysen.kdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dysen.kdemo.R;
import com.dysen.kdemo.activity.KLineActivity;
import com.dysen.kdemo.adapter.recyclerview.SuperRecyclerAdapter;
import com.dysen.kdemo.adapter.recyclerview.SuperRecyclerHolder;
import com.dysen.kdemo.entity.CommonBean;
import com.dysen.kdemo.entity.KLineItemName;
import com.dysen.kdemo.utils.ColorUtils;
import com.dysen.kdemo.utils.Constants;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.views.ItemKview;
import com.dysen.kdemo.views.LoadingFrameLayout;
import com.dysen.kdemo.views.MagicIndicatorView;
import com.dysen.kdemo.views.MarketChartData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dysen.kdemo.utils.SystemConfig.deFormat;
import static com.dysen.kdemo.utils.SystemConfig.toDouble;

public class MarketAdapterNew extends SuperRecyclerAdapter<CommonBean.BtcQc> implements MagicIndicatorView.IOnListener {
    private static Context mContext;
    private static final int TYPE_KLINE = 1;
    private static final int TYPE_GONE = 0;
    private static final int DEFAULT_DATA = 0;
    private static String exchangeType = "QC", currencyType = "BTC";

    int mItemViewType = 0;
    private Map<String, String[][]> chartMap_ls = new HashMap<>();
    private ArrayList<KLineItemName> itemNameList = new ArrayList<>();
    private KLineItemName kLineItemName_ls;
    private ItemKview lineChart;
    private LoadingFrameLayout fl_loading;
    private OnItemClickListener mClickListener;
    private String msymbol_market;
    public MagicIndicatorView magicIndicatorView;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void onItem(int index) {
        kLineItemName_ls = itemNameList.get(index);
        if (mClickListener != null) {
            lineChart.setType(kLineItemName_ls.getKlineType());
            mClickListener.onItemClick(null, index, kLineItemName_ls.getTimeInterval());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int timeInterval);
    }

    public int getmItemViewType() {
        if (mItemViewType == DEFAULT_DATA) {
            return TYPE_GONE;
        } else {
            return TYPE_KLINE;
        }
    }

    public void setmItemViewType(int mItemViewType) {
        this.mItemViewType = mItemViewType;
    }

    public MarketAdapterNew(Context mCtx) {
        super(mCtx);
        mContext = mCtx;
    }

    @Override
    public void convert(SuperRecyclerHolder holder, CommonBean.BtcQc tickerData, int layoutType, final int position) {
        switch (getmItemViewType()) {
            case TYPE_KLINE://显示K线逻辑处理
                initItemName();
                lineChart = (ItemKview) holder.getViewById(R.id.linechart);
                lineChart.setType(kLineItemName_ls.getKlineType());
                fl_loading = (LoadingFrameLayout) holder.getViewById(R.id.fl_loading);
                holder.setText(R.id.tv_price_high, mContext.getResources().getString(R.string.price_high, tickerData.getTicker().getHigh()));
                holder.setText(R.id.tv_price_low, mContext.getResources().getString(R.string.price_low, tickerData.getTicker().getLow()));
                magicIndicatorView = (MagicIndicatorView) holder.getViewById(R.id.magicIndicatorView);

//                magicIndicatorView.setTitle_list2(itemNameList);
//                magicIndicatorView.setTitle_list(Arrays.asList(new String[]{"时分", "15分", "时K", "日K", "周K"}));
//                magicIndicatorView.initView();
//                magicIndicatorView.setiOnListener(this);
                final List<TextView> views = new ArrayList<>();
                views.add((TextView) holder.getViewById(R.id.tv_0));
                views.add((TextView) holder.getViewById(R.id.tv_1));
                views.add((TextView) holder.getViewById(R.id.tv_2));
                views.add((TextView) holder.getViewById(R.id.tv_3));
                views.add((TextView) holder.getViewById(R.id.tv_4));
                for (int i = 0; i < views.size(); i++) {
                    views.get(i).setText(itemNameList.get(i).getName());
                    views.get(i).setOnClickListener(new View.OnClickListener() {
                        int mIndex = 0;
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < views.size(); i++) {
                                views.get(i).setBackgroundColor(Tools.getColor(R.color.homekline_color));
                                views.get(i).setTextColor(Tools.getColor(R.color.text_color_black2));
                            }

                            switch (v.getId()){
                                case R.id.tv_0:
                                    mIndex = 0;
                                    break;
                                case R.id.tv_1:
                                    mIndex = 1;
                                    break;
                                case R.id.tv_2:
                                    mIndex = 2;
                                    break;
                                case R.id.tv_3:
                                    mIndex = 3;
                                    break;
                                case R.id.tv_4:
                                    mIndex = 4;
                                    break;
                            }
                            views.get(mIndex).setBackgroundResource(R.drawable.btn_ova_hollowl);
                            views.get(mIndex).setTextColor(Tools.getColor(R.color.black));
                            kLineItemName_ls = itemNameList.get(mIndex);
                            if (mClickListener != null) {
                                lineChart.setType(kLineItemName_ls.getKlineType());
                                mClickListener.onItemClick(null, mIndex, kLineItemName_ls.getTimeInterval());
                            }
                        }
                    });
                }

                holder.getViewById(R.id.linechart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mContext.startActivity(new Intent(mContext, KLineActivity.class));
                    }
                });

                setChartData(chartMap_ls);
            case TYPE_GONE://不显示K线逻辑处理
                String riseRate = getRate(tickerData.getTicker().getRiseRate());
                holder.setText(R.id.tv_currentPrice_d, Constants.CurrencyType.CNY.symbol() + deFormat(tickerData.getTicker().getLastRmb(), -8));
                holder.setText(R.id.tv_currentPrice, exchangeType + deFormat(tickerData.getTicker().getLast(), -8));
                holder.setTextColor(R.id.tv_currentPrice, ((riseRate.indexOf("-") != -1) ? ColorUtils.getGreen(mContext) : ColorUtils.getRed(mContext)));
                holder.setText(R.id.tv_market_rate, riseRate);
                holder.setBackgroundResource(R.id.tv_market_rate, R.drawable.bnt_login);
                holder.setText(R.id.tv_currency_name, currencyType);
                holder.setText(R.id.tv_exchange_type, "/" + exchangeType);
                holder.setText(R.id.tv_jylx, String.format(mContext.getResources().getString(R.string.market_24xsl), getVol(tickerData.getTicker().getVol())));
                //https://s.gw299.com/statics/img/v3/app_icon/icon_default/icon_btc_64.png //币种的图标url
                Glide.with(mContext).load("https://s.gw299.com/statics/img/v3/app_icon/icon_default/icon_btc_64.png")
                        .apply(RequestOptions.circleCropTransform()).into((ImageView) holder.getViewById(R.id.img_market_type));

                holder.getViewById(R.id.ll_market_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mClickListener.onItemClick(v, position, 0);
                    }
                });
        }
    }

    @Override
    public int getLayoutAsViewType(CommonBean.BtcQc tickerData, int position) {
        return (getmItemViewType() == TYPE_KLINE) ? R.layout.fragment_market_kline_item : R.layout.fragment_market_item;
    }

    //获取带单位的成交量
    public static String getVol(String vol) {
        String dw_str = "";
        double vol_a = toDouble(vol);
        if (vol_a > 10000) {
            vol_a = vol_a / 10000.0;
            dw_str = mContext.getResources().getString(R.string.market_dw_w);
        }
        return deFormat(vol_a + "", -2) + dw_str;
    }

    //获取涨跌幅
    public static String getRate(String rate) {
        if (rate != null) {
            //是否超出尾数限制
            String str_rate = rate;
            if (str_rate.indexOf(".") > 0 && str_rate.length() > 3) {
                String str_1 = str_rate.substring(0, str_rate.indexOf(".") + 1);
                String str_2 = str_rate.substring(str_rate.indexOf(".") + 1, str_rate.length());
                if (str_2.length() > 2) {
                    str_2 = str_2.substring(0, 2);
                }
                str_rate = str_1 + str_2;
            }
            if (str_rate.indexOf("-") == -1) {
                rate = "+" + str_rate + "%";
            } else {
                rate = str_rate + "%";
            }
        } else {
            rate = "+0.00%";
        }
        return rate;
    }

    public static void createChartDataSet(final LoadingFrameLayout fl_loading, final ItemKview linechart, String[][] marketChartDatas) {
        List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
        if (marketChartDatas != null && marketChartDatas.length > 0) {
            for (int i = 0; i < marketChartDatas.length; i++) {
                marketChartDataLists.add(new MarketChartData(marketChartDatas[i]));
            }
            //更新图表
            try {
                linechart.setOHLCData(marketChartDataLists);
                linechart.setYIsLeft(false);
                linechart.setHaveBorder(false);
                linechart.setShowXText(true);
                linechart.setShowYText(true);
                if (linechart.getType() == 1) {
                    //分时
                    linechart.setLatLine(3);
                    linechart.setLogLine(6);
                    linechart.setHaveBorder(false);
                    linechart.setChartTop(10);
                    linechart.setHaveBorder(true);
                    linechart.setChartBottom(10);
                    linechart.setLongitudeIsShow(true);
                } else {
                    //k线
                    linechart.setLatLine(3);
                    linechart.setLogLine(6);
                    linechart.setHaveBorder(false);
                    linechart.setChartTop(2 * linechart.DEFAULT_AXIS_TITLE_SIZE);
                    linechart.setChartBottom(3 * linechart.DEFAULT_AXIS_TITLE_SIZE);
                    linechart.setHaveBorder(true);
                    linechart.setLongitudeIsShow(true);
                }
                linechart.postInvalidate();
                fl_loading.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        } else {
            if (!fl_loading.isShown()) {
                fl_loading.setVisibility(View.VISIBLE);
            }
            linechart.clearOHLCData();
        }
    }

    public void setChartData(final Map<String, String[][]> chartMap) {
        chartMap_ls = chartMap;
//        final String[][] chartData = chartMap.get(mTickerData.getSymbol() + "_" + kLineItemName_ls.getTimeInterval());
        final String[][] chartData = chartMap.get(msymbol_market);

        createChartDataSet(fl_loading, lineChart, chartData);
    }

    private void initItemName() {
        itemNameList = new ArrayList<>();
        itemNameList.add(new KLineItemName(1, mContext.getResources().getString(R.string.time_min), 1 * 60));
        itemNameList.add(new KLineItemName(0, mContext.getResources().getString(R.string.min_k), 15 * 60));
        itemNameList.add(new KLineItemName(0, mContext.getResources().getString(R.string.hours_k), 60 * 60));
        itemNameList.add(new KLineItemName(0, mContext.getResources().getString(R.string.days_k), 24 * 60 * 60));
        itemNameList.add(new KLineItemName(0, mContext.getResources().getString(R.string.weeks_k), 7 * 24 * 60 * 60));
        kLineItemName_ls = itemNameList.get(0);
    }

    public void putChartMap(String symbol_market, String[][] chartData) {
        msymbol_market = symbol_market;
        chartMap_ls.put(symbol_market, chartData);
        notifyDataSetChanged();
    }
}
