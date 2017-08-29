/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import com.squareup.leakcanary.RefWatcher;

import dagger.Module;
import dagger.Provides;


@Module
public class LeakCanaryModule {

    private final RefWatcher refWatcher;


    public LeakCanaryModule(RefWatcher refWatcher) {
        this.refWatcher = refWatcher;
    }


    @Provides
    protected RefWatcher provideRefWatcher() {
        return refWatcher;
    }
}
