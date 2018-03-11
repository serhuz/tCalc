/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.di;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.di.annotation.HistoryFactory;
import ua.sergeimunovarov.tcalc.di.annotation.MainActivityFactory;
import ua.sergeimunovarov.tcalc.main.Editor;
import ua.sergeimunovarov.tcalc.main.MainActivityViewModel;
import ua.sergeimunovarov.tcalc.main.feedback.HapticFeedback;
import ua.sergeimunovarov.tcalc.main.history.HistoryViewModel;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;
import ua.sergeimunovarov.tcalc.main.viewmodel.StringProvider;


@Module
public class ViewModelModule {

    @Provides
    @HistoryFactory
    @Singleton
    protected ViewModelProvider.Factory provideHistoryViewModelFactory(EntryDao dao) {
        return new HistoryViewModel.Factory(dao);
    }


    @Provides
    @MainActivityFactory
    @Singleton
    protected ViewModelProvider.Factory provideMainActivityViewModelFactory(Editor editor,
                                                                            ApplicationPreferences preferences,
                                                                            StringProvider provider) {
        return new MainActivityViewModel.Factory(editor, preferences, provider);
    }


    @Provides
    @Singleton
    protected Editor provideEditor(HapticFeedback feedback) {
        return new Editor(feedback);
    }


    @Provides
    @Singleton
    protected StringProvider provideStringProvider(Context context) {
        return new StringProvider(context);
    }
}
