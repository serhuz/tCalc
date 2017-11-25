/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.ClipboardManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.help.HelpActivity;
import ua.sergeimunovarov.tcalc.settings.SettingsActivity;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static ua.sergeimunovarov.tcalc.CustomMatchers.hasText;
import static ua.sergeimunovarov.tcalc.CustomMatchers.isToast;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public final IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    @SuppressWarnings("WeakerAccess")
    @InjectFromComponent
    ApplicationPreferences mApplicationPreferences;


    @After
    public void tearDown() throws Exception {
        mApplicationPreferences.getPreferences().edit().clear().apply();
    }


    @Test
    public void openFormatDialog() throws Exception {
        onView().withId(R.id.action_format).perform().click();

        onView().withText(R.string.title_dialog_format).check().matches(isDisplayed());
    }


    @Test
    public void chooseFormat1() throws Exception {
        onView().withId(R.id.action_format).perform().click();

        onView().withText(R.string.format_hms_mod24).perform().click();

        // verify that dialog is closed
        onView().withText(R.string.title_dialog_format).check().doesNotExist();

        assertThat(mApplicationPreferences.loadFormatPreference())
                .isEqualTo(ApplicationPreferences.FormatConstants.HMS_MOD24);
    }


    @Test
    public void chooseFormat2() throws Exception {
        onView().withId(R.id.action_format).perform().click();

        onView().withText(R.string.format_hms).perform().click();

        // verify that dialog is closed
        onView().withText(R.string.title_dialog_format).check().doesNotExist();

        assertThat(mApplicationPreferences.loadFormatPreference())
                .isEqualTo(ApplicationPreferences.FormatConstants.HMS);
    }


    @Test
    public void chooseFormat3() throws Exception {
        onView().withId(R.id.action_format).perform().click();

        onView().withText(R.string.format_dhms).perform().click();

        // verify that dialog is closed
        onView().withText(R.string.title_dialog_format).check().doesNotExist();

        assertThat(mApplicationPreferences.loadFormatPreference())
                .isEqualTo(ApplicationPreferences.FormatConstants.DHMS);
    }


    @Test
    public void chooseFormat4() throws Exception {
        onView().withId(R.id.action_format).perform().click();

        onView().withText(R.string.format_ms).perform().click();

        // verify that dialog is closed
        onView().withText(R.string.title_dialog_format).check().doesNotExist();

        assertThat(mApplicationPreferences.loadFormatPreference())
                .isEqualTo(ApplicationPreferences.FormatConstants.MS);
    }


    @Test
    public void launchSettingsActivity() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_settings).perform().click();

        intended(hasComponent(SettingsActivity.class.getName()));
    }


    @Test
    public void launchHelpActivity() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_instructions).perform().click();

        intended(hasComponent(HelpActivity.class.getName()));
    }


    @Test
    public void exit() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.action_exit).perform().click();

        assertThat(mActivityTestRule.getActivity().isFinishing()).isTrue();
    }


    @Test
    public void openInsertTimeDialog() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.insert_current_time).perform().click();

        onView().withText(R.string.select_time_format).check().matches(isDisplayed());
    }


    @Test
    public void insertTime() throws Exception {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView().withText(R.string.insert_current_time).perform().click();

        onView().withText(R.string.format_hms).perform().click();

        // verify dialog is closed
        onView().withText(R.string.select_time_format).check().doesNotExist();
        onView().withId(R.id.input).check().matches(hasText());
    }


    @Test
    public void copyResult() throws Exception {
        onEditText().withId(R.id.input).perform().typeText("2+2");
        onView().withId(R.id.btn_eq).perform().click();

        onView().withId(R.id.action_copy).perform().click();

        Espresso.onView(withText(R.string.toast_result_copied)).inRoot(isToast()).check(matches(isDisplayed()));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            ClipboardManager clipboardManager = InstrumentationRegistry.getTargetContext().getSystemService(ClipboardManager.class);
            assertThat(clipboardManager.hasPrimaryClip()).isTrue();
            assertThat(clipboardManager.getPrimaryClip().getItemAt(0).getText()).isEqualTo("4");
        });
    }
}
