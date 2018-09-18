package com.dysen.common_library.tools.chart;

import android.content.Context;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.vip.zb.tool.chart
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/17 - 上午9:36
 * @info
 */
public class BarChartActivityOnlyUtils {

    protected static BarChart mChart;

    /**
     *
     * @param context
     * @param mCharts
     * @param xAxisValues     x_value
     * @param yAxisValues     y_value
     * @param label           标题
     */
    public static void init(Context context,
                            BarChart mCharts ,
                            List<String> xAxisValues,
                            List<Float> yAxisValues,
                            String label){

        mChart = mCharts;
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        //自定义x轴显示

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new CustomValueFormatter.MyIAxisValueFormatter(xAxisValues));
        xAxis.setLabelCount(xAxisValues.size() - 1, false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(0, false);
        leftAxis.setValueFormatter(new CustomValueFormatter.MyIAxisValueFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(new CustomValueFormatter.MyIAxisValueFormatter());
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            yVals1.add(new BarEntry(i, yAxisValues.get(i)));
            yVals2.add(new BarEntry(i, yAxisValues.get(i)+20));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {

            set1 = new BarDataSet(yVals1, "A");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData data = new BarData(set1);
            data.setValueFormatter(new LargeValueFormatter());

            mChart.setData(data);
        }
    }

}
