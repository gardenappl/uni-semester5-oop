package ua.yuriih.lab2.parsers;

import ua.yuriih.lab2.PageComparator;
import ua.yuriih.lab2.model.*;
import ua.yuriih.lab2.parsers.XMLParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class SiteHandler {
    private static final String SITE = "site";
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
    private Site currentSite = new Site();
    private List<Page> pages = new ArrayList<>();
    private Page currentPage;

    public void SiteHandler() {
        currentSite = objectFactory.createSite();
        currentPage = objectFactory.createPage();
    }
    
    public boolean shouldSetAtStart(String qName) {
        return qName.equals(PAGE) || qName.equals(CHARS);
    }

    public void setValue(String qName, String data, Map<String, String> attributeMap) throws XMLParserException {
        switch (qName) {
            case SITE -> { /* no-op */ }
            case PAGE -> {
                currentPage = objectFactory.createPage();
                currentPage.setId(attributeMap.get(ID));
                pages.add(currentPage);
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
                    currentPage.getChars().setPoll(Poll.fromValue(data));
            case PAID ->
                    currentPage.getChars().setPaid(Boolean.parseBoolean(data));

            default -> throw new XMLParserException("Invalid qualified name " + qName);
        }
    }
    
    public Site getSite() {
        currentSite.getPage().clear();
        pages.sort(new PageComparator());
        currentSite.getPage().addAll(pages);
        return currentSite;
    }
}
