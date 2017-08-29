/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;


import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


public class DebugApplication extends Application {

    @Override
    protected RefWatcher installLeakCanary() {
        return LeakCanary.install(this);
    }
}
