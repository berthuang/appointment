package com.android.appointment.provider;

import com.android.appointment.provider.DatabaseExtra.FAVORITE_ADDRESS_TABLE;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class FavoriteAddress extends ContentProvider {

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_CODE_ADDRESSES = 0;

    private static final int URI_CODE_ADDRESS = 1;

    static {
        URI_MATCHER.addURI(FAVORITE_ADDRESS_TABLE.AUTHORITY, FAVORITE_ADDRESS_TABLE.NAME,
                URI_CODE_ADDRESSES);
        // In these patterns, "#" is the conversation ID.
        URI_MATCHER.addURI(FAVORITE_ADDRESS_TABLE.AUTHORITY, FAVORITE_ADDRESS_TABLE.NAME + "/#",
                URI_CODE_ADDRESS);

    }

    private AppointmentDatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = AppointmentDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case URI_CODE_ADDRESSES:
                cursor = db.query(FAVORITE_ADDRESS_TABLE.NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case URI_CODE_ADDRESS:
                String _idStr = uri.getLastPathSegment();
                String finalSelection = concatSelections(selection,
                        FAVORITE_ADDRESS_TABLE._ID + "= " + _idStr);
                cursor = db.query(FAVORITE_ADDRESS_TABLE.NAME, projection, finalSelection,
                        selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return DatabaseExtra.VND_ANDROID_DIR_APPOINTMENT;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri res = null;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            switch (URI_MATCHER.match(uri)) {
                case URI_CODE_ADDRESSES:
                    long conversationId = db.insert(FAVORITE_ADDRESS_TABLE.NAME, null, values);
                    res = ContentUris.withAppendedId(uri, conversationId);
                    break;
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(FAVORITE_ADDRESS_TABLE.CONTENT_URI, null);
            return res;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int affectedRows = 0;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            switch (URI_MATCHER.match(uri)) {
                case URI_CODE_ADDRESSES:
                    affectedRows = db.delete(FAVORITE_ADDRESS_TABLE.NAME, selection, selectionArgs);
                    break;
                case URI_CODE_ADDRESS:
                    String _idStr = uri.getLastPathSegment();
                    String finalSelection = concatSelections(selection,
                            FAVORITE_ADDRESS_TABLE._ID + "= " + _idStr);
                    affectedRows = db
                            .delete(FAVORITE_ADDRESS_TABLE.NAME, finalSelection, selectionArgs);
                    break;
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(FAVORITE_ADDRESS_TABLE.CONTENT_URI, null);
            return affectedRows;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int affectedRows = 0;
        db.beginTransaction();
        try {
            switch (URI_MATCHER.match(uri)) {
                case URI_CODE_ADDRESSES:
                    affectedRows = db.update(FAVORITE_ADDRESS_TABLE.NAME, values, selection,
                            selectionArgs);
                    break;
                case URI_CODE_ADDRESS:
                    String _idStr = uri.getLastPathSegment();
                    String finalSelection = concatSelections(selection,
                            FAVORITE_ADDRESS_TABLE._ID + "= " + _idStr);
                    affectedRows = db.update(FAVORITE_ADDRESS_TABLE.NAME, values, finalSelection,
                            selectionArgs);
                    break;
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(FAVORITE_ADDRESS_TABLE.CONTENT_URI, null);
            return affectedRows;
        } finally {
            db.endTransaction();
        }
    }

    static String concatSelections(String selection1, String selection2) {
        if (TextUtils.isEmpty(selection1)) {
            return selection2;
        } else if (TextUtils.isEmpty(selection2)) {
            return selection1;
        } else {
            return selection1 + " AND " + selection2;
        }
    }
}
