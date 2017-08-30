/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ContextModule;
import ua.sergeimunovarov.tcalc.di.DaggerAppComponent;
import ua.sergeimunovarov.tcalc.di.LeakCanaryModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;
import ua.sergeimunovarov.tcalc.di.UtilModule;


public class Application extends android.app.Application {

    private static AppComponent sAppComponent;

    private RefWatcher mRefWatcher;


    public static AppComponent getAppComponent() {
        return sAppComponent;
    }


    public static void setAppComponent(AppComponent appComponent) {
        Application.sAppComponent = appComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        mRefWatcher = installLeakCanary();

        sAppComponent = buildAppComponent();
    }


    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }


    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .preferencesModule(new PreferencesModule())
                .leakCanaryModule(new LeakCanaryModule(mRefWatcher))
                .utilModule(new UtilModule())
                .build();
    }
}
