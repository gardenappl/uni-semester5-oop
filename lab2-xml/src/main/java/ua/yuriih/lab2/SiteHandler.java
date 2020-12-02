package ua.yuriih.lab2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.yuriih.lab2.model.ObjectFactory;
import ua.yuriih.lab2.model.Page;
import ua.yuriih.lab2.model.Site;
import ua.yuriih.lab2.model.Type;

public class SiteHandler extends DefaultHandler {
    private static final String PAGE = "page";
    private static final String ID = "id";
    
    private static final String TITLE = "title";
    private static final String TYPE = "type";
    private static final String CHARS = "chars";
    private static final String AUTHORIZE = "authorize";

    private static final String HAS_EMAIL = "hasEmail";
    private static final String HAS_NEWS = "hasNews";
    private static final String HAS_ARCHIVES = "hasArchives";
    private static final String POLL = "poll";
    private static final String PAID = "paid";

    private static final ObjectFactory objectFactory = new ObjectFactory();
    private Site currentSite;
    private Page currentPage;
    private String currentElementData;

    @Override
    public void startDocument() throws SAXException {
        currentSite = objectFactory.createSite();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals(PAGE)) {
            currentPage = objectFactory.createPage();
            currentPage.setId(attributes.getValue(ID));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentElementData = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case PAGE -> currentSite.getPage().add(currentPage);

            case TITLE -> currentPage.setTitle(currentElementData);
            case TYPE -> currentPage.setType(Type.fromValue(currentElementData));
            case CHARS -> currentPage.setChars(objectFactory.createChars());
            case AUTHORIZE -> currentPage.setAuthorize(Boolean.parseBoolean(currentElementData));

            case HAS_EMAIL ->
                    currentPage.getChars().setHasEmail(Boolean.parseBoolean(currentElementData));
            case HAS_ARCHIVES ->
                    currentPage.getChars().setHasArchives(Boolean.parseBoolean(currentElementData));
            case HAS_NEWS ->
                    currentPage.getChars().setHasNews(Boolean.parseBoolean(currentElementData));
            case POLL ->
                    currentPage.getChars().setPoll(currentElementData);
            case PAID ->
                    currentPage.getChars().setPaid(Boolean.parseBoolean(currentElementData));
        }
    }
    
    public Site getCurrentSite() {
        return currentSite;
    }
}
