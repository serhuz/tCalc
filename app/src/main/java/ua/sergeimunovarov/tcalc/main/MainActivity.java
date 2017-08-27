/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.AbstractTransitionActivity;
import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.Patterns;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.databinding.ActivityMainBinding;
import ua.sergeimunovarov.tcalc.info.HelpActivity;
import ua.sergeimunovarov.tcalc.main.input.BaseInputFragment;
import ua.sergeimunovarov.tcalc.main.input.CalcInputFragment;
import ua.sergeimunovarov.tcalc.main.input.InputListener;
import ua.sergeimunovarov.tcalc.main.input.PhoneInputFragment;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.settings.SettingsActivity;

public class MainActivity extends AbstractTransitionActivity implements
        FormatDialogFragment.FormatSelectionListener, InputListener,
        InsertTimeDialogFragment.TimeInsertionListener, LoaderManager.LoaderCallbacks<Result> {

    public static final String PREFIX_RESULT = "= %s";
    public static final String BRACKETS = "()";
    public static final char PAR_LEFT = '(';
    public static final char PAR_RIGHT = ')';

    public static final String KEY_RESULT = "result";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_CALC_ERROR = "err";
    private static final String KEY_MEMORY = "mem";

    private static final String TAG_FORMAT = "fmt";
    private static final String TAG_INPUT = "inp";
    private static final String TAG_TSTAMP = "tstamp";

    /**
     * Clipboard label.
     */
    private static final String LABEL = MainActivity.class.getName();

    private static final int CALC_LOADER_ID = 128;

    @Inject
    ApplicationPreferences preferences;

    private EditText inputBox;

    private TextView resultTextView;

    private boolean hasPermanentMenuKey;

    private ClipboardManager mClipboard;
    private Vibrator mVibrator;

    private Toast toast = null;

    private int mOutputFormat;
    private boolean mVibroEnabled;
    private int mVibroDuration;

    private boolean calculationError = false;
    private Result calculationResult = null;
    private Result storedResult = null;

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        inputBox = mBinding.input;
        resultTextView = mBinding.result;

        Application.getAppComponent().inject(this);

        getLoaderManager().initLoader(CALC_LOADER_ID, null, this);

        hasPermanentMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();

        configureInputBehavior();
        configureActionBar();

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void configureInputBehavior() {
        inputBox.setRawInputType(InputType.TYPE_CLASS_TEXT);
        inputBox.setTextIsSelectable(true);
    }

    /**
     * Configures ActionBar visibility for landscape and portrait orientations.
     */
    private void configureActionBar() {
        int orientation = getResources().getConfiguration().orientation;
        ActionBar bar = getActionBar();
        if (bar != null) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (getActionBar().isShowing() && hasPermanentMenuKey) getActionBar().hide();
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (!getActionBar().isShowing()) getActionBar().show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && hasPermanentMenuKey) {
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                return true;
            case R.id.action_instructions:
                launchActivity(new Intent(MainActivity.this, HelpActivity.class));
                return true;
            case R.id.action_format:
                showFormatDialog();
                return true;
            case R.id.action_timestamp:
                showTimestampDialog();
            case R.id.action_copy:
                copyResultToClipboard();
                return true;
            case R.id.action_settings:
                launchActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shows dialog with output format selection.
     */
    private void showFormatDialog() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_FORMAT);
        if (fragment != null) transaction.remove(fragment);

        FormatDialogFragment.create().show(transaction, TAG_FORMAT);
    }

    private void showTimestampDialog() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_TSTAMP);
        if (fragment != null) transaction.remove(fragment);

        InsertTimeDialogFragment.create().show(transaction, TAG_TSTAMP);
    }

    /**
     * Copies calculation result to system clipboard if and only if
     * there are no calculation errors.
     */
    private void copyResultToClipboard() {
        if (!calculationError && calculationResult != null) {
            ClipData clipData = ClipData.newPlainText(LABEL, calculationResult.value());
            mClipboard.setPrimaryClip(clipData);
            this.showToast(toast, getResources().getString(R.string.toast_copy));
        }
    }

    private void showToast(Toast t, String msg) {
        if (t == null) {
            t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else if (t.getView() == null) {
            t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            t.setText(msg);
        }

        t.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RESULT, calculationResult);
        outState.putParcelable(KEY_MEMORY, storedResult);
        outState.putBoolean(KEY_CALC_ERROR, calculationError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculationError = savedInstanceState.getBoolean(KEY_CALC_ERROR);
        calculationResult = savedInstanceState.getParcelable(KEY_RESULT);
        storedResult = savedInstanceState.getParcelable(KEY_MEMORY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.numpad_container, inputFragment, TAG_INPUT)
                .commit();
    }

    @Override
    public void onOutputFormatChanged(int which) {
        mOutputFormat = which;
        onCalculateResult();
    }

    @Override
    public void onCalculateResult() {
        String input = inputBox.getText().toString();
        if (!input.isEmpty()) {
            Bundle args = new Bundle();
            args.putInt(CalculationLoader.KEY_FORMAT, mOutputFormat);
            args.putString(CalculationLoader.KEY_INPUT, input);
            getLoaderManager().restartLoader(CALC_LOADER_ID, args, this);
        }
    }

    @Override
    public void onInputReceived(CharSequence cs) {
        insertCharacter(cs);
    }

    /**
     * Inserts string into EditText element. If multiple characters inside
     * EditText are selected, they are replaced with the given character.
     *
     * @param cs text to insert
     */
    private void insertCharacter(CharSequence cs) {
        int selectionStart = inputBox.getSelectionStart();
        int selectionEnd = inputBox.getSelectionEnd();

        Editable editable = inputBox.getText();
        if (selectionStart == selectionEnd) {
            editable.insert(selectionStart, cs);
        } else {
            editable.replace(selectionStart, selectionEnd, cs);
            inputBox.setSelection(selectionEnd - (selectionEnd - selectionStart) + 1);
        }

        this.vibrate();
    }

    private void vibrate() {
        if (mVibroEnabled && mVibrator.hasVibrator()) {
            mVibrator.vibrate(mVibroDuration);
        }
    }

    @Override
    public void onClearInput() {
        inputBox.getText().clear();
        resultTextView.setText("");
        calculationResult = null;
        calculationError = false;
        vibrate();
    }

    @Override
    public void onDeleteInput() {
        Editable text = inputBox.getText();
        if (text.length() == 0) return;

        // get cursor/selection parameters
        int selectionStart = inputBox.getSelectionStart();
        int selectionEnd = inputBox.getSelectionEnd();
        int selectionLength = 1;

        if (selectionStart == selectionEnd) { // simple cursor
            if (selectionStart == 0) return; // cursor at the start, nothing to erase
            text.delete(selectionStart - 1, selectionStart);
        } else { // multiple character selection, delete whole selection
            text.delete(selectionStart, selectionEnd);
            selectionLength = selectionEnd - selectionStart;
        }

        inputBox.setSelection(selectionEnd - selectionLength);
        this.vibrate();
    }

    @Override
    public void onInsertBrackets() {
        encloseSelectionInParentheses();
    }

    private void encloseSelectionInParentheses() {
        int selectionStart = inputBox.getSelectionStart();
        int selectionEnd = inputBox.getSelectionEnd();

        if (selectionStart == selectionEnd) {
            insertCharacter(BRACKETS);
        } else {
            StringBuilder sb = new StringBuilder(inputBox.getText().toString());
            sb.insert(selectionStart, PAR_LEFT);
            sb.insert(selectionEnd + 1, PAR_RIGHT);
            inputBox.setText(sb.toString());
            inputBox.setSelection(selectionEnd + 2);
            vibrate();
        }
    }

    @Override
    public void onMemoryStore() {
        storeResult();
    }

    private void storeResult() {
        if (calculationResult != null && !calculationError) {
            storedResult = calculationResult;
            Toast.makeText(this, R.string.toast_ms, Toast.LENGTH_SHORT).show();
            vibrate();
        }
    }

    @Override
    public void onMemoryRecall() {
        if (storedResult != null) {
            insertStoredValue(storedResult);
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
        if (calculationResult != null && !calculationError) {
            insertStoredValue(calculationResult);
        }
    }

    @Override
    public void onTimeSelected(String timestamp) {
        insertCharacter(timestamp);
    }

    @Override
    public Loader<Result> onCreateLoader(int id, Bundle args) {
        return new CalculationLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<Result> loader, Result data) {
        if (data == null) {
            return;
        }

        switch (data.type()) {
            case RESULT_ERR:
                calculationError = true;
                resultTextView.setText(data.value());
                break;
            case RESULT_OK_VALUE:
            case RESULT_OK:
                calculationError = false;
                calculationResult = data;
                resultTextView.setText(
                        String.format(PREFIX_RESULT, data.value())
                );
                vibrate();
                break;
        }

        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Result> loader) {
    }
}
