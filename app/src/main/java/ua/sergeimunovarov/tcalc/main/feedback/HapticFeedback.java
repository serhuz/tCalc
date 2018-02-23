/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.feedback;

import android.os.Vibrator;
import android.support.annotation.NonNull;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;


public class HapticFeedback {

    private final Vibrator mVibrator;
    private final ApplicationPreferences mPreferences;


    public HapticFeedback(@NonNull Vibrator vibrator,
                          @NonNull ApplicationPreferences preferences) {
        mVibrator = vibrator;
        mPreferences = preferences;
    }


    public void vibrate() {
        if (mPreferences.loadVibrationPreference() && mVibrator.hasVibrator()) {
            mVibrator.vibrate(mPreferences.loadVibrationDurationPreference());
        }
    }
}
