package com.dysen.kdemo.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.MarketDepth;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bitbank on 2017/6/29.
 */
public class DepthView extends View {


    /* 图形填充景色 */
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final int DEFAULT_LEFT_FILL_COLOR = Color.RED;
    public static final int DEFAULT_RIGHT_FILL_COLOR = Color.GREEN;
    public static int DEFAULT_TEXT_COLOR = Color.BLACK;
    public static final float DEFAULT_STRKEWIDTH = 4.0f;
    public static final boolean isRight = true;
    private MarketDepth mMarketDepth;
    private double mMaxNum = 0.0;
    //Y轴数值个数
    private double mYNum = 4;

    //Y轴数递增值
    private double mYStep = 10;

    //X轴数递增值
    private double mXStep = 0;

    private float DEFAULT_AXIS_TITLE_SIZE = 10.0f;
    // - ask: 卖方深度
    private List<List<String>> asks = new ArrayList<>();
    /// - bid: 买方深度
    private List<List<String>> bids = new ArrayList<>();

    //默认背景颜色
    private int bgColor = DEFAULT_BG_COLOR;

    //默认字体颜色
    private int textColor = DEFAULT_BG_COLOR;

    //默认边框颜色
    private int kbColor = DEFAULT_BG_COLOR;
    //y轴标签
    private List<String> mYAxisLabels;
    //x轴的标签
    private List<String> mXAxisLabels;


    //买盘坐标点
    private List<PointF> mAsksPoints;
    //卖盘坐标
    private List<PointF> mBidsPoints;
    private boolean isInnerYAxis = false;                     // 是否把y坐标内嵌到图表仲
    /// 是否显示X轴标签
    private boolean showXAxisLabel = true;

    private float xBottomTitleHight = DEFAULT_AXIS_TITLE_SIZE;


    private boolean isShowYZero = true;
    //Y轴上字体的长度
    private double YTextWidth = 0;

    private int decimal = 2;                                        //小数位的长度
    //默认买卖档位10档
    private double mGear = 10;

    public void setGear(double gear) {
        this.mGear = gear;
    }

    private Context mContext;
    private DecimalFormat mDecimalFormat;
    private Paint mFillPaint;
    private Paint mTextPaint;
    private Paint mbkPaint;
    private Resources mResources;

    public DepthView(Context context) {
        super(context);
        init(context);
    }

    public DepthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DepthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        float value = spValue * fontScale + 0.5f;
        return value;
    }

    private void init(Context context) {
        mContext = context;
        mDecimalFormat = new DecimalFormat("######0.00");
        mAsksPoints = new ArrayList<PointF>();
        mBidsPoints = new ArrayList<PointF>();
        mYAxisLabels = new ArrayList();
        mXAxisLabels = new ArrayList();
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        DEFAULT_AXIS_TITLE_SIZE = sp2px(mContext, DEFAULT_AXIS_TITLE_SIZE);
        mFillPaint.setColor(DEFAULT_TEXT_COLOR);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        mResources = context.getResources();
        textColor = mResources.getColor(R.color.white);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        xBottomTitleHight = 3 * DEFAULT_AXIS_TITLE_SIZE;

        kbColor = mResources.getColor(R.color.status_bar_bg);
        mbkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbkPaint.setColor(kbColor);
        mbkPaint.setAntiAlias(true);
        mbkPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        bgColor = mResources.getColor(R.color.kbg);
    }

    public void setMarketDepthData(MarketDepth marketDepth) {
        this.mMarketDepth = marketDepth;
        findMaxNum(marketDepth);
    }


    //找到买卖盘最大数量
    private void findMaxNum(MarketDepth marketDepth) {
        mYAxisLabels.clear();
        mXAxisLabels.clear();
        asks = marketDepth.getAsks();
        bids = marketDepth.getBids();
        if (asks.size() > 0) {
            double max = 0;
            for (int i = 0; i < asks.size(); i++) {
                List<String> str = asks.get(i);
                max = max + Double.parseDouble(str.get(1));
            }
            mMaxNum = max;
            mXAxisLabels.add(asks.get(0).get(0));
            mXAxisLabels.add(asks.get(asks.size() - 1).get(0));
        }
        if (bids.size() > 0) {
            double max = 0;
            for (int i = 0; i < bids.size(); i++) {
                List<String> str = bids.get(i);
                max = max + Double.parseDouble(str.get(1));
            }
            if (max > mMaxNum) {
                mMaxNum = max;
            }
            mXAxisLabels.add(bids.get(0).get(0));
            mXAxisLabels.add(bids.get(bids.size() - 1).get(0));
        }

        Double value = Double.parseDouble(mDecimalFormat.format(mMaxNum / mYNum));
        if (isShowYZero)
            mYAxisLabels.add("0.0");
        for (int i = 1; i <= mYNum; i++) {
            String values = Double.parseDouble(mDecimalFormat.format(value * i)) + "";
            BigDecimal bigDecimal = new BigDecimal(values);

            if (bigDecimal.compareTo(new BigDecimal(1000)) >= 0) {
                values = deFormatNew(bigDecimal.divide(new BigDecimal(1000)).toString(), 2) + "K";
            }
               /* if(bigDecimal.compareTo(new BigDecimal(10000))>=0)
                {
                    values=deFormatNew(bigDecimal.divide(new BigDecimal(10000)).toString(),2)+"W";
                }*/
            mYAxisLabels.add(values);
               /* if(YTextWidth<mFillPaint.measureText(values))
                {
                    YTextWidth =mFillPaint.measureText(values);
                }*/
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private void initAsksAndBidsPoint(int viewWidth, int viewHeight) {
        if (mMarketDepth == null)
            return;
        mAsksPoints.clear();
        mBidsPoints.clear();
        asks = mMarketDepth.getAsks();
        bids = mMarketDepth.getBids();
        double askNum = 0;
        for (int i = 0; i < bids.size(); i++) {
            PointF point = new PointF();
            float x = (float) mXStep * ((float) (bids.size() - 1 - i));
            List<String> str = bids.get(i);
            askNum = askNum + Double.parseDouble(str.get(1));
            float y = (float) (viewHeight - Math.floor(askNum / mMaxNum * viewHeight));
            point.set(x, y);
            mAsksPoints.add(point);
        }
        double bidNum = 0;
        for (int i = asks.size() - 1; i >= 0; i--) {
            PointF point = new PointF();
            float x = (float) ((asks.size()) * mXStep + mXStep * (asks.size() - 1 - i));
            List<String> str = asks.get(i);
            bidNum = Double.parseDouble(str.get(1)) + bidNum;
            float y = (float) (viewHeight - Math.floor(bidNum / mMaxNum * viewHeight));
            point.set(x, y);
            mBidsPoints.add(point);
        }

    }

    public void setBgColor(int color) {
        this.bgColor = color;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }



/*   @Override
    public boolean onTouchEvent(MotionEvent event) {

        Toast.makeText(mContext,"-----X:"+event.getX()+"-----Y:"+event.getY(),Toast.LENGTH_LONG).show();
        return super.onTouchEvent(event);
    }*/

    public void clearData() {
        mMarketDepth = null;
        mAsksPoints.clear();
        mBidsPoints.clear();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mResources.getColor(R.color.kbg)); //白色背景
        if (mMarketDepth == null && mAsksPoints.size() <= 0 && mBidsPoints.size() <= 0)
            return;
        int viewHeight = getHeight() - (int) xBottomTitleHight;
        int viewWidth = getWidth();

        if ((asks.size() + bids.size()) != 2 * mGear) {
            mXStep = (float) viewWidth / (asks.size() + bids.size() - 1);
        } else {
            mXStep = (float) viewWidth / (mGear * 2 - 1);
        }
        initAsksAndBidsPoint(viewWidth, viewHeight);

        mFillPaint.setColor(mResources.getColor(R.color.dept_red));                    //设置画笔颜色
        mFillPaint.setStrokeJoin(Paint.Join.ROUND);
        mFillPaint.setStrokeWidth(DEFAULT_STRKEWIDTH);              //线宽
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        if (mAsksPoints.size() <= 0)
            return;
        Path askpath = new Path();                     //Path对象
        askpath.moveTo(mAsksPoints.get(0).x, viewHeight);
        for (int i = 0; i < mAsksPoints.size(); i++) {
            askpath.lineTo(mAsksPoints.get(i).x, mAsksPoints.get(i).y);
        }

        askpath.lineTo(mAsksPoints.get(mAsksPoints.size() - 1).x, viewHeight);
        askpath.moveTo(mAsksPoints.get(0).x, viewHeight);
        canvas.drawPath(askpath, mFillPaint);

        Paint mLeftLinePaint = new Paint();
        mLeftLinePaint.setAntiAlias(true);
        mLeftLinePaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        mLeftLinePaint.setColor(mResources.getColor(R.color.dept_red));
        mLeftLinePaint.setStrokeWidth(DEFAULT_STRKEWIDTH);              //线宽
        mLeftLinePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mAsksPoints.size(); i++) {

            if (i == 0) {
                canvas.drawLine(mAsksPoints.get(0).x, viewHeight, mAsksPoints.get(i).x, mAsksPoints.get(i).y, mLeftLinePaint);
            } else {
                canvas.drawLine(mAsksPoints.get(i - 1).x, mAsksPoints.get(i - 1).y, mAsksPoints.get(i).x, mAsksPoints.get(i).y, mLeftLinePaint);
            }
        }


        if (mBidsPoints.size() <= 0)
            return;
        mFillPaint.setColor(mResources.getColor(R.color.dept_greed));                    //设置画笔颜色
        Path bidpath = new Path();                     //Path对象
        bidpath.moveTo(mBidsPoints.get(0).x, viewHeight);
        for (int i = 0; i < mBidsPoints.size(); i++) {
            bidpath.lineTo(mBidsPoints.get(i).x, mBidsPoints.get(i).y);

        }
        bidpath.lineTo(viewWidth, viewHeight);
        bidpath.moveTo(mBidsPoints.get(0).x, viewHeight);
        canvas.drawPath(bidpath, mFillPaint);

        Paint mRightLinePaint = new Paint();
        mRightLinePaint.setAntiAlias(true);
        mRightLinePaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        mRightLinePaint.setColor(mResources.getColor(R.color.dept_greed));
        mRightLinePaint.setStrokeWidth(DEFAULT_STRKEWIDTH);              //线宽
        mRightLinePaint.setStyle(Paint.Style.FILL);
        //边界线
        for (int i = 0; i < mBidsPoints.size(); i++) {
            if (i == 0) {
                canvas.drawLine(mBidsPoints.get(0).x, viewHeight, mBidsPoints.get(i).x, mBidsPoints.get(i).y, mRightLinePaint);
            } else {
                canvas.drawLine(mBidsPoints.get(i - 1).x, mBidsPoints.get(i - 1).y, mBidsPoints.get(i).x, mBidsPoints.get(i).y, mRightLinePaint);
            }
        }

        //画X轴刻度
        if (bids.size() > 0) {
            canvas.drawText(bids.get(bids.size() - 1).get(0).toString(), mAsksPoints.get(0).x - mTextPaint.measureText(bids.get(0).get(0).toString()), viewHeight + 2 * DEFAULT_AXIS_TITLE_SIZE, mTextPaint);
            canvas.drawText(bids.get(bids.size() - 1).get(0).toString(), mAsksPoints.get(mAsksPoints.size() - 1).x, viewHeight + 2 * DEFAULT_AXIS_TITLE_SIZE, mTextPaint);
        }
        if (asks.size() > 0) {
            canvas.drawText(asks.get(0).get(0).toString(), mBidsPoints.get(0).x, viewHeight + 2 * DEFAULT_AXIS_TITLE_SIZE, mTextPaint);
            canvas.drawText(asks.get(asks.size() - 1).get(0).toString(), viewWidth - mTextPaint.measureText(asks.get(asks.size() - 1).get(0).toString()), viewHeight + 2 * DEFAULT_AXIS_TITLE_SIZE, mTextPaint);
        }


        Rect rect = new Rect();
        //画y轴刻度
        for (int i = 0; i < mYAxisLabels.size(); i++) {
            canvas.drawLine(1, viewHeight - i * (viewHeight / (mYAxisLabels.size() - 1)), viewWidth, viewHeight - i * (viewHeight / (mYAxisLabels.size() - 1)), mbkPaint);


            mTextPaint.getTextBounds(mYAxisLabels.get(mYAxisLabels.size() - 1 - i), 0, 1, rect);
            if (i != mYAxisLabels.size() - 1) {
                canvas.drawText(mYAxisLabels.get(mYAxisLabels.size() - 1 - i), (float) (viewWidth - mFillPaint.measureText(mYAxisLabels.get(mYAxisLabels.size() - 1 - i)) - 10), rect.height() + 5 + i * (viewHeight / (mYAxisLabels.size() - 1)), mTextPaint);
            } else {
                canvas.drawText(mYAxisLabels.get(mYAxisLabels.size() - 1 - i), (float) (viewWidth - mFillPaint.measureText(mYAxisLabels.get(mYAxisLabels.size() - 1 - i)) - 10), i * (viewHeight / (mYAxisLabels.size() - 1)), mTextPaint);
            }

        }


    }

    public static String deFormatNew(String str, int type) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            String str_ws = "0.#";
            if (type == -1) {
                str_ws = "0.00";
            }
            for (int n = 1; type > 1 && n < type; n++) {
                str_ws = str_ws + "#";
            }
            DecimalFormat df_ls = new DecimalFormat(str_ws);
            str = df_ls.format(bigDecimal.setScale(type, BigDecimal.ROUND_FLOOR).doubleValue());
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }
}
