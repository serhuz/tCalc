/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.databinding.ObservableField;
import android.text.Editable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.main.feedback.HapticFeedback;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.main.views.SelectionState;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@SuppressWarnings("WeakerAccess")
public class EditorTest {

    @Mock
    HapticFeedback mFeedback;

    @Mock
    ObservableField<Editable> mEditableInput;

    @Mock
    ObservableField<SelectionState> mSelectionState;

    private Editor mEditor;


    @Before
    public void setUp() {
        initMocks(this);
        mEditor = new Editor(mFeedback);
    }


    @After
    public void tearDown() {
        reset(mFeedback, mEditableInput, mSelectionState);
    }


    @Test
    public void insertBrackets1() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());
        when(mSelectionState.get()).thenReturn(SelectionState.create(Editor.POSITION_START));

        mEditor.insertBrackets(mEditableInput, mSelectionState);

        verify(mEditor).input(eq(Editor.BRACKETS), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void insertBrackets2() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(1);
        when(editable.toString()).thenReturn("1");
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get())
                .thenReturn(SelectionState.create(Editor.POSITION_START, Editor.POSITION_START + 1));

        mEditor.insertBrackets(mEditableInput, mSelectionState);

        verify(mFeedback).vibrate();
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(Editor.POSITION_START + "(1)".length())));
    }


    @Test
    public void input1() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(0);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(0));

        mEditor.input("1", mEditableInput, mSelectionState);

        verify(editable).insert(eq(0), eq("1"));
        verify(mEditableInput).set(eq(editable));
        verify(mFeedback).vibrate();
    }


    @Test
    public void input2() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(3);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(0, 3));

        mEditor.input("1", mEditableInput, mSelectionState);

        verify(editable).replace(eq(0), eq(3), eq("1"));
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(SelectionState.create(1));
        verify(mFeedback).vibrate();
    }


    @Test
    public void insertValueEntry1() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());

        Entry entry = new Entry(
                "1+1",
                Result.ResultType.RESULT_OK_VALUE,
                "2",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.MS
        );

        mEditor.insertEntry(entry, mEditableInput, mSelectionState);

        verify(mEditor).input(eq("2"), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void insertValueEntry2() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());

        Entry entry = new Entry(
                "1+1",
                Result.ResultType.RESULT_OK_VALUE,
                "2",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.DHMS
        );

        mEditor.insertEntry(entry, mEditableInput, mSelectionState);

        verify(mEditor).input(eq("2"), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void insertMSEntry1() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());

        Entry entry = new Entry(
                "0:10+0:10",
                Result.ResultType.RESULT_OK,
                "20:00",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.MS
        );

        mEditor.insertEntry(entry, mEditableInput, mSelectionState);

        verify(mEditor).input(eq("00:20:00"), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void insertMSEntry2() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());

        Entry entry = new Entry(
                "0:10+0:10",
                Result.ResultType.RESULT_OK,
                "20:00.000",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.MS
        );

        mEditor.insertEntry(entry, mEditableInput, mSelectionState);

        verify(mEditor).input(eq("00:20:00"), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void insertDHMSEntry() {
        mEditor = spy(mEditor);
        doNothing().when(mEditor).input(any(), any(), any());

        Entry entry = new Entry(
                "10:00+10:00",
                Result.ResultType.RESULT_OK,
                "0d. 20:00:00",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.DHMS
        );

        mEditor.insertEntry(entry, mEditableInput, mSelectionState);

        verify(mEditor).input(eq("20:00:00"), eq(mEditableInput), eq(mSelectionState));
    }


    @Test
    public void clear() {
        Editable editable = mock(Editable.class);
        when(mEditableInput.get()).thenReturn(editable);

        mEditor.clear(mEditableInput, mSelectionState);

        verify(editable).clear();
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(Editor.POSITION_START)));
    }


    @Test
    public void delete1() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(10);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(1));
        mEditor.delete(mEditableInput, mSelectionState);

        verify(editable).delete(eq(0), eq(1));
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(0)));
        verify(mFeedback).vibrate();
    }


    @Test
    public void delete2() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(10);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(3));
        mEditor.delete(mEditableInput, mSelectionState);

        verify(editable).delete(eq(2), eq(3));
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(2)));
        verify(mFeedback).vibrate();
    }


    @Test
    public void notDelete1() {
        when(mEditableInput.get()).thenReturn(mock(Editable.class));

        mEditor.delete(mEditableInput, mSelectionState);

        verify(mSelectionState, never()).get();
    }


    @Test
    public void notDelete2() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(10);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(Editor.POSITION_START));

        mEditor.delete(mEditableInput, mSelectionState);

        verify(editable, never()).delete(anyInt(), anyInt());
    }


    @Test
    public void deleteRange1() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(10);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(1, 3));
        mEditor.delete(mEditableInput, mSelectionState);

        verify(editable).delete(eq(1), eq(3));
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(1)));
        verify(mFeedback).vibrate();
    }


    @Test
    public void deleteRange2() {
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(10);
        when(mEditableInput.get()).thenReturn(editable);
        when(mSelectionState.get()).thenReturn(SelectionState.create(0, 2));
        mEditor.delete(mEditableInput, mSelectionState);

        verify(editable).delete(eq(0), eq(2));
        verify(mEditableInput).set(eq(editable));
        verify(mSelectionState).set(eq(SelectionState.create(0)));
        verify(mFeedback).vibrate();
    }


    @Test
    public void vibrate() {
        mEditor.vibrate();

        verify(mFeedback).vibrate();
    }
}
