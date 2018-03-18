/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;


public class DbOnCreateCallback extends RoomDatabase.Callback {

    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TIMESTAMP = "ts";
    private static final String TABLE_NAME = "history";

    private static final String SQL_CREATE_CLEAN_TRIGGER = "CREATE TRIGGER IF NOT EXISTS clean_db "
            + "AFTER INSERT ON " + TABLE_NAME
            + " BEGIN "
            + "DELETE FROM " + TABLE_NAME
            + " WHERE " + TABLE_NAME + "." + _ID + " NOT IN "
            + "(SELECT " + TABLE_NAME + "." + _ID
            + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_TIMESTAMP
            + " DESC LIMIT 50); " +
            "END;";


    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLEAN_TRIGGER);
    }
}
