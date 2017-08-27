/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ContextModule;
import ua.sergeimunovarov.tcalc.di.DaggerAppComponent;
import ua.sergeimunovarov.tcalc.di.LeakCanaryModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;
import ua.sergeimunovarov.tcalc.di.UtilModule;

public class Application extends android.app.Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static void setAppComponent(AppComponent appComponent) {
        Application.appComponent = appComponent;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        refWatcher = installLeakCanary();

        initPreferences();
        appComponent = buildAppComponent();
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .preferencesModule(new PreferencesModule())
                .leakCanaryModule(new LeakCanaryModule(refWatcher))
                .utilModule(new UtilModule())
                .build();
    }

    private void initPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean readAgain;
        if (BuildConfig.DEBUG) {
            readAgain = true;

            // To avoid bugs in debug mode all preferences are cleared.
            // This should help avoid crashes due to NPEs and other stuff.
            preferences.edit().clear().apply();
        } else {
            readAgain = false;
        }

        PreferenceManager.setDefaultValues(this, R.xml.prefs, readAgain);
    }
}
