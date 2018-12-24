package com.dysen.kdemo.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.MALineEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bitbank on 2017/6/7.
 */
public class ItemKview  extends ItemGridChartKView {

    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndext;

    /**
     * 显示的OHLC数据个数
     */
    private int mShowDataNum;

    /**
     * 当前数据的最大最小值
     */
    private double mMaxPrice;
    private double mMinPrice;

    //记录最小值
    private int minIndex;
    //记录最大值
    private int maxIndex;


    /**
     * 当前数据的最大最小值
     */
    private double mCloseMaxPrice;
    private double mCloseMinPrice;

    //记录最小值
    private int minCloseIndex;
    //记录最大值
    private int maxCloseIndex;

    /**
     * 显示纬线数
     */
    private int latitudeNum = super.DEFAULT_LATITUDE_NUM;

    /**
     * 显示经线数
     */
    private int longtitudeNum = super.DEFAULT_LOGITUDE_NUM;

    /**
     * 显示的最小Candle数
     */
    private final static int MIN_CANDLE_NUM = 10;
    /**
     * 显示的最大Candle数
     */
    private final static int MAX_CANDLE_NUM = 480;

    /**
     * 默认显示的Candle数
     */
    private final static int DEFAULT_CANDLE_NUM = 120;



    private boolean showMaxMinTextShow =true;
    public void setShowMaxMinTextShow(boolean show)
    {
        this.showMaxMinTextShow =show;
    }
    /**
     * Candle宽度
     */
    private double mCandleWidth;
    /**
     * MA数据
     */
    private List<MALineEntity> MALineData;
    /**
     * OHLC数据
     */
    private List<MarketChartData> mOHLCData = new ArrayList<MarketChartData>();

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
    public static int kline30dayline = 0x535d66;

    public static int klineRed = 0xCD1A1E;
    public static int klineGreen = 0x7AA376;


    //tpye=0为 k线 1为分时
    private int type=1;
    public void setType(int t)
    {
        this.type = t;
    }
    public int getType()
    {
        return type;
    }
    private Context mConext;
    private Resources res;

    public ItemKview(Context context) {
        super(context);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public ItemKview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public ItemKview(Context context, AttributeSet attrs, int defStyle) {
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
        klineline= res.getColor(R.color.klinered);
        kline5dayline = res.getColor(R.color.kline30dayline);
        kline10dayline = res.getColor(R.color.kline10dayline);
        kline30dayline = res.getColor(R.color.kline30dayline);
        klineRed = res.getColor(R.color.klinered);
        klineGreen = res.getColor(R.color.klinegreen);
    }
    public void setKlinelineColor(int color)
    {
        klineline= color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  initShowDataNum();
        initAxisX();
        initAxisY();
        super.onDraw(canvas);
        if(type==0)
        {
            drawUpperRegion(canvas);
            drawMA(canvas);
        }else {
          //  drawTime(canvas);
            drawCloseTime(canvas);
        }
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
            int step = (int)Math.floor(mShowDataNum/longtitudeNum);

            for (int i = 0; i < longtitudeNum && mDataStartIndext + (i)*step < mOHLCData.size(); i++) {


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

        super.setAxisXTitles(TitleX);

    }

    /**
     * 初始化Y轴
     */
    protected void initAxisY() {
        //默认保留8位
        int format=8;

        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> TitleY = new ArrayList<String>();
        float height = getUperChartHeight();
        if(height==0||height==0.0f)
            return;
        double high =0;
        double low =0;
        float average =1;
        String value="";

        //1分时 0k线
        if(type==1) {
            high = mOHLCData.get(maxCloseIndex).getClosePrice();
            low = mOHLCData.get(minCloseIndex).getClosePrice();
        }else {
            high =mOHLCData.get(maxIndex).getHighPrice();
            low =mOHLCData.get(minIndex).getLowPrice();
        }
        BigDecimal hBigDecimal=BigDecimal.valueOf(high);
        BigDecimal lBigDecimal=BigDecimal.valueOf(low);
        String maxp=deFormatNew(hBigDecimal.toString(),8);
        String minp=deFormatNew(lBigDecimal.toString(),8);

        int max=getPoint(maxp);
        int min=getPoint(minp);
        if(max>min)
        {
            format=max;
        }else
        {
            format=min;
        }
        if(format>8)
            format=8;

        //1分时 0k线
        if(type==1)
        {
            average = (float) ((mCloseMaxPrice - mCloseMinPrice) / height) / 10 * 10;

            //处理最小值
            BigDecimal bigDecimal=BigDecimal.valueOf(mCloseMinPrice);
            value=deFormatNew(bigDecimal.toString(),format);
            TitleY.add(value);

            average = average*(getUperChartHeight()/latitudeNum);

            //处理所有Y刻度
            for (float i = 0; i < latitudeNum; i++) {
                BigDecimal  mBigDecimal=BigDecimal.valueOf(mCloseMinPrice + i * average);
                value =mBigDecimal.toString() ;
                value=deFormatNew(value,format);
                TitleY.add(value);
            }
            //处理最大值
            BigDecimal maxDecimal=BigDecimal.valueOf(mCloseMaxPrice);
            value = deFormatNew(maxDecimal.toString(),format);
        }else
        {
             average = (float) ((mMaxPrice - mMinPrice) / height) / 10 * 10;

            //处理最小值
            BigDecimal bigDecimal=BigDecimal.valueOf(mMinPrice);
             value=deFormatNew(bigDecimal.toString(),format);
            TitleY.add(value);

            average = average*(getUperChartHeight()/latitudeNum);

            //处理所有Y刻度
            for (float i = 0; i < latitudeNum; i++) {
                BigDecimal  mBigDecimal=BigDecimal.valueOf(mMinPrice + i * average);
                value =mBigDecimal.toString() ;
                value=deFormatNew(value,format);
                TitleY.add(value);
            }
            //处理最大值
            BigDecimal maxDecimal=BigDecimal.valueOf(mMaxPrice);
            value = deFormatNew(maxDecimal.toString(),format);
        }

        TitleY.add(value);
        super.setAxisYTitles(TitleY);
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
            str=str;
        }
        return str;
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
        paint.setColor(super.DEFAULT_COLOR_Front);
        paint.setStyle(Paint.Style.FILL);

        float width = getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
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
                if ((i * mCandleWidth + mCandleWidth / 2) > (w + 25)) {
                    canvas.drawLine(startX, high-DEFAULT_AXIS_TITLE_SIZE , startX + 25, high-DEFAULT_AXIS_TITLE_SIZE , paint);
                    canvas.drawLine(startX, high-DEFAULT_AXIS_TITLE_SIZE , startX + 10, high-DEFAULT_AXIS_TITLE_SIZE - 10, paint);
                    canvas.drawLine(startX, high-DEFAULT_AXIS_TITLE_SIZE , startX + 10, high-DEFAULT_AXIS_TITLE_SIZE  + 10, paint);
                    canvas.drawText(deFormatNew(maxPrice,8), startX + 25, high-DEFAULT_AXIS_TITLE_SIZE  + rect.height() / 2, paint);
                } else {
                    canvas.drawLine(startX, high-DEFAULT_AXIS_TITLE_SIZE, startX - 25, high-DEFAULT_AXIS_TITLE_SIZE , paint);
                    canvas.drawLine(startX, high-DEFAULT_AXIS_TITLE_SIZE , startX - 10, high -DEFAULT_AXIS_TITLE_SIZE + 10, paint);
                    canvas.drawLine(startX, high -DEFAULT_AXIS_TITLE_SIZE, startX - 10, high -DEFAULT_AXIS_TITLE_SIZE - 10, paint);
                    canvas.drawText(deFormatNew(maxPrice,8), startX - 25 - w, high-DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                }

            }
            if (mDataStartIndext + i == minIndex) {
                String minPrice = entity.getLowPrice() + "";
                paint.getTextBounds(minPrice, 0, 1, rect);
                float w = paint.measureText(minPrice);
                //左箭头
                if ((i * mCandleWidth + mCandleWidth / 2) > (w + 25)) {
                    canvas.drawLine(startX, low+DEFAULT_AXIS_TITLE_SIZE , startX + 25, low+DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, low+DEFAULT_AXIS_TITLE_SIZE ,startX + 10, low  + 10+DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, low+DEFAULT_AXIS_TITLE_SIZE , startX + 10, low  - 10+DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawText(deFormatNew(minPrice,8), startX + 25, low  + DEFAULT_AXIS_TITLE_SIZE+rect.height() / 2 , paint);
                } else {
                    canvas.drawLine(startX, low+DEFAULT_AXIS_TITLE_SIZE  , startX - 25, low+DEFAULT_AXIS_TITLE_SIZE , paint);
                    canvas.drawLine(startX, low +DEFAULT_AXIS_TITLE_SIZE, startX - 10, low  - 10+DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, low +DEFAULT_AXIS_TITLE_SIZE, startX - 10, low  + 10+DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawText(deFormatNew(minPrice,8), startX - 25 - w, low + DEFAULT_AXIS_TITLE_SIZE+rect.height() / 2, paint);
                }
            }
        }
    }

    private void setTimeMaxMinPrice() {

        if (mOHLCData == null || mOHLCData.size() <= 0||mDataStartIndext<0) {
            return;
        }
        mMinPrice = mOHLCData.get(mDataStartIndext).getLowPrice();
        mMaxPrice = mOHLCData.get(mDataStartIndext).getHighPrice();

        minIndex = mDataStartIndext;
        maxIndex = mDataStartIndext;


        mCloseMinPrice = mOHLCData.get(mDataStartIndext).getClosePrice();
        mCloseMaxPrice = mOHLCData.get(mDataStartIndext).getClosePrice();

        minCloseIndex = mDataStartIndext;
        maxCloseIndex = mDataStartIndext;


        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndext + i);

            if (mCloseMinPrice > entity.getClosePrice()) {
                mCloseMinPrice = entity.getClosePrice();
                minCloseIndex = mDataStartIndext + i;
            }
            if (mCloseMaxPrice < entity.getClosePrice()) {
                mCloseMaxPrice = entity.getClosePrice();
                maxCloseIndex = i + mDataStartIndext;
            }

            if (mMinPrice > entity.getLowPrice()) {
                mMinPrice = entity.getLowPrice();
                minIndex = mDataStartIndext + i;
            }
            if (mMaxPrice < entity.getHighPrice()) {
                mMaxPrice = entity.getHighPrice();
                maxIndex = i + mDataStartIndext;
            }
        }
    }

    private void drawCloseTime(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() < 0)
            return;
        setTimeMaxMinPrice();
        float width = getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        double rate =1;
        if(mCloseMaxPrice!=mCloseMinPrice)
        {
            rate= (getUperChartHeight()) / (mCloseMaxPrice - mCloseMinPrice);
        }
        else {
            rate= 1;
        }
        // 绘制上部曲线图及上部分MA值
        float startX = 0;
        float startY = 0;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        //  paint.setPathEffect(new CornerPathEffect(5));
        paint.setColor(klineline);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            if (i != 0) {

                float wt = (startX + (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f)) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startY;
                p3.x = wt;
                p4.y = (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndext + i).getClosePrice()) * rate)  + UPER_CHART_MARGIN_TOP;
                p4.x = wt;

                Path path = new Path();
                path.moveTo(startX, startY);
                path.cubicTo(p3.x, p3.y, p4.x, p4.y, (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f), (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndext + i).getClosePrice()) * rate)  + UPER_CHART_MARGIN_TOP);
                 canvas.drawPath(path, paint);
            }
            startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f);
            startY = (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndext + i).getClosePrice()) * rate)  + UPER_CHART_MARGIN_TOP;
            Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintzt.setStrokeWidth(4);
            paintzt.setAntiAlias(true);
            paintzt.setColor(klineline);
            paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            paintzt.setStyle(Paint.Style.FILL);

            if(showMaxMinTextShow)
            {
                Rect rect = new Rect();

                //画最大和最小值
                if (mDataStartIndext + i == maxCloseIndex) {
                    String price = mOHLCData.get(mDataStartIndext + i).getClosePrice() + "";
                    paintzt.getTextBounds(price, 0, 1, rect);
                    float w = paintzt.measureText(price);

                    if ((startX-w) > 0) {
                        canvas.drawCircle(startX, startY, 10, paintzt);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getClosePrice()+"",8),startX-w,startY-DEFAULT_AXIS_TITLE_SIZE+5,paintzt);
                    }else {
                        canvas.drawCircle(startX, startY, 10, paintzt);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getClosePrice()+"",8),startX,startY-DEFAULT_AXIS_TITLE_SIZE+5,paintzt);
                    }

                }

                if (mDataStartIndext + i == minCloseIndex) {
                    String price = mOHLCData.get(mDataStartIndext + i).getClosePrice() + "";
                    paintzt.getTextBounds(price, 0, 1, rect);
                    float w = paintzt.measureText(price);


                    if ((startX+w) > super.getWidth()) {
                        canvas.drawCircle(startX, startY, 10, paintzt);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getClosePrice()+"",8),startX-w,startY+4*DEFAULT_AXIS_TITLE_SIZE/3,paintzt);
                    }else {
                        canvas.drawCircle(startX, startY, 10, paintzt);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getClosePrice()+"",8),startX,startY+4*DEFAULT_AXIS_TITLE_SIZE/3,paintzt);
                    }

                }
            }
        }

    }


    private void drawTime(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() < 0)
            return;
        setTimeMaxMinPrice();
        float width = getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        double rate =1;
        if(mMaxPrice!=mMinPrice)
        {
            rate= (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        }
        else {
            rate= 1;
        }
        // 绘制上部曲线图及上部分MA值
        float startX = 0;
        float startY = 0;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(klineline);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);


        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            if (i != 0) {
                canvas.drawLine(
                        startX,
                        startY < UPER_CHART_MARGIN_TOP ? UPER_CHART_MARGIN_TOP : startY,
                        (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f),
                        (float) (((mMaxPrice - mOHLCData.get(mDataStartIndext + i).getHighPrice()) * rate)  + UPER_CHART_MARGIN_TOP) > super.getHeight()-UPER_CHART_MARGIN_BOTTOM ?
                                super.getHeight()-UPER_CHART_MARGIN_BOTTOM:
                                (float) ((mMaxPrice - mOHLCData.get(mDataStartIndext + i).getHighPrice()) * rate)  + UPER_CHART_MARGIN_TOP,
                        paint);
            }
            startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f);
            startY = (float) ((mMaxPrice - mOHLCData.get(mDataStartIndext + i).getHighPrice()) * rate)  + UPER_CHART_MARGIN_TOP;

            if(showMaxMinTextShow)
            {
                Rect rect = new Rect();

                //画最大和最小值
                if (mDataStartIndext + i == maxIndex) {
                    String price = mOHLCData.get(mDataStartIndext + i).getHighPrice() + "";
                    paint.getTextBounds(price, 0, 1, rect);
                    float w = paint.measureText(price);

                    if ((startX-w) > 0) {
                        canvas.drawCircle(startX, startY, 10, paint);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getHighPrice()+"",8),startX-w,startY-DEFAULT_AXIS_TITLE_SIZE+5,paint);
                    }else {
                        canvas.drawCircle(startX, startY, 10, paint);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getHighPrice()+"",8),startX,startY-DEFAULT_AXIS_TITLE_SIZE+5,paint);
                    }

                   /* canvas.drawCircle(startX, startY, 10, paint);
                    canvas.drawText(mOHLCData.get(mDataStartIndext + i).getHighPrice()+"",startX-2*DEFAULT_AXIS_TITLE_SIZE,startY+2*DEFAULT_AXIS_TITLE_SIZE,paint);*/

                }

                if (mDataStartIndext + i == minIndex) {
                    String price = mOHLCData.get(mDataStartIndext + i).getLowPrice() + "";
                    paint.getTextBounds(price, 0, 1, rect);
                    float w = paint.measureText(price);


                    if ((startX+w) > super.getWidth()) {
                        canvas.drawCircle(startX, startY, 10, paint);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getLowPrice()+"",8),startX-w,startY+DEFAULT_AXIS_TITLE_SIZE,paint);
                    }else {
                        canvas.drawCircle(startX, startY, 10, paint);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndext + i).getLowPrice()+"",8),startX,startY+DEFAULT_AXIS_TITLE_SIZE,paint);
                    }

                  /*  canvas.drawCircle(startX, startY, 10, paint);
                    canvas.drawText(mOHLCData.get(mDataStartIndext + i).getHighPrice()+"",startX+DEFAULT_AXIS_TITLE_SIZE,startY,paint);*/
                }
            }
        }

    }

    private void drawMA(Canvas canvas) {
        if (MALineData == null || MALineData.size() < 0)
            return;

        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MALineData.size(); j++) {
            MALineEntity lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i <  mShowDataNum && mDataStartIndext + i < mOHLCData.size() ; i++) {
                if (i != 0) {
                    canvas.drawLine(
                            startX,
                            startY < UPER_CHART_MARGIN_TOP ? UPER_CHART_MARGIN_TOP : startY,
                            (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f),
                            (float) (((mMaxPrice - lineEntity.getLineData().get(i)) * rate)  + UPER_CHART_MARGIN_TOP) > super.getHeight()-UPER_CHART_MARGIN_BOTTOM ?
                                    super.getHeight()-UPER_CHART_MARGIN_BOTTOM:
                                    (float) ((mMaxPrice - lineEntity.getLineData().get(i)) * rate)  + UPER_CHART_MARGIN_TOP,
                            paint);
                }
                startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT  - mCandleWidth * i - mCandleWidth * 0.5f);
                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(i)) * rate)  + UPER_CHART_MARGIN_TOP;
            }
        }
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
    private void setCurrentData() {

        initShowDataNum();
        setTimeMaxMinPrice();
        setMaxMinPrice();
    }


    private void setMaxMinPrice() {

        if (mOHLCData == null || mOHLCData.size() <= 0||mDataStartIndext<0) {
            return;
        }
        mMinPrice = mOHLCData.get(mDataStartIndext).getLowPrice();
        mMaxPrice = mOHLCData.get(mDataStartIndext).getHighPrice();

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
        }
    }

    public void setOHLCData(List<MarketChartData> OHLCData) {

        //分时，小时切换，重置  mDataStartIndext
        mDataStartIndext = 0;
        mShowDataNum = DEFAULT_CANDLE_NUM;
        minCloseIndex=0;
        maxCloseIndex=0;
        minIndex=0;
        maxIndex=0;
        if (OHLCData == null || OHLCData.size() <= 0) {
            return;
        }

        if (null != mOHLCData) {
            mOHLCData.clear();
        }
        for (MarketChartData e : OHLCData) {
            addData(e);
        }
        initMALineData();
        setCurrentData();
        postInvalidate();
    }
    public void clearOHLCData() {
        if (null != mOHLCData) {
            mOHLCData.clear();
            postInvalidate();
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

    //K线均线
    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(kline30dayline);
        MA5.setLineData(initMA(mOHLCData, 5));

        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(kline10dayline);
        MA10.setLineData(initMA(mOHLCData, 10));

        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);

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

}


