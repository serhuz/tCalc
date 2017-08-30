/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.settings;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static cortado.Cortado.onView;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static ua.sergeimunovarov.tcalc.ApplicationPreferences.LayoutConstants.LAYOUT_NEW;
import static ua.sergeimunovarov.tcalc.ApplicationPreferences.LayoutConstants.LAYOUT_OLD;


@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public final DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityTestRule = new ActivityTestRule<>(SettingsActivity.class);

    @SuppressWarnings("WeakerAccess")
    @InjectFromComponent
    ApplicationPreferences mApplicationPreferences;


    @After
    public void tearDown() throws Exception {
        mApplicationPreferences.getPreferences().edit().clear().apply();
    }


    @Test
    public void changeLayoutSettings() throws Exception {
        onView().withText(R.string.pref_layout).perform().click();
        onView().withText(getStringFromArray(0)).perform().click();

        assertThat(mApplicationPreferences.loadLayoutPreference()).isEqualTo(LAYOUT_OLD);
    }


    @NonNull
    private String getStringFromArray(int position) {
        return InstrumentationRegistry
                .getTargetContext()
                .getResources()
                .getStringArray(R.array.layout_type_items)[position];
    }


    @Test
    public void changeLayoutSettings1() throws Exception {
        onView().withText(R.string.pref_layout).perform().click();
        onView().withText(getStringFromArray(1)).perform().click();

        assertThat(mApplicationPreferences.loadLayoutPreference()).isEqualTo(LAYOUT_NEW);
    }

    // TODO: 30.08.2017 figure out how to enable vibration setting on AVD and add tests for vibration prefs
}
