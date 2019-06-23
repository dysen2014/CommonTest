package com.dysen.chart;

import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.ColorInt;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @package com.vip.zb.tool.chart
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/11 - 下午2:55
 * @info 资金流向模块里的图表工具类
 */
public class CustomChartUtilsNew {
    static String TAG = "CustomChartUtils";
    private static LineChart lineChart;
    private static YAxis leftAxis;
    private static YAxis rightAxis;
    private static XAxis xAxis;
    public static String mNoDataText = "No chart data available.";
    /**
     * 颜色值
     */
    public static int color_gules  = Color.parseColor("#FF0000");//<color name="color_gules">#FF0000</color>       <!--红色-->
    public static int color_orange = Color.parseColor("#FF7F00");;//<color name="color_orange">#FF7F00</color>       <!--橙色-->
    public static int color_yellow = Color.parseColor("#FFFF00");//<color name="color_yellow">#FFFF00</color>       <!--黄色-->
    public static int color_green  = Color.parseColor("#00FF00");//<color name="color_green">#00FF00</color>       <!--绿色-->
    public static int color_cyan_blue = Color.parseColor("#00FFFF");//<color name="color_cyan_blue">#00FFFF</color>       <!--青色-
    public static int color_blue =   Color.parseColor("#0000FF");//<color name="color_blue">#0000FF</color>       <!--蓝色-->
    public static int color_violet = Color.parseColor("#8B00FF");//<color name="color_violet">#8B00FF</color>       <!--紫色-->
    public static int color_white = Color.parseColor("#ffffff");
    public static int color_red = Color.parseColor("#E91C41");
    public static int color_bg =  Color.parseColor("#243040");
    public static int color_kbg =  Color.parseColor("#172432");
    public static int color_buy1 = Color.parseColor("#FF4843");
    public static int color_buy2 = Color.parseColor("#FF8143");
    public static int color_buy3 = Color.parseColor("#FFB443");
    public static int color_sell1 = Color.parseColor("#00CA89");
    public static int color_sell2 = Color.parseColor("#00CAB1");
    public static int color_sell3 = Color.parseColor("#00A7CA");
    public static int color_transparent = Color.parseColor("#00000000");

    /**
     * 不显示无数据的提示
     *
     * @param mChart
     */
    public static void NotShowNoDataText(Chart mChart) {
        mChart.clear();
        mChart.notifyDataSetChanged();
        mChart.setNoDataText("你还没有记录数据");
        mChart.setNoDataTextColor(Color.WHITE);
        mChart.invalidate();
    }

    /**
     * 配置PieChart 基础设置
     *
     * @param mPieChart    图表
     * @param mLabels      x 轴标签
     * @param isShowLegend 是否显示图例
     */
    public static void configPieChart(PieChart mPieChart, final List<String> mLabels, String centerText, boolean isShowLegend) {
        mPieChart.setUsePercentValues(false);
        mPieChart.getDescription().setEnabled(false);
        //减速摩擦系数,值越大表示会缓慢停止
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterText(centerText);
        mPieChart.setCenterTextSize(16f);
        //给饼图设置左上右下的边距
        mPieChart.setExtraOffsets(40f, 20f, 40f, 20f);

        //这个方法为true就是环形图，为false就是饼图
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(color_bg);
        mPieChart.setCenterTextColor(color_white);
        mPieChart.setTransparentCircleColor(color_kbg);//设置半透明圆环的颜色
        mPieChart.setTransparentCircleAlpha(255);//设置半透明圆环的透明度
        mPieChart.setHoleRadius(45f);//设置中间环形的半径
        mPieChart.setTransparentCircleRadius(70f);//设置半透明圆环的半径,看着就有一种立体的感觉
        mPieChart.setDrawCenterText(true);//设置饼图中间是否可以添加文字
        mPieChart.setRotationAngle(0);//设置初始旋转角度
        mPieChart.setRotation(0f);

        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);//设置可以手动旋转
        //这个方法默认是true，设置为false之后，点击每一块不能向外突出
        mPieChart.setHighlightPerTapEnabled(true);

        mPieChart.animateXY(3000, 3000);

        Legend legend = mPieChart.getLegend();
        // 是否显示图例
        if (isShowLegend) {
            legend.setEnabled(true);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
        } else {
            legend.setEnabled(false);
        }
    }

    /**
     * 初始化数据
     *
     * @param pieChart
     * @param pieDataSets
     */
    public static void initData(PieChart pieChart, PieDataSet... pieDataSets) {
        PieData pieData = null;
        for (PieDataSet pieDataSet : pieDataSets) {
            initDataSet(pieDataSet);
            pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return super.getFormattedValue(value);
                }
            });
            pieData.setValueTextSize(10f);
//            pieData.setValueTextColors(Arrays.asList(new Integer[]{color_red, color_green}));
            pieData.setValueTextColors(Arrays.asList(new Integer[]{color_buy1, color_buy2, color_buy3, color_sell1, color_sell2, color_sell3}));
            pieData.notifyDataChanged();
            pieData.setDrawValues(false);//是否在图上显示数值
            // undo all highlights
            pieChart.setData(pieData);
            pieChart.highlightValues(null);
            List<PieEntry> values = pieDataSet.getValues();
            for (int i = 0; i < values.size(); i++) {

                if (values.get(0).getValue() < values.get(1).getValue()) {
                    if (values.get(0).getValue() < values.get(1).getValue() / 2) {
                        pieChart.setRotationAngle(180);
                    } else
                        pieChart.setRotationAngle(90);
                }else
                        pieChart.setRotationAngle(30);
            }
        }
        //通知数据已经改变
        pieChart.notifyDataSetChanged();

        pieChart.invalidate();
    }

    private static void initDataSet(PieDataSet pieDataSet) {
        pieDataSet.setSliceSpace(5f);//设置饼图模块之间的距离
        pieDataSet.setSelectionShift(5f);//设置饼图模块选中之后弹出去的长度
        //设置饼图的颜色
        pieDataSet.setColors(Arrays.asList(new Integer[]{color_buy1, color_buy2, color_buy3, color_sell1, color_sell2, color_sell3}));
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        pieDataSet.setValueLinePart1Length(0.8f);//      当值位置为外边线时，表示线的前半段长度。
        pieDataSet.setValueLinePart2Length(0.1f);//      当值位置为外边线时，表示线的后半段长度。
        pieDataSet.setValueTextColors(Arrays.asList(new Integer[]{color_buy1, color_buy2, color_buy3, color_sell1, color_sell2, color_sell3}));
//        pieDataSet.setValueLineColor(Color.parseColor("#a1a1a1"));
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setAutomaticallyDisableSliceSpacing(false);
        //当值显示在界面外面的时候是否允许改变量行长度
        pieDataSet.setValueLineVariableLength(true);
//        pieDataSet.setValueLineColor(color_red);
        //设置线的宽度
        pieDataSet.setValueLineWidth(0.5f);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//设置将Y轴的值拿出去
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//设置项X值拿出去
    }

    /**
     * 配置Chart 基础设置
     *
     * @param mChart       图表
     * @param mLabels      x 轴标签
     * @param isShowLegend 是否显示图例
     */
    public static void configChart(LineChart mChart, final List<String> mLabels, boolean isShowLegend) {

        lineChart = mChart;
        leftAxis = mChart.getAxisLeft();
        rightAxis = mChart.getAxisRight();
        xAxis = mChart.getXAxis();

        mChart.setDrawGridBackground(false);
        //是否显示边界
        mChart.setDrawBorders(false);
        mChart.setScaleEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setNoDataText("");
        mChart.setEnabled(false);

        // 不显示描述数据
        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);

        Legend legend = mChart.getLegend();
        // 是否显示图例
        if (isShowLegend) {
            legend.setEnabled(true);
            legend.setTextColor(Color.WHITE);
            legend.setForm(Legend.LegendForm.LINE);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setYEntrySpace(20f);
            //图例的大小
            legend.setFormSize(7f);
            // 图例描述文字大小
            legend.setTextSize(10);
            legend.setXEntrySpace(20f);

        } else {
            legend.setEnabled(false);
        }

        // 是否显示x轴线
        xAxis.setDrawAxisLine(false);
        // 设置x轴线的颜色
        xAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        // 是否绘制x方向网格线
        xAxis.setDrawGridLines(false);
        //x方向网格线的颜色
        xAxis.setGridColor(color_blue);
        xAxis.setAxisLineColor(color_violet);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置x轴文字的大小
        xAxis.setTextSize(10);
        // 引用标签
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        xAxis.setLabelCount(mLabels.size(), true);
        // 设置x轴文字颜色
        xAxis.setTextColor(color_white);
        // 设置x轴每最小刻度 interval
        xAxis.setGranularity(1f);
        //如果设置为true，则在绘制时会避免“剪掉”在x轴上的图表或屏幕边缘的第一个和最后一个坐标轴标签项。
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);
        // 设置y轴数据的位置
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setYOffset(-6f);
        // 不从y轴发出横向直线
        rightAxis.setDrawGridLines(true);
        rightAxis.setGridColor(color_bg);
        // 是否显示y轴坐标线
        rightAxis.setDrawZeroLine(false);
        rightAxis.setZeroLineColor(color_bg);
        // 设置y轴的文字颜色
        rightAxis.setTextColor(color_white);
        rightAxis.setAxisLineColor(color_transparent);
        rightAxis.setDrawZeroLine(true);
        // 设置y轴文字的大小
        rightAxis.setTextSize(10);
        rightAxis.setSpaceTop(10f);
        rightAxis.setSpaceBottom(10f);
        rightAxis.setLabelCount(3, true);
        rightAxis.setEnabled(true);
        rightAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        leftAxis.setEnabled(false);
        // 不显示x, y轴
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawLimitLinesBehindData(true);
        Matrix matrix = new Matrix();
        // 根据数据量来确定 x轴缩放大倍
        if (mLabels.size() <= 10) {
            matrix.postScale(1.0f, 1.0f);
        } else if (mLabels.size() <= 15) {
            matrix.postScale(1.5f, 1.0f);
        } else if (mLabels.size() <= 20) {
            matrix.postScale(2.0f, 1.0f);
        } else {
            matrix.postScale(3.0f, 1.0f);
        }

        // 在图表动画显示之前进行缩放
        mChart.getViewPortHandler().refresh(matrix, mChart, false);
        // x轴执行动画
        mChart.animateXY(3000, 3000);
        mChart.setExtraOffsets(0, 10, 0, 10);
    }

    /**
     * 初始化数据
     *
     * @param lineChart
     * @param lineDataSets
     */
    public static void initData(LineChart lineChart, LineDataSet... lineDataSets) {
        LineData lineData = null;
        for (LineDataSet lineDataSet : lineDataSets) {
            initDataSet(lineDataSet);
            lineData = new LineData(lineDataSets);
            lineData.notifyDataChanged();
            lineData.setDrawValues(false);
            lineChart.getAxisRight().setAxisMaximum(lineData.getYMax());
            lineChart.getAxisRight().setAxisMinimum(lineData.getYMin());
            if (Math.abs(lineData.getYMax() - lineData.getYMin()) > 0) {
                lineChart.getAxisRight().resetAxisMinimum();
                lineChart.getAxisLeft().resetAxisMinimum();

                lineChart.getAxisRight().setAxisMaximum(lineData.getYMax());
                lineChart.getAxisRight().setAxisMinimum(lineData.getYMin());
                lineChart.getAxisLeft().setAxisMaximum(lineData.getYMax());
                lineChart.getAxisLeft().setAxisMinimum(lineData.getYMin());


            } else {
                lineChart.getAxisRight().setAxisMinimum(0f);
                lineChart.getAxisLeft().setAxisMinimum(0f);
            }
            LimitLine limitLine = new LimitLine(0f, "0.00");
            limitLine.setLineColor(color_bg);
            limitLine.setTextSize(10f);
            limitLine.setTextColor(color_white);
            lineChart.getAxisRight().addLimitLine(limitLine);
            lineChart.setData(lineData);
        }
        //通知数据已经改变
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
//        lineChart.setVisibleXRangeMaximum(5);
        //移到某个位置
//        chart.moveViewToX(lineData.getEntryCount() - 5);
        lineChart.invalidate();
    }

    private static void initDataSet(LineDataSet lineDataSet) {

        lineDataSet.setLineWidth(0.5f);
        lineDataSet.setCircleRadius(1.5f);
        // 设置平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        // 不显示坐标点的小圆
        lineDataSet.setDrawCircles(false);
        // 不显示定位线
        lineDataSet.setHighlightEnabled(false);
    }

    /**
     * 获取LineDataSet
     *
     * @param entries
     * @param label
     * @param textColor
     * @param lineColor
     * @return
     */
    public static LineDataSet getLineData(List<Entry> entries, String label, @ColorInt int textColor, @ColorInt int lineColor, boolean isFill) {

        LineDataSet dataSet = new LineDataSet(entries, label);
        // 设置曲线的颜色
        dataSet.setColor(lineColor);
        //数值文字颜色
        dataSet.setValueTextColor(textColor);
        // 模式为贝塞尔曲线
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        // 是否绘制数据值
        dataSet.setDrawValues(false);
        // 是否绘制圆点
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(false);
        // 这里有一个坑，当我们想隐藏掉高亮线的时候，MarkerView 跟着不见了
        // 因此只有将它设置成透明色
        dataSet.setHighlightEnabled(true);// 隐藏点击时候的高亮线
        //设置高亮线为透明色
        dataSet.setHighLightColor(Color.TRANSPARENT);

        if (isFill) {
            //是否设置填充曲线到x轴之间的区域
            dataSet.setDrawFilled(true);
            // 填充颜色
            dataSet.setFillColor(lineColor);
        }
        //设置圆点的颜色
        dataSet.setCircleColor(lineColor);
        // 设置圆点半径
        dataSet.setCircleRadius(3.5f);
        // 设置线的宽度
        dataSet.setLineWidth(1f);
        return dataSet;
    }

    /**
     * 获取barDataSet
     *
     * @param entries
     * @param label
     * @param textColor
     * @param lineColor
     * @return
     */
    public static BarDataSet getBarDataSet(List<BarEntry> entries, String label, @ColorInt int textColor, @ColorInt int lineColor) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setBarBorderWidth(5);
        dataSet.setBarShadowColor(lineColor);
        dataSet.setValueTextColor(textColor);
        dataSet.setDrawValues(false);
        return dataSet;
    }

    /**
     * @param barChart
     * @param xAxisValues x_value
     * @param yAxisValues y_value
     * @param label       标题
     */
    public static void init(BarChart barChart, List<String> xAxisValues, List<Float> yAxisValues, String label) {

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);//不显示与图表网格线
        barChart.setDrawBorders(false);//不显示图表边框

        //自定义x轴显示
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        xAxis.setTextColor(color_white);
        xAxis.setAxisLineColor(color_transparent);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(0, false);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        // 设置x轴的LimitLine
        LimitLine yLimitLine = new LimitLine(0f, "");
        yLimitLine.setLineColor(color_kbg);
        yLimitLine.setTextColor(Color.RED);
        leftAxis.addLimitLine(yLimitLine);
        leftAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        leftAxis.setTextColor(color_white);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            yVals1.add(new BarEntry(i, yAxisValues.get(i)));
            yVals2.add(new BarEntry(i, yAxisValues.get(i) + 20));
        }

        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {

            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData data = new BarData(set1);
            data.setValueFormatter(new LargeValueFormatter());

            barChart.setData(data);
        }
    }

    /**
     * 配置柱状图基础设置
     *
     * @param barChart
     * @param xLabels
     */
    public static void configBarChart(final BarChart barChart, final List<String> xLabels) {
        barChart.getDescription().setEnabled(false);//设置描述
        barChart.setPinchZoom(false);//设置按比例放缩柱状图
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setScaleEnabled(false);
        barChart.setDragEnabled(true);
        barChart.setNoDataText(""); // 没有数据时的提示文案
        barChart.setDrawGridBackground(false);//不显示与图表网格线
        barChart.setDrawBorders(false);//不显示图表边框
        XAxis xAxis = barChart.getXAxis();//获取x轴
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setTextSize(12);//设置标签字体大小
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);             //设置label在底下
        xAxis.setTextColor(color_white);
        xAxis.setAxisLineColor(color_transparent);
        xAxis.setLabelCount(xLabels.size(), false);//设置标签显示的个数 ,第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        /**
         * 设置X轴文字顺时针旋转角度
         */
        xAxis.setLabelRotationAngle(0);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        YAxis leftAxis = barChart.getAxisLeft();//获取左侧y轴
        YAxis rightAxis = barChart.getAxisRight();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setDrawGridLines(false);//不显示Y轴网格线
        leftAxis.setDrawLabels(false);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(false);//禁止绘制y轴
        leftAxis.setLabelCount(xLabels.size(), false);                      //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        leftAxis.setAxisLineColor(Color.parseColor("#4cffffff"));
        leftAxis.setTextColor(color_white);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });
        // 设置x轴的LimitLine
        LimitLine yLimitLine = new LimitLine(0f, "");
        yLimitLine.setLineColor(color_kbg);
        yLimitLine.setTextColor(Color.RED);
        // 获得左侧侧坐标轴
        leftAxis.addLimitLine(yLimitLine);
        leftAxis.setEnabled(true);

        barChart.getAxisRight().setEnabled(false);//禁用右侧y轴
        barChart.getLegend().setEnabled(false);

        Matrix matrix = new Matrix();
        // 根据数据量来确定 x轴缩放大倍
        if (xLabels.size() <= 10) {
            matrix.postScale(1.0f, 1.0f);
        } else if (xLabels.size() <= 15) {
            matrix.postScale(1.5f, 1.0f);
        } else if (xLabels.size() <= 20) {
            matrix.postScale(2.0f, 1.0f);
        } else {
            matrix.postScale(3.0f, 1.0f);
        }
        barChart.getViewPortHandler().refresh(matrix, barChart, false);
        barChart.setExtraOffsets(0, 10, 0, 10);
        barChart.setFitBars(true);//使两侧的柱图完全显示
        barChart.animateXY(3000, 3000);//数据显示动画，从左往右依次显示
    }

    public static void initBarChart(final BarChart barChart, int chartType, BarDataSet... barDataSets) {
        List<Integer> colors1 = Arrays.asList(new Integer[]{color_buy1, color_buy2, color_buy3});
        List<Integer> colors2 = Arrays.asList(new Integer[]{color_sell1, color_sell2, color_sell3});
        BarData barData = null;

        for (BarDataSet barDataSet : barDataSets) {
            if (chartType == 0)
                barDataSet.setColors(colors1);
            else
                barDataSet.setColors(colors2);

            initDataSet(barDataSet);
            barData = new BarData(barDataSet);
            barData.setValueTextSize(10f);
            // 设置bar的宽度，但是点很多少的时候好像没作用，会拉得很宽
            barData.setBarWidth(0.6f);
            // 设置value值 颜色
            barData.setValueTextColor(Color.WHITE);
            barData.setHighlightEnabled(true);
            //设置y轴显示的标签
            barData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return super.getFormattedValue(value);
                }
            });

            barChart.getAxisRight().setAxisMaximum(barData.getYMax());
            barChart.getAxisRight().setAxisMinimum(barData.getYMin());
            barChart.setData(barData);
        }
        barChart.invalidate();
    }

    private static void initDataSet(BarDataSet barDataSet) {
        barDataSet.setValueTextColor(Color.WHITE);
    }

    /**
     * 初始化柱状图图表数据
     *
     * @param chart
     * @param entries
     * @param title
     * @param barColor
     */
    public static void initBarChart(BarChart chart, List<BarEntry> entries, String title, @ColorInt int barColor) {
        BarDataSet set1 = null, set2 = null;
        List<BarEntry> entrie1 = new ArrayList<>();
        List<BarEntry> entrie2 = new ArrayList<>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        for (BarEntry barEntry : entries) {
            String sVal = barEntry.getY() + "";
            if (Character.isDigit(sVal.charAt(0))) {
                entrie1.add(new BarEntry(barEntry.getX(), barEntry.getY()));
                set1 = new BarDataSet(entrie1, title);
                set1.setColor(color_red);
                set1.setValueTextColor(Color.WHITE);
                dataSets.add(set1);
            } else {
                entrie2.add(new BarEntry(barEntry.getX(), barEntry.getY()));
                set2 = new BarDataSet(entrie2, title);
                set2.setColor(color_green);
                set2.setValueTextColor(Color.WHITE);
                dataSets.add(set2);
            }
        }

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        // 设置bar的宽度，但是点很多少的时候好像没作用，会拉得很宽
        data.setBarWidth(0.7f);
        // 设置value值 颜色
        data.setValueTextColor(Color.WHITE);
        data.setHighlightEnabled(true);
        //设置y轴显示的标签
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value);
            }
        });

            chart.getAxisLeft().setAxisMinimum(data.getYMin());
            chart.getAxisLeft().setAxisMaximum(data.getYMax());

        chart.getXAxis().setYOffset(20f);
        chart.setExtraOffsets(0f, 30f, 0f, 20f);

        // 设置x轴的LimitLine
        LimitLine yLimitLine = new LimitLine(0f, "");
        yLimitLine.setLineColor(color_kbg);
        yLimitLine.setTextColor(Color.RED);
        // 获得左侧侧坐标轴
        chart.getAxisLeft().addLimitLine(yLimitLine);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
