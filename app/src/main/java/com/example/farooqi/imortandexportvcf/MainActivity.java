package com.example.farooqi.imortandexportvcf;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farooqi.imortandexportvcf.data.ExportVCF;
import com.example.farooqi.imortandexportvcf.data.User;
import com.example.farooqi.imortandexportvcf.tasks.ExportTask;
import com.example.farooqi.imortandexportvcf.tasks.ImportTask;
import com.example.farooqi.imortandexportvcf.utils.NetworkUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_CONTACT = 100;

    ProgressBar proBar;
    List<User> userList;
    static String fileName = "";

    public MainActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]
                            {Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_READ_CONTACT);
        }

        proBar = findViewById(R.id.pro_bar);
        userList = new ArrayList<>();
    }


    public void exportVCF(View view) {
       new ExportTask(MainActivity.this, proBar).execute();
    }

    public void importVCF(View view) {
        if (!getFileName().isEmpty()) {
            String url = "http://konnect.aptechmedia.com/uploads/89/" + getFileName();
            NetworkUtils utils = new NetworkUtils();
            utils.importVCFFromServer(url, MainActivity.this, new NetworkUtils.ResponseListener() {
                @Override
                public void onResponseReceived(List<User> list) {
                    userList.addAll(list);
                    if (userList != null && userList.size() > 0) {
                        new ImportTask(proBar, MainActivity.this).execute(userList);
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this,
                    "first export contacts", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFileName(String fName) {
        fileName = fName;
    }

    public String getFileName() {
        return fileName;
    }
}
