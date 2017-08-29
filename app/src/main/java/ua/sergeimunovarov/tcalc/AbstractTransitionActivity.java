/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.Fade;
import android.view.Window;


public abstract class AbstractTransitionActivity extends Activity {

    protected static final int TRANSITION_DURATION_MILLIS = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setDuration(TRANSITION_DURATION_MILLIS);

        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(TRANSITION_DURATION_MILLIS);

        getWindow().setEnterTransition(fadeIn);
        getWindow().setExitTransition(fadeOut);
    }


    public void launchActivity(@NonNull Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            super.startActivity(intent);
        } else {
            //noinspection unchecked
            super.startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            );
        }
    }
}
