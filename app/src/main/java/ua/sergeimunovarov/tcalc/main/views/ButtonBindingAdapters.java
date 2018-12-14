/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;


public final class ButtonBindingAdapters {


    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("repeatInterval")
    public static void setRepeatActionListener(@NonNull ImageButton button,
                                               @IntRange(from = 100) int repeatDelay) {
        Handler[] handler = {null};

        Runnable repeatRunnable = new Runnable() {
            @Override
            public void run() {
                button.performClick();
                handler[0].postDelayed(this, repeatDelay);
            }
        };

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (handler[0] != null) return true;
                    handler[0] = new Handler();
                    handler[0].postDelayed(repeatRunnable, repeatDelay);
                    break;
                case MotionEvent.ACTION_UP:
                    if (handler[0] == null) return true;
                    handler[0].removeCallbacks(repeatRunnable);
                    handler[0] = null;
                    break;
                default:
                    break;
            }
            return false;
        });
    }
}
