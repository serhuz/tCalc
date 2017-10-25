/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history.db;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;

import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import ua.sergeimunovarov.tcalc.main.ops.Result;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class HistoryDaoTest {

    private Context mContext;
    private HistoryDbHelper mDbHelper;
    private HistoryDao mDao;


    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mDbHelper = new HistoryDbHelper(mContext);
        mDao = new HistoryDao(mDbHelper.getWritableDatabase());
    }


    @After
    public void tearDown() throws Exception {
        mDbHelper.close();
        mContext.deleteDatabase(HistoryDbHelper.DB_NAME);
    }


    @Test
    public void addEntry() throws Exception {
        Entry expected = Entry.create(null, "1+1", Result.ResultType.RESULT_OK_VALUE.toString(), "2", System.currentTimeMillis());
        mDao.addEntry(expected);

        Cursor cursor = mDao.queryEntries();
        assertThat(cursor.getCount()).isEqualTo(1);
        assertThat(cursor.getColumnNames()).containsExactlyInAnyOrder(
                HistoryContract.HistoryEntry._ID,
                HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION,
                HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_TYPE,
                HistoryContract.HistoryEntry.COLUMN_NAME_RESULT_VALUE,
                HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP
        );

        int index = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_EXPRESSION);
        assertThat(index).isNotEqualTo(-1);

        cursor.moveToFirst();
        assertThat(cursor.getString(index)).isEqualTo(expected.expression());
    }


    @Test
    public void addEntries() throws Exception {
        Entry first = Entry.create(null, "1+1", Result.ResultType.RESULT_OK_VALUE.toString(), "2", System.currentTimeMillis());
        Entry second = Entry.create(null, "10:00/2", Result.ResultType.RESULT_OK.toString(), "10:00:00", System.currentTimeMillis() + 1_000L);
        List<Entry> entries = Arrays.asList(first, second);

        mDao.addEntries(entries);

        assertThat(mDao.getEntries()).isNotNull().hasSize(2).has(new Condition<List<? extends Entry>>() {
            @Override
            public boolean matches(List<? extends Entry> value) {
                return value.get(0).resultValue().contentEquals(second.resultValue())
                        && value.get(0).timestamp() == second.timestamp()
                        && value.get(1).resultValue().contentEquals(first.resultValue())
                        && value.get(1).timestamp() == first.timestamp();
            }
        });
    }


    @Test
    public void getEntries() throws Exception {
        Entry entry = Entry.create(null, "1+1", Result.ResultType.RESULT_OK_VALUE.toString(), "2", System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            mDao.addEntry(entry);
        }

        List<Entry> entries = mDao.getEntries();
        assertThat(entries).hasSize(10).has(new Condition<List<? extends Entry>>() {
            @Override
            public boolean matches(List<? extends Entry> value) {
                for (Entry current : value) {
                    if (!current.expression().contentEquals(entry.expression())
                            && current.timestamp() == entry.timestamp()
                            && current.resultType().contentEquals(entry.resultType())
                            && current.resultValue().contentEquals(entry.resultValue())) {
                        return false;
                    }
                }
                return true;
            }
        });
    }


    @Test
    public void deleteEntries() throws Exception {
        Entry entry = Entry.create(null, "1+1", Result.ResultType.RESULT_OK_VALUE.toString(), "2", System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            mDao.addEntry(entry);
        }

        int size = mDao.getEntries().size();
        int deleted = mDao.deleteAllEntries();

        assertThat(deleted).isEqualTo(size);
    }
}
