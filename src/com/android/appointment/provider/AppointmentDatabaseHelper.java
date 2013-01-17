package com.android.appointment.provider;

import com.android.appointment.provider.DatabaseExtra.CONVERSATION_TABLE;
import com.android.appointment.provider.DatabaseExtra.FAVORITE_ADDRESS_TABLE;
import com.android.appointment.provider.DatabaseExtra.RECIPIENT_TABLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppointmentDatabaseHelper extends SQLiteOpenHelper {

    private static AppointmentDatabaseHelper sInstance;

    public AppointmentDatabaseHelper(Context context) {
        super(context, DatabaseExtra.DATABASE_NAME, null, DatabaseExtra.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CONVERSATION_TABLE.NAME + "(\n" +
                CONVERSATION_TABLE._ID + " INTEGER PRIMARY KEY,\n" +
                CONVERSATION_TABLE.CONVERSATION_ID + " INTEGER NOT NULL,\n" +
                CONVERSATION_TABLE.DESCRIPTION + " TEXT,\n" +
                CONVERSATION_TABLE.DATE + " INTEGER DEFAULT 0,\n" +
                CONVERSATION_TABLE.LONGITUDE + " DOUBLE DEFAULT NULL,\n" +
                CONVERSATION_TABLE.LATITUDE + " DOUBLE DEFAULT NULL,\n" +
                CONVERSATION_TABLE.LOCATION_DESCRIPTION + " TEXT,\n" +
                CONVERSATION_TABLE.BOX + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + RECIPIENT_TABLE.NAME + "(\n" +
                RECIPIENT_TABLE._ID + " INTEGER PRIMARY KEY,\n" +
                RECIPIENT_TABLE.PARTICIPANT + " TEXT,\n" +
                RECIPIENT_TABLE.SHARE_WITH_OTHERS + " INTEGER,\n" +
                RECIPIENT_TABLE.CONVERSATION_ID + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITE_ADDRESS_TABLE.NAME + "(\n" +
                FAVORITE_ADDRESS_TABLE._ID + " INTEGER PRIMARY KEY,\n" +
                FAVORITE_ADDRESS_TABLE.LONGITUDE + " DOUBLE DEFAULT NULL,\n" +
                FAVORITE_ADDRESS_TABLE.LATITUDE + " DOUBLE DEFAULT NULL,\n" +
                FAVORITE_ADDRESS_TABLE.LOCATION_DESCRIPTION + " TEXT)");

        createDeleteRecipientTrigger(db);
    }

    /**
     * Trigger for delete recipients when delete conversation
     *
     * @param db
     */
    private void createDeleteRecipientTrigger(SQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER delete_recipients_on_conversation_delete\n" +
                "AFTER DELETE ON " + CONVERSATION_TABLE.NAME + "\n" +
                "BEGIN\n" + "DELETE FROM " + RECIPIENT_TABLE.NAME + "\n" +
                "WHERE " + RECIPIENT_TABLE.CONVERSATION_ID + " = old." + CONVERSATION_TABLE.CONVERSATION_ID + ";\n" +
                "END;\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static synchronized AppointmentDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppointmentDatabaseHelper(context);
        }
        return sInstance;
    }

}
