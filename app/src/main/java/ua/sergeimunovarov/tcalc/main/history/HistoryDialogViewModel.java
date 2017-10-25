/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import ua.sergeimunovarov.tcalc.main.history.listeners.DialogDismissListener;


public class HistoryDialogViewModel extends BaseObservable {

    public final ObservableBoolean mProgressVisible = new ObservableBoolean(true);
    public final ObservableBoolean mEmptyViewVisible = new ObservableBoolean(false);
    public final ObservableBoolean mEntriesVisible = new ObservableBoolean(false);

    private final DialogDismissListener mListener;


    public HistoryDialogViewModel(@NonNull DialogDismissListener listener) {
        mListener = listener;
    }


    public void dismiss() {
        mListener.onDismiss();
    }
}
