package com.dysen.common_library.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.common_library.R;
import com.dysen.common_library.adapter.recycler.StringUtils;
import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.StatusBarUtil;
import com.dysen.common_library.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/23 - 下午2:07
 * @info
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseCallback {

    /**
     * the tag for log messages
     */
    protected String TAG = getClass().getSimpleName();
    BaseActivity aty;
    protected Context mContext;
    protected Handler mHandler;

    protected TextView tvBack;
    protected TextView tvTitle;
    protected TextView tvMenu;
    protected LinearLayout vContent;
    protected LinearLayout llBg;
    protected LinearLayoutCompat llTitleBg;
    protected String defaultContent = "";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedinstancestate) {
        super.onCreate(savedinstancestate);

        setContentView(getLayoutId());

        baseInit();

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        llTitleBg.setBackgroundColor(Tools.getColor(R.color.transparent));
        initView();
    }

    /**
     * 修改状态栏的夜色
     *
     * @param colorId
     */
    protected void setStatusColor(int colorId) {

        if (llBg == null)
            llBg = findViewById(R.id.ll_bg);
        llBg.setBackgroundColor(Tools.getColor(colorId));
        StatusBarUtil.setTransparent(this);
    }

    protected void setStatusBG(int drawableId) {

        if (llBg == null)
            llBg = findViewById(R.id.ll_bg);
        llBg.setBackgroundResource(drawableId);
        StatusBarUtil.setTransparent(this);
    }

    protected void baseInit() {
        if (mHandler == null)
            mHandler = new Handler();
        mContext = aty = this;
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvMenu = findViewById(R.id.tv_menu);
        llTitleBg = findViewById((R.id.ll_title_bg));
        llBg = findViewById((R.id.ll_bg));
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Tools.eventBusRegister(this);
    }

    @Override
    public void initView() {

    }

    /**
     * set screen view
     *
     * @param layoutResID
     */
    public void baseSetContentView(int layoutResID) {
        vContent = findViewById(R.id.v_content); //v_content是在基类布局文件中预定义的layout区域
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //通过LayoutInflater填充基类的layout区域
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResID, null);
        ButterKnife.bind(this, v);
        vContent.addView(v, layoutParams);
        setListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_base;
    }

    protected void gotoNext(Class cls, boolean... isfinish) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (isfinish.length > 0)
            if (isfinish[0])
                finish();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.eventBusUnregister(this);
    }

    protected void transAty(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void transAty(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    public int gColor(@ColorInt int colorId) {
        return Tools.getColor(colorId);
    }

    public String gString(@StringRes int stringId) {
        return Tools.getString(stringId);
    }
    public String gString(@StringRes int stringId, Object... formatArgs) {
        return Tools.getString(stringId, formatArgs);
    }
    public String[] gArrays(@ArrayRes int arrayId) {
        return Tools.getStringArray(arrayId);
    }

    protected BaseActivity sText(TextView textView, String content) {
        if (isNull(textView))
            return this;
        textView.setText(StringUtils.obtainNoNullText(content, defaultContent));
        return this;
    }

    protected BaseActivity sTextColor(TextView textView, int colorId) {
        if (isNull(textView))
            return this;
        textView.setTextColor(colorId);
        return this;
    }

    protected BaseActivity sTextSize(TextView textView, float size) {
        if (isNull(textView))
            return this;
        textView.setTextSize(size);
        return this;
    }

    public boolean isNull(Object object) {
        return object == null ? true : false;
    }

    /**
     * 打印请求返回的数据信息
     *
     * @param object
     */
    protected void printResponseData(int type, Object object) {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        //lambda$getRecommendList$2$InvitationRecordFragment
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
//            LogUtils.w("==========method==========" + stackTrace[i].getMethodName());
            if (stackTrace[i].getMethodName().contains("lambda$")) {
                methodName = stackTrace[i].getMethodName();
            }
        }
        methodName = methodName.replace("lambda$", "");
        methodName = methodName.substring(0, methodName.indexOf("$"));
        //此种创建方式可以防止转换Json时出现转义Bug
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        if (type == 1)
            LogUtils.v("请求--\t得到的数据：" + methodName + "\n-----------start--------------\n" + gson.toJson(object) + "\n-------------end-------------");
        else
            LogUtils.e("请求--\t异常的数据：" + methodName + "\n-----------start--------------\n" + gson.toJson(object) + "\n-------------end-------------");
    }
}
