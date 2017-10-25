/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.FragmentHistoryBinding;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;
import ua.sergeimunovarov.tcalc.main.history.listeners.DialogDismissListener;
import ua.sergeimunovarov.tcalc.main.history.listeners.HistoryEntryClickListener;
import ua.sergeimunovarov.tcalc.main.history.listeners.InsertListener;


public class HistoryDialogFragment extends AppCompatDialogFragment implements DialogDismissListener, HistoryEntryClickListener {

    private static final String TAG = HistoryDialogFragment.class.getSimpleName();
    private static final int HISTORY_LOADER_ID = 129;

    @Inject
    HistoryDbHelper mDbHelper;

    private final LoaderManager.LoaderCallbacks<List<Entry>> mCallbacks = new HistoryLoaderCallbacks();

    private HistoryDialogViewModel mDialogViewModel;
    private HistoryAdapter mAdapter;
    private InsertListener mInsertListener;


    public static HistoryDialogFragment create() {
        return new HistoryDialogFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getAppComponent().inject(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mInsertListener = (InsertListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity should implement InsertListener", e);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.fragment_history, null, false
        );
        mDialogViewModel = new HistoryDialogViewModel(this);
        binding.setModel(mDialogViewModel);

        mAdapter = new HistoryAdapter(this);
        binding.historyEntries.setAdapter(mAdapter);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.DialogAnimation)
                .setTitle(R.string.title_dialog_history)
                .setView(binding.getRoot())
                .create();

        getActivity().getSupportLoaderManager().initLoader(HISTORY_LOADER_ID, null, mCallbacks);

        return alertDialog;
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }


    @Override
    public void onDismiss() {
        dismiss();
    }


    @Override
    public void onInsert(Entry entry) {
        mInsertListener.onInsert(entry);
        dismiss();
    }


    private class HistoryLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Entry>> {

        @Override
        public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
            return new HistoryLoader(getContext(), mDbHelper);
        }


        @Override
        public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
            if (data.isEmpty()) {
                mDialogViewModel.mEmptyViewVisible.set(true);
                mDialogViewModel.mEntriesVisible.set(false);
                mDialogViewModel.mProgressVisible.set(false);
            } else {
                mAdapter.setEntries(data);
                mDialogViewModel.mEmptyViewVisible.set(false);
                mDialogViewModel.mEntriesVisible.set(true);
                mDialogViewModel.mProgressVisible.set(false);
            }

            getActivity().getSupportLoaderManager().destroyLoader(loader.getId());
        }


        @Override
        public void onLoaderReset(Loader<List<Entry>> loader) {

        }
    }
}
