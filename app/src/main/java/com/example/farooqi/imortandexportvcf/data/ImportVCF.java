package com.example.farooqi.imortandexportvcf.data;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMSUNG on 2/14/2018.
 */

public class ImportVCF {
    Context context;

    public ImportVCF(Context context){
        this.context = context;
    }

    public String showRestul(List<User> userList) {
        int count = 0;
        for (User user : userList) {
            newContacts(user.getName(), user.getTel());
            count++;
            Log.i("MainActivity_size_count", "Count: " + count);
            if (count == userList.size()) {
                Log.i("MainActivity_size_count", "Count: " + count);
                break;
            }
        }
        return "contact imported";
    }

    private void newContacts(String name, String tel) {
        //  Initializing a ContentProviderOperation object
        ArrayList<ContentProviderOperation> op = new ArrayList<>();
        int index = op.size();
        op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        );

        op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, tel)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());


        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
