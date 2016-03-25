package ru.solargateteam.galnetru;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnected());
    }

    public static long getUnixTime(String dateString) {
        long unixTime = 0;
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            Date date = dateFormat.parse(dateString);
            unixTime = date.getTime() / 1000;
        } catch (ParseException e) {
            Log.e(Global.TAG, "Wrong pubDate: " + dateString);
        }
        return unixTime;
    }

    // Хак на кривые ссылки
    public static boolean checkURL(String url) {
        if (url.charAt(url.length() - 1) == '0' ||
                url.charAt(url.length() - 1) == '1' ||
                url.charAt(url.length() - 1) == '2' ||
                url.charAt(url.length() - 1) == '3' ||
                url.charAt(url.length() - 1) == '4' ||
                url.charAt(url.length() - 1) == '5' ||
                url.charAt(url.length() - 1) == '6' ||
                url.charAt(url.length() - 1) == '7' ||
                url.charAt(url.length() - 1) == '8' ||
                url.charAt(url.length() - 1) == '9') {
            return true;
        }
        return false;
    }

}
