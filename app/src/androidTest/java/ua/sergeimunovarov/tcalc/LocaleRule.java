/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Locale;

import androidx.test.InstrumentationRegistry;


public class LocaleRule implements TestRule {

    private final Locale[] mLocales;
    private Locale mDeviceLocale;


    public LocaleRule(Locale... locales) {
        mLocales = locales;
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    if (mLocales != null) {
                        mDeviceLocale = Locale.getDefault();
                        for (Locale locale : mLocales) {
                            setLocale(locale);
                            base.evaluate();
                        }
                    }
                } finally {
                    if (mDeviceLocale != null) {
                        setLocale(mDeviceLocale);
                    }
                }
            }
        };
    }


    private void setLocale(Locale locale) {
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Locale.setDefault(locale);
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, displayMetrics);
    }
}
