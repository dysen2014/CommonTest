package com.dysen.kdemo.kline;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.MarketDepthData;
import com.dysen.kdemo.entity.TransRecord;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.StringUtils;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.views.ScrollViewForList;
import com.google.gson.internal.LinkedTreeMap;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @package com.vip.zb.fragment
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/17 - 下午6:10
 * @info
 */
public class KRecordlFragment extends Fragment {
    private static Context mContext;
    private final long BEFORE_TIME = 6 * 60 * 60 * 1000;//近6小时　
    public final static int INTERVAL_COUNT = 18;//K线 x轴  间隔
    private double max_asks_number = 0.00, max_bids_number = 0.00;

    private View mView;

    private static String exchangeType, currencyType;
    List<String> list = new ArrayList<String>();

    private ScrollViewForList listview2;

    private ArrayList mArrayListTransRecord = new ArrayList();

    private List<MarketDepthData> asks_list = new ArrayList<MarketDepthData>();
    private List<MarketDepthData> bids_list = new ArrayList<MarketDepthData>();

    private QuickAdapter<TransRecord> adapter2;

    public static KRecordlFragment newInstance(Context context) {
        mContext = context;
        Bundle args = new Bundle();

        KRecordlFragment fragment = new KRecordlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_fragment_k_record, null);
        initView();
        return mView;
    }

    public void init(String exchangeType, String currencyType) {
        this.exchangeType = exchangeType;
        this.currencyType = currencyType;
        getLastTrades();
    }

    public void initView() {

        listview2 = (ScrollViewForList) mView.findViewById(R.id.listview2);

        initAdapter();
        getLastTrades();
    }

    private void initAdapter() {

        adapter2 = new QuickAdapter<TransRecord>(mContext, R.layout.kline_transrecords_listitem, mArrayListTransRecord) {
            @Override
            protected void convert(BaseAdapterHelper helper, final TransRecord item) {
                BigDecimal bigDecimal = new BigDecimal(item.getDate() * 1000);
                String result = bigDecimal.toString();
                helper.setText(R.id.tv_time, StringUtils.dateFormat4(result));
                helper.setText(R.id.tv_price, item.getPrice());
                helper.setText(R.id.tv_num, item.getAmount());
                try {

                    TextView tv_time = (TextView) helper.getView().findViewById(R.id.tv_time);
                    TextView tv_price = (TextView) helper.getView().findViewById(R.id.tv_price);
                    TextView tv_num = (TextView) helper.getView().findViewById(R.id.tv_num);
                    if (item.getType().equals("buy")) {
                        tv_time.setTextColor(Tools.getColor(R.color.text_color_green));
                        tv_price.setTextColor(Tools.getColor(R.color.text_color_green));
                        tv_num.setTextColor(Tools.getColor(R.color.text_color_green));
                    } else {
                        tv_time.setTextColor(Tools.getColor(R.color.text_color_red));
                        tv_price.setTextColor(Tools.getColor(R.color.text_color_red));
                        tv_num.setTextColor(Tools.getColor(R.color.text_color_red));
                    }

                } catch (Exception e) {
                }
            }
        };

        listview2.setAdapter(adapter2);
    }


    //获取成交记录
    public void getLastTrades() {
        String datas = Tools.getString(R.string.record_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(datas);
                JSONArray jsonArray = jsonObject.optJSONArray("datas");
                List<TransRecord> transRecords = JsonUtils.parseList(jsonArray.toString(), TransRecord.class);
                if (isAdded())
                    loadData(transRecords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData(List<TransRecord> transRecords) {
        for (int i = 0; i < transRecords.size(); i++) {
            TransRecord data = transRecords.get(transRecords.size() - 1 - i);
//            TransRecord mTransRecord = new TransRecord();
//            mTransRecord.setAmount((String) data.get("amount"));
//            mTransRecord.setDate((Double) data.get("date"));
//            mTransRecord.setPrice((String) data.get("price"));
//            mTransRecord.setType((String) data.get("type"));
//            mTransRecord.setTrade_type((String) data.get("trade_type"));
//            mTransRecord.setTid((Double) data.get("tid"));
            mArrayListTransRecord.add(data);
        }
        adapter2.replaceAll(mArrayListTransRecord);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
