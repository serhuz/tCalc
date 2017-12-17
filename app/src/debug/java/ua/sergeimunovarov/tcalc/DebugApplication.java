/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;


public class DebugApplication extends Application {

    private static final int MAX_STORED_HEAP_DUMPS = 3;


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        installLeakCanary();
        initPreferences();
    }


    protected void installLeakCanary() {
        LeakCanary.refWatcher(this)
                .maxStoredHeapDumps(MAX_STORED_HEAP_DUMPS)
                .buildAndInstall();
    }


    protected void initPreferences() {
        if (!BuildConfig.SHOULD_CLEAR_PREFS_ON_START) return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();

        PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
    }
}
