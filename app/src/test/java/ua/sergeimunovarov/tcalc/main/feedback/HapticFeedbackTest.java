/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.feedback;

import android.os.Vibrator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SuppressWarnings("WeakerAccess")
public class HapticFeedbackTest {

    @Mock
    Vibrator mVibrator;

    @Mock
    ApplicationPreferences mPreferences;

    private HapticFeedback mHapticFeedback;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mHapticFeedback = new HapticFeedback(mVibrator, mPreferences);
    }


    @After
    public void tearDown() {
        reset(mVibrator, mPreferences);
    }


    @Test
    public void callVibrate() {
        when(mPreferences.loadVibrationPreference()).thenReturn(true);
        when(mVibrator.hasVibrator()).thenReturn(true);
        when(mPreferences.loadVibrationDurationPreference()).thenReturn(50);

        mHapticFeedback.vibrate();

        verify(mVibrator).vibrate(eq(50L));
    }


    @Test
    public void notCallVibrateWhenDisabled() {
        when(mPreferences.loadVibrationPreference()).thenReturn(false);

        mHapticFeedback.vibrate();

        verify(mVibrator, never()).vibrate(anyLong());
    }


    @Test
    public void notCallVibrateWhenNoVibrator() {
        when(mPreferences.loadVibrationPreference()).thenReturn(true);
        when(mVibrator.hasVibrator()).thenReturn(false);

        mHapticFeedback.vibrate();

        verify(mVibrator, never()).vibrate(anyLong());
    }
}