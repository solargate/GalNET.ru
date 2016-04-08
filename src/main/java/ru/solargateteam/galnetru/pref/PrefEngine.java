package ru.solargateteam.galnetru.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.solargateteam.galnetru.R;

public class PrefEngine {

    private static final String PREF_REFRESH_INTERVAL = "PREF_REFRESH_INTERVAL";
    private static final String PREF_USE_NOTIFICATION = "PREF_USE_NOTIFICATION";
    private static final String PREF_USE_GALNET_FONT  = "PREF_USE_GALNET_FONT";

    // Hidden
    private static final String PREF_FIRST_START      = "PREF_FIRST_START";

    SharedPreferences sp;

    public PrefEngine(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void initDefaults(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    public int getRefreshInterval() {
        int res;
        String value = sp.getString(PREF_REFRESH_INTERVAL, "1h");
        switch (value) {
            case "1m":
                res = 1;
                break;
            case "15m":
                res = 15;
                break;
            case "30m":
                res = 30;
                break;
            case "1h":
                res = 60;
                break;
            case "4h":
                res = 240;
                break;
            case "6h":
                res = 360;
                break;
            default:
                res = 60;
                break;
        }
        return res;
    }

    public boolean isNotificationEnabled() {
        return sp.getBoolean(PREF_USE_NOTIFICATION, true);
    }

    public boolean useGalNETFont() {
        return sp.getBoolean(PREF_USE_GALNET_FONT, false);
    }

    public boolean isFirstStart() {
        if (sp.getBoolean(PREF_FIRST_START, true)) {
            sp.edit().putBoolean(PREF_FIRST_START, false).commit();
            return true;
        }

        return false;
    }
}
