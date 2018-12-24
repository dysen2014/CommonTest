package com.dysen.kdemo.utils.chart;

import com.dysen.kdemo.kline.KCapitalFragment;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @package com.vip.zb.tool.chart
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/12 - 下午5:06
 * @info
 */
public class CustomValueFormatter {


    public static class MyIValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (Math.abs(entry.getY() - 0) > 0) {
                return parseFlost(entry.getY());
            } else
                return "0.00";
        }
    }

    public static class MyIAxisValueFormatter implements IAxisValueFormatter {
        List<String> mValues;
        int count = 0;

        public MyIAxisValueFormatter() {
            mValues = null;
        }

        public MyIAxisValueFormatter(List<String> values) {
            this.mValues = values;
            count = 0;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (mValues != null) {
                int i = (int) value;
                if (i < 0) i = 0;
                while (i >= mValues.size()) {
                    i = i / (KCapitalFragment.INTERVAL_COUNT);//下标是从零开始的
                }
                return mValues.get(i % mValues.size());
            } else {
                return parseFlost(value);
            }
        }
    }

    /**
     * 数据达到万位时 转换已万位显示
     *
     * @param value
     * @return
     */
    public static String parseFlost(float value) {
        if (value >= 10000.0 || value <= -10000.0)
            if (value >= 10000*10000.0 || value <= -10000*10000.0)
                return new DecimalFormat("###,###,###,##0.00").format(value * 0.00000001) + "亿";
            else if (value >= 1000*10000.0 || value <= -1000*10000.0)
                return new DecimalFormat("###,###,###,##0.00").format(value * 0.0000001) + "千万";
            else
                return new DecimalFormat("###,###,###,##0.00").format(value * 0.0001) + "万";
        else if (String.valueOf(value).contains("."))
            if (String.valueOf(value).equalsIgnoreCase("0.0"))
                return "0.00";
            else
                return new DecimalFormat("###,###,###,##0.00").format(value);
        else
            return value + "";
    }
}

