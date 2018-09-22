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
import ua.sergeimunovarov.tcalc.main.history.HistoryViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;
import ua.sergeimunovarov.tcalc.main.input.BaseInputFragment;
import ua.sergeimunovarov.tcalc.settings.SettingsFragment;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        DbModule.class,
        PreferencesModule.class,
        UtilsModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    Context appContext();

    ApplicationPreferences appPreferences();

    EntryDao entryDao();

    void inject(MainActivity mainActivity);

    void inject(FormatDialogFragment formatDialogFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(HistoryViewModel historyViewModel);

    void inject(BaseInputFragment baseInputFragment);
}
