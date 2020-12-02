package ua.yuriih.lab2;

import ua.yuriih.lab2.model.Page;
import ua.yuriih.lab2.model.Site;

import java.util.Comparator;

public class PageComparator implements Comparator<Page> {
    @Override
    public int compare(Page page1, Page page2) {
        return page1.getId().compareTo(page2.getId());
    }
}
