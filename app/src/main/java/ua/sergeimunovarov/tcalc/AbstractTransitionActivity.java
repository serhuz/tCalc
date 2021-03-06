/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;


public abstract class AbstractTransitionActivity extends AppCompatActivity {

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
                    intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            );
        }
    }
}
