package com.dysen.kdemo.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.KDJ;
import com.dysen.kdemo.entity.MACD;
import com.dysen.kdemo.entity.MALineEntity;
import com.dysen.kdemo.utils.Tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
量能，kdj macd
*/

public class TargetView extends View {
    private Context mConext;
    private Resources res;
    /** 默认字体大小 **/
    public float DEFAULT_AXIS_TITLE_SIZE =10;
    /** 默认Y轴刻度显示长度 */
    private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 7;
    private boolean LongitudeIsShow = true;
    private boolean LatitudeIsShow = true;
    /** 默认经线数 */
    public   int DEFAULT_LOGITUDE_NUM = 6;
    /** 默认纬线数 */
    public   int DEFAULT_LATITUDE_NUM = 6;
    public void  setLatLine(int num)
    {
        DEFAULT_LATITUDE_NUM = num;
    }
    /**
     * EMA数据
     */
    private List<MALineEntity> EMALineData;

    /** 图表跟右边距离  x轴突出的位置*/
    public float DEFAULT_AXIS_MARGIN_RIGHT_X = DEFAULT_AXIS_TITLE_SIZE*DEFAULT_AXIS_Y_MAX_TITLE_LENGTH/2;
    //经线间隔度
    private float longitudeSpacing;
    //维线间隔度
    private float latitudeSpacing;
    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndext;
    private float olddistance = 0f;
    private int count = 0;
    /**
     * 显示的OHLC数据个数
     */
    private int mShowDataNum;

    //默认保留8位
    private int format=8;

    /**
     * BOll数据
     */
    private List<MALineEntity> BOLLLineData;

    /**
     * MA数据
     */
    private List<MALineEntity> MALineData;
    /**
    /** 纵轴Y刻度*/
    private List<String> axisYTitles;

    /** 横轴X刻度*/
    private List<String> axisXTitles;

    /**
     * 当前数据的最大最小值
     */
    private double mMaxPrice;
    private double mMinPrice;
    /** 背景色 */
    private int mBackGround;

    /** 边框颜色 */
    private int mBkColor;

    /** 边框颜色 */
    private int mClickColor;

    /**
     * 成交量最大值
     */
    private double mMaxVol;
    /**
     * 成交量最大值
     */
    private double mMinVol;
    //记录最小值
    private int minIndex;
    //记录最大值
    private int maxIndex;
    private boolean mShowK = false;
    private boolean mShowMACD = false;
    private boolean mShowJKD = false;
    private boolean mShowVol = false;
    private boolean isInnerYAxis = true;
    private float mStartX;
    private float mStartY;
    private boolean showXAxial = true;
    private  boolean showXText =true;
    private  boolean showEMA =false;
    private  boolean showMA =true;
    private  boolean showBoll =false;
    /** 上表的上间隙 */
    public  float UPER_CHART_MARGIN_TOP = 2*DEFAULT_AXIS_TITLE_SIZE;
    /** 上表的上间隙 */
    public  float UPER_CHART_MARGIN_BOTTOM = 2*DEFAULT_AXIS_TITLE_SIZE;
    /**
     * 默认字体颜色
     **/
    public static int ztColor = 0x535d66;
    /**
     * 默认分时均线颜色
     **/
    public static int klineline = 0x535d66;

    /**
     * 默认五日均线颜色
     **/
    public static int kline5dayline = 0x535d66;
    /**
     * 默认十日均线颜色
     **/
    public static int kline10dayline = 0x535d66;
    /**
     * 默认30日均线颜色
     **/

    /**
     * 显示的最小Candle数
     */
    private final static int MIN_CANDLE_NUM = 10;
    /**
     * 显示的最大Candle数
     */
    private final static int MAX_CANDLE_NUM = 200;

    /**
     * 默认显示的Candle数
     */
    private final static int DEFAULT_CANDLE_NUM = 120;

    public static int kline30dayline = 0x535d66;

    public static int klineRed = 0xCD1A1E;
    public static int klineGreen = 0x7AA376;
    private boolean isLongitudeIsShow=true;
    public void setLongitudeIsShow(boolean show)
    {
        isLongitudeIsShow=show;
    }
    /**
     * 显示纬线数
     */
   // private int latitudeNum = DEFAULT_LATITUDE_NUM;
    /**
     * OHLC数据
     */
    private List<MarketChartData> mOHLCData = new ArrayList<MarketChartData>();
    private float lineWidth = 1f;
    private boolean showMaxValue=false;
    /**
     * 量能均线数组
     */
    private List<MALineEntity> MAVLineData;
    private MACD mMACDData;
    private KDJ mKDJData;
    /**
     * Candle宽度
     */
    private double mCandleWidth;
    public TargetView(Context context) {
        super(context);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public TargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public TargetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mConext = context;
        res = mConext.getResources();
        init();
    }


    private void init() {
        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndext = 0;
        mMaxPrice = 0;
        mMinPrice = 0;
        DEFAULT_AXIS_TITLE_SIZE = sp2px(mConext,DEFAULT_AXIS_TITLE_SIZE);
        UPER_CHART_MARGIN_TOP = 3*DEFAULT_AXIS_TITLE_SIZE;
        UPER_CHART_MARGIN_BOTTOM= 2*DEFAULT_AXIS_TITLE_SIZE;
        DEFAULT_AXIS_MARGIN_RIGHT_X = DEFAULT_AXIS_TITLE_SIZE*DEFAULT_AXIS_Y_MAX_TITLE_LENGTH/3*2;
        mBackGround = res.getColor(R.color.kbg);
        mBkColor= res.getColor(R.color.status_bar_bg);
        mClickColor= res.getColor(R.color.kline_click_bk);
        ztColor= res.getColor(R.color.white);
        klineline= res.getColor(R.color.kline30dayline);
        kline5dayline = res.getColor(R.color.kline5dayline);
        kline10dayline = res.getColor(R.color.kline10dayline);
        kline30dayline = res.getColor(R.color.kline30dayline);
        klineRed = res.getColor(R.color.klinered);
        klineGreen = res.getColor(R.color.klinegreen);
    }
    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return  (spValue * fontScale + 0.5f);
    }
    public void setShowXText(boolean show)
    {
        this.showXText=show;
    }

    public void setShowXAxial(boolean show)
    {
        this.showXAxial=show;
    }

    public void setOHLCData(List<MarketChartData> OHLCData) {

        //分时，小时切换，重置  mDataStartIndext
        mDataStartIndext = 0;
        mShowDataNum = DEFAULT_CANDLE_NUM;
        if (OHLCData == null || OHLCData.size() <= 0) {
            return;
        }

        if (null != mOHLCData) {
            mOHLCData.clear();
        }
        for (MarketChartData e : OHLCData) {
            addData(e);
        }

        initMAStickLineData();
        mMACDData = new MACD(mOHLCData);
        mKDJData = new KDJ(mOHLCData);


        if(showMA)
        initMALineData();
        if(showEMA)
        initEMALineData();
        if(showBoll)
        initBOLLLineData();
        setCurrentData();

        float width = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;

        postInvalidate();
    }
    //K线均线
    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(kline5dayline);
        MA5.setLineData(initMA(mOHLCData, 5));

        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(kline10dayline);
        MA10.setLineData(initMA(mOHLCData, 10));

        MALineEntity MA30 = new MALineEntity();
        MA30.setTitle("MA30");
        MA30.setLineColor(kline30dayline);
        MA30.setLineData(initMA(mOHLCData, 30));

        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);
        MALineData.add(MA30);
    }
    //K线EMA
    private void initEMALineData() {
        MALineEntity EMA5 = new MALineEntity();
        EMA5.setTitle("EMA5");
        EMA5.setLineColor(kline5dayline);
        EMA5.setLineData(initEMA(mOHLCData, 5));

        MALineEntity EMA10 = new MALineEntity();
        EMA10.setTitle("EMA10");
        EMA10.setLineColor(kline10dayline);
        EMA10.setLineData(initEMA(mOHLCData, 10));

        MALineEntity EMA30 = new MALineEntity();
        EMA30.setTitle("EMA30");
        EMA30.setLineColor(kline30dayline);
        EMA30.setLineData(initEMA(mOHLCData, 30));

        EMALineData = new ArrayList<MALineEntity>();
        EMALineData.add(EMA5);
        EMALineData.add(EMA10);
        EMALineData.add(EMA30);
    }

    //K线均线
    private void initBOLLLineData() {


        MALineEntity MA20 = new MALineEntity();
        MA20.setTitle("MA20");
        MA20.setLineColor(kline5dayline);
        MA20.setLineData(initMA(mOHLCData, 20));


        MALineEntity MD = new MALineEntity();
        MD.setTitle("MD");
        MD.setLineColor(kline5dayline);
        MD.setLineData(initMD(mOHLCData, 20,MA20));

        MALineEntity MB = new MALineEntity();
        MB.setTitle("MB");
        MB.setLineColor(kline5dayline);
        MB.setLineData(initMBBOLL(mOHLCData, 20,MA20));

        MALineEntity UP = new MALineEntity();
        UP.setTitle("UB");
        UP.setLineColor(kline10dayline);
        UP.setLineData(initUPBOLL(mOHLCData, 20,MB,MD));

        MALineEntity DN = new MALineEntity();
        DN.setTitle("LB");
        DN.setLineColor(kline30dayline);
        DN.setLineData(initDNBOLL(mOHLCData, 20,MB,MD));

        BOLLLineData = new ArrayList<MALineEntity>();
        BOLLLineData.add(MB);
        BOLLLineData.add(UP);
        BOLLLineData.add(DN);
    }

    /**
     * 初始化EMA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initEMA(List<MarketChartData> entityList, int days) {
        List<Double> MAValues = new ArrayList<Double>();
        Double prev_price = 0.0;
        DecimalFormat   df =new   DecimalFormat("#.00");
        for (int i = entityList.size()-1; i>=0 ; i--) {
            Double c = entityList.get(i).getClosePrice();
            Double price= 0.0;
            if ( i < entityList.size()-1) {
                price = prev_price + (c-prev_price)*2/(days+1);
            } else {
                price= entityList.get(i).getClosePrice();

            }
            prev_price =price;
            MAValues.add(price);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }

    /**
     * 初始化MD值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initMD(List<MarketChartData> entityList, int days,MALineEntity ma20) {

        MALineEntity ma= ma20;
         double md=0;
        List<Double> MAValues = new ArrayList<Double>();
        Double price = 0.0;
        DecimalFormat   df =new   DecimalFormat("#.00");

        for (int i = entityList.size() - 1; i >= 0; i--) {
            Double maprice =ma.getLineData().get(i);
            int num=entityList.size() - i;
            if (num>= days) {
                double dx=0;
                for(int j=0;j<days;j++)
                {
                    int index = i+j;
                    Double close = entityList.get(index).getClosePrice();
                    dx=dx+Math.pow(close-maprice,2);
                }
                md=dx/(days);

            } else {
                double dx=0;
                for(int j=0;j<num;j++)
                {
                    int index = i+j;
                    Double map =ma.getLineData().get(i);
                    Double close = entityList.get(index).getClosePrice();
                    double temp1=close-maprice;
                    double temp2=Math.pow(close-maprice,2);
                    dx=dx+Math.pow(close-maprice,2);

                }
                md=dx/(num);

            }
            md = Math.pow(md,0.5);
            MAValues.add(md);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }
    /**
     * 初始化Boll值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initMBBOLL(List<MarketChartData> entityList, int days,MALineEntity ma20) {
        MALineEntity ma= ma20;
        List<Double> MAValues = new ArrayList<Double>();
        Double price = 0.0;
        DecimalFormat   df =new   DecimalFormat("#.00");
        for (int i = entityList.size() - 1; i >= 0; i--) {

            price = ma.getLineData().get(i);
            MAValues.add(price);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }
    private List<Double> initUPBOLL(List<MarketChartData> entityList, int days, MALineEntity mb,MALineEntity md) {

        List<Double> MAValues = new ArrayList<Double>();
        Double price = 0.0;
        DecimalFormat   df =new   DecimalFormat("#.00");

        for (int i = entityList.size() - 1; i >= 0; i--) {
            price = mb.getLineData().get(i)+2*md.getLineData().get(i);
            MAValues.add(price);
        }
        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }

    private List<Double> initDNBOLL(List<MarketChartData> entityList, int days, MALineEntity mb,MALineEntity md) {
        List<Double> MAValues = new ArrayList<Double>();
        Double price = 0.0;
        DecimalFormat   df =new   DecimalFormat("#.00");

        for (int i = entityList.size() - 1; i >= 0; i--) {
            price = mb.getLineData().get(i)-2*md.getLineData().get(i);
            MAValues.add(price);
        }
        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }

    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initMA(List<MarketChartData> entityList, int days) {
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Double> MAValues = new ArrayList<Double>();

        Double sum = 0.0;
        Double avg = 0.0;
        //  java.text.DecimalFormat   df =new   java.text.DecimalFormat("#.00");
        for (int i = entityList.size() - 1; i >= 0; i--) {
            Double close = entityList.get(i).getClosePrice();
            if (entityList.size() - i < days) {
                sum = sum + close;
                int d=entityList.size() - i;
                avg = sum / d;
                // avg= Double.parseDouble(df.format(avg));

            } else {
                sum = 0.0;
                for(int j=0;j<days;j++)
                {
                    sum =sum+entityList.get(i+j).getClosePrice();
                }

                avg = sum / days;
                //  avg= Double.parseDouble(df.format(avg));

            }
            MAValues.add(avg);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }



    private void setCurrentData() {
        try {

            initShowDataNum();
            setMaxMinPrice();
        }catch (Exception e)
        {
        }
    }

    private void setMaxMinPrice() {

        if (mOHLCData == null || mOHLCData.size() <= 0||mDataStartIndext<0) {
            return;
        }
        mMinPrice = mOHLCData.get(mDataStartIndext).getLowPrice();
        mMaxPrice = mOHLCData.get(mDataStartIndext).getHighPrice();
        mMaxVol = mOHLCData.get(mDataStartIndext).getVol();
        mMinVol = mOHLCData.get(mDataStartIndext).getVol();
        minIndex = mDataStartIndext;
        maxIndex = mDataStartIndext;
        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndext + i);

            if (mMinPrice > entity.getLowPrice()) {
                mMinPrice = entity.getLowPrice();
                minIndex = mDataStartIndext + i;
            }
            if (mMaxPrice < entity.getHighPrice()) {
                mMaxPrice = entity.getHighPrice();
                maxIndex = i + mDataStartIndext;
            }


            if (mMaxVol < entity.getVol()) {
                mMaxVol = entity.getVol();
            }
            if (mMinVol > entity.getVol()) {
                mMinVol = entity.getVol();
            }

            //  mMaxVol = mMaxVol > entity.getVol() ? mMaxVol : entity.getVol();
        }

    }

    public int getPoint(String s)
    {
        int position = 0;
        int index = s.indexOf(".");
        if(index!=-1)
        {
            position=s.length();
            index=s.indexOf(".");
            position=position-index-1;
        }else
        {
            return 0;
        }
        return position;
    }
    private void initShowDataNum()
    {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }
        if (MIN_CANDLE_NUM > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }

    }

    public void addData(MarketChartData entity) {
        if (null != entity) {
            if (null == mOHLCData || 0 == mOHLCData.size()) {
                mOHLCData = new ArrayList<MarketChartData>();
                this.mMinPrice = ((int) entity.getLowPrice()) / 10 * 10;
                this.mMaxPrice = ((int) entity.getHighPrice()) / 10 * 10;
            }

            this.mOHLCData.add(entity);

            if (this.mMinPrice > entity.getLowPrice()) {
                this.mMinPrice = ((int) entity.getLowPrice()) / 10 * 10;
            }

            if (this.mMaxPrice < entity.getHighPrice()) {
                this.mMaxPrice = 10 + ((int) entity.getHighPrice()) / 10 * 10;
            }

        }
    }
    //量能线均线
    private void initMAStickLineData() {
        //以下计算VOL
        MAVLineData = new ArrayList<MALineEntity>();

        //计算5日均线
        MALineEntity VMA5 = new MALineEntity();
        VMA5.setTitle("MA5");
        VMA5.setLineColor(kline5dayline);
        VMA5.setLineData(initVMA(mOHLCData,5));
        MAVLineData.add(VMA5);

        //计算10日均线
        MALineEntity VMA10 = new MALineEntity();
        VMA10.setTitle("MA10");
        VMA10.setLineColor(kline10dayline);
        VMA10.setLineData(initVMA(mOHLCData,10));
        MAVLineData.add(VMA10);


    }
    private List<Double> initVMA(List<MarketChartData> entityList, int days) {

        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Double> MAValues = new ArrayList<Double>();

        Double sum = 0.0;
        Double avg = 0.0;
        //  java.text.DecimalFormat   df =new   java.text.DecimalFormat("#.00");
        for (int i = entityList.size() - 1; i >= 0; i--) {
            Double close = entityList.get(i).getVol();
            if (entityList.size() - i < days) {
                sum = sum + close;
                int d=entityList.size() - i;
                avg = sum / d;
                //     avg=Double.parseDouble(df.format(avg));

            } else {
                sum = 0.0;
                for(int j=0;j<days;j++)
                {
                    sum =sum+entityList.get(i+j).getVol();
                }

                avg = sum / days;
                //     avg=Double.parseDouble(df.format(avg));

            }
            MAValues.add(avg);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(showXAxial)
        {
            initAxisX();
        }
        if(mShowK)
        {

            initAxisY();
        }

        super.onDraw(canvas);
        setBackgroundColor(mBackGround);

        int viewHeight = getHeight();
        int viewWidth = getWidth();


        //经度
        longitudeSpacing = (viewWidth - DEFAULT_AXIS_MARGIN_RIGHT_X)/DEFAULT_LOGITUDE_NUM;
        //纬线
        latitudeSpacing =(viewHeight-UPER_CHART_MARGIN_BOTTOM-UPER_CHART_MARGIN_TOP )/DEFAULT_LATITUDE_NUM;

        // 绘制纬线
        drawLatitudes(canvas, viewWidth, latitudeSpacing);
        // 绘制经线


        drawLongitudes(canvas,viewHeight,longitudeSpacing);
       // drawBorders(canvas,viewHeight,viewWidth);

        if(mShowK)
        {
            drawUpperRegion(canvas);
             if(showMA)
             drawMA(canvas);
             if(showEMA)
            drawEMA(canvas);
             if(showBoll)
            drawBoll(canvas);
        }

        if(mShowMACD)
        {
            drawMACD(canvas);
        }
        if(mShowJKD)
        {
            drawKDJ(canvas);
        }
        if(mShowVol)
        {
            drawSticks(canvas);
            drawVMA(canvas);
        }
        drawWithFingerClick(canvas);
    }
    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawBorders(Canvas canvas, int viewHeight, int viewWidth) {
        Paint paint = new Paint();
        paint.setColor(mBkColor);
        paint.setStrokeWidth(1);

        canvas.drawLine(0, 0, 0,viewHeight, paint);
        canvas.drawLine(0, 0, viewWidth,0, paint);
        canvas.drawLine(viewWidth, 0, viewWidth,viewHeight, paint);

        canvas.drawLine(0,viewHeight-2, viewWidth,viewHeight-2, paint);
        canvas.drawLine(viewWidth,0, viewWidth,viewHeight-2, paint);

    }
    /**
     * 绘制纬线
     *
     * @param canvas
     * @param viewWidth
     */
    private void drawLatitudes(Canvas canvas, int viewWidth, float latitudeSpacing ) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mBkColor);
        paint.setStrokeWidth(1);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintzt.setColor(ztColor);
        paintzt.setStrokeWidth(1);
        paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        int max=getPoint(mMaxPrice+"");
        int min=getPoint(mMinPrice+"");
        if(max>min)
        {
            format=max;
        }else
        {
            format=min;
        }
        if(format>8)
            format=8;


        for (int i = 0; i <=DEFAULT_LATITUDE_NUM; i++) {
                if(LatitudeIsShow)
                    canvas.drawLine(0, UPER_CHART_MARGIN_TOP+latitudeSpacing * (i), viewWidth-DEFAULT_AXIS_MARGIN_RIGHT_X, UPER_CHART_MARGIN_TOP  + latitudeSpacing * (i), paint);
            // 绘制Y刻度
               if(mShowK&&axisYTitles!=null&&axisYTitles.size()>0)
                canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), viewWidth - sp2px(mConext, 40),  latitudeSpacing * (i)+UPER_CHART_MARGIN_TOP, paintzt);
        }

    }

    /**
     * 绘制经线
     *
     * @param canvas
     */
    private void drawLongitudes(Canvas canvas, int viewHeight, float longitudeSpacing) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mBkColor);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);


        Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintzt.setColor(ztColor);
        paintzt.setStrokeWidth(1);
        paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        if(showXAxial) {
            try {
                for (int i = 0; i < axisXTitles.size(); i++) {
                    float tWidth = paint.measureText(axisXTitles.get(axisXTitles.size() - i - 1)) / 2;
                    //canvas.drawText(axisXTitles.get(axisXTitles.size()-i-1), super.getWidth()-longitudeSpacing * (i)-tWidth-DEFAULT_AXIS_MARGIN_RIGHT_X, getHeight(), paint);
                    canvas.drawText(axisXTitles.get(axisXTitles.size() - i - 1), longitudeSpacing * (i + 1) - tWidth, getHeight() - DEFAULT_AXIS_TITLE_SIZE / 2, paintzt);
                }
            } catch (Exception e) {

            }
        }

        if(isLongitudeIsShow)
        {
            for (int i = 0; i<=DEFAULT_LOGITUDE_NUM; i++) {
                if(LongitudeIsShow)
                {
                    canvas.drawLine(longitudeSpacing * i, UPER_CHART_MARGIN_TOP, longitudeSpacing * i, viewHeight, paint);
                }
            }
        }


    }
    private void drawMA(Canvas canvas) {
        if (MALineData == null || MALineData.size() < 0)
            return;
        setMaxMinPrice();
        double rate = (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MALineData.size(); j++) {
            MALineEntity lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {

                    canvas.drawLine(
                            startX,
                            startY ,
                            (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) (((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP),
                            paint);


                }
                startX = (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP;


            }
        }
    }
    private void drawEMA(Canvas canvas) {
        if (EMALineData == null || EMALineData.size() < 0)
            return;
        setMaxMinPrice();
        double rate = (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < EMALineData.size(); j++) {
            MALineEntity lineEntity = EMALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {

                    canvas.drawLine(
                            startX,
                            startY ,
                            (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) (((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP),
                            paint);


                }
                startX = (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP;


            }
        }
    }


    private void drawBoll(Canvas canvas) {
        if (BOLLLineData == null || BOLLLineData.size() < 0)
            return;
        setMaxMinPrice();
        double rate = (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < BOLLLineData.size(); j++) {
            MALineEntity lineEntity = BOLLLineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {

                    canvas.drawLine(
                            startX,
                            startY ,
                            (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) (((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP),
                            paint);


                }
                startX = (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2 - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP;


            }
        }
    }
    private void drawUpperRegion(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }

        // 绘制蜡烛图
        Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(klineRed);

        Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setColor(klineGreen);


        // 绘最大最小值
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        paint.setColor(ztColor);

        float width = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        double rate = (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM) / (mMaxPrice - mMinPrice);
        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndext + i);

            float open = (float) ((mMaxPrice - entity.getOpenPrice()) * rate  + UPER_CHART_MARGIN_TOP);
            float close = (float) ((mMaxPrice - entity.getClosePrice()) * rate  + UPER_CHART_MARGIN_TOP);
            float high = (float) ((mMaxPrice - entity.getHighPrice()) * rate  + UPER_CHART_MARGIN_TOP);
            float low = (float) ((mMaxPrice - entity.getLowPrice()) * rate  + UPER_CHART_MARGIN_TOP);


            float left = (float) (width - mCandleWidth * (i + 1));
            float right = (float) (width - mCandleWidth * i);
            float startX = (float) (width - mCandleWidth * i - mCandleWidth / 2);

            if (open < close) {
                canvas.drawRect(left + 1, open, right - 1, close, greenPaint);
                canvas.drawLine(startX, high, startX, low, greenPaint);
            } else if (open == close) {
                canvas.drawRect(left + 1, close - 1, right - 1, open + 1, redPaint);
                canvas.drawLine(startX, high, startX, low, redPaint);
            } else {
                canvas.drawRect(left + 1, close, right - 1, open, redPaint);
                canvas.drawLine(startX, high, startX, close, redPaint);
                canvas.drawLine(startX, open, startX, low, redPaint);
            }

            Rect rect = new Rect();
            //画最大和最小值
            if (mDataStartIndext + i == maxIndex) {
                String maxPrice = entity.getHighPrice() + "";
                paint.getTextBounds(maxPrice, 0, 1, rect);
                float w = paint.measureText(maxPrice);
                //左箭头
                if ((i * mCandleWidth + mCandleWidth / 2) > (w + 50)) {
                    canvas.drawLine(startX, high , startX + 25, high , paint);
                    canvas.drawLine(startX, high , startX + 10, high - 10, paint);
                    canvas.drawLine(startX, high , startX + 10, high  + 10, paint);
                    canvas.drawText(deFormatNew(maxPrice,8), startX + 25, high  + rect.height() / 2, paint);
                } else {
                    canvas.drawLine(startX, high, startX - 25, high , paint);
                    canvas.drawLine(startX, high , startX - 10, high  + 10, paint);
                    canvas.drawLine(startX, high , startX - 10, high  - 10, paint);
                    canvas.drawText(deFormatNew(maxPrice,8), startX - 25 - w, high + rect.height() / 2, paint);
                }

            }
            if (mDataStartIndext + i == minIndex) {
                String minPrice = entity.getLowPrice() + "";
                paint.getTextBounds(minPrice, 0, 1, rect);
                float w = paint.measureText(minPrice);
                //左箭头
                if ((i * mCandleWidth + mCandleWidth / 2) > (w + 50)) {
                    canvas.drawLine(startX, low , startX + 25, low , paint);
                    canvas.drawLine(startX, low ,startX + 10, low  + 10, paint);
                    canvas.drawLine(startX, low , startX + 10, low  - 10, paint);
                    canvas.drawText(deFormatNew(minPrice,8), startX + 25, low  + rect.height() / 2, paint);
                } else {
                    canvas.drawLine(startX, low  , startX - 25, low , paint);
                    canvas.drawLine(startX, low , startX - 10, low  - 10, paint);
                    canvas.drawLine(startX, low , startX - 10, low  + 10, paint);
                    canvas.drawText(deFormatNew(minPrice,8), startX - 25 - w, low + rect.height() / 2, paint);
                }
            }
        }
    }

    /**
     * 绘制半�?明文本�?
     * @param content
     * @param canvas
     */
    private void drawAlphaXTextBox(String content, Canvas canvas) {


        if (touchPoint==null)
            return;
        Paint mPaintBox = new Paint();
        mPaintBox.setColor(mBackGround);
        //mPaintBox.setAlpha(150);

        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(ztColor);
        mPaintBoxLine.setAntiAlias(true);
        mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        float strw = mPaintBoxLine.measureText(content);

        float left = touchPoint.x-strw/2;
        float right = touchPoint.x+strw/2;
        float top = 0;
        float bottom=0;
        top =getHeight()-DEFAULT_AXIS_TITLE_SIZE-10;
        bottom =getHeight()-2;


        canvas.drawRect(left, top, right, bottom, mPaintBox);
        Paint borderPaint = new Paint();
        borderPaint.setColor(ztColor);
        borderPaint.setStrokeWidth(2);
        canvas.drawLine(left, top, left, bottom, borderPaint);
        canvas.drawLine(left, top, right, top, borderPaint);
        canvas.drawLine(right, bottom, right, top, borderPaint);
        canvas.drawLine(right, bottom, left, bottom, borderPaint);
        canvas.drawText(content, touchPoint.x - strw / 2, getHeight()-10, mPaintBoxLine);

    }
    /**
     * 初始化Y轴
     */
    protected void initAxisY() {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> TitleY = new ArrayList<String>();
        float height = getHeight();
        if(height==0||height==0.0f)
            return;
        float average = (float) ((mMaxPrice - mMinPrice) / (height-UPER_CHART_MARGIN_TOP)) ;
        average = average*((height-UPER_CHART_MARGIN_TOP)/DEFAULT_LATITUDE_NUM);

        //处理所有Y刻度
        for (float i = 0; i < DEFAULT_LATITUDE_NUM; i++) {
            String value = String.valueOf(mMinPrice + i * average);
            //     String value =  Float.parseFloat(new DecimalFormat("0000.00000000").format((mMinPrice + i * average)))+"";
            value=deFormatNew(value,8);
            if (value.length() < DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
                while (value.length() < DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
                    value = new String(" ") + value;
                }
            } else if (value.length() > DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
                value = value.substring(0, (int)DEFAULT_AXIS_Y_MAX_TITLE_LENGTH);
            }
            TitleY.add(value);
        }
        //处理最大值
        String value = String.valueOf(mMaxPrice);
        value=deFormatNew(value,8);
        if (value.length() < DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
            while (value.length() <DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
                value = new String(" ") + value;
            }
        }else if (value.length() > DEFAULT_AXIS_Y_MAX_TITLE_LENGTH) {
            value = value.substring(0, (int)DEFAULT_AXIS_Y_MAX_TITLE_LENGTH);
        }
          TitleY.add(value);
         setAxisYTitles(TitleY);
    }


    /**
     * 初始化X轴
     */
    protected void initAxisX() {

        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> TitleX = new ArrayList<String>();

        if (null != mOHLCData) {
            int step = (int)Math.floor(mShowDataNum/DEFAULT_LOGITUDE_NUM);

            for (int i = 0; i < DEFAULT_LOGITUDE_NUM && mDataStartIndext + (i)*step < mOHLCData.size(); i++) {

                String  time = String.valueOf(mOHLCData.get((i)*step+ mDataStartIndext).getTime2());
                if(time.equals("00:00"))
                {
                    time = String.valueOf(mOHLCData.get((i)*step+ mDataStartIndext).getTime4());
                }else
                {
                    time = String.valueOf(mOHLCData.get((i)*step+ mDataStartIndext).getTime2());
                }
                TitleX.add(time);
            }
        }

        setAxisXTitles(TitleX);
    }
    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }
    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }

    public static String deFormatNew(String str,int type){
        try{
            BigDecimal bigDecimal=new BigDecimal(str);
            String str_ws="0.#";
            if(type==-1){
                str_ws="0.00";
            }
            for(int n=1;type>1&&n<type;n++){
                str_ws=str_ws+"#";
            }
            DecimalFormat df_ls = new DecimalFormat(str_ws);
            str=df_ls.format(bigDecimal.setScale(type,BigDecimal.ROUND_FLOOR).doubleValue());
        }catch (Exception e){
            str="0.00";
        }
        return str;
    }

    private void drawMACD(Canvas canvas) {

        try {
            if (mOHLCData == null || mOHLCData.size() <= 0) {
                return;
            }
            float lowertop =  1;
            float lowerHight = getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM;
            float viewWidth = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X;
            float zero = 0;

            Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            whitePaint.setColor(kline10dayline);
            whitePaint.setStyle(Paint.Style.FILL);
            whitePaint.setStrokeWidth(lineWidth);
            Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            yellowPaint.setStyle(Paint.Style.FILL);
            yellowPaint.setColor(kline5dayline);
            yellowPaint.setStrokeWidth(lineWidth);

            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(ztColor);
            textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            if (mMACDData == null)
                return;
            List<Double> DEA = mMACDData.getDEA();
            List<Double> DIF = mMACDData.getDIF();
            List<Double> BAR = mMACDData.getBAR();
            // try {
            double low = DIF.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < BAR.size(); i++) {
                low = low < BAR.get(i) ? low : BAR.get(i);
                high = high > BAR.get(i) ? high : BAR.get(i);
                low = low < DIF.get(i) ? low : DIF.get(i);
                high = high > DIF.get(i) ? high : DIF.get(i);
                low = low < DEA.get(i) ? low : DEA.get(i);
                high = high > DEA.get(i) ? high : DEA.get(i);
            }
            if (Math.abs(low) > Math.abs(high)) {
                rate = lowerHight / (2 * Math.abs(low));
            } else {
                rate = lowerHight / (2 * Math.abs(high));
            }
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            zero = UPER_CHART_MARGIN_TOP+(getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM)/2;
            // 绘制双线
            float dea = 0.0f;
            float dif = 0.0f;

            double bar=0;

            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < BAR.size(); i++) {
                if((mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)>=BAR.size())
                    return;
                // 绘制矩形
                if (BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i) >= 0.0) {
                    paint.setColor(klineRed);
                    float top = (float) (zero - BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i) * rate);
                    if(BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)>bar)
                    {
                        paint.setStyle(Paint.Style.STROKE);
                    }else
                    {
                        paint.setStyle(Paint.Style.FILL);
                    }

                    float left =(float) mCandleWidth * (i-mDataStartIndext)-1;
                    float right =(float) mCandleWidth * (i-mDataStartIndext+1)-2;;

                    canvas.drawRect(left, top, right, zero, paint);
                } else {
                    paint.setColor(klineGreen);
                    if(BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)>bar)
                    {
                        paint.setStyle(Paint.Style.STROKE);
                    }else
                    {
                        paint.setStyle(Paint.Style.FILL);
                    }
                    float bottom = (float) (zero - BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i) * rate);

                    float left =(float) mCandleWidth * (i-mDataStartIndext)-1;
                    float right =(float) mCandleWidth * (i-mDataStartIndext+1)-2;

                    canvas.drawRect(left, zero,right, bottom, paint);
                }

                if (i != mDataStartIndext) {
                    float startx =viewWidth - (float) mCandleWidth * (mDataStartIndext + mShowDataNum-i)-(float) mCandleWidth / 2;
                    float starty=dea;
                    float stopx =viewWidth - (float) mCandleWidth * (mDataStartIndext + mShowDataNum-i-1) - (float) mCandleWidth / 2;
                    float stopy=zero - (float) ((DEA.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)) * rate);

                    canvas.drawLine(startx, starty, stopx, stopy, whitePaint);

                    canvas.drawLine(viewWidth  - (float) mCandleWidth * (mDataStartIndext + mShowDataNum-i) - (float) mCandleWidth / 2,dif
                            , viewWidth
                                    - (float) mCandleWidth * (mDataStartIndext + mShowDataNum-i-1)
                                    - (float) mCandleWidth / 2, zero - (float) ((DIF.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)) * rate), yellowPaint);
                }
                bar = BAR.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i);
                dea = zero - (float) ((DEA.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)) * rate);
                dif = zero - (float) ((DIF.get(mDataStartIndext+mDataStartIndext + mShowDataNum-1-i)) * rate);

            }

            float margin_right = DEFAULT_AXIS_MARGIN_RIGHT_X;
            if(isInnerYAxis)
            {
                margin_right=sp2px(mConext,40);
            }

            canvas.drawText(new DecimalFormat("#.##").format(high), super.getWidth() - margin_right, lowertop
                    + UPER_CHART_MARGIN_TOP - 2, textPaint);
            if(!showMaxValue)
            {
                canvas.drawText(new DecimalFormat("#.##").format(0), super.getWidth() - margin_right, lowertop
                        + lowerHight / 2 + UPER_CHART_MARGIN_TOP, textPaint);
                canvas.drawText(new DecimalFormat("#.##").format(low), super.getWidth() - margin_right, lowertop + lowerHight+UPER_CHART_MARGIN_TOP,
                        textPaint);
            }

        } catch (Exception e) {

        }
    }

    public void setDataStartIndext(int indext)
    {
        this.mDataStartIndext=indext;
    }

    private void drawKDJ(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        float lowertop = UPER_CHART_MARGIN_TOP+1;
        float lowerHight = getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM;
        float viewWidth = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X;

        Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.RED);
        whitePaint.setStrokeWidth(lineWidth);

        Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowPaint.setColor(Color.GREEN);
        yellowPaint.setStrokeWidth(lineWidth);

        Paint magentaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        magentaPaint.setColor(Color.MAGENTA);
        magentaPaint.setStrokeWidth(lineWidth);


        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ztColor);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        if (mKDJData == null)
            return;
        List<Double> Ks = mKDJData.getK();
        List<Double> Ds = mKDJData.getD();
        List<Double> Js = mKDJData.getJ();

        double low = 0;
        double high = 100;
        float zero = getHeight()-UPER_CHART_MARGIN_TOP;
        double rate = lowerHight / (high - low);
        // 绘制白、黄、紫线
        float k = 50f;
        float d = 50f;
        float j = 50f;
        for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {

            if (i != mDataStartIndext) {
                canvas.drawLine(viewWidth - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Ks.get(i) * rate), viewWidth  - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, k, whitePaint);
                canvas.drawLine(viewWidth  - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Ds.get(i) * rate), viewWidth  - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, d, yellowPaint);
                canvas.drawLine(viewWidth  - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Js.get(i) * rate), viewWidth  - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, j, magentaPaint);
            }
            k = (float) (zero - Ks.get(i) * rate);
            d = (float) (zero - Ds.get(i) * rate);
            j = (float) (zero - Js.get(i) * rate);


        }

        float margin_right=DEFAULT_AXIS_MARGIN_RIGHT_X;

        canvas.drawText(new DecimalFormat("#.##").format(high), super.getWidth() - margin_right, lowertop + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
        canvas.drawText(new DecimalFormat("#.##").format(50), super.getWidth() - margin_right, lowertop + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
        canvas.drawText(new DecimalFormat("#.##").format(low), super.getWidth() - margin_right, lowertop + lowerHight, textPaint);

    }

    /**
     * 绘制柱状线
     *
     * @param canvas
     */
    protected void drawSticks(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        // 蜡烛棒宽度
        float stickWidth = ((super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - 2) / mShowDataNum);
        // 蜡烛棒起始绘制位置
        float stickX = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X - stickWidth;

        Paint mPaintRedStick = new Paint();
        mPaintRedStick.setColor(klineRed);

        Paint mPaintGreenStick = new Paint();
        mPaintGreenStick.setColor(klineGreen);

        if (null != mOHLCData) {

            //判断显示为方柱或显示为线条
            for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
               MarketChartData ohlc = mOHLCData.get(mDataStartIndext + i);

                float highY = (float) ((ohlc.getVol()
                        / (mMaxVol)) * (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM));

                if (ohlc.getClosePrice() > ohlc.getOpenPrice()) {
                    canvas.drawRect(stickX + 1, getHeight()-UPER_CHART_MARGIN_BOTTOM  - highY, stickX + stickWidth - 1, getHeight() -UPER_CHART_MARGIN_BOTTOM, mPaintRedStick);
                } else {
                    canvas.drawRect(stickX + 1, getHeight()-UPER_CHART_MARGIN_BOTTOM  - highY, stickX + stickWidth - 1, getHeight()-UPER_CHART_MARGIN_BOTTOM, mPaintGreenStick);
                }
                //X位移
                stickX = stickX - stickWidth;
            }


        }



        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ztColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(lineWidth);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);


        float margin_right=DEFAULT_AXIS_MARGIN_RIGHT_X;
        if(isInnerYAxis)
        {
            margin_right=sp2px(mConext,40);
        }


        BigDecimal bigDecimal= new BigDecimal(mMaxVol);
        if(bigDecimal.compareTo(new BigDecimal(1000))>=0)
        {
            String vol=bigDecimal.divide(new BigDecimal(1000)).toString();
            canvas.drawText(deFormatNew(vol,0)+"K", super.getWidth() - margin_right - 1, UPER_CHART_MARGIN_TOP, textPaint);
        }else {
            canvas.drawText(new DecimalFormat("#.##").format(mMaxVol), super.getWidth() - margin_right - 1, UPER_CHART_MARGIN_TOP, textPaint);
        }

        if(!showMaxValue) {
            BigDecimal maxDecimal= new BigDecimal(mMaxVol/2);
            if(maxDecimal.compareTo(new BigDecimal(1000))>=0) {
                String vol = maxDecimal.divide(new BigDecimal(1000)).toString();
                canvas.drawText(deFormatNew(vol,0)+"K", super.getWidth() - margin_right - 1, (getHeight())/2, textPaint);
            }else {
                canvas.drawText(new DecimalFormat("#.##").format((mMaxVol) / 2), super.getWidth() - margin_right - 1, (getHeight())/2, textPaint);
            }

            canvas.drawText(new DecimalFormat("#.##").format(0), super.getWidth() - margin_right - 1, getHeight()-UPER_CHART_MARGIN_BOTTOM , textPaint);
        }
    }
    /** 当前被选中touch点 */
    private PointF touchPoint = null;

    /** 选中位置X坐标 */
    private float clickPostX = 0f;

    /** 选中位置Y坐标*/
    private float clickPostY = 0f;

    /** 点击时横轴X刻度*/
    private String axisXTitleClick="";

    /** 点击时横轴Y刻度*/
    private String axisYTitleClick="";
    /**
     * 获取X轴刻度的百分比最大1
     *
     * @param value
     * @return
     */
    public String getAxisXGraduate(Object value) {

        float length = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X;
        float valueLength = ((Float) value).floatValue();

        return String.valueOf(valueLength / length);
    }
    public PointF getTouchPoint() {
        return touchPoint;
    }
    public int getSelectedIndex() {
        if (null == getTouchPoint()) {
            return 0;
        }
        float graduate = Float.valueOf(getAxisXGraduate(getTouchPoint().x));
        int index = (int) Math.floor(graduate * mShowDataNum);

        if (index >= mShowDataNum) {
            index = mShowDataNum - 1;
        } else if (index < 0) {
            index = 0;
        }

        return index;
    }
    public void setShowEMA()
    {
        showMA = false;
        showBoll=false;
        showEMA=true;
        initEMALineData();
        postInvalidate();
    }
    public void setShowBOLL()
    {
        showMA = false;
        showBoll=true;
        showEMA=false;
        initBOLLLineData();
        postInvalidate();
    }
    public void setShowMA()
    {
        showMA = true;
        showBoll=false;
        showEMA=false;
        initMALineData();
        postInvalidate();
    }
    private TargetView mTargetView=null;
    public void setTargetView(TargetView targetView)
    {
        this.mTargetView=targetView;
    }

    /*版本2*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return true;
        }
        PointF point = null;
        if (event.getPointerCount() == 1) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = event.getX();
                    mStartY = event.getY();
                    point = new PointF(mStartX, mStartY);
                    setTouchPoint(point);
                    postInvalidate();
                    if(mTargetView!=null)
                    {
                        float startX = (float) (mCandleWidth * (getSelectedIndex() + 1) - mCandleWidth / 2);
                        point = new PointF(startX, mStartY);
                        mTargetView.setTouchPoint(point);
                        mTargetView.postInvalidate();
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mOHLCData == null || mOHLCData.size() <= 0) {
                        return true;
                    }
                    float horizontalSpacing = event.getX() - mStartX;

                    if (Math.floor(Math.abs(horizontalSpacing)) < mCandleWidth) {

                        point = new PointF(mStartX, mStartY);
                        setTouchPoint(point);
                        postInvalidate();
                        if(mTargetView!=null)
                        {
                            float startX = (float) (mCandleWidth * (getSelectedIndex() + 1) - mCandleWidth / 2);
                            point = new PointF(startX, mStartY);
                            mTargetView.setTouchPoint(point);
                            mTargetView.postInvalidate();
                        }
                        return true;
                    }
                    //  startMotion(event,horizontalSpacing);
                    int mMoveNum = (int) Math.abs(Math.floor(Math.abs(horizontalSpacing) / mCandleWidth));
                    if (mMoveNum == 0)
                        mMoveNum = 1;

                    if (horizontalSpacing < 0) {
                        mDataStartIndext = mDataStartIndext - mMoveNum;
                        if (mDataStartIndext < 0) {
                            mDataStartIndext = 0;
                        }
                    } else if (horizontalSpacing > 0) {
                        mDataStartIndext = mDataStartIndext + mMoveNum;
                        if (mDataStartIndext + mShowDataNum > mOHLCData.size()) {
                            mDataStartIndext = mOHLCData.size() - mShowDataNum;
                        }
                    }
                    mStartX = event.getX();
                    mStartY = event.getY();
                    setCurrentData();
                    postInvalidate();
                    if(mTargetView!=null)
                    {
                        mTargetView.setDataStartIndext(mDataStartIndext);
                        mTargetView.postInvalidate();
                    }
                    break;
            }
        } else if (event.getPointerCount() == 2) {

            if(mShowVol||mShowJKD||mShowMACD)
                return true;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                    mStartX = event.getX();
                    mStartY = event.getY();
                    point = new PointF(mStartX, mStartY);
                    setTouchPoint(point);
                    if (count == 0) {
                        olddistance = spacing(event);
                        count++;
                    } else {
                        count = 0;
                        if (spacing(event) - olddistance < 0) {
                            zoomIn((int) (spacing(event) / mCandleWidth));
                        } else {
                            zoomOut((int) (spacing(event) / mCandleWidth));
                        }
                        setCurrentData();
                    }
                    postInvalidate();

                    if(mTargetView!=null)
                    {
                        float startX = (float) (mCandleWidth * (getSelectedIndex() + 1) - mCandleWidth / 2);
                        point = new PointF(startX, mStartY);

                        mTargetView.setTouchPoint(point);
                        mTargetView.setShowDataNum(mShowDataNum);
                        mTargetView.postInvalidate();
                    }
                    break;
            }
        }
        return true;
    }

    //缩小
    private void zoomIn(int move) {

        if (mShowDataNum + mDataStartIndext >= mOHLCData.size())
            return;
        mShowDataNum = mShowDataNum + move;
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }
        if (mShowDataNum > MAX_CANDLE_NUM) {
            mShowDataNum = MAX_CANDLE_NUM;
        }

    }

    //放大
    private void zoomOut(int move) {
        mShowDataNum = mShowDataNum - move;
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }

    }

    public void setShowDataNum(int num)
    {
        this.mShowDataNum = num;

        float width = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;

    }

    // 计算移动距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    public void setAxisXClickTitle(String axisXTitle) {
        this.axisXTitleClick = axisXTitle;
    }
    public void setAxisYClickTitle(String axisYTitle) {
        this.axisYTitleClick = axisYTitle;
    }
    public void setTouchPoint(PointF p) {
        this.touchPoint=p;
    }
    /*
     * 单点击事件
     */
    protected void drawWithFingerClick(Canvas canvas) {

        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }

        int index = getSelectedIndex();
        if (getTouchPoint() == null)
            return;

        // talkingdata异常修复: java.lang.IndexOutOfBoundsException
        if (mOHLCData.size() <= (mShowDataNum - 1 - index + mDataStartIndext)) {
            return;
        }

        float y = getTouchPoint().y;
        //纠正出界
        if (y < UPER_CHART_MARGIN_TOP) {
            y = UPER_CHART_MARGIN_TOP;
        }
        if (y > getHeight()-UPER_CHART_MARGIN_TOP) {
            y = getHeight()-UPER_CHART_MARGIN_TOP;
        }
        if (getTouchPoint() != null && mOHLCData != null && mOHLCData.size() > 0 ) {
            if(showXText)
            setAxisXClickTitle(String.valueOf(mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getTime3()));

            float roate = 1 - (y - UPER_CHART_MARGIN_TOP) / (getHeight()-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM);

            setAxisYClickTitle(roate * (mMaxPrice - mMinPrice) + mMinPrice + "");

        }
        float startX = (float) (mCandleWidth * (index + 1) - mCandleWidth / 2);
        PointF piont = new PointF(startX, y);
        setTouchPoint(piont);
        if(mShowK)
        {
            double cha=mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getClosePrice()-mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getOpenPrice();
            String rate = deFormatNew((cha/mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getOpenPrice())*100 +"",2);
            drawMAText(canvas, mShowDataNum - 1 - index + mDataStartIndext, kline5dayline, kline10dayline, kline30dayline);
            drawAlphaTopTextBox2("   "+res.getString(R.string.open) +"  "+ mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getOpenPrice() +"  "+ res.getString(R.string.high) +"  "+ mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getHighPrice() +"  "+
                    res.getString(R.string.low) + mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getLowPrice() +"  "+ res.getString(R.string.close) + mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getClosePrice() +"  "+ res.getString(R.string.vol) +"  "+ mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getVol()+"  "+ res.getString(R.string.rate) +"  "+rate+"%", canvas);
        }

        if(mShowVol)
        drawVMAText(MAVLineData, canvas, mShowDataNum - 1 - index + mDataStartIndext, kline5dayline, kline10dayline);
            //  drawAlphaMiddleTextBox(res.getString(R.string.vol) + mOHLCData.get(mShowDataNum - 1 - index + mDataStartIndext).getVol(), canvas);
            if (mMACDData != null && mShowMACD) {
                drawAlphaTopTextBox2("  "+"MACD(12,26,9) DIF:" + new DecimalFormat("#.##").format(mMACDData.getDIF().get(mShowDataNum - 1 - index + mDataStartIndext)) + " DEA:" + new DecimalFormat("#.##").format(mMACDData.getDEA().get(mShowDataNum - 1 - index + mDataStartIndext))
                        + "MACD:" + new DecimalFormat("#.##").format(mMACDData.getBAR().get(mShowDataNum - 1 - index + mDataStartIndext)), canvas);
            }

            if (mKDJData != null && mShowJKD) {
                drawAlphaTopTextBox2("  "+"KDJ(9,3,3) K:" + new DecimalFormat("#.##").format(mKDJData.getK().get(mShowDataNum - 1 - index + mDataStartIndext)) + " D:" + new DecimalFormat("#.##").format(mKDJData.getD().get(mShowDataNum - 1 - index + mDataStartIndext)) + "J:" + new DecimalFormat("#.##").format(mKDJData.getJ().get(mShowDataNum - 1 - index + mDataStartIndext)), canvas);
            }


        Paint mPaint = new Paint();
        mPaint.setColor(mClickColor);
        mPaint.setStrokeWidth(2);
        float lineVLength = 0;
        // 垂直线高度
        lineVLength = getHeight() -3;


        if(!getAxisXClickTitle().equals("")&&showXText)
            drawAlphaXTextBox(getAxisXClickTitle(),canvas);

        if (!getAxisYClickTitle().equals("")&&mShowK)
            drawAlphaYTextBox(deFormatNew(getAxisYClickTitle() + "", 8), canvas);

        if (touchPoint != null) {
            float value = DEFAULT_AXIS_MARGIN_RIGHT_X;
                // 显示纵线
                canvas.drawLine(touchPoint.x > super.getWidth() - value ? super.getWidth() - value : touchPoint.x, 1f, touchPoint.x > super.getWidth() - value ? super.getWidth() - value : touchPoint.x, lineVLength, mPaint);
                // 显示横线
                canvas.drawLine(1, touchPoint.y , super.getWidth() - value, touchPoint.y , mPaint);
                //画实心圆
                canvas.drawCircle( touchPoint.x,  touchPoint.y, 10, mPaint);
                //画空心圆
                mPaint.setStyle(Paint.Style.STROKE);
               // canvas.drawCircle(touchPoint.x , touchPoint.y, 25, mPaint);

        }
    }

    /**
     * 绘制半�?明文本�?
     * @param content
     * @param canvas
     */

    public void drawAlphaTopTextBox2(String content, Canvas canvas) {
        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(ztColor);
        mPaintBoxLine.setAntiAlias(true);
        mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(content, 2f, DEFAULT_AXIS_TITLE_SIZE-2f, mPaintBoxLine);
    }

    public void drawAlphaTopTextBox1(String content, Canvas canvas) {
        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(ztColor);
        mPaintBoxLine.setAntiAlias(true);
        mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(content, 2f, UPER_CHART_MARGIN_TOP-2f, mPaintBoxLine);
    }

    public String getAxisYClickTitle() {
        return axisYTitleClick;
    }
    public String getAxisXClickTitle() {
        return axisXTitleClick;
    }
    /**
     * ma5,ma10,ma30均线
     */

    public void drawMAText(Canvas canvas, int index, int k5, int k10, int k30) {
        String ma5text="";
        String ma10text="";
        String ma30text="";

        if(showMA)
        {
            ma5text  = "   MA5:" + new DecimalFormat("#.##").format(MALineData.get(0).getLineData().get(index));
            ma10text = "   MA10:" + new DecimalFormat("#.##").format(MALineData.get(1).getLineData().get(index));
            ma30text = "   MA30:" + new DecimalFormat("#.##").format(MALineData.get(2).getLineData().get(index));
        }

        if(showEMA)
        {
            ma5text  = "   EMA5:" + new DecimalFormat("#.##").format(EMALineData.get(0).getLineData().get(index));
            ma10text = "   EMA10:" + new DecimalFormat("#.##").format(EMALineData.get(1).getLineData().get(index));
            ma30text = "   EMA30:" + new DecimalFormat("#.##").format(EMALineData.get(2).getLineData().get(index));
        }

        if(showBoll)
        {
            ma5text  = "   UB:" + new DecimalFormat("#.##").format(BOLLLineData.get(0).getLineData().get(index));
            ma10text = "   BOLL:" + new DecimalFormat("#.##").format(BOLLLineData.get(1).getLineData().get(index));
            ma30text = "   LB:" + new DecimalFormat("#.##").format(BOLLLineData.get(2).getLineData().get(index));
        }


        Paint ma5 = new Paint();
        ma5.setColor(k5);
        ma5.setAntiAlias(true);
        ma5.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma5text, 2f, 2*DEFAULT_AXIS_TITLE_SIZE - 2f, ma5);


        float tWidth = ma5.measureText(ma5text);
        Paint ma10 = new Paint();
        ma10.setColor(k10);
        ma10.setAntiAlias(true);
        ma10.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma10text, 2f + tWidth, 2*DEFAULT_AXIS_TITLE_SIZE - 2f, ma10);


        float width = ma10.measureText(ma10text);
        Paint ma30 = new Paint();
        ma30.setColor(k30);
        ma30.setAntiAlias(true);
        ma30.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma30text, 2f + tWidth + width, 2*DEFAULT_AXIS_TITLE_SIZE - 2f, ma30);
    }

    /**
     * ma5,ma10,ma30均线
     */

    public void drawVMAText(List<MALineEntity> VMALineData, Canvas canvas, int index, int k5, int k10) {
        String ma5text = "  VMA5:" + new DecimalFormat("#.##").format(VMALineData.get(0).getLineData().get(index));
        String ma10text = "  VMA10:" + new DecimalFormat("#.##").format(VMALineData.get(1).getLineData().get(index));
        Paint ma5 = new Paint();
        ma5.setColor(k5);
        ma5.setAntiAlias(true);
        ma5.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma5text, 2f, UPER_CHART_MARGIN_TOP/2 - 2f, ma5);


        float tWidth = ma5.measureText(ma5text);
        Paint ma10 = new Paint();
        ma10.setColor(k10);
        ma10.setAntiAlias(true);
        ma10.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma10text, 2f + tWidth, UPER_CHART_MARGIN_TOP/2 - 2f, ma10);

    }


    /**
     * 绘制半�?明文本�?
     *
     * @param content
     * @param canvas
     */

    private void drawAlphaYTextBox(String content, Canvas canvas) {

        if (touchPoint==null)
            return;
        Paint mPaintBox = new Paint();
        mPaintBox.setColor(mBackGround);
        //mPaintBox.setAlpha(150);

        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(ztColor);
        mPaintBoxLine.setAntiAlias(true);
        mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        Rect rect = new Rect();
        mPaintBoxLine.getTextBounds(content, 0, 1, rect);
        float strh = rect.height();


        float value = DEFAULT_AXIS_MARGIN_RIGHT_X;
        float left = super.getWidth()-value;
        float top = touchPoint.y-strh/2;
        float right = super.getWidth()-2;
        float bottom = touchPoint.y+strh/2;

        canvas.drawRect(left, top-3, right, bottom+3, mPaintBox);
        Paint borderPaint = new Paint();
        borderPaint.setColor(ztColor);
        borderPaint.setStrokeWidth(2);
        canvas.drawLine(left, top-3, left, bottom+3, borderPaint);
        canvas.drawLine(left, top-3, right, top-3, borderPaint);
        canvas.drawLine(right, bottom+3, right, top-3, borderPaint);
        canvas.drawLine(right, bottom+3, left, bottom+3, borderPaint);
        float v=DEFAULT_AXIS_MARGIN_RIGHT_X-mPaintBoxLine.measureText(deFormatNew(content,format));
        canvas.drawText(deFormatNew(content,format), left+v/2, bottom, mPaintBoxLine);
    }



    private void drawVMA(Canvas canvas) {
        if (MAVLineData == null || MAVLineData.size() < 0)
            return;

        double rate = (getHeight()-UPER_CHART_MARGIN_BOTTOM-UPER_CHART_MARGIN_TOP) / (mMaxVol - mMinVol);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MAVLineData.size(); j++) {
            MALineEntity lineEntity = MAVLineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {

                    canvas.drawLine(
                            startX,
                            startY < UPER_CHART_MARGIN_TOP?UPER_CHART_MARGIN_TOP : startY,
                            (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X  - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) (((mMaxVol - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)  + UPER_CHART_MARGIN_TOP) <UPER_CHART_MARGIN_TOP ?UPER_CHART_MARGIN_TOP : (float) ((mMaxVol - lineEntity.getLineData().get(mDataStartIndext + i)) * rate) + UPER_CHART_MARGIN_TOP ,
                            paint);


                }
                startX = (float) (super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT_X  - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxVol - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)+UPER_CHART_MARGIN_TOP;


            }
        }
    }
    public void setKDJShow() {
        this.mShowJKD = true;
        this.mShowMACD = false;
        this.mShowVol = false;
        this.mShowK=false;
        postInvalidate();
    }
    public void setKShow() {
        this.mShowJKD = false;
        this.mShowMACD = false;
        this.mShowVol = false;
        this.mShowK=true;
        postInvalidate();
    }
    public void setMACDShow() {
        this.mShowJKD = false;
        this.mShowMACD = true;
        this.mShowVol = false;
        this.mShowK=false;
        postInvalidate();
    }
    public void setVOLShow() {
        this.mShowJKD = false;
        this.mShowMACD = false;
        this.mShowVol = true;
        this.mShowK=false;
        postInvalidate();
    }
    public void setClose() {
        this.mShowMACD = false;
        this.mShowJKD = false;
        postInvalidate();
    }

    private float mDownX, mDownY;
    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private boolean isOperateKLine = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isOperateKLine = false;
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float mDistanceX = Math.abs(ev.getX() - mDownX);
                    if (isOperateKLine || mDistanceX > Math.abs(ev.getY() - mDownY) || mDistanceX >= mTouchSlop) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        isOperateKLine = true;
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isOperateKLine = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
