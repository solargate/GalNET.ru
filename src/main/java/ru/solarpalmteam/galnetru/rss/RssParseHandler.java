package ru.solarpalmteam.galnetru.rss;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.solarpalmteam.galnetru.Global;

public class RssParseHandler extends DefaultHandler {

    private List<RssItem> rssItems;

    private RssItem currentItem;
    private boolean parsingTitle;
    private boolean parsingLink;
    private boolean parsingDescription;

    public RssParseHandler() {
        rssItems = new ArrayList<RssItem>();
    }

    public List<RssItem> getItems() {
        return rssItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (Global.RSS_TAG_ITEM.equals(qName)) {
            currentItem = new RssItem();
        } else if (Global.RSS_TAG_TITLE.equals(qName)) {
            parsingTitle = true;
        } else if (Global.RSS_TAG_LINK.equals(qName)) {
            parsingLink = true;
        } else if (Global.RSS_TAG_DESCRIPTION.equals(qName)) {
            parsingDescription = true;
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
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length)); // TODO: Проверить! Почему-то ниже парсинги в false, а здесь нет
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
        }
    }
}
