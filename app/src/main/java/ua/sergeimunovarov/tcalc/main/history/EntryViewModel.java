/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;


public class EntryViewModel {

    private final HistoryEntryClickListener mClickListener;


    public EntryViewModel(HistoryEntryClickListener clickListener) {
        mClickListener = clickListener;
    }


    public void insert(Entry item) {
        mClickListener.onInsert(item);
    }
}
