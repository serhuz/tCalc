/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ContextModule;
import ua.sergeimunovarov.tcalc.di.DaggerAppComponent;
import ua.sergeimunovarov.tcalc.di.DbModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;


public class Application extends android.app.Application {

    private static AppComponent sAppComponent;


    public static AppComponent getAppComponent() {
        return sAppComponent;
    }


    public static void setAppComponent(AppComponent appComponent) {
        Application.sAppComponent = appComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sAppComponent = buildAppComponent();
    }


    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .dbModule(new DbModule())
                .preferencesModule(new PreferencesModule())
                .build();
    }
}
