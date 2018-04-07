/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ua.sergeimunovarov.tcalc.help.HelpActivityTest;
import ua.sergeimunovarov.tcalc.settings.SettingsActivityTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        HelpActivityTest.class,
        SettingsActivityTest.class
})
public class SecondaryScreens {

}
