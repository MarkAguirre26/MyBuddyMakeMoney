package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "carddb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CardContract.CardEntry.TABLE_NAME + " (" +
                    CardContract.CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CardContract.CardEntry.COLUMN_NAME + " TEXT," +
                    CardContract.CardEntry.COLUMN_PREDICTION + " TEXT," +
                    CardContract.CardEntry.COLUMN_BRAIN + " TEXT," +
                    CardContract.CardEntry.COLUMN_INITIALIZE + " TEXT," +
                    CardContract.CardEntry.COLUMN_WAIT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CardContract.CardEntry.TABLE_NAME;

    public CardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
