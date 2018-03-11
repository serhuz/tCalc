/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.Editable;

import javax.inject.Inject;

import ua.sergeimunovarov.tcalc.main.feedback.HapticFeedback;
import ua.sergeimunovarov.tcalc.main.views.SelectionState;


public class Editor {

    public static final String BRACKETS = "()";
    public static final char PAR_LEFT = '(';
    public static final char PAR_RIGHT = ')';

    private static final int POSITION_START = 0;

    private final HapticFeedback mFeedback;


    @Inject
    public Editor(@NonNull HapticFeedback feedback) {
        mFeedback = feedback;
    }


    public void insertBrackets(ObservableField<Editable> editableInput,
                               ObservableField<SelectionState> selectionState) {
        SelectionState state = selectionState.get();
        if (state.selectionStart() == state.selectionEnd()) {
            input(BRACKETS, editableInput, selectionState);
        } else {
            Editable editable = editableInput.get();
            StringBuilder sb = new StringBuilder(editable);
            sb.insert(state.selectionStart(), PAR_LEFT);
            sb.insert(state.selectionEnd() + 1, PAR_RIGHT);
            editable.replace(0, editable.length(), sb);
            editableInput.set(editable);
            selectionState.set(SelectionState.create(state.selectionEnd() + BRACKETS.length()));

            mFeedback.vibrate();
        }
    }


    public void input(CharSequence text,
                      ObservableField<Editable> editableInput,
                      ObservableField<SelectionState> selectionState) {
        Editable editable = editableInput.get();
        SelectionState state = selectionState.get();
        if (state.selectionStart() == state.selectionEnd()) {
            editable.insert(state.selectionStart(), text);
            editableInput.set(editable);
        } else {
            editable.replace(state.selectionStart(), state.selectionEnd(), text);
            int selection = state.selectionEnd() - (state.selectionEnd() - state.selectionStart()) + 1;
            selectionState.set(SelectionState.create(selection));
        }

        mFeedback.vibrate();
    }


    public void clear(ObservableField<Editable> editableInput,
                      ObservableField<SelectionState> selectionState) {
        Editable editable = editableInput.get();
        editable.clear();
        editableInput.set(editable);

        selectionState.set(SelectionState.create(POSITION_START));

        mFeedback.vibrate();
    }


    public void delete(ObservableField<Editable> editableInput,
                       ObservableField<SelectionState> selectionState) {
        Editable editable = editableInput.get();
        if (editable.length() == 0) return;

        int selectionLength = 1;

        SelectionState state = selectionState.get();
        if (state.selectionStart() == state.selectionEnd()) { // simple cursor
            if (state.selectionStart() == POSITION_START)
                return; // cursor at the start, nothing to erase
            editable.delete(state.selectionStart() - 1, state.selectionStart());
        } else { // multiple character selection, delete whole selection
            editable.delete(state.selectionStart(), state.selectionEnd());
            selectionLength = state.selectionEnd() - state.selectionStart();
        }

        editableInput.set(editable);
        selectionState.set(SelectionState.create(state.selectionEnd() - selectionLength));
        mFeedback.vibrate();
    }


    public void vibrate() {
        mFeedback.vibrate();
    }
}
