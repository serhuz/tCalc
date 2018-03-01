/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.actions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import ua.sergeimunovarov.tcalc.main.BaseObservableViewModel;


public class ActionsModel extends BaseObservableViewModel {

    public final ObservableField<String> mResultFormat;

    @NonNull
    private final ActionListener mListener;


    public ActionsModel(@NonNull String initialFormat,
                        @NonNull ActionListener listener) {
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


    public static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final String mInitialFormat;
        @NonNull
        private final ActionListener mListener;


        public Factory(@NonNull String initialFormat,
                       @NonNull ActionListener listener) {
            mInitialFormat = initialFormat;
            mListener = listener;
        }


        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ActionsModel(mInitialFormat, mListener);
        }
    }



    public interface ActionListener {

        void onOpenMenu(View view);

        void onCopyContent();

        void onToggleHistory();

        void onSelectResultFormat();
    }
}
