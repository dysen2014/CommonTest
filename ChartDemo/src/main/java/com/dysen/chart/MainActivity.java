package com.dysen.chart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initPieChart();
    }

    private void initPieChart() {

        List<PieEntry> mPieEntrys = new ArrayList<>();
        List<String> mNames = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            mPieEntrys.add(new PieEntry(new Random().nextFloat()*55, "X val  "+i));
            mNames.add("Y val  "+i);
        }

        CustomChartUtilsNew.configPieChart(pieChart, mNames, "piechart test", false);
        CustomChartUtilsNew.initData(pieChart, new PieDataSet(mPieEntrys, ""));
    }

    private void initView() {

    }
}
