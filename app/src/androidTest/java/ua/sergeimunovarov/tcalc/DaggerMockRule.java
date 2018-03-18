package ua.sergeimunovarov.tcalc;

import android.support.test.InstrumentationRegistry;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ApplicationModule;
import ua.sergeimunovarov.tcalc.di.DbModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;
import ua.sergeimunovarov.tcalc.di.UtilsModule;
import ua.sergeimunovarov.tcalc.di.ViewModelModule;


public class DaggerMockRule extends it.cosenonjaviste.daggermock.DaggerMockRule<AppComponent> {

    public DaggerMockRule() {
        super(AppComponent.class,
                new ApplicationModule(InstrumentationRegistry.getTargetContext()),
                new DbModule(),
                new PreferencesModule(),
                new ViewModelModule(),
                new UtilsModule());
        set(Application::setAppComponent);
    }
}
