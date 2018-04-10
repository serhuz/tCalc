/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import org.junit.Before;
import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class EntryViewModelTest {


    private HistoryAdapter.EntryClickEvent mEvent;
    private EntryViewModel mViewModel;


    @Before
    public void setUp() {
        mEvent = mock(HistoryAdapter.EntryClickEvent.class);
        mViewModel = new EntryViewModel(mEvent);
    }


    @Test
    public void insert() {
        Entry entry = mock(Entry.class);

        mViewModel.insert(entry);

        verify(mEvent).setValue(eq(entry));
    }
}
