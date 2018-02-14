package com.example.farooqi.imortandexportvcf.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farooqi.imortandexportvcf.MainActivity;
import com.example.farooqi.imortandexportvcf.data.ExportVCF;
import com.example.farooqi.imortandexportvcf.utils.NetworkUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class ExportTask extends AsyncTask<Void, Void, File> {

    ProgressBar proBar;
    Context context;
    public ExportTask(Context context, ProgressBar proBar) {
        this.proBar = proBar;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        proBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected File doInBackground(Void... voids) {
        File file = null;
        ExportVCF export = new ExportVCF(context);
        try {
            file = export.getVcardString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected void onPostExecute(File file) {
        proBar.setVisibility(View.INVISIBLE);
        try {
            NetworkUtils.exportVCFToServer(file, new NetworkUtils.TaskComplete() {
                @Override
                public void onTaskCompleted(String result, String fileName) {
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    MainActivity main = new MainActivity();
                    main.setFileName(fileName);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
