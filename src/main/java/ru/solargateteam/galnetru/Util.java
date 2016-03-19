package ru.solargateteam.galnetru;

public class Util {

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
