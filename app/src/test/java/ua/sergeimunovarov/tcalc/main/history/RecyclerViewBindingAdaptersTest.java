/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.support.v7.widget.RecyclerView;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.ops.Result;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RecyclerViewBindingAdaptersTest {

    @Test
    public void setItems() {
        RecyclerView recyclerView = mock(RecyclerView.class);
        HistoryAdapter adapter = mock(HistoryAdapter.class);
        when(recyclerView.getAdapter()).thenReturn(adapter);
        Entry item = new Entry("2+2", Result.ResultType.RESULT_OK, "4", 1000, 0);

        List<Entry> items = Collections.singletonList(item);
        RecyclerViewBindingAdapters.setHistoryItems(recyclerView, items);

        verify(adapter).setItems(eq(items));
    }
}
