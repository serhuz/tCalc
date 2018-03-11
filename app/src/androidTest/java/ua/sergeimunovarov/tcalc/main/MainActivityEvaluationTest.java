package ua.sergeimunovarov.tcalc.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static cortado.Cortado.onButton;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;
import static ua.sergeimunovarov.tcalc.CustomMatchers.hasCurrentText;


@RunWith(AndroidJUnit4.class)
public class MainActivityEvaluationTest {

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @SuppressWarnings("WeakerAccess")
    @InjectFromComponent
    ApplicationPreferences mApplicationPreferences;


    @Before
    public void setUp() {
        mApplicationPreferences.getPreferences().edit().clear().apply();
    }


    @Test
    public void performEvaluation1() {
        onView().withId(R.id.input).perform().typeText("2+2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 4"));
    }


    @Test
    public void performEvaluation2() {
        onView().withId(R.id.input).perform().typeText("2+2*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 6"));
    }


    @Test
    public void performEvaluation3() {
        onView().withId(R.id.input).perform().typeText("(2+2)*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 8"));
    }


    @Test
    public void performEvaluation4() {
        onView().withId(R.id.input).perform().typeText("2/0");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("Error (/0)"));
    }


    @Test
    public void performEvaluation5() {
        onView().withId(R.id.input).perform().typeText("2*0");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 0"));
    }


    @Test
    public void performEvaluation6() {
        onView().withId(R.id.input).perform().typeText("2*(-1)");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= -2"));
    }


    @Test
    public void performEvaluation7() {
        onView().withId(R.id.input).perform().typeText("2/(-1)");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= -2"));
    }


    @Test
    public void performEvaluation8() {
        onView().withId(R.id.input).perform().typeText("(-1)/2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= -0.5"));
    }


    @Test
    public void performEvaluation9() {
        onView().withId(R.id.input).perform().typeText("(-1)/2+0.5");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 0"));
    }


    @Test
    public void performEvaluation10() {
        onView().withId(R.id.input).perform().typeText("0:10*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:20:00"));
    }


    @Test
    public void performEvaluation11() {
        onView().withId(R.id.input).perform().typeText("0:10/2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:05:00"));
    }


    @Test
    public void performEvaluation12() {
        onView().withId(R.id.input).perform().typeText("0:10*2+0:05");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:25:00"));
    }


    @Test
    public void performEvaluation13() {
        onView().withId(R.id.input).perform().typeText("0:10*2+4*0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 01:00:00"));
    }


    @Test
    public void performEvaluation14() {
        onView().withId(R.id.input).perform().typeText("0:10-0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:00:00"));
    }


    @Test
    public void performEvaluation15() {
        onView().withId(R.id.input).perform().typeText("0:10-0:20");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= -00:10:00"));
    }


    @Test
    public void performEvaluation16() {
        onView().withId(R.id.input).perform().typeText("0:20/0:20");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 1"));
    }


    @Test
    public void performEvaluation17() {
        onView().withId(R.id.input).perform().typeText("0:20/0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 2"));
    }


    @Test
    public void performEvaluation18() {
        onView().withId(R.id.input).perform().typeText("0:20*0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("Error (T*T)"));
    }


    @Test
    public void performEvaluation19() {
        onView().withId(R.id.input).perform().typeText("0:20+1");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("Error (T+V)"));
    }


    @Test
    public void preformEvaluation20() {
        onEditText().withId(R.id.input).perform().typeText("0:00:10/3");
        onButton().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:00:03.333"));
    }


    @Test
    public void preformEvaluation21() {
        onEditText().withId(R.id.input).perform().typeText("0:00:9/3");
        onButton().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(hasCurrentText("= 00:00:03"));
    }
}
