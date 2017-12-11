/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import java.util.Locale;


public final class Locales {

    private Locales() {
        throw new AssertionError();
    }


    public static Locale english() {
        return Locale.ENGLISH;
    }


    public static Locale russian() {
        return new Locale.Builder().setLanguage("ru").build();
    }


    public static Locale ukrainian() {
        return new Locale.Builder().setLanguage("uk").build();
    }
}
