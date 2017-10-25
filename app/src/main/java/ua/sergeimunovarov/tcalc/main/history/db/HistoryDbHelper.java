/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class HistoryDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "tcalc.db";
    public static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + HistoryContract.HistoryEntry.TABLE_NAME + " ("
            + HistoryContract.HistoryEntry._ID + " INTEGER PRIMARY KEY ASC, "
            + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " INTEGER NOT NULL, "
            + HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION + " TEXT NOT NULL, "
            + HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_TYPE + " TEXT NOT NULL, "
            + HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE + " TEXT NOT NULL)";

    private static final String SQL_CREATE_TIMESTAMP_INDEX = "CREATE INDEX IF NOT EXISTS "
            + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + "_index"
            + " ON " + HistoryContract.HistoryEntry.TABLE_NAME
            + " (" + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + ")";

    private static final String SQL_CREATE_CLEAN_TRIGGER = "CREATE TRIGGER IF NOT EXISTS clean_db "
            + "AFTER INSERT ON " + HistoryContract.HistoryEntry.TABLE_NAME
            + " BEGIN "
            + "DELETE FROM " + HistoryContract.HistoryEntry.TABLE_NAME
            + " WHERE " + HistoryContract.HistoryEntry.TABLE_NAME + "." + HistoryContract.HistoryEntry._ID + " NOT IN "
            + "(SELECT " + HistoryContract.HistoryEntry.TABLE_NAME + "." + HistoryContract.HistoryEntry._ID
            + " FROM " + HistoryContract.HistoryEntry.TABLE_NAME + " ORDER BY " + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP
            + " DESC LIMIT 50); " +
            "END;";


    public HistoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public void clearData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(HistoryContract.HistoryEntry.TABLE_NAME, null, null);
        onCreate(db);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(SQL_CREATE_TABLE);
            db.execSQL(SQL_CREATE_TIMESTAMP_INDEX);
            db.execSQL(SQL_CREATE_CLEAN_TRIGGER);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
