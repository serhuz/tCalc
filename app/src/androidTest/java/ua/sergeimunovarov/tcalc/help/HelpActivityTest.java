package ua.sergeimunovarov.tcalc.help;


import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@MediumTest
@RunWith(AndroidJUnit4.class)
public class HelpActivityTest {

    @Rule
    public DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public IntentsTestRule<HelpActivity> mIntentsTestRule = new IntentsTestRule<>(HelpActivity.class);


    @Test
    public void displayWebView() {
        onView(withId(R.id.info_webview)).check(matches(isDisplayed()));
    }


    @Test
    public void displayDialogOnClick() {
        onView(withId(R.id.action_rate)).perform(click());

        onView(withText(R.string.title_dialog_rate)).check(matches(isDisplayed()));
        onView(withText(R.string.message_rate_dialog)).check(matches(isDisplayed()));
        onView(withText(R.string.btn_yes)).check(matches(isDisplayed()));
        onView(withText(R.string.btn_no)).check(matches(isDisplayed()));
    }


    @Test
    public void hideDialogOnCancel() {
        onView(withId(R.id.action_rate)).perform(click());

        onView(withText(R.string.btn_no)).perform(click());

        onView(withText(R.string.title_dialog_rate)).check(doesNotExist());
    }


    @Test
    public void goToGooglePlay() {
        onView(withId(R.id.action_rate)).perform(click());

        onView(withText(R.string.btn_yes)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(
                        Uri.parse(mIntentsTestRule.getActivity().getString(R.string.link_google_play))
                )
        ));
    }


    @Test
    public void goToOSSLicenseActivity() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.action_licenses)).perform(click());

        intended(hasComponent(OssLicensesMenuActivity.class.getName()));
    }


    @Test
    public void openPrivacyPolicy() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.action_privacy_policy)).perform(click());

        intended(hasData(Uri.parse(BuildConfig.PRIVACY_POLICY_URL)));
    }
}
