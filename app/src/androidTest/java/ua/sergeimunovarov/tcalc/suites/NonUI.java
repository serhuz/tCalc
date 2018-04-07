/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ua.sergeimunovarov.tcalc.main.ops.CalcFacadeAndroidTest;
import ua.sergeimunovarov.tcalc.main.ops.ResultTest;
import ua.sergeimunovarov.tcalc.main.ops.TokenTest;
import ua.sergeimunovarov.tcalc.settings.SavedStateTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        SavedStateTest.class,
        CalcFacadeAndroidTest.class,
        ResultTest.class,
        TokenTest.class
})
public class NonUI {

}
