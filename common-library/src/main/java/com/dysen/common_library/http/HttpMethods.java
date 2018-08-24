package com.dysen.common_library.http;

import com.dysen.common_library.base.AppContext;
import com.dysen.common_library.bean.CommonBean;
import com.dysen.common_library.bean.TestBean;
import com.dysen.common_library.subscribers.ProgressSubscriber;
import com.dysen.common_library.subscribers.ProgressSubscriber2;
import com.dysen.common_library.subscribers.ProgressSubscriber3;
import com.dysen.common_library.ui.UIHelper;
import com.dysen.common_library.utils.LogUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dysen.common_library.utils.Tools.runOnUiThread;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 */
public class HttpMethods {
    public static String BASE_URL = "";

    private Retrofit retrofit;
    private ApiService apiService;

    //构造方法私有
    private HttpMethods() {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized void dstroyInstance() {

    }

    public Map<String, String> paramPut(List<String> map, List<String> list) {
        Map<String, String> param = new HashMap<String, String>();
        int select_index = 0;
        for (String str : map) {
            try {
                param.put(str, list.get(select_index));
                select_index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return param;

    }

    private static HttpMethods trans_instance = null;

    public static HttpMethods getInstanceTrans(String url) {
        if (trans_instance == null) {
            synchronized (HttpMethods.class) {
                if (trans_instance == null) {
                    BASE_URL = url;
                    trans_instance = new HttpMethods();
                }
            }
        }
        return trans_instance;
    }

    /**
     * 获取App 版本
     */
    public void getAppVersion(ProgressSubscriber2<TestBean> subscriber) {
        Observable observable = apiService.getAppVersion().map(new HttpResultFunc2());
        toSubscribe2(observable, subscriber);
    }

    /*private <T> void toSubscribe2(observable<T> o, Observer<T> t){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(t);
    }
    */
    private <T> void toSubscribe(Observable<HttpResult<T>> o, ProgressSubscriber<T> t) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(t);
    }

    private <T> void toSubscribe2(Observable<T> o, ProgressSubscriber2<T> t) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(t);
    }

    private void toMsgSubscribe(Observable o, ProgressSubscriber3 t) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(t);
    }

    /**
     * 不处理HttpResult<T> 直接返回
     */
    private class HttpResultFunc<T> implements Function<HttpResult<T>, HttpResult<T>> {
        @Override
        public HttpResult<T> apply(@NonNull HttpResult<T> tHttpResult) throws ApiException {
            LogUtils.e("tHttpResult", tHttpResult + "");
            String code = tHttpResult.getResMsg().getCode();
            String message = tHttpResult.getResMsg().getMessage();
            String method = tHttpResult.getResMsg().getMethod();
            //授权失败
            if (HttpParam.AUTHFAIL.equals(code)) {
                authfail();
            }
            if (HttpParam.SUCCESS.equals(code)) {

            } else {
                //throw new ApiException(method+"_"+message);
            }
            return tHttpResult;
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc2<T> implements Function<HttpResult<T>, T> {
        @Override
        public T apply(@NonNull HttpResult<T> tHttpResult) throws ApiException {
            LogUtils.e("tHttpResult", tHttpResult + "");
            String code = tHttpResult.getResMsg().getCode();
            String message = tHttpResult.getResMsg().getMessage();
            String method = tHttpResult.getResMsg().getMethod();
            //授权失败
            if (HttpParam.AUTHFAIL.equals(code)) {
                authfail();
            }
            if (HttpParam.SUCCESS.equals(code)) {
                toastMessage(method, message);
                return tHttpResult.getDatas();
            } else {
                throw new ApiException(message + "__" + method);
            }
        }
    }

    private class HttpResultMsgFunc implements Function<HttpResult, ResMsg> {
        @Override
        public ResMsg apply(@NonNull HttpResult tHttpResult) throws ApiException {
            LogUtils.e("tHttpResult", tHttpResult + "");
            String code = tHttpResult.getResMsg().getCode();
            String message = tHttpResult.getResMsg().getMessage();
            String method = tHttpResult.getResMsg().getMethod();
            //授权失败
            if (HttpParam.AUTHFAIL.equals(code)) {
                authfail();
            }
            if (HttpParam.SUCCESS.equals(code)) {
                return tHttpResult.getResMsg();
            } else {
                throw new ApiException(message + "__" + method);
            }
        }
    }


    /**
     * 不处理ResultMsg 直接返回
     */
    private class HttpResultMsgFunc2 implements Function<HttpResult, ResMsg> {
        @Override
        public ResMsg apply(@NonNull HttpResult tHttpResult) throws ApiException {
            LogUtils.e("tHttpResult", tHttpResult + "");
            String code = tHttpResult.getResMsg().getCode();
            String message = tHttpResult.getResMsg().getMessage();
            String method = tHttpResult.getResMsg().getMethod();
            //授权失败
            if (HttpParam.AUTHFAIL.equals(code)) {
                authfail();
            }
            if (HttpParam.SUCCESS.equals(code)) {

            } else {
                //throw new ApiException(method+"_"+message);
            }
            return tHttpResult.getResMsg();
        }
    }

    private void authfail() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIHelper.ToastMessage(AppContext.getInstance(), "授权失败");
                }
            });
        } catch (Exception e) {

        }
    }

    private void toastMessage(final String method, final String message) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] strArr = {"sendCode"};
                    for (String str_ls : strArr) {
                        if (str_ls.toLowerCase().equals(method.toLowerCase())) {
                            UIHelper.ToastMessage(AppContext.getInstance(), message);
                        }
                    }
                }
            });

        } catch (Exception e) {

        }
    }
}
