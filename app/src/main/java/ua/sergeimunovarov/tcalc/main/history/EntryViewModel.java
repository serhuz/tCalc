/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;


public class EntryViewModel {

    private final HistoryAdapter.EntryClickEvent mEvent;


    public EntryViewModel(HistoryAdapter.EntryClickEvent event) {
        mEvent = event;
    }


    public void insert(Entry item) {
        mEvent.setValue(item);
    }
}
