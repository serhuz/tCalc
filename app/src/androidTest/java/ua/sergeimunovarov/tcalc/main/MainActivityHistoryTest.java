/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onButton;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityHistoryTest {

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        InstrumentationRegistry.getTargetContext().deleteDatabase("tcalc_room.db");
    }


    @Test
    public void showEmptyHistory() {
        onView().withId(R.id.btn_history_toggle).perform().click();

        onView().withText(R.string.label_history_empty).check().matches(isDisplayed());
    }


    @Test
    public void showCalculationHistory() {
        onEditText().withId(R.id.input).perform().typeText("2+2");
        onButton().withId(R.id.btn_eq).perform().click();
        onEditText().withId(R.id.input).perform().clearText();
        onEditText().withId(R.id.input).perform().typeText("0:10*2");
        onButton().withId(R.id.btn_eq).perform().click();

        onView().withId(R.id.btn_history_toggle).perform().click();

        onView().withId(R.id.history_entries).check(matches(hasChildCount(2)));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("2+2"))));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("0:10*2"))));
    }
}
