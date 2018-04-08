/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.ops.Result;
import ua.sergeimunovarov.tcalc.main.viewmodel.StringProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@SuppressWarnings("WeakerAccess")
public class MainActivityViewModelTest {

    private static final Result RESULT_OK_VAL = Result.create(Result.ResultType.RESULT_OK_VALUE, "2", "1+1");
    private static final Result RESULT_ERR = Result.create(Result.ResultType.RESULT_ERR, "T+V", "10:00+1");

    @Mock
    Editor mEditor;

    @Mock
    ApplicationPreferences mPreferences;

    @Mock
    StringProvider mStringProvider;

    private MainActivityViewModel mViewModel;


    @Before
    public void setUp() {
        initMocks(this);
        mViewModel = new MainActivityViewModel(mEditor, mPreferences, mStringProvider);
    }


    @After
    public void tearDown() {
        reset(mEditor, mPreferences, mStringProvider);
    }


    @Test
    public void input() {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_0);
        when(button.getText()).thenReturn("0");

        mViewModel.input(button);

        verify(mEditor).input(eq("0"), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void inputMultiply() {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_multiply);

        mViewModel.input(button);

        verify(mEditor).input(eq("*"), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void inputDivide() {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_divide);

        mViewModel.input(button);

        verify(mEditor).input(eq("/"), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void clear() {
        mViewModel.clear();

        verify(mEditor).clear(eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
        assertThat(mViewModel.mResult.get()).isEmpty();
        assertThat(mViewModel.mCalculationError.get()).isFalse();
    }


    @Test
    public void delete() {
        mViewModel.delete();

        verify(mEditor).delete(eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void inputBrackets() {
        mViewModel.inputDoubleBrackets();

        verify(mEditor).insertBrackets(eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void storeCurrentResult() {
        mViewModel.mCalculationError.set(false);
        mViewModel.mCalculationResult.set(RESULT_OK_VAL);

        mViewModel.storeCurrentResult();

        assertThat(mViewModel.mStoredResult.get()).isEqualTo(RESULT_OK_VAL);
        verify(mEditor).vibrate();
    }


    @Test
    public void notStoreCurrentResultWhenNull() {
        mViewModel.mCalculationError.set(false);
        mViewModel.mCalculationResult.set(null);

        mViewModel.storeCurrentResult();

        assertThat(mViewModel.mStoredResult.get()).isNull();
        verify(mEditor, never()).vibrate();
    }


    @Test
    public void notStoreCurrentResultWhenError() {
        mViewModel.mCalculationError.set(true);
        mViewModel.mCalculationResult.set(RESULT_ERR);

        mViewModel.storeCurrentResult();

        assertThat(mViewModel.mStoredResult.get()).isNull();
        verify(mEditor, never()).vibrate();
    }


    @Test
    public void insertStoredValue() {
        mViewModel.mStoredResult.set(RESULT_OK_VAL);

        mViewModel.insertStoredValue();

        verify(mEditor)
                .input(eq(RESULT_OK_VAL.formattedValue()), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void notInsertStoredValue() {
        mViewModel.mStoredResult.set(null);

        mViewModel.insertStoredValue();

        verify(mEditor, never()).input(any(), any(), any());
    }


    @Test
    public void insertAnswer() {
        mViewModel.mCalculationError.set(false);
        mViewModel.mCalculationResult.set(RESULT_OK_VAL);

        mViewModel.insertAnswer();

        verify(mEditor)
                .input(eq(RESULT_OK_VAL.formattedValue()), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void notInsertAnswerWhenError() {
        mViewModel.mCalculationError.set(true);
        mViewModel.mCalculationResult.set(RESULT_ERR);

        mViewModel.insertAnswer();

        verify(mEditor, never()).input(any(), any(), any());
    }


    @Test
    public void notInsertAnswerWhenNull() {
        mViewModel.mCalculationError.set(false);
        mViewModel.mCalculationResult.set(null);

        mViewModel.insertAnswer();

        verify(mEditor, never()).input(any(), any(), any());
    }


    @Test
    public void insertCurrentTime() {
        mViewModel.insertCurrentTime("10:20");

        verify(mEditor).input(eq("10:20"), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void insertEntry() {
        Entry entry = new Entry(
                "1+1",
                Result.ResultType.RESULT_OK_VALUE,
                "2",
                System.currentTimeMillis(),
                ApplicationPreferences.FormatConstants.MS
        );

        mViewModel.insertEntry(entry);

        verify(mEditor).insertEntry(eq(entry), eq(mViewModel.mEditableInput), eq(mViewModel.mSelectionState));
    }


    @Test
    public void setCalculationResult() {
        when(mStringProvider.provideFormattedString(anyInt(), anyString()))
                .thenAnswer((Answer<String>) invocation -> invocation.getArgument(1));

        mViewModel.setCalculationResult(RESULT_OK_VAL);

        assertThat(mViewModel.mCalculationError.get()).isFalse();
        assertThat(mViewModel.mCalculationResult.get()).isEqualTo(RESULT_OK_VAL);
        assertThat(mViewModel.mResult.get()).isEqualTo(RESULT_OK_VAL.formattedValue());
    }


    @Test
    public void setCalculationError() {
        mViewModel.setCalculationResult(RESULT_ERR);

        assertThat(mViewModel.mCalculationError.get()).isTrue();
        assertThat(mViewModel.mResult.get()).isEqualTo(RESULT_ERR.value());
    }
}
