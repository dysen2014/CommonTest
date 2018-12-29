package com.dysen.kdemo.kline;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.activity.KLineActivity;
import com.dysen.kdemo.entity.CapitalFlow;
import com.dysen.kdemo.utils.ColorUtils;
import com.dysen.kdemo.utils.DateUtils;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.utils.ViewUtils;
import com.dysen.kdemo.utils.chart.CustomChartUtils;
import com.dysen.kdemo.utils.chart.CustomValueFormatter;
import com.dysen.kdemo.views.ScrollViewForList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.JsonArray;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @package com.vip.zb.fragment
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/17 - 下午6:10
 * @info
 */
public class KCapitalFragment extends Fragment {
    private static Context mContext;
    private final long BEFORE_TIME = 6 * 60 * 60 * 1000;//近6小时　
    public final static int INTERVAL_COUNT = 18;//K线 x轴  间隔

    private View mView;
    private static KLineActivity mActivity;

    private static String exchangeType = "QC", currencyType = "BTC";
    List<String> list = new ArrayList<String>();

    TextView tv_fund_unit, tv_fund_net;
    ScrollViewForList listview_history, listview_bigRecord;

    private LineChart mLineChart;
    private BarChart mBarChart, mBarChartBuy, mBarChartSell;
    private PieChart mPieChart;
    private BarDataSet mBarDataSet;
    private LineDataSet mLineDataSet;
    String marketName, timeRange = "5";

    TextView tv_0, tv_1, tv_2, tv_3;

    TextView tv_history_state, tv_bigRecord_state;
    private List<CapitalFlow.HistoryFunds.ListBean> mArrayListHistoryFunds = new ArrayList<>();
    private List<CapitalFlow.BigRecord.ListBean> mArrayListBigRecord = new ArrayList<>();
    private QuickAdapter<CapitalFlow.HistoryFunds.ListBean> adapter_history_funds;
    private QuickAdapter<CapitalFlow.BigRecord.ListBean> adapter_big_record;

    public static KCapitalFragment newInstance(Context context) {
        mContext = context;
        Bundle args = new Bundle();

        KCapitalFragment fragment = new KCapitalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_fragment_k_capital, null);
        initView();
        return mView;
    }

    public void init(String exchangeType, String currencyType) {
        this.exchangeType = exchangeType;
        this.currencyType = currencyType;
        getKline();
        getFundDistribute();
        getHistoryFunds();
        getNetfunds();
        getBigRecord();
    }

    public void initView() {

        listview_history = (ScrollViewForList) mView.findViewById(R.id.listview_history);
        listview_bigRecord = (ScrollViewForList) mView.findViewById(R.id.listview_bigRecord);
        tv_history_state = (TextView) mView.findViewById(R.id.tv_history_state);
        tv_bigRecord_state = (TextView) mView.findViewById(R.id.tv_bigRecord_state);

        tv_0 = (TextView) mView.findViewById(R.id.tv_0);
        tv_1 = (TextView) mView.findViewById(R.id.tv_1);
        tv_2 = (TextView) mView.findViewById(R.id.tv_2);
        tv_3 = (TextView) mView.findViewById(R.id.tv_3);
        mLineChart = mView.findViewById(R.id.lineChart);
        mBarChart = mView.findViewById(R.id.barChart);
        mPieChart = mView.findViewById(R.id.pieChart);
        mBarChartBuy = mView.findViewById(R.id.barChartBuy);
        mBarChartSell = mView.findViewById(R.id.barChartSell);
        tv_fund_unit = (TextView) mView.findViewById(R.id.tv_fund_unit);
        tv_fund_net = (TextView) mView.findViewById(R.id.tv_fund_net);

        initAdapter();
        initDatas();
        initMethod();
    }

    private void initMethod() {
        getKline();
        getFundDistribute();
        getHistoryFunds();
        getNetfunds();
        getBigRecord();
    }

    private void initDatas() {
        tv_1.setText(getString(R.string.hcf_amount, exchangeType));
        tv_2.setText(getString(R.string.hcf_quantitative, currencyType));
        tv_3.setText(getString(R.string.hcf_net_price, exchangeType));

        tv_fund_unit.setText(getString(R.string.fund_unit, currencyType));
        tv_fund_net.setText(Html.fromHtml(DateUtils.dateSimpleFormat(new Date(), new SimpleDateFormat("MM月dd日")) + "\t\t" + getString(R.string.fund_net,
                "<font color='#E70101'><big><big>" + "--" + "</big></big></font> ")));
    }

    private void initAdapter() {

        adapter_history_funds = new QuickAdapter<CapitalFlow.HistoryFunds.ListBean>(mContext, R.layout.historical_capital_flow_item, mArrayListHistoryFunds) {
            @Override
            protected void convert(BaseAdapterHelper helper, final CapitalFlow.HistoryFunds.ListBean listBean) {
                TextView tv_0 = (TextView) helper.getView().findViewById(R.id.tv_0);
                TextView tv_1 = (TextView) helper.getView().findViewById(R.id.tv_1);
                TextView tv_2 = (TextView) helper.getView().findViewById(R.id.tv_2);
                TextView tv_3 = (TextView) helper.getView().findViewById(R.id.tv_3);

                tv_0.setText(initTime(listBean.getTime()));
                tv_1.setText(CustomValueFormatter.parseFlost(listBean.getBuy()));
                tv_2.setText(CustomValueFormatter.parseFlost(listBean.getSell()));
                tv_3.setText(CustomValueFormatter.parseFlost(listBean.getNet()));

                tv_0.setTextColor(ColorUtils.getWhite(mContext));
                tv_1.setTextColor(ColorUtils.getRed(mContext));
                tv_2.setTextColor(ColorUtils.getGreen(mContext));
                if (Tools.checkPositiveNumber(String.valueOf(listBean.getNet())))
                    tv_3.setTextColor(ColorUtils.getRed(mContext));
                else
                    tv_3.setTextColor(ColorUtils.getGreen(mContext));
            }

            //{"array":[[1,0,0,0],[5,0,0,0],[10,0,0,0],[60,0,0,0],[1440,0,0,0],[10080,0,0,0]]}
            private String initTime(long a) {
                String timeName = null;
                switch ((int) a) {
                    case 1:
                        timeName = "今日";
                        break;
                    case 5:
                        timeName = "5分钟";
                        break;
                    case 10:
                        timeName = "10分钟";
                        break;
                    case 60:
                        timeName = "1小时";
                        break;
                    case 1440:
                        timeName = "1天";
                        break;
                    case 10080:
                        timeName = "1周";
                        break;
                    default:
                        timeName = a + "分钟";
                        break;
                }
                return timeName;
            }
        };
        adapter_big_record = new QuickAdapter<CapitalFlow.BigRecord.ListBean>(mContext, R.layout.historical_capital_flow_item, mArrayListBigRecord) {
            @Override
            protected void convert(BaseAdapterHelper helper, final CapitalFlow.BigRecord.ListBean listBean) {
                TextView tv_0 = (TextView) helper.getView().findViewById(R.id.tv_0);
                TextView tv_1 = (TextView) helper.getView().findViewById(R.id.tv_1);
                TextView tv_2 = (TextView) helper.getView().findViewById(R.id.tv_2);
                TextView tv_3 = (TextView) helper.getView().findViewById(R.id.tv_3);

                tv_0.setText(DateUtils.dateSimpleFormat(new Date(listBean.getTime()), new SimpleDateFormat("HH:mm:ss")));
                tv_1.setText(CustomValueFormatter.parseFlost(listBean.getCountPrice()));
                tv_2.setText(CustomValueFormatter.parseFlost(listBean.getQuantitative()));
                tv_3.setText(CustomValueFormatter.parseFlost(listBean.getPrice()));
                //type 类型 0 买 1卖
                switch (listBean.getType()) {
                    case 0:
                        tv_1.setText(Tools.getString(R.string.hcf_buy) + " " + CustomValueFormatter.parseFlost(listBean.getCountPrice()));
                        tv_1.setTextColor(Tools.getColor(R.color.text_color_red));
                        break;
                    case 1:
                        tv_1.setText(Tools.getString(R.string.hcf_sell) + " " + CustomValueFormatter.parseFlost(listBean.getCountPrice()));
                        tv_1.setTextColor(Tools.getColor(R.color.text_color_green));
                        break;
                }

                tv_0.setTextColor(Tools.getColor(R.color.text_color_white));
                tv_2.setTextColor(Tools.getColor(R.color.text_color_white));
                tv_3.setTextColor(Tools.getColor(R.color.text_color_white));
            }
        };

        listview_history.setAdapter(adapter_history_funds);
        listview_bigRecord.setAdapter(adapter_big_record);
    }

    private void createNetfunds(List<List<String>> netFunds) {
        List<String> barChartLists = new ArrayList<>();
        List<BarEntry> barEntries = new ArrayList<>();
        List<List<String>> array = netFunds;
        tv_fund_unit.setText(getString(R.string.fund_unit, currencyType));
        if (array == null || array.size() == 0) {
            if (mBarDataSet != null) {
                mBarChart.setData(new BarData(mBarDataSet));
                mBarChart.clear();// hides the set
            }
            mBarChart.setNoDataText(CustomChartUtils.mNoDataText);
            return;
        } else {
            if (array.size() > 5) {//保留最近5日的资金净流入
                array = array.subList(array.size() - 5, array.size());
            }
            //柱状图
            for (int i = 0; i < array.size(); i++) {
                barChartLists.add(DateUtils.dateSimpleFormat(DateUtils.getOtherFormat(i - array.size() + 1), new SimpleDateFormat("MM-dd")));
//            barChartLists.add(DateUtils.dateSimpleFormat(new Date(array.get(i).get(0)), new SimpleDateFormat("MM-dd")));
                barEntries.add(new BarEntry(i, Float.valueOf(array.get(i).get(1))));
            }

            String netInFlow = CustomValueFormatter.parseFlost(Float.valueOf(array.get(array.size() - 1).get(1)));
            if (Tools.checkPositiveNumber(netInFlow)) {
                tv_fund_net.setText(Html.fromHtml(DateUtils.dateSimpleFormat(new Date(), new SimpleDateFormat("MM月dd日")) + "\t\t" + getString(R.string.fund_net,
                        "<font color='#E70101'><big><big>" + netInFlow + "</big></big></font>")));
            } else {
                tv_fund_net.setText(Html.fromHtml(DateUtils.dateSimpleFormat(new Date(), new SimpleDateFormat("MM月dd日")) + "\t\t" + getString(R.string.fund_net,
                        "<font color='#08BA52'><big><big>" + netInFlow + "</big></big></font>")));
            }
        }
        mBarDataSet = new BarDataSet(barEntries, "");
        CustomChartUtils.configBarChart(mBarChart, barChartLists);
        CustomChartUtils.initBarChart(mBarChart, barEntries, "sendy", Tools.getColor(R.color.text_color_red));
    }

    private void createHistoryFunds(List<List<String>> historyFunds) {
        mArrayListHistoryFunds.clear();
        List<List<String>> array = historyFunds;
        CapitalFlow.HistoryFunds.ListBean listBean = null;
        if (array == null || array.size() == 0) {
            adapter_history_funds.clear();
            tv_history_state.setVisibility(View.VISIBLE);
            listview_history.setVisibility(View.GONE);
        } else {
            tv_history_state.setVisibility(View.GONE);
            listview_history.setVisibility(View.VISIBLE);
            for (List<String> list : array) {
                listBean = new CapitalFlow.HistoryFunds.ListBean(Long.valueOf(list.get(0)), Float.valueOf(list.get(1)), Float.valueOf(list.get(2)), Float.valueOf(list.get(3)));
                mArrayListHistoryFunds.add(listBean);
            }
//        ViewUtils.setHeight(adapter_history_funds, listview_history);

            adapter_history_funds.replaceAll(mArrayListHistoryFunds);
            adapter_history_funds.notifyDataSetChanged();
        }
    }

    private void createBigRecord(List<List<String>> bigRecords) {
        mArrayListBigRecord.clear();
        List<List<String>> array = bigRecords;
        CapitalFlow.BigRecord.ListBean listBean = null;

        tv_1.setText(getString(R.string.hcf_amount, exchangeType));
        tv_2.setText(getString(R.string.hcf_quantitative, currencyType));
        tv_3.setText(getString(R.string.hcf_net_price, exchangeType));
        if (array == null || array.size() == 0) {
            adapter_big_record.clear();
            tv_bigRecord_state.setText(Tools.getString(R.string.now_big_no_data, CustomValueFormatter.parseFlost(500000F)));
            tv_bigRecord_state.setVisibility(View.VISIBLE);
            listview_bigRecord.setVisibility(View.GONE);
        } else {
            tv_bigRecord_state.setVisibility(View.GONE);
            listview_bigRecord.setVisibility(View.VISIBLE);
            for (List<String> list : array) {
                listBean = new CapitalFlow.BigRecord.ListBean(Long.valueOf(list.get(0)), Integer.valueOf(list.get(1)), Float.valueOf(list.get(2)), Float.valueOf(list.get(3)), Float.valueOf(list.get(4)));
                mArrayListBigRecord.add(listBean);
            }
            ViewUtils.setHeight(adapter_big_record, listview_bigRecord, 10);
            adapter_big_record.replaceAll(mArrayListBigRecord);
            adapter_big_record.notifyDataSetChanged();
        }
    }

    private void createFundDistribute(CapitalFlow.FundDistribute.ObjBean objBean) {
        CapitalFlow.FundDistribute.ObjBean obj = objBean;
        if (obj == null)
            return;
        //饼图
        double buyAmount = obj.getBuyVolume();
        double sellAmount = obj.getSellVolume();
        String buyName = Tools.getString(R.string.hcf_buy);
        String sellName = Tools.getString(R.string.hcf_sell);
        PieEntry buyEntry = new PieEntry((float) buyAmount, buyName);
        PieEntry sellEntry = new PieEntry((float) sellAmount, sellName);

        List<Float> buys = Arrays.asList(new Float[]{(float) obj.getLargeTransBuyVolume(), (float) obj.getMiddleTransBuyVolume(), (float) obj.getSmallTransBuyVolume()});
        List<Float> sells = Arrays.asList(new Float[]{(float) obj.getLargeTransSellVolume(), (float) obj.getMiddleTransSellVolume(), (float) obj.getSmallTransSellVolume()});

        CustomChartUtils.configPieChart(mPieChart, Arrays.asList(new String[]{buyName, sellName}), Tools.getString(R.string.capital_transaction_distribution), false);
        CustomChartUtils.initData(mPieChart, new PieDataSet(Arrays.asList(new PieEntry[]{buyEntry, sellEntry}), ""));

        //柱状图
        List<String> barChartLists = Arrays.asList(new String[]{Tools.getString(R.string.large_trans), Tools.getString(R.string.middle_trans), Tools.getString(R.string.small_trans)});
        List<BarEntry> buyBarEntries = new ArrayList<>();
        List<BarEntry> sellBarEntries = new ArrayList<>();
        for (int i = 0; i < buys.size(); i++) {

            buyBarEntries.add(new BarEntry(i, buys.get(i)));
            sellBarEntries.add(new BarEntry(i, sells.get(i)));
        }

        CustomChartUtils.configBarChart(mBarChartBuy, barChartLists);
        CustomChartUtils.initBarChart(mBarChartBuy, 0, new BarDataSet(buyBarEntries, ""));
        CustomChartUtils.configBarChart(mBarChartSell, barChartLists);
        CustomChartUtils.initBarChart(mBarChartSell, 1, new BarDataSet(sellBarEntries, ""));
    }

    private void createKLine(List<List<String>> klines) {
        long nowTime = 0, firstTime = 0;
        List<String> lineChartLists = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();
        List<List<String>> array = klines;
        if (array == null || array.size() == 0) {

            if (mLineDataSet != null) {
                mLineChart.setData(new LineData(mLineDataSet));
                mLineChart.clear();// hides the set
            }
            mLineChart.setNoDataText(CustomChartUtils.mNoDataText);
            return;
        } else {
            List<List<String>> lists = new ArrayList<>();
            lineChartLists.clear();
            nowTime = Long.valueOf(array.get(array.size() - 1).get(0));//获得最近时间戳
            firstTime = nowTime - BEFORE_TIME;////获得六小时前时间戳
            for (int i = 0; i < array.size(); i++) {
                if (Long.valueOf(array.get(i).get(0)) >= firstTime) {
                    lists.add(array.get(i));
                }
            }

            for (int i = 0; i < lists.size(); i++) {
                lineEntries.add(new Entry(i, Float.valueOf(lists.get(i).get(1))));
            }
            for (int i = 0; i < lists.size(); i += INTERVAL_COUNT) {

                lineChartLists.add(DateUtils.dateSimpleFormat(new Date(Long.valueOf(lists.get(i).get(0))), new SimpleDateFormat("HH:mm")));
            }
        }
        mLineDataSet = new LineDataSet(lineEntries, "");
        CustomChartUtils.configChart(mLineChart, lineChartLists, false);
        CustomChartUtils.initData(mLineChart, CustomChartUtils.getLineData(lineEntries, "sendy", Color.WHITE, Color.RED, false));
    }

    /**
     * 资金流向=================================================
     * 开始
     */

    /**
     * 获取最新资金净量
     */
    public void getKline() {
        String datas = Tools.getString(R.string.capital_kline_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = JsonUtils.getJsonObject(datas, "datas");
                JSONArray arrays = JsonUtils.getJsonArray(jsonObject, "array");
                List<List<String>> klines = new ArrayList<>();
                List<String> kline = new ArrayList<>();
                for (int i = 0; i < arrays.length(); i++) {
                    kline = new ArrayList<>();
                    for (int j = 0; j < arrays.optJSONArray(i).length(); j++) {
                        kline.add(arrays.optJSONArray(i).optString(j));
                    }
                    klines.add(kline);
                }
                if (isAdded())
                    createKLine(klines);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取最近24资金分布图
     */
    public void getFundDistribute() {

        String datas = Tools.getString(R.string.capital_fund_distribute_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = JsonUtils.getJsonObject(datas, "datas");
                CapitalFlow.FundDistribute.ObjBean objBean = JsonUtils.parseObject(jsonObject.optString("obj"), CapitalFlow.FundDistribute.ObjBean.class);
                if (isAdded())
                    createFundDistribute(objBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getBigRecord 获取最近24小时大单记录
     */
    public void getBigRecord() {

        String datas = Tools.getString(R.string.capital_big_record_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = JsonUtils.getJsonObject(datas, "datas");
                JSONArray arrays = JsonUtils.getJsonArray(jsonObject, "array");
                List<List<String>> bigRecords = new ArrayList<>();
                List<String> bigRecord = new ArrayList<>();
                for (int i = 0; i < arrays.length(); i++) {
                    bigRecord = new ArrayList<>();
                    for (int j = 0; j < arrays.optJSONArray(i).length(); j++) {
                        bigRecord.add(arrays.optJSONArray(i).optString(j));
                    }
                    bigRecords.add(bigRecord);
                }
                if (isAdded())
                    createBigRecord(bigRecords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取历史资金记录
     */
    public void getHistoryFunds() {

        String datas = Tools.getString(R.string.capital_history_funds_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = JsonUtils.getJsonObject(datas, "datas");
                JSONArray arrays = JsonUtils.getJsonArray(jsonObject, "array");
                List<List<String>> historyFunds = new ArrayList<>();
                List<String> historyFund = new ArrayList<>();
                for (int i = 0; i < arrays.length(); i++) {
                    historyFund = new ArrayList<>();
                    for (int j = 0; j < arrays.optJSONArray(i).length(); j++) {
                        historyFund.add(arrays.optJSONArray(i).optString(j));
                    }
                    historyFunds.add(historyFund);
                }
                if (isAdded())
                    createHistoryFunds(historyFunds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取近5天资金流向图
     */
    public void getNetfunds() {

        String datas = Tools.getString(R.string.capital_net_funds_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = JsonUtils.getJsonObject(datas, "datas");
                JSONArray arrays = JsonUtils.getJsonArray(jsonObject, "array");
                List<List<String>> netFunds = new ArrayList<>();
                List<String> netFund = new ArrayList<>();
                for (int i = 0; i < arrays.length(); i++) {
                    netFund = new ArrayList<>();
                    for (int j = 0; j < arrays.optJSONArray(i).length(); j++) {
                        netFund.add(arrays.optJSONArray(i).optString(j));
                    }
                    netFunds.add(netFund);
                }
                if (isAdded())
                    createNetfunds(netFunds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 资金流向=================================================
     * 结束
     */

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
