/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.pm.ActivityInfo;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.CustomActivityTestRule;
import ua.sergeimunovarov.tcalc.LocaleRule;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.ScreenshotWatcher;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ua.sergeimunovarov.tcalc.Locales.english;
import static ua.sergeimunovarov.tcalc.Locales.russian;
import static ua.sergeimunovarov.tcalc.Locales.ukrainian;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityScreenshot {

    private final LocaleRule mLocaleRule = new LocaleRule(english(), russian(), ukrainian());
    private final ScreenshotWatcher mScreenshotWatcher = new ScreenshotWatcher();
    private final GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
    private final CustomActivityTestRule mActivityTestRule = new CustomActivityTestRule<>(MainActivity.class);

    @Rule
    public final RuleChain mRuleChain = RuleChain.outerRule(mLocaleRule)
            .around(mScreenshotWatcher)
            .around(mGrantPermissionRule)
            .around(mActivityTestRule);


    @Test
    public void emptyMainActivityPortrait() {
    }


    @Test
    public void emptyMainActivityLandscape() {
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    @Test
    public void formatSelectionDialog() {
        onView(withId(R.id.indicator_format)).perform(click());
    }


    @Test
    public void mainActivityEvaluateExpression() {
        onView(withId(R.id.input)).perform(typeText("20:00-2*5:00"));
        onView(withId(R.id.btn_eq)).perform(click());
    }
}
