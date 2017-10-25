/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import ua.sergeimunovarov.tcalc.main.history.db.HistoryDao;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;


public class HistoryDaoLoader extends AsyncTaskLoader<HistoryDao> {

    @NonNull
    private final HistoryDbHelper mDbHelper;


    public HistoryDaoLoader(Context context,
                            @NonNull HistoryDbHelper dbHelper) {
        super(context);
        mDbHelper = dbHelper;
    }


    @Override
    public HistoryDao loadInBackground() {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return new HistoryDao(database);
    }
}
