package ua.sergeimunovarov.tcalc.info;


import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static cortado.Cortado.onView;
import static org.hamcrest.Matchers.allOf;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class HelpActivityTest {

    @Rule
    public DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public IntentsTestRule<HelpActivity> mIntentsTestRule = new IntentsTestRule<>(HelpActivity.class);

    @Test
    public void displayWebView() throws Exception {
        onView().withId(R.id.info_webview).check().matches(isDisplayed());
    }

    @Test
    public void displayDialogOnClick() throws Exception {
        onView().withId(R.id.action_rate).perform(click());

        onView().withText(R.string.title_dialog_rate).check().matches(isDisplayed());
        onView().withText(R.string.message_rate_dialog).check().matches(isDisplayed());
        onView().withText(R.string.btn_yes).check().matches(isDisplayed());
        onView().withText(R.string.btn_no).check().matches(isDisplayed());
    }

    @Test
    public void hideDialogOnCancel() throws Exception {
        onView().withId(R.id.action_rate).perform(click());

        onView().withText(R.string.btn_no).perform(click());

        onView().withText(R.string.title_dialog_rate).check().doesNotExist();
    }

    @Test
    public void goToGooglePlay() throws Exception {
        onView().withId(R.id.action_rate).perform(click());

        onView().withText(R.string.btn_yes).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(
                        Uri.parse(mIntentsTestRule.getActivity().getString(R.string.link_google_play))
                )
        ));
    }
}
