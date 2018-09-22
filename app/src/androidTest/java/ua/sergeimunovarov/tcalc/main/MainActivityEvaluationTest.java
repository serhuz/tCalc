/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ua.sergeimunovarov.tcalc.CustomMatchers.hasCurrentText;
import static ua.sergeimunovarov.tcalc.CustomViewActions.setText;


@RunWith(Parameterized.class)
public class MainActivityEvaluationTest {

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Parameterized.Parameter()
    public String givenText;

    @Parameterized.Parameter(1)
    public String expectedText;

    @SuppressWarnings("WeakerAccess")
    @InjectFromComponent
    ApplicationPreferences mApplicationPreferences;


    @Before
    public void setUp() {
        mApplicationPreferences.getPreferences().edit().clear().apply();
    }


    @Test
    public void performEvaluation() {
        onView(withId(R.id.input)).perform(setText(givenText));
        onView(withId(R.id.btn_eq)).perform(click());
        onView(withId(R.id.result)).check(matches(hasCurrentText(expectedText)));
    }


    @Parameterized.Parameters(name = "Evaluate expression {index}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(
                new Object[][]{
                        {"2+2", "= 4"},
                        {"2+2*2", "= 6"},
                        {"(2+2)*2", "= 8"},
                        {"2/0", "Error (/0)"},
                        {"2*0", "= 0"},
                        {"2*(-1)", "= -2"},
                        {"2/(-1)", "= -2"},
                        {"(-1)/2", "= -0.5"},
                        {"(-1)/2+0.5", "= 0"},
                        {"0:10*2", "= 00:20:00"},
                        {"0:10/2", "= 00:05:00"},
                        {"0:10*2+0:05", "= 00:25:00"},
                        {"0:10*2+4*0:10", "= 01:00:00"},
                        {"0:10-0:10", "= 00:00:00"},
                        {"0:10-0:20", "= -00:10:00"},
                        {"0:20/0:20", "= 1"},
                        {"0:20/0:10", "= 2"},
                        {"0:20*0:10", "Error (T*T)"},
                        {"0:20+1", "Error (T+V)"},
                        {"0:00:10/3", "= 00:00:03.333"},
                        {"0:00:9/3", "= 00:00:03"}
                }
        );
    }
}
