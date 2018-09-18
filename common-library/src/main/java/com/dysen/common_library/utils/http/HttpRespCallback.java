package com.dysen.common_library.utils.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * HTTP请求回调对象
 *
 * Created by Luis on 2018/1/30.
 */
public abstract class HttpRespCallback {

	public void onSuccess(int statusCode, JSONObject response) {

	}

	public void onSuccess(int statusCode, String response) throws JSONException {

	}

	public void onSuccess(int statusCode, byte[] response) {

	}
	
	public void onFailure(Throwable e, JSONObject errorResponse) {

	}
	
	public void onFailure(Throwable e, String errorResponse) {
		
	}
	
}
