package ru.solargateteam.galnetru;

import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupParser {

    @Nullable
    public static String getImageURL(String newsURL) {

        try {

            Document doc = Jsoup.connect(newsURL).get();
            Elements img = doc.select("img.u-photo");
            return img.attr("src");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
