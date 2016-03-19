package ru.solargateteam.galnetru;

import android.app.Application;

public class GalNetRuApp extends Application{

    private static GalNetRuApp app;

    public static GalNetRuApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
