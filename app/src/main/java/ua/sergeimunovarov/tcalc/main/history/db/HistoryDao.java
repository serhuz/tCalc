/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class HistoryDao {

    private static final int MAX_DB_ITEMS = 50;

    private final SQLiteDatabase mDatabase;


    public HistoryDao(SQLiteDatabase database) {
        mDatabase = database;
    }


    public List<Entry> getEntries() {
        List<Entry> resultList = new ArrayList<>(MAX_DB_ITEMS);
        Cursor cursor = queryEntries();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int index;
            index = cursor.getColumnIndex(HistoryContract.HistoryEntry._ID);
            long id = cursor.getLong(index);
            index = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP);
            long timestamp = cursor.getLong(index);
            index = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION);
            String expression = cursor.getString(index);
            index = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_TYPE);
            String resultType = cursor.getString(index);
            index = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE);
            String resultValue = cursor.getString(index);
            Entry entry = Entry.create(id, expression, resultType, resultValue, timestamp);
            resultList.add(entry);
        }
        cursor.close();
        return resultList;
    }


    Cursor queryEntries() {
        return mDatabase.query(
                HistoryContract.HistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC",
                "50"
        );
    }


    public int deleteAllEntries() {
        return mDatabase.delete(HistoryContract.HistoryEntry.TABLE_NAME, "1", null);
    }


    public long addEntry(Entry entry) {
        return mDatabase.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, entry.getContentValues());
    }


    public void addEntries(List<Entry> entries) {
        mDatabase.beginTransaction();
        try {
            for (Entry entry : entries) {
                mDatabase.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, entry.getContentValues());
            }
        } finally {
            mDatabase.setTransactionSuccessful();
        }
    }
}
