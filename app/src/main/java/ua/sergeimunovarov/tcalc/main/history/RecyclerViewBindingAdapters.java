/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.databinding.BindingAdapter;
import android.support.annotation.IntDef;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import ua.sergeimunovarov.tcalc.main.history.db.Entry;


public final class RecyclerViewBindingAdapters {

    private RecyclerViewBindingAdapters() {
        throw new AssertionError();
    }


    @BindingAdapter("dividerItemDecorator")
    public static void setDividerItemDecorator(RecyclerView view,
                                               @DecoratorOrientation int orientation) {
        view.addItemDecoration(new DividerItemDecoration(view.getContext(), orientation));
    }


    @BindingAdapter("historyItems")
    public static void setHistoryItems(RecyclerView view,
                                       List<Entry> items) {
        HistoryAdapter.class.cast(view.getAdapter()).setItems(items);
        view.scrollToPosition(0);
    }


    @IntDef({DividerItemDecoration.HORIZONTAL, DividerItemDecoration.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface DecoratorOrientation {

    }
}
