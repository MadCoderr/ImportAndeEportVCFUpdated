package com.example.farooqi.imortandexportvcf.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farooqi.imortandexportvcf.data.ImportVCF;
import com.example.farooqi.imortandexportvcf.data.User;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class ImportTask extends AsyncTask<List<User>, Void, String> {

    ProgressBar proBar;
    Context context;
    static Activity activity;

    public ImportTask(ProgressBar proBar, Context context) {
        this.proBar = proBar;
        this.context = context;
    }

    public static void setActivity(Activity act) {
        activity = act;
    }

    @Override
    protected void onPreExecute() {
        proBar.setVisibility(View.VISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected String doInBackground(List<User>[] lists) {
        ImportVCF importVCF = new ImportVCF(context);
        Log.i("check_list", lists[0].toString());
        String result = importVCF.showRestul(lists[0]);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        proBar.setVisibility(View.INVISIBLE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
