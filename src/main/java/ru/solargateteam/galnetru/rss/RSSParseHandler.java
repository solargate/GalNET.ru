package ru.solargateteam.galnetru.rss;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.solargateteam.galnetru.Global;

public class RSSParseHandler extends DefaultHandler {

    private List<RSSItem> rssItems;

    private RSSItem currentItem;
    private boolean parsingTitle;
    private boolean parsingLink;
    private boolean parsingDescription;
    private boolean parsingGUID;
    private boolean parsingPubDate;

    public RSSParseHandler() {
        rssItems = new ArrayList<RSSItem>();
    }

    public List<RSSItem> getItems() {
        return rssItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (Global.RSS_TAG_ITEM.equals(qName)) {
            currentItem = new RSSItem();
        } else if (Global.RSS_TAG_TITLE.equals(qName)) {
            parsingTitle = true;
        } else if (Global.RSS_TAG_LINK.equals(qName)) {
            parsingLink = true;
        } else if (Global.RSS_TAG_DESCRIPTION.equals(qName)) {
            parsingDescription = true;
        } else if (Global.RSS_TAG_GUID.equals(qName)) {
            parsingGUID = true;
        } else if (Global.RSS_TAG_PUBDATE.equals(qName)) {
            parsingPubDate = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (Global.RSS_TAG_ITEM.equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;
        } else if (Global.RSS_TAG_TITLE.equals(qName)) {
            parsingTitle = false;
        } else if (Global.RSS_TAG_LINK.equals(qName)) {
            parsingLink = false;
        } else if (Global.RSS_TAG_DESCRIPTION.equals(qName)) {
            parsingDescription = false;
        } else if (Global.RSS_TAG_GUID.equals(qName)) {
            parsingGUID = false;
        } else if (Global.RSS_TAG_PUBDATE.equals(qName)) {
            parsingPubDate = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null) {
                currentItem.setTitle(new String(ch, start, length));
            }
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        } else if (parsingDescription) {
            if (currentItem != null) {
                currentItem.setDescription(new String(ch, start, length));
                parsingDescription = false;
            }
        } else if (parsingGUID) {
            if (currentItem != null) {
                currentItem.setGuid(new String(ch, start, length));
                parsingGUID = false;
            }
        } else if (parsingPubDate) {
            if (currentItem != null) {
                currentItem.setPubDate(new String(ch, start, length));
                parsingPubDate = false;
            }
        }
    }
}
