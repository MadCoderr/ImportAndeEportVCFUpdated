package com.example.farooqi.imortandexportvcf.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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

    public ImportTask(ProgressBar proBar, Context context) {
        this.proBar = proBar;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        proBar.setVisibility(View.VISIBLE);
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

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
