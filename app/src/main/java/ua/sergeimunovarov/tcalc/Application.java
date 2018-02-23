/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ApplicationModule;
import ua.sergeimunovarov.tcalc.di.DaggerAppComponent;


public class Application extends android.app.Application {

    private static AppComponent sAppComponent;


    public static AppComponent getAppComponent() {
        return sAppComponent;
    }


    public static void setAppComponent(AppComponent appComponent) {
        sAppComponent = appComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = buildAppComponent();
    }


    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
