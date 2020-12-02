package ua.yuriih.lab2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.yuriih.lab2.model.ObjectFactory;
import ua.yuriih.lab2.model.Page;
import ua.yuriih.lab2.model.Site;
import ua.yuriih.lab2.model.Type;

import java.util.Map;

public class SiteHandler {
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

    public void SiteHandler() {
        currentSite = objectFactory.createSite();
        currentPage = objectFactory.createPage();
    }
    
    public boolean shouldSetAtStart(String qName) {
        return qName.equals(PAGE) || qName.equals(CHARS);
    }

    public void setValue(String qName, String data, Map<String, String> attributes) {
        switch (qName) {
            case PAGE -> {
                currentPage = objectFactory.createPage();
                currentPage.setId(attributes.get(ID));
                currentSite.getPage().add(currentPage);
            }

            case TITLE -> currentPage.setTitle(data);
            case TYPE -> currentPage.setType(Type.fromValue(data));
            case CHARS -> currentPage.setChars(objectFactory.createChars());
            case AUTHORIZE -> currentPage.setAuthorize(Boolean.parseBoolean(data));

            case HAS_EMAIL ->
                    currentPage.getChars().setHasEmail(Boolean.parseBoolean(data));
            case HAS_ARCHIVES ->
                    currentPage.getChars().setHasArchives(Boolean.parseBoolean(data));
            case HAS_NEWS ->
                    currentPage.getChars().setHasNews(Boolean.parseBoolean(data));
            case POLL ->
                    currentPage.getChars().setPoll(data);
            case PAID ->
                    currentPage.getChars().setPaid(Boolean.parseBoolean(data));
        }
    }
    
    public Site getSite() {
        return currentSite;
    }
}
