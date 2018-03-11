/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;


public class DbOnCreateCallback extends RoomDatabase.Callback {

    private static final String SQL_CREATE_CLEAN_TRIGGER = "CREATE TRIGGER IF NOT EXISTS clean_db "
            + "AFTER INSERT ON " + HistoryContract.HistoryEntry.TABLE_NAME
            + " BEGIN "
            + "DELETE FROM " + HistoryContract.HistoryEntry.TABLE_NAME
            + " WHERE " + HistoryContract.HistoryEntry.TABLE_NAME + "." + HistoryContract.HistoryEntry._ID + " NOT IN "
            + "(SELECT " + HistoryContract.HistoryEntry.TABLE_NAME + "." + HistoryContract.HistoryEntry._ID
            + " FROM " + HistoryContract.HistoryEntry.TABLE_NAME + " ORDER BY " + HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP
            + " DESC LIMIT 50); " +
            "END;";


    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLEAN_TRIGGER);
    }
}
