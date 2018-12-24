package com.dysen.kdemo.kline;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.MarketDepth;
import com.dysen.kdemo.entity.MarketDepthData;
import com.dysen.kdemo.utils.ColorUtils;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.Tools;
import com.dysen.kdemo.utils.Utils;
import com.dysen.kdemo.views.DepthView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dysen.kdemo.utils.Tools.toDouble;
import static com.dysen.kdemo.views.DepthView.deFormatNew;

/**
 * @package com.vip.zb.fragment
 * @email dy.sen@qq.com
 * created by dysen on 2018/10/17 - 下午6:10
 * @info
 */
public class KAnalyzeFragment extends Fragment {
    private static Context mContext;
    private double max_asks_number = 0.00, max_bids_number = 0.00;

    private View mView;

    private static String exchangeType, currencyType;
    List<String> list = new ArrayList<String>();

    private boolean isAskList = true;
    private int numberBixDian = 8, exchangeBixDian = 8;//小数点后多少位

    private DepthView depthview;
    private TextView tv_show_buy, tv_show_sell, tv_vbuy, tv_vsell;

    private ListView listview1;

    private List<MarketDepthData> asks_list = new ArrayList<MarketDepthData>();
    private List<MarketDepthData> bids_list = new ArrayList<MarketDepthData>();

    private QuickAdapter<MarketDepthData> adapter1;

    public static KAnalyzeFragment newInstance(Context context) {
        mContext = context;
        Bundle args = new Bundle();

        KAnalyzeFragment fragment = new KAnalyzeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_fragment_k_analyze, null);
        initView();
        return mView;
    }

    public void init(String exchangeType, String currencyType) {
        this.exchangeType = exchangeType;
        this.currencyType = currencyType;
        marketDepth();
    }

    public void initView() {

        listview1 = (ListView) mView.findViewById(R.id.listview1);
        tv_show_buy = (TextView) mView.findViewById(R.id.tv_show_buy);
        tv_show_sell = (TextView) mView.findViewById(R.id.tv_show_sell);

        tv_vbuy = (TextView) mView.findViewById(R.id.tv_vbuy);
        tv_vsell = (TextView) mView.findViewById(R.id.tv_vsell);
        depthview = (DepthView) mView.findViewById(R.id.depthview);
        marketDepth();
        initAdapter();
    }

    private void setTextBuyOrSellScale() {

        double sellw = (max_asks_number / (max_asks_number + max_bids_number));
        double buyw = (max_bids_number / (max_asks_number + max_bids_number));

        int width = Utils.getRealScreenSize(mContext).x;


        String sell = deFormat1(sellw + "", 4);
        String buy = deFormat1(buyw + "", 4);

        int sell_width = (int) (toDouble(sell) * width);
        int buy_width = (int) (toDouble(buy) * width);


        // tv_show_sell.setText(deFormat1(toDouble(sell)*100+"",4)+"%");
        // tv_show_buy.setText(deFormat1(toDouble(buy)*100+"",4)+"%");
        tv_vsell.setText(deFormat1(toDouble(sell) * 100 + "", 4) + "%");
        tv_vbuy.setText(deFormat1(toDouble(buy) * 100 + "", 4) + "%");

        tv_show_sell.setWidth(sell_width);
        tv_show_buy.setWidth(buy_width);

        if (toDouble(sell) < 0.1) {
            tv_show_sell.setWidth(75);
            tv_show_buy.setWidth(width - 75);
        }

        if (toDouble(buy) < 0.1) {
            tv_show_sell.setWidth(width - 75);
            tv_show_buy.setWidth(75);
        }
    }

    public String deFormat1(String str, int type) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            String str_ws = "";
            if (type >= 0) {
                String str_ws_ls = "0.#";
                for (int n = 1; type > 1 && n < type; n++) {
                    str_ws_ls = str_ws_ls + "#";
                }
                str_ws = str_ws_ls;
            } else {
                String str_ws_ls = "0.00";
                type = type * -1;
                for (int n = 2; type > 2 && n < type; n++) {
                    str_ws_ls = str_ws_ls + "#";
                }
                str_ws = str_ws_ls;
            }
            DecimalFormat df_ls = new DecimalFormat(str_ws);
            str = df_ls.format(bigDecimal.setScale(type, BigDecimal.ROUND_HALF_UP).doubleValue());
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    private void initAdapter() {
        adapter1 = new QuickAdapter<MarketDepthData>(mContext, R.layout.listview_buyorsell_item, asks_list) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MarketDepthData item) {
                final int position = helper.getPosition();

                if (isAskList) {
                    helper.setText(R.id.tv_sell_number, deFormatNew(item.getNumber(), numberBixDian));
                    helper.setText(R.id.tv_sell_price, deFormatNew(item.getPrice(), exchangeBixDian));

                    if (bids_list.size() > 0 && bids_list.size() > position) {
                        MarketDepthData itemsell = bids_list.get(position);
                        helper.setText(R.id.tv_buy_number, deFormatNew(itemsell.getNumber(), numberBixDian));
                        helper.setText(R.id.tv_buy_price, deFormatNew(itemsell.getPrice(), exchangeBixDian));
                    } else {
                        helper.setText(R.id.tv_buy_number, "");
                        helper.setText(R.id.tv_buy_price, "");
                    }
                } else {
                    helper.setText(R.id.tv_buy_number, deFormatNew(item.getNumber(), numberBixDian));
                    helper.setText(R.id.tv_buy_price, deFormatNew(item.getPrice(), exchangeBixDian));
                    if (asks_list.size() > 0 && asks_list.size() > position) {
                        MarketDepthData itemsell = asks_list.get(position);
                        helper.setText(R.id.tv_sell_number, deFormatNew(itemsell.getNumber(), numberBixDian));
                        helper.setText(R.id.tv_sell_price, deFormatNew(itemsell.getPrice(), exchangeBixDian));
                    } else {
                        helper.setText(R.id.tv_sell_number, "");
                        helper.setText(R.id.tv_sell_price, "");
                    }
                }

                try {
                    TextView tv_buy_price = (TextView) helper.getView().findViewById(R.id.tv_buy_price);
                    tv_buy_price.setTextColor(Tools.getColor(R.color.text_color_red));

                    TextView tv_sell_price = (TextView) helper.getView().findViewById(R.id.tv_sell_price);
                    tv_sell_price.setTextColor(Tools.getColor(R.color.text_color_green));

                } catch (Exception e) {
                }
            }
        };

        listview1.setAdapter(adapter1);
    }

    //获取行情盘口深度
    public void marketDepth() {
        String datas = Tools.getString(R.string.analyze_data);
        if (!datas.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(datas);
                MarketDepth marketDepth = JsonUtils.parseObject(jsonObject.optString("datas"), MarketDepth.class);
                if (isAdded())
                loadData(marketDepth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData(MarketDepth marketDepth) {
        if (true) {
            depthview.setGear(50);
            depthview.setBgColor(ColorUtils.getHeaderColor(mContext));
            depthview.setTextColor(ColorUtils.getWhite(mContext));
            depthview.setMarketDepthData(marketDepth);
            depthview.postInvalidate();

            try {
                List<List<String>> asks = marketDepth.getAsks();
                List<List<String>> bids = marketDepth.getBids();
                asks_list.clear();
                max_asks_number = 0.00;
                if (asks != null && asks.size() > 0) {
                    for (int n = 0; asks.size() > n && n < 20; n++) {
                        //  String str[] = asks[asks.length - n - 1];
                        List<String> str = asks.get(n);
                        MarketDepthData marketDepthData = new MarketDepthData();
                        //   marketDepthData.setId((asks.length - n) + "");
                        marketDepthData.setId(n + 1 + "");
                        marketDepthData.setPrice(str.get(0));
                        marketDepthData.setNumber(str.get(1));
                        asks_list.add(marketDepthData);
                        max_asks_number = max_asks_number + toDouble(str.get(1));

                    }
                }
                bids_list.clear();
                max_bids_number = 0.00;
                if (bids != null && bids.size() > 0) {
                    for (int n = 0; bids.size() > n && n < 20; n++) {
                        // String str[] = bids[bids.length - n-1];
                        List<String> str = bids.get(n);
                        MarketDepthData marketDepthData = new MarketDepthData();
                        //  marketDepthData.setId((bids.length -n) + "");
                        marketDepthData.setId((n + 1) + "");
                        marketDepthData.setPrice(str.get(0));
                        marketDepthData.setNumber(str.get(1));
                        bids_list.add(marketDepthData);
                        max_bids_number = max_bids_number + toDouble(str.get(1));
                    }
                }
                if (asks.size() > bids.size()) {
                    isAskList = true;
                    adapter1.replaceAll(asks_list);
                } else {
                    isAskList = false;
                    adapter1.replaceAll(bids_list);
                }
                adapter1.notifyDataSetChanged();
                //   listview1.setSelection(bids_list.size());


                setTextBuyOrSellScale();
            } catch (Exception e) {

            }
        } else {
            try {
                asks_list.clear();
                bids_list.clear();
                adapter1.replaceAll(bids_list);
                adapter1.notifyDataSetChanged();

                tv_vsell.setText("");
                tv_vbuy.setText("");

                depthview.clearData();
                depthview.postInvalidate();

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
