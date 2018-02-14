package com.example.farooqi.imortandexportvcf.utils;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class RestClient {
    private static final String BASE_UTL = "http://konnect.aptechmedia.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public  void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteUrl(url), params, handler);
    }

    private static String getAbsoluteUrl(String url) {
        return BASE_UTL + url;
    }
}
