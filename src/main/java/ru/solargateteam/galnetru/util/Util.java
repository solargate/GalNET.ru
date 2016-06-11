package ru.solargateteam.galnetru.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.R;

public class Util {

    public static String strProcessForWebView(String inputString, boolean useGalnetFont) {
        String returnString;
        returnString  = "<html><head><meta charset=\"utf-8\">";
        returnString += "<style type=\"text/css\">";
        if (useGalnetFont) {
            returnString += "@font-face {font-family: GalNET; src: url(\"file:///android_asset/" + Global.FONT_JURA_BOLD + "\")}";
            returnString += "body {color: " + R.color.colorEDOrange + "; font-family: GalNET; text-align: justify;}";
        }
        else
            returnString += "body {color: #E38D13; text-align: justify;}";
        returnString += "img{display: inline; height: auto; max-width: 100%;}";
        returnString += "</style>";
        returnString += "</head>";
        returnString += inputString;
        returnString += "</body></html>";
        return returnString;
    }

    public static String strProcessHTML(String inputString) {
        return inputString.replaceAll("</p>", "")
                .replaceAll("<p>", "\n")
                .replaceAll("\n\n\n", "\n\n")
                .replaceAll("&laquo;", "\"")
                .replaceAll("&raquo;", "\"")
                .replaceAll("&ldquo;", "\"")
                .replaceAll("&rdquo;", "\"")
                .replaceAll("&quot;", "\"")
                .replaceAll("<b>", "")
                .replaceAll("</b>", "")
                .replaceAll("<hr/>", "");
    }

    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnected());
    }

    public static String makeDateStringFromUnixTime(long unixTime) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US);
            Date date = new Date(unixTime * 1000);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
