/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Component;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.main.FormatDialogFragment;
import ua.sergeimunovarov.tcalc.main.MainActivity;
import ua.sergeimunovarov.tcalc.main.views.TypefaceHolder;
import ua.sergeimunovarov.tcalc.settings.SettingsFragment;

@Singleton
@Component(modules = {
        ContextModule.class,
        PreferencesModule.class,
        LeakCanaryModule.class,
        UtilModule.class
})
public interface AppComponent {

    Context appContext();

    ApplicationPreferences appPreferences();

    TypefaceHolder typefaceHolder();

    RefWatcher refWatcher();

    void inject(MainActivity mainActivity);

    void inject(FormatDialogFragment formatDialogFragment);

    void inject(SettingsFragment settingsFragment);
}
