package com.dysen.kdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 日期处理工具
 * BASE_DATE_FORMAT     yyyy-MM-dd HH:mm:ss
 * NORMAL_DATE_FORMAT   yyyy-MM-dd
 * SHORT_DATE_FORMAT    yyyyMMdd
 * FULL_DATE_FORMAT     yyyyMMddHHmmss
 * Created by Luis on 2018/3/13.
 */

public class DateUtils extends android.text.format.DateUtils{

    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    public static SimpleDateFormat BASE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    /**
     * yyyy-MM-dd字符串
     */
    public static SimpleDateFormat NORMAL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    /**
     * yyyyMMdd字符串
     */
    public static SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    /**
     * yyyyMMddHHmmss字符串
     */
    public static SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    /**
     * HH:mm:ss字符串
     */
    public static final String DEFAULT_FORMAT_TIME = "HH:mm:ss";
    public static final String HHmm = "HH:mm";
    public static final String MMdd = "MM-dd";

    public static String getHHmm(Date date){
        return DateUtils.dateSimpleFormat(date, new SimpleDateFormat(HHmm));
    }
public static String getMMdd(Date date){
        return DateUtils.dateSimpleFormat(date, new SimpleDateFormat(MMdd));
    }

    /**
     * 获取当前时间的前或后（年 月 日 时 分 秒）
     *
     * @param date     当前时间
     * @param dateType 年0   月1  日2  时3  分4  秒5
     * @param number   前或后个数
     * @return
     */
    public static Date getBeforeOrAfter(Date date, int dateType, int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (dateType) {
            case 0:
                calendar.add(Calendar.YEAR, number);
                break;
            case 1:
                calendar.add(Calendar.MONTH, number);
                break;
            case 2:
                calendar.add(Calendar.DAY_OF_YEAR, number);
                break;
            case 3:
                calendar.add(Calendar.HOUR, number);
                break;
            case 4:
                calendar.add(Calendar.MINUTE, number);
                break;
            case 5:
                calendar.add(Calendar.SECOND, number);
                break;
        }
//        String dateStr = SHORT_DATE_FORMAT.format(calendar.getTime());
        return calendar.getTime();
    }

    /**
     * 获取一个月前的日期
     *
     * @param date     传入的日期
     * @param monthSum 前或后的月数
     * @return
     */
    public static String getMonthBeforeOrAfter(Date date, int monthSum) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthSum);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    public static String getMonthBeforeOrAfter(int monthSum) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, monthSum);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    /**
     * 判断日期是否为今天
     *
     * @param dateString 日期字符串, 格式：yyyy-MM-dd HH:mm:ss
     * @return 如果是同一天则返回true, 否则返回false
     */
    public static boolean isToday(String dateString) {
        try {
            Date date = BASE_DATE_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Calendar calendar1 = Calendar.getInstance();
            return (calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
                    && (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isToday(long timeInMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMills);
        Calendar calendar1 = Calendar.getInstance();
        return (calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
                && (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 判断日期是否在当前时间之前
     *
     * @param dateString 日期字符串, 格式：yyyy-MM-dd HH:mm:ss
     * @return 如果在当前之前，返回true，否则返回false
     */
    public static boolean isBeforeNow(String dateString) {
        try {
            Date date = BASE_DATE_FORMAT.parse(dateString);
            return System.currentTimeMillis() > date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断日期是否在当前时间之后
     *
     * @param dateString 日期字符串, 格式：yyyy-MM-dd HH:mm:ss
     * @return 如果在当前之前，返回true，否则返回false
     */
    public static boolean isAfterNow(String dateString) {
        try {
            Date date = BASE_DATE_FORMAT.parse(dateString);
            return System.currentTimeMillis() < date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDateString(long timestamp) {
        return BASE_DATE_FORMAT.format(new Date(timestamp));
    }

    public static String getNormalDateString(long timestamp) {
        return NORMAL_DATE_FORMAT.format(new Date(timestamp));
    }

    public static String getShortDateString(long timestamp) {
        return SHORT_DATE_FORMAT.format(new Date(timestamp));
    }

    public static String getFullDateString(long timestamp) {
        return FULL_DATE_FORMAT.format(new Date(timestamp));
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 计算时间方法
     *
     * @param beginDayStr 开始时间
     * @param endDayStr   结束时间
     * @return
     */
    public static long subDay(String beginDayStr, String endDayStr) {
        Date beginDate = null;
        Date endDate = null;

        long day = 0;
        try {
            beginDate = sdf.parse(beginDayStr);
            endDate = sdf.parse(endDayStr);

            // 计算天数
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 当天入住、当天离开算1天时间
        if (day == 0) {
            day = 1;
        }

        return day;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayStr() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMddhhmmss");

        // 订单编号
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getToday(SimpleDateFormat format) {

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format.format(today);
        return todayStr;
    }

    /**
     * 获取当天日期
     *
     * @param pattern "yyyy-mm-dd"
     * @return
     */
    public static String getToday(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format.format(today);
        return todayStr;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMdd");

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    public static String getTodayF() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }


    /**
     * 获取当前时分秒
     *
     * @return
     */
    public static Map<String, String> getDatetimes() {
        SimpleDateFormat f_yyyy = new SimpleDateFormat("yyyy");
        SimpleDateFormat f_MM = new SimpleDateFormat("MM");
        SimpleDateFormat f_dd = new SimpleDateFormat("dd");
        SimpleDateFormat f_HH = new SimpleDateFormat("HH");
        SimpleDateFormat f_mm = new SimpleDateFormat("mm");
        SimpleDateFormat f_ss = new SimpleDateFormat("ss");
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String d_yyyy = f_yyyy.format(today);    // 年
        String d_MM = f_MM.format(today);        // 月
        String d_dd = f_dd.format(today);        // 日
        String t_HH = f_HH.format(today);        // 时
        String t_mm = f_mm.format(today);        // 分
        String t_ss = f_ss.format(today);        // 秒
        Map<String, String> datetimes = new HashMap<String, String>();
        datetimes.put("yyyy", d_yyyy);
        datetimes.put("MM", d_MM);
        datetimes.put("dd", d_dd);
        datetimes.put("HH", t_HH);
        datetimes.put("mm", t_mm);
        datetimes.put("ss", t_ss);

        return datetimes;
    }

    public static Map<String, String> getDateMapByStringValue(String strData) {
        SimpleDateFormat f_yyyy = new SimpleDateFormat("yyyy");
        SimpleDateFormat f_MM = new SimpleDateFormat("MM");
        SimpleDateFormat f_dd = new SimpleDateFormat("dd");
        SimpleDateFormat f_HH = new SimpleDateFormat("HH");
        SimpleDateFormat f_mm = new SimpleDateFormat("mm");
        SimpleDateFormat f_ss = new SimpleDateFormat("ss");


        Date today = strToDate(strData);
        String d_yyyy = f_yyyy.format(today);    // 年
        String d_MM = f_MM.format(today);        // 月
        String d_dd = f_dd.format(today);        // 日
        String t_HH = f_HH.format(today);        // 时
        String t_mm = f_mm.format(today);        // 分
        String t_ss = f_ss.format(today);        // 秒
        Map<String, String> datetimes = new HashMap<String, String>();
        datetimes.put("yyyy", d_yyyy);
        datetimes.put("MM", d_MM);
        datetimes.put("dd", d_dd);
        datetimes.put("HH", t_HH);
        datetimes.put("mm", t_mm);
        datetimes.put("ss", t_ss);

        return datetimes;
    }

    /****
     * 获取系统当前的日期
     *
     * @author chenjianfan
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public static String getCurrentWeek(Date ...date) {
        Calendar cal = Calendar.getInstance();
        if (date.length > 0) {
            cal.setTime(date[0]);
        } else {
            cal.setTime(getToday());
        }
        int week = cal.get(Calendar.DAY_OF_WEEK);
        String strWeek = "星期天";
        switch (week) {
            case 1:
                strWeek = "星期天";
                break;
            case 2:
                strWeek = "星期一";
                break;
            case 3:
                strWeek = "星期二";
                break;
            case 4:
                strWeek = "星期三";
                break;
            case 5:
                strWeek = "星期四";
                break;
            case 6:
                strWeek = "星期五";
                break;
            case 7:
                strWeek = "星期六";
                break;
            default:
                strWeek = "星期天";
                break;
        }
        return strWeek;
    }

    /****
     * 获取系统当前的时间
     *
     * @author chenjianfan
     * @return
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(calendar.getTime());
    }


    /**
     * 格式转换
     *
     * @param str
     * @return
     */
    public static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToDate(String str, SimpleDateFormat format) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String str2Format(String str, SimpleDateFormat format) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format.format(date);
    }


    /**
     * 获得当天时间
     *
     * @param date
     * @param iDaydiff
     * @return
     */
    public static String getDay(Date date, int iDaydiff) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, iDaydiff);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }


    /**
     * 获取当天日期
     *
     * @return
     */
    public static Date getToday() {
        /** 定义日期对象 */
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        return today;
    }

    /**
     * 将年月日的int转成date
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     */
    public static Date getDate(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day);
        return mCalendar.getTime();
    }

    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy-MM-dd
     * @param end   终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    public static long getIntervalDays(String strat, String end) {
        return ((java.sql.Date.valueOf(end)).getTime() - (java.sql.Date
                .valueOf(strat)).getTime()) / (3600 * 24 * 1000);
    }

    /**
     * 获得当前年份
     *
     * @return year(int)
     */
    public static int getCurrentYear() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     *
     * @return month(int) 1-12
     */
    public static int getCurrentMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当月几号
     *
     * @return day(int)
     */
    public static int getDayOfMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得昨天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static String getYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -1);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 获得前天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    public static String getBeforeYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -2);
        return getDateFormat(mCalendar.getTime());
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return
     */
    public static String getOtherDay(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return getDateFormat(mCalendar.getTime());
    }

    public static Date getOtherFormat(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return mCalendar.getTime();
    }

    /**
     * 获得几小时之前或者几小时之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return
     */
    public static String getOtherHour(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.HOUR_OF_DAY, diff);
        return dateSimpleFormat(mCalendar.getTime(), BASE_DATE_FORMAT);
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param sDate  给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static String getCalcDateFormat(String sDate, int amount) {
        Date date = getCalcDate(getDateByDateFormat(sDate), amount);
        return getDateFormat(date);
    }

    /**
     * 将"yyyy-MM-dd" 格式的字符串转成Date
     *
     * @param strDate
     * @return Date
     */
    public static Date getDateByDateFormat(String strDate) {
        return getDateByFormat(strDate, NORMAL_DATE_FORMAT);
    }

    /**
     * 将指定格式的时间字符串转成Date对象
     *
     * @param strDate 时间字符串
     * @param format  格式化字符串
     * @return Date
     */
    public static Date getDateByFormat(String strDate, SimpleDateFormat format) {
        return getDateByFormat(strDate, format);
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static Date getCalcDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得一个计算十分秒之后的日期对象
     *
     * @param date
     * @param hOffset 时偏移量，可为负
     * @param mOffset 分偏移量，可为负
     * @param sOffset 秒偏移量，可为负
     * @return
     */
    public static Date getCalcTime(Date date, int hOffset, int mOffset, int sOffset) {
        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hOffset);
        cal.add(Calendar.MINUTE, mOffset);
        cal.add(Calendar.SECOND, sOffset);
        return cal.getTime();
    }

    /**
     * 根据指定的年月日小时分秒，返回一个java.Util.Date对象。
     *
     * @param year      年
     * @param month     月 0-11
     * @param date      日
     * @param hourOfDay 小时 0-23
     * @param minute    分 0-59
     * @param second    秒 0-59
     * @return 一个Date对象
     */
    public static Date getDate(int year, int month, int date, int hourOfDay,
                               int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }

    /**
     * 获得年月日数据
     *
     * @param sDate yyyy-MM-dd格式
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    public static int[] getYearMonthAndDayFrom(String sDate) {
        return getYearMonthAndDayFromDate(getDateByDateFormat(sDate));
    }

    /**
     * 获得年月日数据
     *
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    public static int[] getYearMonthAndDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] arr = new int[3];
        arr[0] = calendar.get(Calendar.YEAR);
        arr[1] = calendar.get(Calendar.MONTH);
        arr[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return arr;
    }

    /**
     * 将年月日的int转成yyyy-MM-dd的字符串
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     *              对输入项未做判断
     */
    public static String getDateFormat(int year, int month, int day) {
        return getDateFormat(getDate(year, month, day));
    }

    /**
     * 将date转成yyyy-MM-dd字符串<br>
     *
     * @param date Date对象
     * @return yyyy-MM-dd
     */
    public static String getDateFormat(Date date) {
        return dateSimpleFormat(date, NORMAL_DATE_FORMAT);
    }

    /**
     * 将date转成字符串
     *
     * @param date   Date
     * @param format SimpleDateFormat
     *               <br>
     *               注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateSimpleFormat(Date date, SimpleDateFormat format) {
        if (format == null)
            format = BASE_DATE_FORMAT;
        return (date == null ? "" : format.format(date));
    }

    /**
     * 字符串格式的时间转换成整形 如 "09:21" ---> 921
     *
     * @param date
     * @return
     */
    public static int dateFormat(String date) {
        if (date == null && ("".equals(date) || "null".equals(date)))
            return 0;
        return Integer.valueOf(date.replace(":", ""));
    }

}
