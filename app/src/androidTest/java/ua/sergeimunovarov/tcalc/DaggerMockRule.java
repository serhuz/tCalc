package ua.sergeimunovarov.tcalc;

import android.support.test.InstrumentationRegistry;

import com.squareup.leakcanary.RefWatcher;

import ua.sergeimunovarov.tcalc.di.AppComponent;
import ua.sergeimunovarov.tcalc.di.ContextModule;
import ua.sergeimunovarov.tcalc.di.DbModule;
import ua.sergeimunovarov.tcalc.di.LeakCanaryModule;
import ua.sergeimunovarov.tcalc.di.PreferencesModule;
import ua.sergeimunovarov.tcalc.di.UtilModule;

public class DaggerMockRule extends it.cosenonjaviste.daggermock.DaggerMockRule<AppComponent> {
    public DaggerMockRule() {
        super(AppComponent.class,
                new ContextModule(InstrumentationRegistry.getTargetContext()),
                new DbModule(),
                new PreferencesModule(),
                new LeakCanaryModule(RefWatcher.DISABLED),
                new UtilModule());
        set(Application::setAppComponent);
    }
}
