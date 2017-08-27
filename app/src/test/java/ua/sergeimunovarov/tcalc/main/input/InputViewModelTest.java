package ua.sergeimunovarov.tcalc.main.input;

import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.sergeimunovarov.tcalc.R;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InputViewModelTest {

    private InputListener mInputListener;
    private InputViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        mInputListener = mock(InputListener.class);
        mViewModel = new InputViewModel(mInputListener);
    }

    @After
    public void tearDown() throws Exception {
        reset(mInputListener);
    }

    @Test
    public void handleEqualInput() throws Exception {
        mViewModel.handleEqualInput(mock(View.class));
        verify(mInputListener, times(1)).onCalculateResult();
    }

    @Test
    public void handleMemoryStoreInput() throws Exception {
        mViewModel.handleMemoryStoreInput(mock(View.class));
        verify(mInputListener, times(1)).onMemoryStore();
    }

    @Test
    public void handleMemoryRecallInput() throws Exception {
        mViewModel.handleMemoryRecallInput(mock(View.class));
        verify(mInputListener, times(1)).onMemoryRecall();
    }

    @Test
    public void handleInsertAnswer() throws Exception {
        mViewModel.handleInsertPreviousAnswerInput(mock(View.class));
        verify(mInputListener, times(1)).onInsertAnswer();
    }

    @Test
    public void handleDoubleBracketsInput() throws Exception {
        mViewModel.handleDoubleBracketsInput(mock(View.class));
        verify(mInputListener, times(1)).onInsertBrackets();
    }

    @Test
    public void handleDeleteInput() throws Exception {
        mViewModel.handleDeleteInput(mock(View.class));
        verify(mInputListener, times(1)).onDeleteInput();
    }

    @Test
    public void handleClearInput() throws Exception {
        mViewModel.handleClearInput(mock(View.class));
        verify(mInputListener, times(1)).onClearInput();
    }

    @Test
    public void handleSymbolInput1() throws Exception {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_multiply);

        mViewModel.handleSymbolInput(button);
        verify(mInputListener, times(1)).onInputReceived(eq("*"));
    }

    @Test
    public void handleSymbolInput2() throws Exception {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_divide);

        mViewModel.handleSymbolInput(button);
        verify(mInputListener, times(1)).onInputReceived(eq("/"));
    }

    @Test
    public void handleSymbolInput3() throws Exception {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_0);
        when(button.getText()).thenReturn("0");

        mViewModel.handleSymbolInput(button);
        verify(mInputListener, times(1)).onInputReceived(eq("0"));
    }

    @Test
    public void handleSymbolInput4() throws Exception {
        Button button = mock(Button.class);
        when(button.getId()).thenReturn(R.id.btn_col);
        when(button.getText()).thenReturn(":");

        mViewModel.handleSymbolInput(button);
        verify(mInputListener, times(1)).onInputReceived(eq(":"));
    }
}
