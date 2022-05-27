package com.wlcookies.fundemo.ui.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.db.MyDb;
import com.wlcookies.fundemo.provider.TestContentProvider;

public class TestContentProviderActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, TestContentProviderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_content_provider);

//        ContentValues contentValues = new ContentValues();
//        contentValues.put("mode", "2");
//        getContentResolver().insert(Uri.parse("content://com.wlcookies.fundemo.testcontentprovider/tab"), contentValues);

        EditText modeEt = findViewById(R.id.mode_et);
        Button updateBt = findViewById(R.id.update_bt);

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mode = modeEt.getText().toString();
                if (!TextUtils.isEmpty(mode)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("mode", mode);
                    getContentResolver().update(Uri.parse("content://com.wlcookies.fundemo.testcontentprovider/tab/1"), contentValues, null, null);
                }
            }
        });
    }
}