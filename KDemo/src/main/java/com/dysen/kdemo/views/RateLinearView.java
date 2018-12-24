package com.dysen.kdemo.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.kdemo.R;
import com.dysen.kdemo.entity.CommonBean;
import com.dysen.kdemo.utils.JsonUtils;
import com.dysen.kdemo.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dysen.kdemo.utils.SystemConfig.deFormat;

public class RateLinearView extends LinearLayout {
    private Resources mResources;
    private Context mContext;
    @BindView(R.id.bitcoin)
    TextView bitcoin;
    @BindView(R.id.usd_price)
    TextView usd_price;
    @BindView(R.id.rmb_price)
    TextView rmb_price;


    @BindView(R.id.open)
    TextView open;
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.hight)
    TextView hight;
    @BindView(R.id.low)
    TextView low;
    @BindView(R.id.rate)
    TextView rate;
    @BindView(R.id.vol)
    TextView vol;

    private String symbol = "zbbtcqc", currencyType = "QC", exchangeType = "BTC", assist = "";

    private List<CommonBean.BtcQc> tickerDatas = new ArrayList<>();

    public RateLinearView(Context context) {
        super(context);
        mContext = context;
        mResources = context.getResources();
    }

    public RateLinearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mResources = context.getResources();
        //加载自定义数据的布局
        View root = LayoutInflater.from(context).inflate(R.layout.linearlayout_rate, this, true);
        ButterKnife.bind(this, root);
        //拿到布局文件中的每一个自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.rate);

        //拿到每一个自定义属性的值
        int rate_closeColor = typedArray.getColor(R.styleable.rate_closeColor, mResources.getColor(R.color.white));//标题
        int rate_cnyColor = typedArray.getColor(R.styleable.rate_cnyColor, mResources.getColor(R.color.white));//标题
        int rate_coinNameColor = typedArray.getColor(R.styleable.rate_coinNameColor, mResources.getColor(R.color.white));//标题
        int rate_highColor = typedArray.getColor(R.styleable.rate_highColor, mResources.getColor(R.color.white));//标题
        int rate_lowColor = typedArray.getColor(R.styleable.rate_lowColor, mResources.getColor(R.color.white));//标题
        int rate_openColor = typedArray.getColor(R.styleable.rate_openColor, mResources.getColor(R.color.white));//标题
        int rate_usdColor = typedArray.getColor(R.styleable.rate_usdColor, mResources.getColor(R.color.white));//标题
        int rate_volColor = typedArray.getColor(R.styleable.rate_volColor, mResources.getColor(R.color.white));//标题
        int rate_textColor = typedArray.getColor(R.styleable.rate_textColor, mResources.getColor(R.color.white));//标题
        int rate_rateColor = typedArray.getColor(R.styleable.rate_rateColor, mResources.getColor(R.color.white));//标题


        float rate_closeSize = typedArray.getDimension(R.styleable.rate_closeSize, 12.0f);//标题
        float rate_cnySize = typedArray.getDimension(R.styleable.rate_cnySize, 12.0f);
        float rate_coinNameSize = typedArray.getDimension(R.styleable.rate_coinNameSize, 12.0f);
        float rate_highSize = typedArray.getDimension(R.styleable.rate_highSize, 12.0f);
        float rate_lowSize = typedArray.getDimension(R.styleable.rate_lowSize, 12.0f);
        float rate_openSize = typedArray.getDimension(R.styleable.rate_openSize, 12.0f);
        float rate_usdSize = typedArray.getDimension(R.styleable.rate_usdSize, 12.0f);
        float rate_volSize = typedArray.getDimension(R.styleable.rate_volSize, 12.0f);
        float rate_rateSize = typedArray.getDimension(R.styleable.rate_rateSize, 12.0f);
        float rate_textSize = typedArray.getDimension(R.styleable.rate_textSize, 12.0f);

        String rate_vol = typedArray.getString(R.styleable.rate_vol);//标题
        String rate_close = typedArray.getString(R.styleable.rate_close);//标题
        String rate_open = typedArray.getString(R.styleable.rate_open);//标题
        String rate_low = typedArray.getString(R.styleable.rate_low);//标题
        String rate_high = typedArray.getString(R.styleable.rate_high);//标题
        String rate_rate = typedArray.getString(R.styleable.rate_rate);//标题
        String rate_coinName = typedArray.getString(R.styleable.rate_coinName);//标题
        String rate_cnyPrice = typedArray.getString(R.styleable.rate_cnyPrice);//标题
        String rate_usdPrice = typedArray.getString(R.styleable.rate_usdPrice);//标题


        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

/*        int usd_text_size= (int)this.getResources().getDimension(R.dimen.usd_text_size);
        int coin_text_size= (int)this.getResources().getDimension(R.dimen.coin_text_size);
        int rmb_text_size= (int)this.getResources().getDimension(R.dimen.rmb_text_size);

        bitcoin.setTextSize(coin_text_size/fontScale);
        usd_price.setTextSize(usd_text_size/fontScale);
        rmb_price.setTextSize(rmb_text_size/fontScale);*/

        //  bitcoin.setTextSize(size);
        bitcoin.setTextSize(rate_coinNameSize / fontScale);
        bitcoin.setTextColor(rate_coinNameColor);
        bitcoin.setText(rate_coinName);

        usd_price.setTextSize(rate_usdSize / fontScale);
        usd_price.setTextColor(rate_usdColor);

        usd_price.setText(rate_usdPrice);


        rmb_price.setTextSize(rate_cnySize / fontScale);
        rmb_price.setTextColor(rate_cnyColor);
        rmb_price.setText(rate_cnyPrice);


        open.setTextSize(rate_openSize);
        open.setTextColor(rate_openColor);
        open.setText(rate_open);


        close.setTextSize(rate_closeSize);
        close.setTextColor(rate_closeColor);
        close.setText(rate_close);


        hight.setTextSize(rate_highSize);
        hight.setTextColor(rate_highColor);
        hight.setText(rate_high);


        low.setTextSize(rate_lowSize);
        low.setTextColor(rate_lowColor);
        low.setText(rate_low);

        vol.setTextSize(rate_volSize);
        vol.setTextColor(rate_volColor);
        vol.setText(rate_vol);


        rate.setTextSize(rate_rateSize);
        rate.setTextColor(rate_rateColor);
        rate.setText(rate_rate);

        //回收资源
        typedArray.recycle();

    }

    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    public void marketlist() {
        String data = Tools.getString(R.string.btc_qc);
        if (!data.isEmpty()) {
            try {
                JSONArray datas = JsonUtils.getJsonArray(new JSONObject(data), "datas");
                tickerDatas = JsonUtils.parseList(datas.toString(), CommonBean.BtcQc.class);
                for (CommonBean.BtcQc btcQc : tickerDatas) {
                    btcQc.getSymbol();
                    setTextView(btcQc);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUsdPriceTextSize(int size) {
        usd_price.setTextSize(size);
    }

    public void setTextView(CommonBean.BtcQc btcQc) {
        String str_rate = btcQc.getTicker().getRiseRate();
        String str_clo = "#08BA52";
        Drawable drawable;
        if (str_rate.indexOf("-") != -1) {
            drawable = mResources.getDrawable(R.mipmap.ico_down2_green);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rate.setText(btcQc.getTicker().getRiseRate() + "%");

            rate.setTextColor(mResources.getColor(R.color.bnt_color2));
            usd_price.setTextColor(mResources.getColor(R.color.bnt_color2));
            rmb_price.setTextColor(mResources.getColor(R.color.bnt_color2));
        } else {
            drawable = mResources.getDrawable(R.mipmap.ico_up2_red);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rate.setText("+" + btcQc.getTicker().getRiseRate() + "%");

            rate.setTextColor(mResources.getColor(R.color.bnt_color));
            usd_price.setTextColor(mResources.getColor(R.color.bnt_color));
            rmb_price.setTextColor(mResources.getColor(R.color.bnt_color));
        }

        hight.setText(deFormat(btcQc.getTicker().getHigh(), 8));
        low.setText(deFormat(btcQc.getTicker().getLow(), 8));
        vol.setText(deFormat(btcQc.getTicker().getVol(), 8));
        open.setText(deFormat(btcQc.getTicker().getBuy(), 8));
        close.setText(deFormat(btcQc.getTicker().getSell(), 8));

        bitcoin.setText(btcQc.getCoinName() + "  " + mResources.getString(R.string.trans_xj));

        usd_price.setText(currencyType + " " + deFormat(btcQc.getTicker().getLast(), 8));
        rmb_price.setText("￥ " + deFormat(btcQc.getTicker().getLastRmb(), 8));

    }
}
