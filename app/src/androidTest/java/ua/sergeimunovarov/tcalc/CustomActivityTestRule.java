/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.internal.runner.junit4.statement.UiThreadStatement;
import android.support.test.runner.MonitoringInstrumentation;
import android.support.test.runner.intercepting.SingleActivityFactory;
import android.util.Log;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;

import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.internal.util.Checks.checkState;


/**
 * This rule provides functional testing of a single {@link Activity}. When {@code launchActivity}
 * is set to true in the constructor, the Activity under test will be launched before each test
 * annotated with <a href="http://junit.org/javadoc/latest/org/junit/Test.html"><code>Test</code>
 * </a> and before methods annotated with <a
 * href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>, and it
 * will be terminated after the test is completed and methods annotated with <a
 * href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After
 * </code></a> are finished.
 * <p>
 * <p>The Activity can be manually launched with {@link #launchActivity(Intent)}, and manually
 * finished with {@link #finishActivity()}. If the Activity is running at the end of the test, the
 * test rule will finish it.
 * <p>
 * <p>During the duration of the test you will be able to manipulate your Activity directly using
 * the reference obtained from {@link #getActivity()}. If the Activity is finished and relaunched,
 * the reference returned by {@link #getActivity()} will always point to the current instance of the
 * Activity.
 *
 * @param <T> The Activity class under test
 */
public class CustomActivityTestRule<T extends Activity> implements TestRule {

    public static final String FIELD_RESULT_CODE = "mResultCode";
    public static final String FIELD_RESULT_DATA = "mResultData";

    private static final String TAG = "ActivityTestRule";
    private static final int NO_FLAGS_SET = 0;

    private final Class<T> mActivityClass;
    private final String mTargetPackage;

    private final int mLaunchFlags;

    @VisibleForTesting
    T mActivity;

    private Instrumentation mInstrumentation;
    private boolean mInitialTouchMode = false;
    private boolean mLaunchActivity = false;
    private SingleActivityFactory<T> mActivityFactory;


    /**
     * Similar to {@link #CustomActivityTestRule(Class, boolean)} but with "touch mode" disabled.
     *
     * @param activityClass The activity under test. This must be a class in the instrumentation
     *                      targetPackage specified in the AndroidManifest.xml
     * @see CustomActivityTestRule#CustomActivityTestRule(Class, boolean, boolean)
     */
    public CustomActivityTestRule(Class<T> activityClass) {
        this(activityClass, false);
    }


    /**
     * Similar to {@link #CustomActivityTestRule(Class, boolean, boolean)} but defaults to launch the
     * activity under test once per <a href="http://junit.org/javadoc/latest/org/junit/Test.html">
     * <code>Test</code></a> method. It is launched before the first <a
     * href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>
     * method, and terminated after the last <a
     * href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a> method.
     *
     * @param activityClass    The activity under test. This must be a class in the instrumentation
     *                         targetPackage specified in the AndroidManifest.xml
     * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
     * @see CustomActivityTestRule#CustomActivityTestRule(Class, boolean, boolean)
     */
    public CustomActivityTestRule(Class<T> activityClass, boolean initialTouchMode) {
        this(activityClass, initialTouchMode, true);
    }


    /**
     * Similar to {@link #CustomActivityTestRule(Class, String, int, boolean, boolean)} but defaults to
     * launch the Activity with the default target package name {@link
     * InstrumentationRegistry#getTargetContext()#getPackageName} and {@link
     * Intent#FLAG_ACTIVITY_NEW_TASK} launch flag.
     *
     * @param activityClass    The activity under test. This must be a class in the instrumentation
     *                         targetPackage specified in the AndroidManifest.xml
     * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
     * @param launchActivity   true if the Activity should be launched once per <a
     *                         href="http://junit.org/javadoc/latest/org/junit/Test.html"><code>Test</code></a> method. It
     *                         will be launched before the first <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>
     *                         method, and terminated after the last <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a>
     *                         method.
     */
    public CustomActivityTestRule(
            Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        this(
                activityClass,
                InstrumentationRegistry.getTargetContext().getPackageName(),
                Intent.FLAG_ACTIVITY_NEW_TASK,
                initialTouchMode,
                launchActivity);
    }


    /**
     * Creates an {@link CustomActivityTestRule} for the Activity under test.
     *
     * @param activityClass    The activity under test. This must be a class in the instrumentation
     *                         targetPackage specified in the AndroidManifest.xml
     * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
     * @param launchActivity   true if the Activity should be launched once per <a
     *                         href="http://junit.org/javadoc/latest/org/junit/Test.html"><code>Test</code></a> method. It
     *                         will be launched before the first <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>
     *                         method, and terminated after the last <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a>
     *                         method.
     * @param targetPackage    The name of the target package that the Activity is started under. This
     *                         value is passed down to the start Intent using {@link
     *                         Intent#setClassName(android.content.Context, String)}. Can not be null.
     * @param launchFlags      launch flags to start the Activity under test.
     */
    public CustomActivityTestRule(
            Class<T> activityClass,
            @NonNull String targetPackage,
            int launchFlags,
            boolean initialTouchMode,
            boolean launchActivity) {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mActivityClass = activityClass;
        mTargetPackage = checkNotNull(targetPackage, "targetPackage cannot be null!");
        mLaunchFlags = launchFlags;
        mInitialTouchMode = initialTouchMode;
        mLaunchActivity = launchActivity;
    }


    /**
     * Creates an {@link CustomActivityTestRule} for the Activity under test.
     *
     * @param activityFactory  factory to be used for creating Activity instance
     * @param initialTouchMode true if the Activity should be placed into "touch mode" when started
     * @param launchActivity   true if the Activity should be launched once per <a
     *                         href="http://junit.org/javadoc/latest/org/junit/Test.html"><code>Test</code></a> method. It
     *                         will be launched before the first <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>
     *                         method, and terminated after the last <a
     *                         href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a>
     *                         method.
     */
    public CustomActivityTestRule(
            SingleActivityFactory<T> activityFactory, boolean initialTouchMode, boolean launchActivity) {
        this(activityFactory.getActivityClassToIntercept(), initialTouchMode, launchActivity);
        mActivityFactory = activityFactory;
    }


    /**
     * @return The activity under test.
     */
    public T getActivity() {
        if (mActivity == null) {
            Log.w(TAG, "Activity wasn't created yet");
        }
        return mActivity;
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new ActivityStatement(base);
    }


    /**
     * Launches the Activity under test.
     * <p>
     * <p>Don't call this method directly, unless you explicitly requested not to lazily launch the
     * Activity manually using the launchActivity flag in {@link #CustomActivityTestRule(Class, boolean,
     * boolean)}.
     * <p>
     * <p>Usage:
     * <p>
     * <pre>
     *    &#064;Test
     *    public void customIntentToStartActivity() {
     *        Intent intent = new Intent(Intent.ACTION_PICK);
     *        mActivity = mActivityRule.launchActivity(intent);
     *    }
     * </pre>
     * <p>
     * Note: Custom start Intents provided through this method will take precedence over default
     * Intents that where created in the constructor and any Intent returned from {@link
     * #getActivityIntent()}. The same override rules documented in {@link #getActivityIntent()}
     * apply.
     *
     * @param startIntent The Intent that will be used to start the Activity under test. If {@code
     *                    startIntent} is null, the Intent returned by {@link CustomActivityTestRule#getActivityIntent()}
     *                    is used.
     * @return the Activity launched by this rule.
     */
    public T launchActivity(@Nullable Intent startIntent) {
        if (mActivity != null) {
            // Log a warning since otherwise it's a breaking public change.
            Log.w(
                    TAG,
                    "Activity has already been launched! It must be finished by calling finishActivity() "
                            + "before launchActivity is called again. This warning will turn into a fatal "
                            + "exception in the next version of this library. For now, ignoring and letting "
                            + "fall through.");
        }

        // set initial touch mode
        mInstrumentation.setInTouchMode(mInitialTouchMode);

        // inject custom intent, if provided
        if (null == startIntent) {
            startIntent = getActivityIntent();
            if (null == startIntent) {
                Log.w(
                        TAG,
                        "getActivityIntent() returned null using default: " + "Intent(Intent.ACTION_MAIN)");
                startIntent = new Intent(Intent.ACTION_MAIN);
            }
        }

        // Set target component if not set Intent
        if (null == startIntent.getComponent()) {
            startIntent.setClassName(mTargetPackage, mActivityClass.getName());
        }

        // Set launch flags where if not set Intent
        if (NO_FLAGS_SET == startIntent.getFlags()) {
            startIntent.addFlags(mLaunchFlags);
        }

        Log.i(TAG, String.format("Launching activity: %s", startIntent.getComponent()));

        beforeActivityLaunched();
        // The following cast is correct because the activity we're creating is of the same type as
        // the one passed in
        mActivity = mActivityClass.cast(mInstrumentation.startActivitySync(startIntent));

        mInstrumentation.waitForIdleSync();

        if (mActivity != null) {
            // Notify that Activity was successfully launched
            afterActivityLaunched();
        } else {
            // Log an error message to logcat/instrumentation, that the Activity failed to launch
            String errorMessage =
                    String.format("Activity %s, failed to launch", startIntent.getComponent());
            Bundle bundle = new Bundle();
            bundle.putString(Instrumentation.REPORT_KEY_STREAMRESULT, TAG + " " + errorMessage);
            mInstrumentation.sendStatus(0, bundle);
            Log.e(TAG, errorMessage);
        }

        return mActivity;
    }


    /**
     * Override this method to set up a custom Intent as if supplied to {@link
     * android.content.Context#startActivity}. Custom Intents provided by this method will take
     * precedence over default Intents that where created in the constructor but be overridden by any
     * Intents passed in through {@link #launchActivity(Intent)}.
     * <p>
     * <p>The default Intent (if this method returns null or is not overwritten) is: action = {@link
     * Intent#ACTION_MAIN} flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK} All other intent fields are
     * null or empty.
     * <p>
     * <p>If the custom Intent provided by this methods overrides any of the following fields:
     * <p>
     * <ul>
     * <li>componentName through {@link Intent#setClassName(String, String)}
     * <li>launch flags through {@link Intent#setFlags(int)}
     * </ul>
     * <p>
     * <p>These custom values will be used to start the Activity. However, if some of these values are
     * not set, the default values documented in {@link #CustomActivityTestRule(Class, String, int, boolean,
     * boolean)} are supplemented.
     *
     * @return The Intent as if supplied to {@link android.content.Context#startActivity}.
     */
    protected Intent getActivityIntent() {
        return null;
    }


    /**
     * Override this method to execute any code that should run before your {@link Activity} is
     * created and launched. This method is called before each test method, including any method
     * annotated with <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>
     * Before</code></a>.
     */
    protected void beforeActivityLaunched() {
        // empty by default
    }


    /**
     * Override this method to execute any code that should run after your {@link Activity} is
     * launched, but before any test code is run including any method annotated with <a
     * href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before</code></a>.
     * <p>
     * <p>Prefer <a href="http://junit.sourceforge.net/javadoc/org/junit/Before.html"><code>Before
     * </code></a> over this method. This method should usually not be overwritten directly in tests
     * and only be used by subclasses of ActivityTestRule to get notified when the activity is created
     * and visible but test runs.
     */
    protected void afterActivityLaunched() {
        // empty by default
    }


    @VisibleForTesting
    void setInstrumentation(Instrumentation instrumentation) {
        mInstrumentation = checkNotNull(instrumentation, "instrumentation cannot be null!");
    }


    /**
     * Finishes the currently launched Activity.
     *
     * @throws IllegalStateException if the Activity is not running.
     */
    public void finishActivity() {
        checkState(
                mActivity != null,
                "Activity was not launched. If you manually finished it, you must launch it"
                        + " again before finishing it.");

        mActivity.finish();
        afterActivityFinished();
        mActivity = null;
        mInstrumentation.waitForIdleSync();
    }


    /**
     * Override this method to execute any code that should run after your {@link Activity} is
     * finished. This method is called after each test method, including any method annotated with <a
     * href="http://junit.sourceforge.net/javadoc/org/junit/After.html"><code>After</code></a>.
     */
    protected void afterActivityFinished() {
        // empty by default
    }


    /**
     * This method can be used to retrieve the Activity result of an Activity that has called
     * setResult. Usually, the result is handled in {@code onActivityResult} of parent activity, that
     * has called {@code startActivityForResult}.
     * <p>
     * <p>This method must not be called before {@code Activity.finish} was called.
     *
     * @return the ActivityResult that was set most recently
     */
    public ActivityResult getActivityResult() {
        T activity = mActivity;
        checkState(activity.isFinishing(), "Activity is not finishing!");

        try {
            Field resultCodeField = Activity.class.getDeclaredField(FIELD_RESULT_CODE);
            resultCodeField.setAccessible(true);

            Field resultDataField = Activity.class.getDeclaredField(FIELD_RESULT_DATA);
            resultDataField.setAccessible(true);

            return new ActivityResult(
                    (int) resultCodeField.get(activity), (Intent) resultDataField.get(activity));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                    "Looks like the Android Activity class has changed its"
                            + "private fields for mResultCode or mResultData. "
                            + "Time to update the reflection code.",
                    e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Field mResultCode or mResultData is not accessible", e);
        }
    }


    /**
     * Helper method for running part of a method on the UI thread.
     * <p>
     * <p>Note: In most cases it is simpler to annotate the test method with {@link UiThreadTest}.
     * <p>
     * <p>Use this method if you need to switch in and out of the UI thread within your method.
     *
     * @param runnable runnable containing test code in the {@link Runnable#run()} method
     * @see UiThreadTest
     */
    public void runOnUiThread(Runnable runnable) throws Throwable {
        UiThreadStatement.runOnUiThread(runnable);
    }


    /**
     * <a href="http://junit.org/apidocs/org/junit/runners/model/Statement.html"><code>Statement
     * </code></a> that finishes the activity after the test was executed
     */
    private class ActivityStatement extends Statement {

        private final Statement mBase;


        public ActivityStatement(Statement base) {
            mBase = base;
        }


        @Override
        public void evaluate() throws Throwable {
            MonitoringInstrumentation instrumentation =
                    CustomActivityTestRule.this.mInstrumentation instanceof MonitoringInstrumentation
                            ? (MonitoringInstrumentation) CustomActivityTestRule.this.mInstrumentation
                            : null;
            try {
                if (mActivityFactory != null && instrumentation != null) {
                    instrumentation.interceptActivityUsing(mActivityFactory);
                }
                if (mLaunchActivity) {
                    launchActivity(getActivityIntent());
                }
                mBase.evaluate();

                SystemClock.sleep(1_000);
            } finally {
                if (instrumentation != null) {
                    instrumentation.useDefaultInterceptingActivityFactory();
                }
                if (mActivity != null) {
                    finishActivity();
                }
            }
        }
    }
}
