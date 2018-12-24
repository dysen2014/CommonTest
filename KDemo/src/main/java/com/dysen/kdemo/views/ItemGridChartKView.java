package com.dysen.kdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.dysen.kdemo.R;

import java.util.List;

/**
 * Created by bitbank on 2017/6/7.
 */
public class ItemGridChartKView extends View {

    // ////////////默认值////////////////
    /** 默认背景色 */
    public static final int DEFAULT_BACKGROUD = 0x090A0B;
    /** 默认字体大小 **/
    public float DEFAULT_AXIS_TITLE_SIZE =10;
    /** 虚线效果 */
    private PathEffect mDashEffect;
    /** 默认字体颜色 **/
    public static  int DEFAULT_COLOR_Front = 0x78797A;
    //经维线
    public static  int DEFAULT_COLOR_Line = 0x78797A;
    /** 横轴X刻度*/
    private List<String> axisXTitles;
    /** 纵轴Y刻度*/
    private List<String> axisYTitles;
    //Y轴在图表内部
    protected boolean isInnerYAxis = false;

    /** 默认Y轴刻度显示长度 */
    private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 12;

    /** 默认经线数 */
    public   int DEFAULT_LOGITUDE_NUM = 6;
    /** 默认维线数 */
    public   int DEFAULT_LATITUDE_NUM = 3;
    /** 上表的上间隙 */
    public  float UPER_CHART_MARGIN_TOP;
    /** 上表的下间隙 */
    public  float UPER_CHART_MARGIN_BOTTOM;

    /** 图表跟右边距离  x轴突出的位置*/
    public float DEFAULT_AXIS_MARGIN_RIGHT_X = DEFAULT_AXIS_TITLE_SIZE;
    /** 图表跟右边距离  x轴突出的位置*/
    public float DEFAULT_AXIS_MARGIN_LEFT_X = DEFAULT_AXIS_TITLE_SIZE;
    /** 图表跟右边距离 */
    public float DEFAULT_AXIS_MARGIN_RIGHT = DEFAULT_AXIS_TITLE_SIZE*3;


    // /////////////属性////////////////
    /** 背景色 */
    private int mBackGround;

    /** 上表高度 */
    public float mUperChartHeight;

    public void  setLatLine(int num)
    {
        DEFAULT_LATITUDE_NUM = num;
    }
    public void  setLogLine(int num)
    {
        DEFAULT_LOGITUDE_NUM = num;
    }
    //经线间隔度
    private float longitudeSpacing;
    //维线间隔度
    private float latitudeSpacing;
    private Context mContext;

    private boolean YisLeft = false;
    public void setYIsLeft(boolean isleft)
    {
        this.YisLeft = isleft;
    }
    private boolean LongitudeIsShow = true;
    private boolean LatitudeIsShow = true;


    /** 默认虚线效果 */
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
            new float[] { 15, 15, 15, 15 }, 15);
    private boolean showYText = true;
    public void setShowYText(boolean show)
    {
        this.showYText = show;
    }

    private boolean showXText = true;
    public void setShowXText(boolean show)
    {
        this.showXText = show;
    }

    public void setLongitudeIsShow(boolean show)
    {
        this.LongitudeIsShow = show;
    }
    public void setLatitudeIsShow(boolean show)
    {
        this.LatitudeIsShow = show;
    }
    public ItemGridChartKView(Context context) {
        super(context);
        init(context);
    }
    public ItemGridChartKView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ItemGridChartKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mBackGround = context.getResources().getColor(R.color.homekline_color);
        DEFAULT_COLOR_Front = context.getResources().getColor(R.color.kViewztblack);
        DEFAULT_COLOR_Line = context.getResources().getColor(R.color.kViewztblack);
        initSize();
        UPER_CHART_MARGIN_TOP = 4*DEFAULT_AXIS_TITLE_SIZE;
        UPER_CHART_MARGIN_BOTTOM = DEFAULT_AXIS_TITLE_SIZE*6;
        mDashEffect = DEFAULT_DASH_EFFECT;
    }

    public void initSize()
    {
        DEFAULT_AXIS_TITLE_SIZE = sp2px(mContext,DEFAULT_AXIS_TITLE_SIZE);
        DEFAULT_AXIS_MARGIN_RIGHT =  sp2px(mContext,40);
       // DEFAULT_AXIS_MARGIN_RIGHT =  0;

    }
    public void setChartBottom(float size)
    {
        this.UPER_CHART_MARGIN_BOTTOM = size;
    }
    public void setChartTop(float size)
    {
        this.UPER_CHART_MARGIN_TOP = size;
    }
    /**
     * 重新控件大�?
     */
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


public boolean haveBorder=false;
    public void setHaveBorder(boolean border)
    {
        this.haveBorder = border;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(mBackGround);

        int viewHeight = getHeight();
        int viewWidth = getWidth();
        UPER_CHART_MARGIN_TOP = 2*DEFAULT_AXIS_TITLE_SIZE;
        UPER_CHART_MARGIN_BOTTOM = 4*DEFAULT_AXIS_TITLE_SIZE;
        mUperChartHeight = viewHeight-UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM;
        //经度
        longitudeSpacing = (viewWidth - DEFAULT_AXIS_MARGIN_RIGHT-2)/DEFAULT_LOGITUDE_NUM;
        //维度#####4-8
        latitudeSpacing =(viewHeight - UPER_CHART_MARGIN_TOP-UPER_CHART_MARGIN_BOTTOM)/DEFAULT_LATITUDE_NUM;


        DEFAULT_AXIS_MARGIN_RIGHT_X = longitudeSpacing/4;
        DEFAULT_AXIS_MARGIN_LEFT_X= longitudeSpacing*3/4;

        // 绘制纬线
        drawLatitudes(canvas, viewWidth, latitudeSpacing);
        // 绘制经线
        drawLongitudes(canvas,viewHeight,longitudeSpacing);
     //   if(haveBorder)
     //   drawBorders(canvas,viewHeight,viewWidth);
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawBorders(Canvas canvas, int viewHeight, int viewWidth) {
        Paint paint = new Paint();
        paint.setColor(DEFAULT_COLOR_Line);
        paint.setStrokeWidth(1);

     //   canvas.drawLine(0, 0, 0,viewHeight, paint);
        canvas.drawLine(0, 0, viewWidth,0, paint);
     //   canvas.drawLine(viewWidth, 0, viewWidth,viewHeight, paint);

        canvas.drawLine(0,viewHeight-2, viewWidth,viewHeight-2, paint);
       // canvas.drawLine(viewWidth,0, viewWidth,viewHeight-2, paint);

    }
    /**
     * 绘制纬线
     *
     * @param canvas
     * @param viewWidth
     */
    private void drawLatitudes(Canvas canvas, int viewWidth, float latitudeSpacing ) {
        if(axisYTitles==null||axisYTitles.size()==0)
            return;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(R.color.homekline_ztcolor));
       // paint.setColor(DEFAULT_COLOR_Front);
        paint.setStrokeWidth(0.5f);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
       // paint.setPathEffect(mDashEffect);
        paint.setStyle(Paint.Style.STROKE);


        Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintzt.setColor(DEFAULT_COLOR_Front);
        paintzt.setStrokeWidth(1);
        paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

      ///  Path path = new Path();
        for (int i = 0; i <=DEFAULT_LATITUDE_NUM&&i<axisYTitles.size(); i++) {
            if(LatitudeIsShow)
            {
             /*   path.moveTo(0, UPER_CHART_MARGIN_TOP+latitudeSpacing * (i));
                path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,UPER_CHART_MARGIN_TOP  + latitudeSpacing * (i));
                canvas.drawPath(path, paint);*/
                 canvas.drawLine(0, UPER_CHART_MARGIN_TOP+latitudeSpacing * (i), viewWidth-DEFAULT_AXIS_MARGIN_RIGHT, UPER_CHART_MARGIN_TOP  + latitudeSpacing * (i), paint);
            }


                if (axisYTitles != null&&showYText) {
                    if (YisLeft) {
                        // 绘制Y刻度
                        if (axisYTitles.size() - i - 1 != 0)
                            canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), sp2px(mContext, 5), UPER_CHART_MARGIN_TOP + latitudeSpacing * (i) + DEFAULT_AXIS_TITLE_SIZE, paintzt);
                    } else {
                        // 绘制Y刻度
                        if (axisYTitles.size() - i - 1 != 0)
                        {
                            float w = paint.measureText(axisYTitles.get(axisYTitles.size() - i - 1));
                            if(w>DEFAULT_AXIS_MARGIN_RIGHT)
                            {
                                canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), viewWidth -w, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i), paintzt);
                            }else
                            {
                                canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), viewWidth -DEFAULT_AXIS_MARGIN_RIGHT, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i), paintzt);
                            }


                        }

                    }
                }
        }
        paint.setColor(mContext.getResources().getColor(R.color.text_color_gray));
        canvas.drawLine(0, 2, viewWidth, 2, paint);
        canvas.drawLine(0, getHeight()-UPER_CHART_MARGIN_BOTTOM/2, viewWidth, getHeight()-UPER_CHART_MARGIN_BOTTOM/2, paint);

    }

    /**
     * 绘制经线
     *
     * @param canvas
     */
    private void drawLongitudes(Canvas canvas, int viewHeight, float longitudeSpacing) {

        if(axisXTitles==null||axisXTitles.size()==0)
            return;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
       // paint.setColor(DEFAULT_COLOR_Line);
        paint.setStrokeWidth(0.5f);
        paint.setColor(mContext.getResources().getColor(R.color.homekline_ztcolor));
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
       // paint.setPathEffect(mDashEffect);
        paint.setStyle(Paint.Style.STROKE);

        Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintzt.setColor(DEFAULT_COLOR_Front);
        paintzt.setStrokeWidth(1);
        paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        Path path = new Path();
		for (int i = 0; i <axisXTitles.size()&&i<DEFAULT_LOGITUDE_NUM; i++) {
            float y=viewHeight-DEFAULT_AXIS_TITLE_SIZE;
            if(LongitudeIsShow)
            {
               /* path.moveTo(longitudeSpacing * i+DEFAULT_AXIS_MARGIN_LEFT_X, 0);
                path.lineTo(longitudeSpacing * i+DEFAULT_AXIS_MARGIN_LEFT_X,viewHeight-UPER_CHART_MARGIN_BOTTOM/2);
                canvas.drawPath(path, paint);*/
                canvas.drawLine(longitudeSpacing * i+DEFAULT_AXIS_MARGIN_LEFT_X, 0, longitudeSpacing * i+DEFAULT_AXIS_MARGIN_LEFT_X, viewHeight-UPER_CHART_MARGIN_BOTTOM/2, paint);
            }else
            {

                y=y-DEFAULT_AXIS_TITLE_SIZE;
            }
			if(axisXTitles!=null&&showXText) {
                float tWidth = paint.measureText(axisXTitles.get(i));
                // 绘制刻度
                    canvas.drawText(axisXTitles.get(i), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT-longitudeSpacing * (i)-tWidth, y, paintzt);


            }
		}

    }
    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return  (spValue * fontScale + 0.5f);
    }

    //获取字体大小
    public float adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth=screenWidth>screenHeight?screenWidth:screenHeight;
        float rate = ((float) screenWidth/320);
        return rate; //字体太小也不好看的
    }


    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
    }


    public float getUperChartHeight() {
        return mUperChartHeight;
    }

    public void setUperChartHeight(float UperChartHeight) {
        this.mUperChartHeight = UperChartHeight;
    }

    public int getAxisYMaxTitleLength() {
        return DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;
    }

    public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
        this.DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = axisYMaxTitleLength;
    }


    public float getLongitudeSpacing() {
        return longitudeSpacing;
    }

    public void setLongitudeSpacing(float longitudeSpacing) {
        this.longitudeSpacing = longitudeSpacing;
    }

    public float getLatitudeSpacing() {
        return latitudeSpacing;
    }

    public void setLatitudeSpacing(float latitudeSpacing) {
        this.latitudeSpacing = latitudeSpacing;
    }


    public List<String> getAxisXTitles() {
        return axisXTitles;
    }

    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }

    public List<String> getAxisYTitles() {
        return axisYTitles;
    }

    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }


    /**
     * 获取X轴刻度的百分比最大1
     *
     * @param value
     * @return
     */
    public String getAxisXGraduate(Object value) {

        float length = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT;
        float valueLength = ((Float) value).floatValue();

        return String.valueOf(valueLength / length);
    }

}
