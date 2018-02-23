/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.os.Vibrator;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.main.feedback.HapticFeedback;


@Module
public class UtilsModule {

    @Provides
    protected HapticFeedback provideHapticFeedback(Vibrator vibrator,
                                                   ApplicationPreferences preferences) {
        return new HapticFeedback(vibrator, preferences);
    }
}
