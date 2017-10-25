/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class HistoryDbHelperTest {

    private Context mContext;
    private HistoryDbHelper mHistoryDbHelper;


    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mHistoryDbHelper = new HistoryDbHelper(mContext);
    }


    @Test
    public void createDb() throws Exception {
        SQLiteDatabase database = mHistoryDbHelper.getWritableDatabase();

        assertThat(database.getVersion()).isEqualTo(HistoryDbHelper.DB_VERSION);
        assertThat(mHistoryDbHelper.getDatabaseName()).isEqualTo(HistoryDbHelper.DB_NAME);
    }


    @Test
    public void clearData() throws Exception {
        SQLiteDatabase database = mHistoryDbHelper.getWritableDatabase();

        mHistoryDbHelper.clearData();

        Cursor cursor = database.rawQuery("SELECT * FROM " + HistoryContract.HistoryEntry.TABLE_NAME, null);
        assertThat(cursor.getCount()).isEqualTo(0);
    }


    @Test
    public void cleanDb() throws Exception {
        SQLiteDatabase database = mHistoryDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION, "1+2");
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_TYPE, "value");
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE, "3");

        database.beginTransaction();
        for (int i = 0; i < 60; i++) {
            cv.put(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP, (long) i);
            database.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();

        Cursor cursor = database.rawQuery("SELECT * FROM " + HistoryContract.HistoryEntry.TABLE_NAME, null);
        assertThat(cursor.getCount()).isEqualTo(50);
    }


    @After
    public void tearDown() throws Exception {
        mHistoryDbHelper.close();
        mContext.deleteDatabase(HistoryDbHelper.DB_NAME);
    }
}
