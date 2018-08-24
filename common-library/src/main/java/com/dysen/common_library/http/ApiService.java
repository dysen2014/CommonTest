package com.dysen.common_library.http;

import com.dysen.common_library.bean.CommonBean;
import com.dysen.common_library.bean.TestBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * dysen
 * 2018-08-18 11:20
 * dy.sen@qq.com
 */
public interface ApiService {
    /**
     * test=================================================
     * 开始
     */
    @GET(".json") //获取
    Observable<HttpResult<TestBean>> getAppVersion();

    @POST("getCoinData") //获取IP
    Observable<HttpResult<CommonBean.TestBean>> getCoinData(@QueryMap Map<String, String> config);


}