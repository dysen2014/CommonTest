package com.dysen.kdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.adapter.MarketAdapter;
import com.dysen.kdemo.adapter.MarketAdapterNew;
import com.dysen.kdemo.entity.CommonBean;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rcl_view)
    RecyclerView rclView;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    int mItemViewType;
    private MarketAdapterNew mAdapter;
    private List<CommonBean.BtcQc> tickerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }


    private void initViews() {
        tvTitle.setText(Tools.getString(R.string.app_name));
        mAdapter = new MarketAdapterNew(this);
        rclView.setLayoutManager(Tools.setManager1(LinearLayoutManager.VERTICAL));
        rclView.setAdapter(mAdapter);
        mAdapter.setDatas(initDatas());

        mAdapter.setOnItemClickListener(new MarketAdapterNew.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int timeInterval) {
                int timeInterval_market = 60;
                if (view == null) {
                    timeInterval_market = timeInterval;
                } else {
                    ++mItemViewType;
                    mAdapter.setmItemViewType(mItemViewType % 2);
                }

                indexMarketChart(position, timeInterval_market);
            }
        });

    }

    private List<CommonBean.BtcQc> initDatas() {
//        String btc_qc = FileUtils.readTextFromSDcard(this, "btc_qc.json");
        String btcQc = Tools.getString(R.string.btc_qc);
        if (!btcQc.isEmpty()) {
            try {
                JSONArray datas = JsonUtils.getJsonArray(new JSONObject(btcQc), "datas");
                tickerData = JsonUtils.parseList(datas.toString(), CommonBean.BtcQc.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tickerData;
    }

    private void indexMarketChart(int position, int timeInterval) {
        List<Integer> dataIndex = Arrays.asList(new Integer[]{R.string.btc_qc_chart_0, R.string.btc_qc_chart_1, R.string.btc_qc_chart_2, R.string.btc_qc_chart_3, R.string.btc_qc_chart_4});
        String btcQcChart = getString(dataIndex.get(position));
        if (!btcQcChart.isEmpty()) {
            try {
                JSONObject objects = JsonUtils.getJsonObject(btcQcChart, "datas");
                JSONArray datas = JsonUtils.getJsonArray(objects, "chartData");
                String[][] sChartData = new String[datas.length()][datas.optJSONArray(0).length()];
                for (int i = 0; i < datas.length(); i++) {

                    for (int j = 0; j < datas.optJSONArray(i).length(); j++) {

                        sChartData[i][j] = datas.optJSONArray(i).optString(j);
                    }
                }

                mAdapter.putChartMap(tickerData.get(0).getSymbol() + "_" + timeInterval, sChartData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
