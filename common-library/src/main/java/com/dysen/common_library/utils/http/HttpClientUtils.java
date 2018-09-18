package com.dysen.common_library.utils.http;

import android.support.annotation.Nullable;
import android.util.Log;

import com.dysen.common_library.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * HTTP请求操作类
 *
 * Created by Luis on 2018/1/30.
 */
public class HttpClientUtils {

    // post 表单参数
    private static final String PREFIX = "--";
    private static final String END = "\r\n";
    /**
     * 连接超时
     */
    private static final int CONNECT_TIMEOUT = 10000;
    /**
     * 响应超时
     */
    private static final int RESPONSE_TIMEOUT = 10000;
    /**
     * 线程池，所有请求都在线程池中
     */
    private static ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    public static Runnable get(final String url, @Nullable final Map<String, String> params,
                               final HttpRespCallback responseHandler) {
        if (responseHandler == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL urlEntity;
                    if (params != null && !params.isEmpty()) {
                        Iterator<String> iterator = params.keySet().iterator();
                        StringBuilder paramsBuffer = new StringBuilder(url);
                        paramsBuffer.append("?");
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            paramsBuffer.append(key);
                            paramsBuffer.append("=");
                            paramsBuffer.append(params.get(key));
                            paramsBuffer.append("&");
                        }
                        paramsBuffer.deleteCharAt(paramsBuffer.length() - 1);
                        urlEntity = new URL(paramsBuffer.toString());
                    } else
                        urlEntity = new URL(url);
                    LogUtils.d("HttpClientUtils", "URL is " + urlEntity.toString());
                    urlConnection = (HttpURLConnection) urlEntity.openConnection();
                    urlConnection.setDoOutput(false);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    urlConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    final StringBuilder buffer = new StringBuilder();
                    String lineString;
                    while ((lineString = reader.readLine()) != null)
                        buffer.append(lineString);
                    final int responseCode = urlConnection.getResponseCode();
//                    LogUtils.d("HttpClientUtils", "response code is " + responseCode);
//                    LogUtils.d("HttpClientUtils", "response message:" + urlConnection.getResponseMessage());
//                    LogUtils.d("HttpClientUtils", "response content:" + buffer.toString());
                    String result = buffer.toString();
                    if (result.startsWith("{") && result.endsWith("}"))
                        responseHandler.onSuccess(responseCode, new JSONObject(result));
                    else
                        responseHandler.onSuccess(responseCode, buffer.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "JSONException");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "MalformedURLException");
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "SocketTimeoutException");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "IOException");
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    public static Runnable post(final String url, @Nullable final Map<String, String> params,
                                @Nullable final JSONObject jsonObj, final HttpRespCallback responseHandler) {
        if (responseHandler == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                DataOutputStream outStream = null;
                try {
                    URL urlEntity;
                    if (params != null && !params.isEmpty()) {
                        Iterator<String> iterator = params.keySet().iterator();
                        StringBuilder paramsBuffer = new StringBuilder(url);
                        paramsBuffer.append("?");
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            paramsBuffer.append(key);
                            paramsBuffer.append("=");
                            paramsBuffer.append(params.get(key));
                            paramsBuffer.append("&");
                        }
                        paramsBuffer.deleteCharAt(paramsBuffer.length() - 1);
                        urlEntity = new URL(paramsBuffer.toString());
                    } else
                        urlEntity = new URL(url);

                    LogUtils.d("HttpClientUtils", "URL is " + urlEntity.toString());
                    urlConnection = (HttpURLConnection) urlEntity.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    urlConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
                    if (jsonObj != null) {
                        outStream = new DataOutputStream(urlConnection.getOutputStream());
                        outStream.writeBytes(jsonObj.toString());
                        outStream.flush();
                    }
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    final StringBuilder buffer = new StringBuilder();
                    String lineString;
                    while ((lineString = reader.readLine()) != null)
                        buffer.append(lineString);
                    final int responseCode = urlConnection.getResponseCode();
//                    LogUtils.d("HttpClientUtils", "response code is " + responseCode);
//                    LogUtils.d("HttpClientUtils", "response message:" + urlConnection.getResponseMessage());
//                    LogUtils.d("HttpClientUtils", "response content:" + buffer.toString());
                    String result = buffer.toString();
                    Log.i("http", "response data:"+result);
                    if (result.startsWith("{") && result.endsWith("}"))
                        responseHandler.onSuccess(responseCode, new JSONObject(result));
                    else
                        responseHandler.onSuccess(responseCode, buffer.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "JSONException");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "MalformedURLException");
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "SocketTimeoutException");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "IOException");

                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                        if (outStream != null)
                            outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    public static Runnable post2(final String url, @Nullable final String params,
                                 @Nullable final JSONObject jsonObj, final HttpRespCallback responseHandler) {
        if (responseHandler == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                DataOutputStream outStream = null;
                try {
                    URL urlEntity;
                    if (params != null && !params.isEmpty()) {
//                        Iterator<String> iterator = params.keySet().iterator();
//                        StringBuilder paramsBuffer = new StringBuilder(url);
//                        paramsBuffer.append("?");
//                        while (iterator.hasNext()) {
//                            String key = iterator.next();
//                            paramsBuffer.append(key);
//                            paramsBuffer.append("=");
//                            paramsBuffer.append(params.get(key));
//                            paramsBuffer.append("&");
//                        }
//                        paramsBuffer.deleteCharAt(paramsBuffer.length() - 1);
                        urlEntity = new URL(url+params);
                    } else
                        urlEntity = new URL(url);

                    LogUtils.d("HttpClientUtils", "URL is " + urlEntity.toString());
                    urlConnection = (HttpURLConnection) urlEntity.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    urlConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
                    if (jsonObj != null) {
                        outStream = new DataOutputStream(urlConnection.getOutputStream());
                        outStream.writeBytes(jsonObj.toString());
                        outStream.flush();
                    }
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    final StringBuilder buffer = new StringBuilder();
                    String lineString;
                    while ((lineString = reader.readLine()) != null)
                        buffer.append(lineString);
                    final int responseCode = urlConnection.getResponseCode();
//                    LogUtils.d("HttpClientUtils", "response code is " + responseCode);
//                    LogUtils.d("HttpClientUtils", "response message:" + urlConnection.getResponseMessage());
//                    LogUtils.d("HttpClientUtils", "response content:" + buffer.toString());
                    String result = buffer.toString();
                    Log.i("http", "response data:"+result);
                    if (result.startsWith("{") && result.endsWith("}"))
                        responseHandler.onSuccess(responseCode, new JSONObject(result));
                    else
                        responseHandler.onSuccess(responseCode, buffer.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "JSONException");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "MalformedURLException");
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "SocketTimeoutException");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "IOException");

                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                        if (outStream != null)
                            outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    /**
     * 下载文件
     * @param url   服务器地址
     * @param filePath      文件地址
     * @param responseHandler   回调
     * @return  请求对象
     */
    public static Runnable downloadFile(final String url, final String filePath, final HttpRespCallback responseHandler) {
        if ("".equals(url) || "".equals(filePath) || responseHandler == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                BufferedInputStream inputStream = null;
                BufferedOutputStream outputStream = null;
                try {
                    URL downloadUrl = new URL(url);
                    URLConnection connection = downloadUrl.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    connection.setRequestProperty("Charset", "UTF-8");
                    File file = new File(filePath);
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdirs();
                    byte[] buffer = new byte[1024];
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    int readFileCount = 0;
                    while ((readFileCount = inputStream.read(buffer, 0, 1024)) != -1) {
                        outputStream.write(buffer, 0, readFileCount);
                    }
                    outputStream.flush();
                    responseHandler.onSuccess(200, filePath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "MalformedURLException");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "IOException");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null)
                            inputStream.close();
                        if (outputStream != null)
                            outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
     * @param responseHandler 回调
     * @return 请求对象
     */
    public static Runnable uploadFile(final String url, @Nullable final Map<String, String> params,
                                      final File file, final HttpRespCallback responseHandler) {
        if ("".equals(url) || file == null || responseHandler == null)
            return null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                DataOutputStream outputStream = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    String boundary = UUID.randomUUID().toString().replace("-", "");
                    URL urlEntity = new URL(url);
                    LogUtils.d("HttpClientUtils", "URL is " + urlEntity.toString());
                    urlConnection = (HttpURLConnection) urlEntity.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                    urlConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("connection", "keep-alive");
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
                    // 上传文件
                    outputStream = new DataOutputStream(urlConnection.getOutputStream());
                    // 生成表单，填写参数
                    String start = PREFIX + boundary + END;
                    if (params != null && !params.isEmpty()) {
                        for (String key : params.keySet()) {
                            StringBuilder builder = new StringBuilder();
                            builder.append(start);
                            builder.append("Content-Disposition: form-data; name=\"");
                            builder.append(key);
                            builder.append("\"");
                            builder.append(END);
                            builder.append(END);
                            builder.append(params.get(key));
                            builder.append(END);
                            outputStream.write(builder.toString().getBytes());
                        }
                    }
                    StringBuilder fileBuffer = new StringBuilder();
                    fileBuffer.append(start);
                    String str = "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"";
                    fileBuffer.append(str);
                    fileBuffer.append(END);
                    fileBuffer.append("Content-Type:image/jpeg");
                    fileBuffer.append(END);
                    fileBuffer.append(END);
                    outputStream.write(fileBuffer.toString().getBytes());
                    inputStream = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    outputStream.write(END.getBytes());
                    String endString = PREFIX + boundary + PREFIX + END;
                    outputStream.write(endString.getBytes());
                    outputStream.flush();
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    final StringBuilder buffer = new StringBuilder();
                    String lineString;
                    while ((lineString = reader.readLine()) != null)
                        buffer.append(lineString);
                    final int responseCode = urlConnection.getResponseCode();
//                    LogUtils.d("HttpClientUtils", "response code is " + responseCode);
//                    LogUtils.d("HttpClientUtils", "response message:" + urlConnection.getResponseMessage());
//                    LogUtils.d("HttpClientUtils", "response content:" + buffer.toString());
                    String result = buffer.toString();
                    if (result.startsWith("{") && result.endsWith("}"))
                        responseHandler.onSuccess(responseCode, new JSONObject(result));
                    else
                        responseHandler.onSuccess(responseCode, buffer.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "MalformedURLException");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "IOException");
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseHandler.onFailure(e, "JSONException");
                } finally {
                    try {
                        if (outputStream != null)
                            outputStream.close();
                        if (inputStream != null)
                            inputStream.close();
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThreadPool.execute(task);
        return task;
    }

    public static boolean stopRequest(Runnable task) {
        return mThreadPool.remove(task);
    }

}
