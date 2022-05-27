package com.wlcookies.fundemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.wlcookies.fundemo.db.MyDb;

public class TestContentProvider extends ContentProvider {

    private static final String AUTHORITIES = "com.wlcookies.fundemo.testcontentprovider";
    private static UriMatcher uriMatcher;
    private static final int TAB_CODE = 1;

    private MyDb myDb;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "tab", TAB_CODE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        String type = "";
        switch (uriMatcher.match(uri)) {
            case TAB_CODE:
                type = "vnd.android.cursor.dir/vnd.com.wlcookies.fundemo.testcontentprovider.tab";
                break;
            default:
                break;
        }
        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = myDb.getWritableDatabase();
        long insert = database.insert(MyDb.TAB_NAME, null, values);
        return Uri.parse("content://" + AUTHORITIES + "/tab/" + insert);
    }

    @Override
    public boolean onCreate() {
        myDb = MyDb.getInstance(getContext(), MyDb.TAB_NAME, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = myDb.getWritableDatabase();

        return database.query(MyDb.TAB_NAME, projection, selection, selectionArgs, null, null, sortOrder);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = myDb.getWritableDatabase();
        String id = uri.getPathSegments().get(1);
        int update = database.update(MyDb.TAB_NAME, values, "_ID = ?", new String[]{id});
        getContext().getContentResolver().notifyChange(Uri.parse("content://" + AUTHORITIES + "/tab"), null);
        return 0;
    }
}