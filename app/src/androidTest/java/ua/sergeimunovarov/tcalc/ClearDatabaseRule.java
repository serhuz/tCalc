/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import androidx.test.InstrumentationRegistry;


public class ClearDatabaseRule implements TestRule {

    private final String mDatabaseName;


    public ClearDatabaseRule(String databaseName) {
        mDatabaseName = databaseName;
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                InstrumentationRegistry.getTargetContext().deleteDatabase(mDatabaseName);
                base.evaluate();
            }
        };
    }
}
