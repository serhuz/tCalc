/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ClearDatabaseRule;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.history.db.HistoryDbHelper;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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

    @ClassRule
    public static final ClearDatabaseRule CLEAR_DATABASE_RULE = new ClearDatabaseRule(HistoryDbHelper.DB_NAME);

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @InjectFromComponent
    HistoryDbHelper mDbHelper;


    @After
    public void tearDown() throws Exception {
        mDbHelper.clearData();
    }


    @Test
    public void showEmptyHistory() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_history).perform().click();

        onView().withText(R.string.label_history_empty).check().matches(isDisplayed());
    }


    @Test
    public void showCalculationHistory() throws Exception {
        onEditText().withId(R.id.input).perform().typeText("2+2");
        onButton().withId(R.id.btn_eq).perform().click();
        onEditText().withId(R.id.input).perform().clearText();
        onEditText().withId(R.id.input).perform().typeText("0:10*2");
        onButton().withId(R.id.btn_eq).perform().click();

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_history).perform().click();

        onView().withId(R.id.history_entries).check(matches(hasChildCount(2)));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("2+2"))));
        onView().withId(R.id.history_entries).check(matches(hasDescendant(withText("0:10*2"))));
    }


    @Test
    public void closeDialog() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_history).perform().click();

        onView().withText(R.string.btn_close).perform().click();

        onView().withText(R.string.title_dialog_history).check().doesNotExist();
    }
}
