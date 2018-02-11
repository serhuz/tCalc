/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.Patterns;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.ActivityMainBinding;
import ua.sergeimunovarov.tcalc.help.HelpActivity;
import ua.sergeimunovarov.tcalc.main.actions.ActionsModel;
import ua.sergeimunovarov.tcalc.main.history.HistoryDaoLoader;
import ua.sergeimunovarov.tcalc.main.history.HistoryDialogFragment;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDao;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;
import ua.sergeimunovarov.tcalc.main.history.listeners.InsertListener;
import ua.sergeimunovarov.tcalc.main.input.BaseInputFragment;
import ua.sergeimunovarov.tcalc.main.input.CalcInputFragment;
import ua.sergeimunovarov.tcalc.main.input.InputListener;
import ua.sergeimunovarov.tcalc.main.input.PhoneInputFragment;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.settings.SettingsActivity;


public class MainActivity extends AbstractTransitionActivity implements
        FormatDialogFragment.FormatSelectionListener, InputListener,
        InsertTimeDialogFragment.TimeInsertionListener, InsertListener, ActionsModel.ActionListener {

    public static final String BRACKETS = "()";
    public static final char PAR_LEFT = '(';
    public static final char PAR_RIGHT = ')';

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_CALC_ERROR = "err";
    private static final String KEY_MEMORY = "mem";
    private static final String KEY_RESULT = "result";
    private static final String KEY_PENDING = "pending";

    private static final String TAG_FORMAT = "fmt";
    private static final String TAG_INPUT = "inp";
    private static final String TAG_TSTAMP = "tstamp";
    private static final String TAG_HISTORY = "hist";

    /**
     * Clipboard label.
     */
    private static final String LABEL = MainActivity.class.getName();

    private static final int CALC_LOADER_ID = 128;
    private static final int DAO_LOADER_ID = 130;

    private final ResultLoaderCallbacks mResultCallbacks = new ResultLoaderCallbacks();
    private final HistoryDaoLoaderCallbacks mDaoCallbacks = new HistoryDaoLoaderCallbacks();

    @Inject
    ApplicationPreferences preferences;

    @Inject
    HistoryDbHelper mDbHelper;

    private List<Entry> mPendingEntries;

    private EditText mInputBox;
    private TextView mResultTextView;

    private boolean mHasPermanentMenuKey;

    private Vibrator mVibrator;

    private int mOutputFormat;
    private boolean mVibroEnabled;
    private int mVibroDuration;

    private boolean mCalculationError = false;
    private Result mCalculationResult = null;
    private Result mStoredResult = null;

    private HistoryDao mDao;
    private ActivityMainBinding mBinding;
    private ActionsModel mActionsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.getAppComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // For some reason factory is set after setText on TextSwitcher in binding causing NPE
        mBinding.actionsLayout.indicatorFormat.setFactory(
                () -> LayoutInflater
                        .from(MainActivity.this)
                        .inflate(
                                R.layout.view_indicator_format,
                                mBinding.actionsLayout.indicatorFormat,
                                false
                        )
        );
        mActionsModel = new ActionsModel(
                getResources().getStringArray(R.array.formats)[preferences.loadFormatPreference()],
                this
        );
        mBinding.setActionsModel(mActionsModel);

        mInputBox = mBinding.input;
        mResultTextView = mBinding.result;

        getSupportLoaderManager().initLoader(CALC_LOADER_ID, null, mResultCallbacks);
        getSupportLoaderManager().initLoader(DAO_LOADER_ID, null, mDaoCallbacks);

        mHasPermanentMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();

        configureInputBehavior();
        configureActionBar();

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (savedInstanceState == null) {
            mPendingEntries = new ArrayList<>();
        } else {
            mPendingEntries = savedInstanceState.getParcelableArrayList(KEY_PENDING);
        }
        mDao = new HistoryDao(mDbHelper.getWritableDatabase());
    }


    private void configureInputBehavior() {
        mInputBox.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mInputBox.setTextIsSelectable(true);
    }


    /**
     * Configures ActionBar visibility for landscape and portrait orientations.
     */
    private void configureActionBar() {
        int orientation = getResources().getConfiguration().orientation;
        ActionBar bar = getActionBar();
        if (bar != null) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (getActionBar().isShowing() && mHasPermanentMenuKey) getActionBar().hide();
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (!getActionBar().isShowing()) getActionBar().show();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RESULT, mCalculationResult);
        outState.putParcelable(KEY_MEMORY, mStoredResult);
        outState.putBoolean(KEY_CALC_ERROR, mCalculationError);
        outState.putParcelableArrayList(KEY_PENDING, (ArrayList<? extends Parcelable>) mPendingEntries);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCalculationError = savedInstanceState.getBoolean(KEY_CALC_ERROR);
        mCalculationResult = savedInstanceState.getParcelable(KEY_RESULT);
        mStoredResult = savedInstanceState.getParcelable(KEY_MEMORY);
    }

    private void showDialog(@NonNull AppCompatDialogFragment fragment, @NonNull String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment foundFragment = fragmentManager.findFragmentByTag(tag);
        if (foundFragment != null) transaction.remove(foundFragment);

        fragment.show(transaction, tag);
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
    protected void onResume() {
        super.onResume();

        mVibroEnabled = preferences.loadVibrationPreference();
        mVibroDuration = preferences.loadVibrationDurationPreference();
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

        Log.d(TAG, "Input fragment is " + inputFragment.getClass().getSimpleName());
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
        vibrate();
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
        this.vibrate();
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
            vibrate();
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
            vibrate();
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
        vibrate();
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

        this.vibrate();
    }


    private void vibrate() {
        if (mVibroEnabled && mVibrator.hasVibrator()) {
            mVibrator.vibrate(mVibroDuration);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            onOpenMenu(mBinding.actionsLayout.btnMenu);
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onTimeSelected(String timestamp) {
        insertCharacter(timestamp);
    }


    @Override
    public void onInsert(Entry entry) {
        insertCharacter(entry.resultValue());
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


    @Override
    public void onCopyContent() {
        copyResultToClipboard();
    }


    @Override
    public void onToggleHistory() {
        // TODO show history in bottom sheet
        showDialog(HistoryDialogFragment.create(), TAG_HISTORY);
    }


    @Override
    public void onSelectResultFormat() {
        showDialog(FormatDialogFragment.create(), TAG_FORMAT);
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
                    vibrate();
                    Entry entry = Entry.create(null,
                            mInputBox.getText().toString(),
                            data.type().toString(),
                            data.value(),
                            System.currentTimeMillis(
                            ));
                    if (mDao != null) {
                        mDao.addEntry(entry);
                    } else {
                        mPendingEntries.add(entry);
                    }
                    break;
            }

            getSupportLoaderManager().destroyLoader(loader.getId());
        }


        @Override
        public void onLoaderReset(Loader<Result> loader) {

        }
    }


    private class HistoryDaoLoaderCallbacks implements LoaderManager.LoaderCallbacks<HistoryDao> {

        @Override
        public Loader<HistoryDao> onCreateLoader(int id, Bundle args) {
            return new HistoryDaoLoader(MainActivity.this, mDbHelper);
        }


        @Override
        public void onLoadFinished(Loader<HistoryDao> loader, HistoryDao data) {
            mDao = data;
            if (!mPendingEntries.isEmpty()) {
                mDao.addEntries(mPendingEntries);
            }
        }


        @Override
        public void onLoaderReset(Loader<HistoryDao> loader) {

        }
    }
}
