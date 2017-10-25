/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDao;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;


public class HistoryLoader extends AsyncTaskLoader<List<Entry>> {

    @NonNull
    private final HistoryDbHelper mDbHelper;


    public HistoryLoader(@NonNull Context context,
                         @NonNull HistoryDbHelper dbHelper) {
        super(context);
        mDbHelper = dbHelper;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<Entry> loadInBackground() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        HistoryDao dao = new HistoryDao(database);
        return dao.getEntries();
    }
}
