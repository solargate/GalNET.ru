package ru.solargateteam.galnetru.rss;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RSSReader {

    private String rssUrl;

    public RSSReader(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RSSItem> getItems() throws Exception {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();

        RSSParseHandler handler = new RSSParseHandler();

        saxParser.parse(rssUrl, handler);

        return handler.getItems();
    }
}
