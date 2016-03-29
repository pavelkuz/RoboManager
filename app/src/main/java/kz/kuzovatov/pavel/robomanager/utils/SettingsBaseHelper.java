package kz.kuzovatov.pavel.robomanager.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String SETTINGS_DB = "SETTINGS_DB";
    private static final String TABLE_NAME = "connection_properties";
    private static final String TABLE_CREATE = "CREATE TABLE " + SETTINGS_DB + "." + TABLE_NAME + ";";

    SettingsBaseHelper(Context context) {
            super(context, SETTINGS_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }
}
