package com.dysen.common_library.utils.http;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Xml;

import com.dysen.common_library.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/7/11.
 */

public class OkHttpUtils {

    /**
     * 线程池，所有请求都在线程池中
     */
    private static ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private static final String TAG = "HttpThread";
    public static String jsonData;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

    /**
     * intergrated get请求
     * @param url
     * @param params
     * @param networkResponse
     * @return
     */
    public static Runnable sendRequestGet(final String url, final String params, final CommonInterface.NetworkResponse
            networkResponse) {
        if (networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                //创建一个Request
                final Request request = new Request.Builder()
                        .url(url + params)
//                .url("https://github.com/hongyangAndroid")
                        .build();

                LogUtils.d("http", "sendRequest:" + url + params);
                //new call
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        networkResponse.onFailure(e, "访问失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            integrated(response.body().toString(), networkResponse);//intergrated 方式
                        } catch (Exception e) {
                            e.printStackTrace();
                            networkResponse.onFailure(e, "响应请求数据处理异常！");
                        }
                    }
                });
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    /**
     *  http 请求
     * @param url
     * @param params
     * @param networkResponse
     * @param bl true xml转换 ||| false json 转换
     * @return
     */
    public static Runnable sendRequestGet(final String url, final String params, final boolean bl, final CommonInterface.NetworkResponse
            networkResponse) {
        if (networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                //创建一个Request
                final Request request = new Request.Builder()
                        .url(url + params)
//                .url("https://github.com/hongyangAndroid")
                        .build();

                LogUtils.d("http", "sendRequest:" + url + params);
                //new call
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        networkResponse.onFailure(e, "访问失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
//                        LogUtils.d("http", "sendRequest:" + String.valueOf(response));

                        String tmpBody = null;
                        try {
                            if (bl) {//xml 格式
                                tmpBody = getJsonString(response.body().byteStream(), "UTF-8");
                            } else {//json 格式
                                tmpBody = response.body().string();
                            }
                            LogUtils.d("http", "Response completed: " + tmpBody);
                            networkResponse.onSuccess(0, tmpBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            networkResponse.onFailure(e, "响应请求数据处理异常！");
                        }
                    }
                });
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    private static void integrated(String tmpBody, CommonInterface.NetworkResponse networkResponse) throws JSONException {

        int state = 0;
        try {
            state = getJsonObject(tmpBody).getInt("s");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (state == 1)
            networkResponse.onSuccess(0, tmpBody);
        else if (state != 1) {
            String str = "";
            try {
                JSONObject jsonObject = getJsonObject(tmpBody);
                str = jsonObject.get("m").toString();
            } catch (JSONException e1) {
                e1.printStackTrace();
                str = "Json 转换异常!";
            }
            networkResponse.onFailure(null, str);
        }
    }

    /**
     * 把 xml 格式转换成 json 格式
     *
     * @param inputStream 获得请求响应 转换成流 如response.body().byteStream()
     * @param type        转换编码格式 如 utf-8
     * @return
     * @throws Exception
     */
    public static String getJsonString(InputStream inputStream, String type) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, type);

        int event = parser.getEventType();// 产生第一个事件
        String tmpJsonString = null;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("getJSONReturn".equals(parser.getName())) {// 判断开始标签元素是否是student
                        tmpJsonString = parser.nextText();// 得到student标签的属性值，并设置student的id
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }
        return tmpJsonString;
    }

    public static Runnable sendRequestWithOkHttp(final String url, final JSONObject obj, final
    CommonInterface.NetworkResponse networkResponse) {
        if (networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, String.valueOf(obj));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)//请求地址
                            .post(body)//post 请求参数
                            .build();
                    LogUtils.d("http", "sendRequest:" + url + obj.toString());
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    LogUtils.d("http", "Response completed: " + responseData);

                    networkResponse.onSuccess(0, responseData);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "MalformedURLException");
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "SocketTimeoutException");
                } catch (Exception e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "IOException");
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    public static Runnable sendRequestWithOkHttp(final String url, final String param, final
    CommonInterface.NetworkResponse networkResponse) {
        if (networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, param);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)//请求地址
                            .post(body)//post 请求参数
                            .build();
                    LogUtils.d("http", "sendRequest:" + url + param);
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    LogUtils.d("http", "Response completed: " + responseData);

//                    networkResponse.onSuccess(0, responseData);

                    integrated(responseData, networkResponse);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "MalformedURLException");
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "SocketTimeoutException");
                } catch (Exception e) {
                    e.printStackTrace();
                    networkResponse.onFailure(e, "IOException");
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    /**
     * 取出字符串里某个json 对象 并返回这个对象
     *
     * @param jsonData
     * @param name
     * @return
     * @throws JSONException
     */
    public static JSONObject getJsonObject(String jsonData, String... name) throws JSONException {
        if (!TextUtils.isEmpty(jsonData) && jsonData != null) {
            JSONObject json = new JSONObject(jsonData);
            if (name.length > 0) {
                JSONObject jsonObject = json.optJSONObject(name[0]);
//                LogUtils.d("http response parse", "jsonObject: " + json.toString());
                return jsonObject;
            } else
                return json;
        } else
            return null;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String name) throws JSONException {
        if (jsonObject != null) {
            JSONArray jsonArray = jsonObject.getJSONArray(name);
//            LogUtils.d("http response parse", "jsonArray: " + jsonArray.toString());
            return jsonArray;
        } else
            return null;
    }

    /**
     * json字符串 转成实体类
     *
     * @param <T>
     * @param jsonData
     * @return
     */
    public static <T> List<T> parseList(String jsonData, Class<T> cls) {
        if (!TextUtils.isEmpty(jsonData) || jsonData != null) {
            Gson gson = new Gson();
            List<T> list = new ArrayList<T>();
            JsonArray arry = new JsonParser().parse(jsonData).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
            return list;
        } else
            return null;
    }

    public static <T> T parseObject(String jsonData, Class<T> cls) {
        if (!TextUtils.isEmpty(jsonData) || jsonData != null) {
            Gson gson = new Gson();
            T t = gson.fromJson(jsonData, cls);
            return t;
        } else
            return null;
    }

    /**
     * 下载文件
     * @param url   服务器地址
     * @param filePath      文件地址
     * @param networkResponse   回调
     * @return  请求对象
     */
    public static Runnable downloadFile(final String url, final String filePath, final CommonInterface.NetworkResponse
            networkResponse) {
        if ("".equals(url) || "".equals(filePath) || networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {

                //创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                //创建一个Request
                final Request request = new Request.Builder()
                        .url(url)
//                .url("https://github.com/hongyangAndroid")
                        .build();

                LogUtils.d("http", "sendRequest:" + url);
                //new call
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        networkResponse.onFailure(e, "访问失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
//                        LogUtils.d("http", "sendRequest:" + String.valueOf(response));

                        String tmpBody = null;
                        BufferedOutputStream outputStream = null;
                        try {
                            File file = new File(filePath);
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();
                            int readFileCount = 0;
                            byte[] buffer = new byte[1024];


                            while ((readFileCount = response.body().byteStream().read(buffer, 0, 1024)) != -1){

                                outputStream.write(buffer, 0, readFileCount);
                            }
                            outputStream.flush();
                            networkResponse.onSuccess(200, filePath);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            networkResponse.onFailure(e, "MalformedURLException");
                        } catch (IOException e) {
                            e.printStackTrace();
                            networkResponse.onFailure(e, "IOException");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } finally {
                            try {

                                if (outputStream != null)
                                    outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    /**
     * 上传文件
     * @param url 服务器地址
     * @param params 参数
     * @param file 文件
     * @param networkResponse 回调
     * @return 请求对象
     */
    public static Runnable uploadFile(final String url, @Nullable String params,
                                      final File file, final CommonInterface.NetworkResponse
                                              networkResponse) {
        if ("".equals(url) || file == null || networkResponse == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {

                    OkHttpClient client = new OkHttpClient();
                    // 构建请求 Body
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM);
                    builder.addFormDataPart("platform", "Android");
                    builder.addFormDataPart("File", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    // 构建一个请求
                    Request request = new Request.Builder()
                            .url(url)//请求地址
                            .post(builder.build()).build();//post 请求参数
//                    Response response = client.newCall(request).execute();
                    // new RealCall 发起请求
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            networkResponse.onFailure(e, "");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            response.body().string();
                            try {
                                networkResponse.onSuccess(call.hashCode(), response.body().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            }
        };
        mThreadPool.execute(task);
        return task;
    }

    public static boolean stopRequest(Runnable task) {
        return mThreadPool.remove(task);
    }

}
