package com.in28minutes.springboot.myfirstwebapp.context;

import java.util.Locale;

public class LanguageContext {

    private static final ThreadLocal<Locale> CONTEXT = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        CONTEXT.set(locale);
    }

    public static Locale getLocale() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}