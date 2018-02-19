package com.example.farooqi.imortandexportvcf.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.farooqi.imortandexportvcf.utils.NetworkUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SAMSUNG on 2/13/2018.
 */

public class ExportVCF {

    private static final String LOG_TAG = ExportVCF.class.getSimpleName();
    ArrayList<String> vCard;
    String vFile;
    String storage_path;
    Cursor cursor;

    Context context;

    public ExportVCF(Context context) {
        vCard = new ArrayList<>();
        vFile = null;
        storage_path = null;
        cursor = null;

        this.context = context;
    }

    public File getVcardString() throws FileNotFoundException {
        File file = null;
        vFile = "contacts_test.vcf";
        cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                ,null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                get(cursor);
                cursor.moveToNext();
            }

            if (!(storage_path.isEmpty()) && storage_path != null) {
                file = new File(storage_path);
                Log.i(LOG_TAG, file.getPath());

            } else {
                Log.i(LOG_TAG, "storage path is empty or null");
            }


        } else {
            Log.i(LOG_TAG, "contacts not found");
        }

        return  file;
    }


    private void get(Cursor cursor) {
        String lookUpKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookUpKey);

        AssetFileDescriptor fileDesc;
        try {
            fileDesc = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            FileInputStream fileInputStream = fileDesc.createInputStream();

            byte[] byteArr = readBytes(fileInputStream);
            int readSize = fileInputStream.read(byteArr);

            String vCardString = new String(byteArr);
            vCard.add(vCardString);

            storage_path = Environment.getExternalStorageDirectory().toString() +
                           File.separator + vFile;

            FileOutputStream fileOutputStream = new FileOutputStream(storage_path, true);
            fileOutputStream.write(vCardString.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private byte[] readBytes(InputStream stream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];


        int length = 0;
        try {
            while ((length = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
