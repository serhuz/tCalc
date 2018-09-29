/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ua.sergeimunovarov.tcalc.main.BaseObservableViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;


public class HistoryViewModel extends BaseObservableViewModel {

    public final ObservableList<Entry> mEntries = new ObservableArrayList<>();
    public final LiveData<List<Entry>> mLiveHistoryItems;


    @SuppressWarnings("WeakerAccess")
    public HistoryViewModel(@NonNull EntryDao dao) {
        mLiveHistoryItems = dao.getAll();
    }


    public static class Factory implements ViewModelProvider.Factory {

        private final EntryDao mDao;


        public Factory(@NonNull EntryDao dao) {
            mDao = dao;
        }


        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new HistoryViewModel(mDao);
        }
    }
}
