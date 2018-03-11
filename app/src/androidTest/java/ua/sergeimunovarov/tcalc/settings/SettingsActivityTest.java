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

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onTextView;
import static cortado.Cortado.onView;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static ua.sergeimunovarov.tcalc.ApplicationPreferences.LayoutConstants.LAYOUT_NEW;
import static ua.sergeimunovarov.tcalc.ApplicationPreferences.LayoutConstants.LAYOUT_OLD;
import static ua.sergeimunovarov.tcalc.CustomViewActions.swipeSeekBar;


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


    @Test
    public void changePrecision() throws Exception {
        onView().withText(R.string.pref_precision).perform().click();
        onView().withId(R.id.seekbar).perform(swipeSeekBar(1));

        onTextView().withId(R.id.seekbar_value).check(matches(withText("2")));

        onView().withText(R.string.btn_ok).perform().click();

        assertThat(mApplicationPreferences.loadPrecisionPreference()).isEqualTo(2);
    }
}
