/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.actions;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;


public class ActionsModel extends BaseObservable {

    public final ObservableField<String> mResultFormat;

    @NonNull
    private final ActionListener mListener;


    public ActionsModel(@NonNull String initialFormat, @NonNull ActionListener listener) {
        mResultFormat = new ObservableField<>(initialFormat);
        mListener = listener;
    }


    public void openMenu(View view) {
        mListener.onOpenMenu(view);
    }


    public void copyContent() {
        mListener.onCopyContent();
    }


    public void toggleHistory() {
        mListener.onToggleHistory();
    }


    public void selectResultFormat() {
        mListener.onSelectResultFormat();
    }


    public interface ActionListener {

        void onOpenMenu(View view);

        void onCopyContent();

        void onToggleHistory();

        void onSelectResultFormat();
    }
}
