/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.listeners.HistoryEntryClickListener;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class HistoryEntryViewModelTest {


    private HistoryEntryClickListener mMock;
    private HistoryEntryViewModel mHistoryEntryViewModel;


    @Before
    public void setUp() throws Exception {
        mMock = mock(HistoryEntryClickListener.class);
        mHistoryEntryViewModel = new HistoryEntryViewModel(mMock);
    }


    @After
    public void tearDown() throws Exception {
        reset(mMock);
    }


    @Test
    public void insert() throws Exception {
        Entry entry = mock(Entry.class);

        mHistoryEntryViewModel.insert(entry);

        verify(mMock, times(1)).onInsert(eq(entry));
    }
}