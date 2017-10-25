/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.sergeimunovarov.tcalc.main.history.listeners.DialogDismissListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class HistoryDialogViewModelTest {


    private HistoryDialogViewModel mViewModel;
    private DialogDismissListener mMock;


    @Before
    public void setUp() throws Exception {
        mMock = mock(DialogDismissListener.class);
        mViewModel = new HistoryDialogViewModel(mMock);
    }


    @After
    public void tearDown() throws Exception {
        reset(mMock);
    }


    @Test
    public void dismiss() throws Exception {
        mViewModel.dismiss();

        verify(mMock, times(1)).onDismiss();
    }
}
