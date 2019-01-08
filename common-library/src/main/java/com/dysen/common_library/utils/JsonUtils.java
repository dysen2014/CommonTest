package com.dysen.common_library.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.dysen.kdemo
 * @email dy.sen@qq.com
 * created by dysen on 2018/12/19 - 5:14 PM
 * @info
 */
public class JsonUtils {
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
}
