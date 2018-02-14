package com.example.farooqi.imortandexportvcf.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.farooqi.imortandexportvcf.data.ImportVCFStream;
import com.example.farooqi.imortandexportvcf.data.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class NetworkUtils {
    public interface ResponseListener {
        void onResponseReceived(List<User> list);
    }

    public interface TaskComplete {
        void onTaskCompleted(String result, String fileName);
    }

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    static String result = "";
    static File newFile = null;

     List<String> list;
     List<User> userList;


     public NetworkUtils() {
         list = new ArrayList<>();
         userList = new ArrayList<>();
     }

    public static void exportVCFToServer(File file, final TaskComplete task) throws FileNotFoundException {
        final RequestParams params = new RequestParams();
        params.put("userid", "89");
        params.put("userfile", file);

        RestClient.post("user/uploadvcf", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i(LOG_TAG, response.getString("filename"));
                    Log.i(LOG_TAG, response.toString());
                    String status = response.getString("status");
                    if (status.equals("1")) {
                        Log.i(LOG_TAG, "uploaded");
                        task.onTaskCompleted("uploaded", response.getString("filename"));
                    } else {
                        Log.i(LOG_TAG, "could not uploaded it");
                        task.onTaskCompleted("not uploaded", "null");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void importVCFFromServer(String url, Context context, final ResponseListener listener) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.d("Network", file.getPath() + " " + file.getName());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                newFile = new File(file.getAbsolutePath());
                try {
                    InputStream inputStream = new FileInputStream(newFile);
                    ImportVCFStream vFile = new ImportVCFStream(inputStream);
                    list.addAll(vFile.readData());
                    Log.d("Network", list.toString());
                    userList.addAll(User.extracDataFromFile(list));
                    Log.d("Network", userList.toString());
                    listener.onResponseReceived(userList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            });
    }
}
