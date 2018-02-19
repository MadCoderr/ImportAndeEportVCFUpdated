package com.example.farooqi.imortandexportvcf.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

    static Activity activity;

    public ExportTask(Context context, ProgressBar proBar) {
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
    protected void onPostExecute(final File file) {
        if (file != null) {
            try {
                NetworkUtils.exportVCFToServer(file, new NetworkUtils.TaskComplete() {
                    @Override
                    public void onTaskCompleted(String result) {
                        boolean check = file.getAbsoluteFile().delete();
                        if (check) Log.i("fileDel", "deleted");
                        else Log.i("fileDel", "not deleted");

                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        proBar.setVisibility(View.INVISIBLE);
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            proBar.setVisibility(View.INVISIBLE);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(context, "no contacts found", Toast.LENGTH_SHORT).show();
        }
    }
}
