/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onButton;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;
import static ua.sergeimunovarov.tcalc.CustomViewActions.setText;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityHistoryTest {

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @InjectFromComponent
    EntryDao mDao;


    @Before
    public void setUp() {
        mDao.deleteAll();
    }


    @Ignore("Flaky af")
    @Test
    public void showEmptyHistory() {
        onView().withId(R.id.btn_history_toggle).perform().click();

        onView().withText(R.string.label_history_empty).check().matches(isDisplayed());
    }


    @Test
    public void showCalculationHistory() {
        onEditText().withId(R.id.input).perform(setText("2+2"));
        onButton().withId(R.id.btn_eq).perform().click();
        onEditText().withId(R.id.input).perform().clearText();
        onEditText().withId(R.id.input).perform(setText("0:10*2"));
        onButton().withId(R.id.btn_eq).perform().click();

        onView().withId(R.id.btn_history_toggle).perform().click();

        onView().withId(R.id.history_entries).check(matches(hasChildCount(2)));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("2+2"))));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("0:10*2"))));
    }


    @Test
    public void notAddDuplicates() {
        onEditText().withId(R.id.input).perform(setText("2+2"));
        onButton().withId(R.id.btn_eq).perform().click();
        onEditText().withId(R.id.input).perform().clearText();
        onEditText().withId(R.id.input).perform(setText("2+2"));
        onButton().withId(R.id.btn_eq).perform().click();

        onView().withId(R.id.btn_history_toggle).perform().click();

        onView().withId(R.id.history_entries).check(matches(hasChildCount(1)));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("2+2"))));
    }
}
