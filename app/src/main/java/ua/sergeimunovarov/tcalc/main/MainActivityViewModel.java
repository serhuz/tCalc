/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.text.Editable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.main.viewmodel.SingleLiveEvent;
import ua.sergeimunovarov.tcalc.main.viewmodel.StringProvider;
import ua.sergeimunovarov.tcalc.main.views.SelectionState;


public class MainActivityViewModel extends BaseObservableViewModel {

    private static final int DEFAULT_CURSOR_POSITION = 0;

    public final ObservableField<String> mResultFormat;
    public final ObservableField<String> mResult = new ObservableField<>();
    public final ObservableField<Editable> mEditableInput = new ObservableField<>();
    public final ObservableField<SelectionState> mSelectionState =
            new ObservableField<>(SelectionState.create(DEFAULT_CURSOR_POSITION));

    public final ObservableBoolean mCalculationError = new ObservableBoolean(false);
    public final ObservableField<Result> mCalculationResult = new ObservableField<>();
    public final ObservableField<Result> mStoredResult = new ObservableField<>();

    public final InteractionEvent mInteractionEvent = new InteractionEvent();
    public final CalculateResultEvent mCalculateResultEvent = new CalculateResultEvent();
    public final MenuClickEvent mMenuClickEvent = new MenuClickEvent();

    public final ApplicationPreferences.ResultFormatPref mFormatPref;

    private final StringProvider mStringProvider;
    private final Editor mEditor;


    public MainActivityViewModel(@NonNull Editor editor,
                                 @NonNull ApplicationPreferences preferences,
                                 @NonNull StringProvider stringProvider) {
        mResultFormat = new ObservableField<>(preferences.loadFormatPreferenceString());

        mEditor = editor;
        mFormatPref = preferences.getResultFormat();
        mStringProvider = stringProvider;
    }


    public void input(View view) {
        Button inputButton = (Button) view;

        CharSequence text;
        switch (inputButton.getId()) {
            case R.id.btn_multiply:
                text = "*";
                break;
            case R.id.btn_divide:
                text = "/";
                break;
            default:
                text = inputButton.getText();
                break;
        }

        mEditor.input(text, mEditableInput, mSelectionState);
    }


    public void clear() {
        mEditor.clear(mEditableInput, mSelectionState);
        mResult.set("");
        mCalculationError.set(false);
    }


    public void delete() {
        mEditor.delete(mEditableInput, mSelectionState);
    }


    public void inputDoubleBrackets() {
        mEditor.insertBrackets(mEditableInput, mSelectionState);
    }


    public void recallMemory() {
        mInteractionEvent.setValue(Interaction.MEMORY_RECALL);
    }


    public void storeMemory() {
        mInteractionEvent.setValue(Interaction.MEMORY_STORE);
    }


    public void calculate() {
        mCalculateResultEvent.setValue(mEditableInput.get().toString());
    }


    public void insertPreviousAnswer() {
        mInteractionEvent.setValue(Interaction.INSERT_ANSWER);
    }


    public void copyContent() {
        mInteractionEvent.setValue(Interaction.COPY);
    }


    public void toggleHistory() {
        mInteractionEvent.setValue(Interaction.TOGGLE_HISTORY);
    }


    public void selectResultFormat() {
        mInteractionEvent.setValue(Interaction.OPEN_FORMAT_DIALOG);
    }


    public void storeCurrentResult() {
        boolean calculationError = mCalculationError.get();
        Result result = mCalculationResult.get();

        if (!calculationError && result != null) {
            mStoredResult.set(result);
            mEditor.vibrate();
        }
    }


    public void insertStoredValue() {
        Result result = mStoredResult.get();
        if (result != null) {
            mEditor.input(result.formattedValue(), mEditableInput, mSelectionState);
        }
    }


    public void insertAnswer() {
        boolean calculationError = mCalculationError.get();
        Result result = mCalculationResult.get();
        if (!calculationError && result != null) {
            mEditor.input(result.formattedValue(), mEditableInput, mSelectionState);
        }
    }


    public void insertCurrentTime(String time) {
        mEditor.input(time, mEditableInput, mSelectionState);
    }


    public void insertEntry(Entry entry) {
        mEditor.insertEntry(entry, mEditableInput, mSelectionState);
    }


    public void setCalculationResult(Result result) {
        switch (result.type()) {
            case RESULT_ERR:
                mCalculationError.set(true);
                mResult.set(result.value());
                break;
            default:
                mCalculationError.set(false);
                mCalculationResult.set(result);
                mResult.set(mStringProvider.provideFormattedString(R.string.eq, result.value()));
                break;
        }
    }


    public enum Interaction {
        MEMORY_STORE,
        MEMORY_RECALL,
        INSERT_ANSWER,
        COPY,
        TOGGLE_HISTORY,
        OPEN_FORMAT_DIALOG
    }


    public static class Factory implements ViewModelProvider.Factory {

        private final Editor mEditor;
        private final ApplicationPreferences mPreferences;
        private final StringProvider mProvider;


        public Factory(@NonNull Editor editor,
                       @NonNull ApplicationPreferences preferences,
                       @NonNull StringProvider provider) {
            mEditor = editor;
            mPreferences = preferences;
            mProvider = provider;
        }


        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainActivityViewModel(mEditor, mPreferences, mProvider);
        }
    }


    @SuppressWarnings("WeakerAccess")
    public static class InteractionEvent extends SingleLiveEvent<Interaction> {

    }


    @SuppressWarnings("WeakerAccess")
    public static class CalculateResultEvent extends SingleLiveEvent<String> {

    }


    @SuppressWarnings("WeakerAccess")
    public static class MenuClickEvent extends SingleLiveEvent<Integer> {

    }
}
