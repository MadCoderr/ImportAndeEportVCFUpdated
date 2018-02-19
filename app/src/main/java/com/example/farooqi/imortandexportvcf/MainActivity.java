package com.example.farooqi.imortandexportvcf;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farooqi.imortandexportvcf.data.User;
import com.example.farooqi.imortandexportvcf.tasks.ExportTask;
import com.example.farooqi.imortandexportvcf.tasks.ImportTask;
import com.example.farooqi.imortandexportvcf.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_CONTACT = 100;

    ProgressBar proBar;
    List<User> userList;

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
        ExportTask.setActivity(this);
        ImportTask.setActivity(this);
    }


    public void exportVCF(View view) {
       new ExportTask(MainActivity.this, proBar).execute();
    }


    public void importVCF(View view) {
        proBar.setVisibility(View.VISIBLE);
        MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        NetworkUtils.getFileFromServer(new NetworkUtils.FileName() {
            @Override
            public void fileName(String fileName) {
                Log.i("MainActivity", fileName);
                if (!fileName.isEmpty()) {
                    String url = "http://konnect.aptechmedia.com/uploads/89/" + fileName;
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
                    proBar.setVisibility(View.INVISIBLE);
                    MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(MainActivity.this,
                            "first export contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showSnackBar(View view) {
        Toast.makeText(this, "on click", Toast.LENGTH_SHORT).show();
    }
}
