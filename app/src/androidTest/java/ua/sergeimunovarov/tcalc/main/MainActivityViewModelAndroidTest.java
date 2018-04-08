/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Editable;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.main.viewmodel.StringProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@SuppressWarnings("WeakerAccess")
@RunWith(AndroidJUnit4.class)
public class MainActivityViewModelAndroidTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

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
    public void recallMemory() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.recallMemory();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.MEMORY_RECALL);
    }


    @Test
    public void storeMemory() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.storeMemory();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.MEMORY_STORE);
    }


    @Test
    public void insertPreviousAnswer() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.insertPreviousAnswer();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.INSERT_ANSWER);
    }


    @Test
    public void copyContent() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.copyContent();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.COPY);
    }


    @Test
    public void toggleHistory() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.toggleHistory();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.TOGGLE_HISTORY);
    }


    @Test
    public void selectResultFormat() {
        MainActivityViewModel.Interaction[] actual = new MainActivityViewModel.Interaction[1];
        mViewModel.mInteractionEvent.observeForever(interaction -> actual[0] = interaction);

        mViewModel.selectResultFormat();

        assertThat(actual[0]).isEqualTo(MainActivityViewModel.Interaction.OPEN_FORMAT_DIALOG);
    }


    @Test
    public void calculate() {
        Editable editable = mock(Editable.class);
        when(editable.toString()).thenReturn("1+1");
        mViewModel.mEditableInput.set(editable);

        String[] actual = new String[1];
        mViewModel.mCalculateResultEvent.observeForever(s -> actual[0] = s);

        mViewModel.calculate();

        assertThat(actual[0]).isEqualTo("1+1");
    }
}
