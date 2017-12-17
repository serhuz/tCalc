/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.main.FormatDialogFragment;
import ua.sergeimunovarov.tcalc.main.MainActivity;
import ua.sergeimunovarov.tcalc.main.history.HistoryDialogFragment;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;
import ua.sergeimunovarov.tcalc.settings.SettingsFragment;


@Singleton
@Component(modules = {
        ContextModule.class,
        DbModule.class,
        PreferencesModule.class
})
public interface AppComponent {

    Context appContext();

    ApplicationPreferences appPreferences();

    HistoryDbHelper dbHelper();

    void inject(MainActivity mainActivity);

    void inject(FormatDialogFragment formatDialogFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(HistoryDialogFragment historyDialogFragment);
}
