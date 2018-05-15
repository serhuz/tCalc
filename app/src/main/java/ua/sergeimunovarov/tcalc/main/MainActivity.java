/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.ActivityMainBinding;
import ua.sergeimunovarov.tcalc.di.annotation.HistoryFactory;
import ua.sergeimunovarov.tcalc.di.annotation.MainActivityFactory;
import ua.sergeimunovarov.tcalc.help.HelpActivity;
import ua.sergeimunovarov.tcalc.main.history.HistoryAdapter;
import ua.sergeimunovarov.tcalc.main.history.HistoryViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;
import ua.sergeimunovarov.tcalc.main.input.BaseInputFragment;
import ua.sergeimunovarov.tcalc.main.input.CalcInputFragment;
import ua.sergeimunovarov.tcalc.main.input.PhoneInputFragment;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.settings.SettingsActivity;


public class MainActivity extends AbstractTransitionActivity implements
        InsertTimeDialogFragment.TimeInsertionListener {

    private static final String LABEL = MainActivity.class.getName();
    private static final int CALC_LOADER_ID = 128;

    private final ResultLoaderCallbacks mResultCallbacks = new ResultLoaderCallbacks();

    @Inject
    ApplicationPreferences mPreferences;

    @Inject
    EntryDao mDao;

    @Inject
    ExecutorService mIOExecutor;

    @Inject
    @MainActivityFactory
    ViewModelProvider.Factory mMainActivityFactory;

    @Inject
    @HistoryFactory
    ViewModelProvider.Factory mHistoryFactory;

    private ActivityMainBinding mBinding;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private HistoryViewModel mHistoryViewModel;
    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.getAppComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initViewModel();
        initInput();
        initHistory();

        getSupportLoaderManager().initLoader(CALC_LOADER_ID, null, mResultCallbacks);
    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mMainActivityFactory).get(MainActivityViewModel.class);
        mViewModel.mFormatPref.observe(this, format -> {
            mViewModel.mResultFormat.set(format);
            Editable editable = mViewModel.mEditableInput.get();
            if (editable != null /* can be null when launching */
                    && mPreferences.isRecalculateEnabled()) {
                calculateResult(editable.toString());
            }
        });
        mViewModel.mCalculateResultEvent.observe(this, this::calculateResult);
        mViewModel.mMenuClickEvent.observe(this, id -> {
            if (id == null) return;
            switch (id) {
                case R.id.action_exit:
                    finish();
                    break;
                case R.id.action_instructions:
                    launchActivity(new Intent(MainActivity.this, HelpActivity.class));
                    break;
                case R.id.action_timestamp:
                    showDialog(InsertTimeDialogFragment.create(), InsertTimeDialogFragment.class.getSimpleName());
                    break;
                case R.id.action_settings:
                    launchActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                default:
                    throw new IllegalStateException("Illegal item id");
            }
        });
        mViewModel.mInteractionEvent.observe(this, interaction -> {
            //noinspection ConstantConditions
            switch (interaction) {
                case COPY:
                    copyResult();
                    break;
                case TOGGLE_HISTORY:
                    toggleHistoryVisibility();
                    break;
                case MEMORY_STORE:
                    mViewModel.storeCurrentResult();
                    break;
                case INSERT_ANSWER:
                    mViewModel.insertAnswer();
                    break;
                case MEMORY_RECALL:
                    mViewModel.insertStoredValue();
                    break;
                case OPEN_FORMAT_DIALOG:
                    openFormatDialog();
                    break;
                default:
                    throw new IllegalStateException();
            }
        });

        mBinding.setViewModel(mViewModel);
    }


    private void initInput() {
        mPreferences.getLayout().observe(this, layoutId -> {
            BaseInputFragment inputFragment;
            //noinspection ConstantConditions
            switch (layoutId) {
                case ApplicationPreferences.LayoutConstants.LAYOUT_OLD:
                    inputFragment = PhoneInputFragment.create();
                    break;
                case ApplicationPreferences.LayoutConstants.LAYOUT_NEW:
                    inputFragment = CalcInputFragment.create();
                    break;
                default:
                    throw new IllegalStateException("Unrecognized preference");
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.numpad_container, inputFragment, inputFragment.getClass().getSimpleName())
                    .commit();
        });
    }


    private void initHistory() {
        View historyRoot = mBinding.historyBottomSheet.getRoot();

        mBottomSheetBehavior = BottomSheetBehavior.from(historyRoot);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        HistoryAdapter adapter = new HistoryAdapter();
        adapter.mEntryClickEvent.observe(this, entry -> {
            if (entry != null) {
                mViewModel.insertEntry(entry);
            }
        });

        mBinding.historyBottomSheet.historyEntries.setAdapter(adapter);

        mHistoryViewModel = ViewModelProviders.of(this, mHistoryFactory).get(HistoryViewModel.class);
        mHistoryViewModel.mLiveHistoryItems.observe(
                this,
                historyItems -> {
                    if (historyItems != null && !historyItems.isEmpty()) {
                        mHistoryViewModel.mEntries.clear();
                        mHistoryViewModel.mEntries.addAll(historyItems);
                    }
                }
        );

        mBinding.historyBottomSheet.setModel(mHistoryViewModel);

        mBinding.numpadContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBinding.numpadContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        int height = mBinding.numpadContainer.getHeight();

                        ViewGroup.LayoutParams layoutParams = historyRoot.getLayoutParams();
                        layoutParams.height = height;
                        historyRoot.setLayoutParams(layoutParams);
                        historyRoot.requestLayout();

                        mBottomSheetBehavior.setPeekHeight(height);
                    }
                }
        );
    }


    public void calculateResult(String expression) {
        if (!expression.isEmpty()) {
            Bundle args = new Bundle();
            args.putInt(CalculationLoader.KEY_FORMAT, mPreferences.loadFormatPreference());
            args.putString(CalculationLoader.KEY_INPUT, expression);
            getSupportLoaderManager().restartLoader(CALC_LOADER_ID, args, mResultCallbacks);
        }
    }


    private void showDialog(@NonNull AppCompatDialogFragment fragment, @NonNull String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment foundFragment = fragmentManager.findFragmentByTag(tag);
        if (foundFragment != null) transaction.remove(foundFragment);

        fragment.show(transaction, tag);
    }


    private void copyResult() {
        boolean calculationError = mViewModel.mCalculationError.get();
        Result result = mViewModel.mCalculationResult.get();
        if (!calculationError && result != null) {
            ClipData clipData = ClipData.newPlainText(LABEL, result.value());

            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, R.string.toast_result_copied, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.toast_result_not_copied, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void toggleHistoryVisibility() {
        int state = mBottomSheetBehavior.getState();
        mBottomSheetBehavior.setState(
                state == BottomSheetBehavior.STATE_HIDDEN ?
                        BottomSheetBehavior.STATE_COLLAPSED :
                        BottomSheetBehavior.STATE_HIDDEN
        );
    }


    public void openFormatDialog() {
        showDialog(FormatDialogFragment.create(), FormatDialogFragment.class.getSimpleName());
    }


    @Override
    public void onTimeSelected(String time) {
        mViewModel.insertCurrentTime(time);
    }


    private class ResultLoaderCallbacks implements LoaderManager.LoaderCallbacks<Result> {

        @NonNull
        @Override
        public Loader<Result> onCreateLoader(int id, Bundle args) {
            return new CalculationLoader(MainActivity.this, args);
        }


        @Override
        public void onLoadFinished(@NonNull Loader<Result> loader, Result data) {
            if (data != null) {
                mViewModel.setCalculationResult(data);
                if (data.type() != Result.ResultType.RESULT_ERR) {
                    mIOExecutor.execute(() -> {
                        Entry last = mDao.getLast();
                        Entry current = new Entry(
                                data.expression(),
                                data.type(),
                                data.value(),
                                System.currentTimeMillis(),
                                mPreferences.loadFormatPreference()
                        );
                        if (last == null || !last.sameContents(current)) {
                            mDao.insertItem(current);
                        }
                    });
                }
            }

            getSupportLoaderManager().destroyLoader(loader.getId());
        }


        @Override
        public void onLoaderReset(@NonNull Loader<Result> loader) {

        }
    }
}
