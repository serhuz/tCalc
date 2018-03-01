/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.Patterns;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.ActivityMainBinding;
import ua.sergeimunovarov.tcalc.help.HelpActivity;
import ua.sergeimunovarov.tcalc.main.actions.ActionsModel;
import ua.sergeimunovarov.tcalc.main.feedback.HapticFeedback;
import ua.sergeimunovarov.tcalc.main.history.HistoryAdapter;
import ua.sergeimunovarov.tcalc.main.history.HistoryBottomSheetViewModel;
import ua.sergeimunovarov.tcalc.main.history.HistoryEntryClickListener;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;
import ua.sergeimunovarov.tcalc.main.input.BaseInputFragment;
import ua.sergeimunovarov.tcalc.main.input.CalcInputFragment;
import ua.sergeimunovarov.tcalc.main.input.InputListener;
import ua.sergeimunovarov.tcalc.main.input.PhoneInputFragment;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.settings.SettingsActivity;


public class MainActivity extends AbstractTransitionActivity implements
        FormatDialogFragment.FormatSelectionListener, InputListener,
        InsertTimeDialogFragment.TimeInsertionListener, ActionsModel.ActionListener, HistoryEntryClickListener {

    public static final String BRACKETS = "()";
    public static final char PAR_LEFT = '(';
    public static final char PAR_RIGHT = ')';

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_CALC_ERROR = "err";
    private static final String KEY_MEMORY = "mem";
    private static final String KEY_RESULT = "result";

    private static final String TAG_FORMAT = "fmt";
    private static final String TAG_INPUT = "inp";
    private static final String TAG_TSTAMP = "tstamp";

    /**
     * Clipboard label.
     */
    private static final String LABEL = MainActivity.class.getName();

    private static final int CALC_LOADER_ID = 128;

    private final ResultLoaderCallbacks mResultCallbacks = new ResultLoaderCallbacks();

    @Inject
    ApplicationPreferences preferences;

    @Inject
    EntryDao mDao;

    @Inject
    ExecutorService mIOExecutor;

    @Inject
    HapticFeedback mVibrator;

    private EditText mInputBox;
    private TextView mResultTextView;

    private int mOutputFormat;

    private boolean mCalculationError = false;
    private Result mCalculationResult = null;
    private Result mStoredResult = null;

    private ActivityMainBinding mBinding;
    private ActionsModel mActionsModel;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private HistoryBottomSheetViewModel mHistoryBottomSheetViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.getAppComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // For some reason factory is set after setText is called on TextSwitcher
        // in generated binding class causing NPE
        mBinding.actionsLayout.indicatorFormat.setFactory(
                () -> LayoutInflater
                        .from(MainActivity.this)
                        .inflate(
                                R.layout.view_indicator_format,
                                mBinding.actionsLayout.indicatorFormat,
                                false
                        )
        );

        String initialFormat = preferences.loadFormatPreferenceString();
        mActionsModel = ViewModelProviders
                .of(this, new ActionsModel.Factory(initialFormat, this))
                .get(ActionsModel.class);
        mBinding.setActionsModel(mActionsModel);

        mInputBox = mBinding.input;
        mResultTextView = mBinding.result;

        View historyRoot = mBinding.historyBottonSheet.getRoot();
        mBottomSheetBehavior = BottomSheetBehavior.from(historyRoot);

        mBinding.historyBottonSheet.historyEntries.setAdapter(new HistoryAdapter(this));

        mHistoryBottomSheetViewModel = ViewModelProviders
                .of(this, new HistoryBottomSheetViewModel.Factory(mDao))
                .get(HistoryBottomSheetViewModel.class);

        mBinding.historyBottonSheet.setModel(mHistoryBottomSheetViewModel);
        mHistoryBottomSheetViewModel.mLiveHistoryItems.observe(
                this,
                historyItems -> {
                    if (historyItems != null && !historyItems.isEmpty()) {
                        mHistoryBottomSheetViewModel.mEntries.clear();
                        mHistoryBottomSheetViewModel.mEntries.addAll(historyItems);
                    }
                }
        );

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

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

        getSupportLoaderManager().initLoader(CALC_LOADER_ID, null, mResultCallbacks);
    }


    /**
     * Inserts string into EditText element. If multiple characters inside
     * EditText are selected, they are replaced with the given character.
     *
     * @param cs text to insert
     */
    private void insertCharacter(CharSequence cs) {
        int selectionStart = mInputBox.getSelectionStart();
        int selectionEnd = mInputBox.getSelectionEnd();

        Editable editable = mInputBox.getText();
        if (selectionStart == selectionEnd) {
            editable.insert(selectionStart, cs);
        } else {
            editable.replace(selectionStart, selectionEnd, cs);
            mInputBox.setSelection(selectionEnd - (selectionEnd - selectionStart) + 1);
        }

        mVibrator.vibrate();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RESULT, mCalculationResult);
        outState.putParcelable(KEY_MEMORY, mStoredResult);
        outState.putBoolean(KEY_CALC_ERROR, mCalculationError);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCalculationError = savedInstanceState.getBoolean(KEY_CALC_ERROR);
        mCalculationResult = savedInstanceState.getParcelable(KEY_RESULT);
        mStoredResult = savedInstanceState.getParcelable(KEY_MEMORY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mOutputFormat = preferences.loadFormatPreference();

        BaseInputFragment inputFragment;
        switch (preferences.loadLayoutPreference()) {
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
                .replace(R.id.numpad_container, inputFragment, TAG_INPUT)
                .commit();
    }


    @Override
    public void onOutputFormatChanged(int which) {
        mOutputFormat = which;
        mActionsModel.mResultFormat.set(getResources().getStringArray(R.array.formats)[which]);

        // TODO add ability to change this behavior via settings
        onCalculateResult();
    }


    @Override
    public void onInputReceived(CharSequence cs) {
        insertCharacter(cs);
    }


    @Override
    public void onCalculateResult() {
        String input = mInputBox.getText().toString();
        if (!input.isEmpty()) {
            Bundle args = new Bundle();
            args.putInt(CalculationLoader.KEY_FORMAT, mOutputFormat);
            args.putString(CalculationLoader.KEY_INPUT, input);
            getSupportLoaderManager().restartLoader(CALC_LOADER_ID, args, mResultCallbacks);
        }
    }


    @Override
    public void onClearInput() {
        mInputBox.getText().clear();
        mResultTextView.setText("");
        mCalculationResult = null;
        mCalculationError = false;
        mVibrator.vibrate();
    }


    @Override
    public void onDeleteInput() {
        Editable text = mInputBox.getText();
        if (text.length() == 0) return;

        // get cursor/selection parameters
        int selectionStart = mInputBox.getSelectionStart();
        int selectionEnd = mInputBox.getSelectionEnd();
        int selectionLength = 1;

        if (selectionStart == selectionEnd) { // simple cursor
            if (selectionStart == 0) return; // cursor at the start, nothing to erase
            text.delete(selectionStart - 1, selectionStart);
        } else { // multiple character selection, delete whole selection
            text.delete(selectionStart, selectionEnd);
            selectionLength = selectionEnd - selectionStart;
        }

        mInputBox.setSelection(selectionEnd - selectionLength);
        mVibrator.vibrate();
    }


    @Override
    public void onInsertBrackets() {
        encloseSelectionInParentheses();
    }


    private void encloseSelectionInParentheses() {
        int selectionStart = mInputBox.getSelectionStart();
        int selectionEnd = mInputBox.getSelectionEnd();

        if (selectionStart == selectionEnd) {
            insertCharacter(BRACKETS);
        } else {
            StringBuilder sb = new StringBuilder(mInputBox.getText().toString());
            sb.insert(selectionStart, PAR_LEFT);
            sb.insert(selectionEnd + 1, PAR_RIGHT);
            mInputBox.setText(sb.toString());
            mInputBox.setSelection(selectionEnd + 2);
            mVibrator.vibrate();
        }
    }


    @Override
    public void onMemoryStore() {
        storeResult();
    }


    private void storeResult() {
        if (mCalculationResult != null && !mCalculationError) {
            mStoredResult = mCalculationResult;
            Toast.makeText(this, R.string.toast_ms, Toast.LENGTH_SHORT).show();
            mVibrator.vibrate();
        }
    }


    @Override
    public void onMemoryRecall() {
        if (mStoredResult != null) {
            insertStoredValue(mStoredResult);
        }
    }


    private void insertStoredValue(Result result) {
        String value;
        switch (result.type()) {
            case RESULT_OK:
                value = Converter.formatDurationHms(
                        Converter.toMillis(result.value().replaceAll("-", ""))
                );
                break;
            case RESULT_OK_VALUE:
                value = formatValue(result.value());
                break;
            default:
                return;
        }
        insertCharacter(value);
        mVibrator.vibrate();
    }


    private String formatValue(String value) {
        if (value.startsWith(Patterns.MINUS)) {
            value = PAR_LEFT + value + PAR_RIGHT;
        }
        return value;
    }


    @Override
    public void onInsertAnswer() {
        if (mCalculationResult != null && !mCalculationError) {
            insertStoredValue(mCalculationResult);
        }
    }


    @Override
    public void onTimeSelected(String timestamp) {
        insertCharacter(timestamp);
    }


    @Override
    public void onOpenMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_more);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_exit:
                    finish();
                    break;
                case R.id.action_instructions:
                    launchActivity(new Intent(MainActivity.this, HelpActivity.class));
                    break;
                case R.id.action_timestamp:
                    showDialog(InsertTimeDialogFragment.create(), TAG_TSTAMP);
                    break;
                case R.id.action_settings:
                    launchActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                default:
                    throw new IllegalStateException("Illegal item id: " + item.getItemId());
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDialog(@NonNull AppCompatDialogFragment fragment, @NonNull String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment foundFragment = fragmentManager.findFragmentByTag(tag);
        if (foundFragment != null) transaction.remove(foundFragment);

        fragment.show(transaction, tag);
    }


    @Override
    public void onCopyContent() {
        copyResultToClipboard();
    }


    /**
     * Copies calculation result to system clipboard if and only if
     * there are no calculation errors.
     */
    private void copyResultToClipboard() {
        if (!mCalculationError && mCalculationResult != null) {
            ClipData clipData = ClipData.newPlainText(LABEL, mCalculationResult.value());

            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, getString(R.string.toast_result_copied), Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "ClipboardManager is null");
            }
        }
    }


    @Override
    public void onToggleHistory() {
        int state = mBottomSheetBehavior.getState();
        mBottomSheetBehavior.setState(
                state == BottomSheetBehavior.STATE_HIDDEN ?
                        BottomSheetBehavior.STATE_COLLAPSED :
                        BottomSheetBehavior.STATE_HIDDEN
        );
    }


    @Override
    public void onSelectResultFormat() {
        showDialog(FormatDialogFragment.create(), TAG_FORMAT);
    }


    @Override
    public void onInsert(Entry item) {
        // TODO add ability to check for result time format and convert to current result format when inserting
        insertCharacter(item.getResultValue());
    }


    private class ResultLoaderCallbacks implements LoaderManager.LoaderCallbacks<Result> {

        @Override
        public Loader<Result> onCreateLoader(int id, Bundle args) {
            return new CalculationLoader(MainActivity.this, args);
        }


        @Override
        public void onLoadFinished(Loader<Result> loader, Result data) {
            if (data == null) {
                return;
            }

            switch (data.type()) {
                case RESULT_ERR:
                    mCalculationError = true;
                    mResultTextView.setText(data.value());
                    break;
                case RESULT_OK_VALUE:
                case RESULT_OK:
                    mCalculationError = false;
                    mCalculationResult = data;
                    mResultTextView.setText(getString(R.string.eq, data.value()));
                    mVibrator.vibrate();
                    mIOExecutor.execute(() -> mDao.insertItem(
                            new Entry(
                                    mInputBox.getText().toString(),
                                    data.type(),
                                    data.value(),
                                    System.currentTimeMillis()
                            )
                    ));
                    break;
            }

            getSupportLoaderManager().destroyLoader(loader.getId());
        }


        @Override
        public void onLoaderReset(Loader<Result> loader) {

        }
    }
}
