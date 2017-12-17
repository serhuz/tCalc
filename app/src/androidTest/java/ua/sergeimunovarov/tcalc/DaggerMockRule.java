package ua.sergeimunovarov.tcalc;

import android.support.test.InstrumentationRegistry;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ContextModule;
import ua.sergeimunovarov.tcalc.di.DbModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;


public class DaggerMockRule extends it.cosenonjaviste.daggermock.DaggerMockRule<AppComponent> {

    public DaggerMockRule() {
        super(AppComponent.class,
                new ContextModule(InstrumentationRegistry.getTargetContext()),
                new DbModule(),
                new PreferencesModule());
        set(Application::setAppComponent);
    }
}
