package com.wlcookies.fundemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDb extends SQLiteOpenHelper {

    private volatile static MyDb myDb;

    private MyDb(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    public static MyDb getInstance(@Nullable Context context, @Nullable String name, int version) {
        if (myDb == null) {
            synchronized (MyDb.class) {
                if (myDb == null) {
                    myDb = new MyDb(context, name, version);
                }
            }
        }
        return myDb;
    }

    public static final String TAB_NAME = "tab";
    public static final String TAB_ID = "_ID";
    public static final String TAB_MODE = "mode";

    private static final String CREATE_TAB = "CREATE TABLE " + TAB_NAME + " ( " +
            TAB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TAB_MODE + " TEXT " +
            ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
