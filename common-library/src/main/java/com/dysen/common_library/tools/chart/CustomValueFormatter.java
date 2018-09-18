package com.dysen.common_library.tools.chart;

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
            if (Math.abs(entry.getY() - 0) > 0)
                return parseFlost(entry.getY());
            else
                return "0";
        }
    }

    public static class MyIAxisValueFormatter implements IAxisValueFormatter {
        List<String> mValues;
        int count = 0;

        public MyIAxisValueFormatter() {
        }

        public MyIAxisValueFormatter(List<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            if (mValues != null) {
                int i = (++count - 1) % mValues.size();

                return mValues.get(i);
            } else {
                count = 0;
                if (Math.abs(value - 0) > 0)
                    return parseFlost(value);
                else
                    return "0";
            }
        }
    }

    /**
     * 数据达到万位时 转换已万位显示
     * @param value
     * @return
     */
    public static String parseFlost(float value) {
        if (value > 10000.0)
            return new DecimalFormat(".00").format(value * 0.0001) + "万";
        else if (String.valueOf(value).contains("."))
            return new DecimalFormat("###,###,###,##0.00").format(value);
        else
            return value + "";
    }
}

