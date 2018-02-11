/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.actions;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;


public class ActionsModelTest {


    private ActionsModel.ActionListener mListener;


    @Before
    public void setUp() throws Exception {
        mListener = mock(ActionsModel.ActionListener.class);
    }


    @After
    public void tearDown() throws Exception {
        reset(mListener);
    }


    @Test
    public void create() throws Exception {
        String expected = "HH:MM:SS";
        ActionsModel model = new ActionsModel(expected, mListener);

        String actual = model.mResultFormat.get();

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void openMenu() throws Exception {
        ActionsModel model = new ActionsModel("test", mListener);
        View view = mock(View.class);

        model.openMenu(view);

        verify(mListener).onOpenMenu(eq(view));
    }


    @Test
    public void copyContent() throws Exception {
        ActionsModel model = new ActionsModel("test", mListener);

        model.copyContent();

        verify(mListener).onCopyContent();
    }


    @Test
    public void toggleHistory() throws Exception {
        ActionsModel model = new ActionsModel("test", mListener);

        model.toggleHistory();

        verify(mListener).onToggleHistory();
    }


    @Test
    public void selectResultFormat() throws Exception {
        ActionsModel model = new ActionsModel("test", mListener);

        model.selectResultFormat();

        verify(mListener).onSelectResultFormat();
    }
}
