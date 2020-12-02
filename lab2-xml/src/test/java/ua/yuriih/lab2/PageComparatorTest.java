package ua.yuriih.lab2;

import org.junit.jupiter.api.Test;
import ua.yuriih.lab2.model.ObjectFactory;
import ua.yuriih.lab2.model.Page;

import static org.junit.jupiter.api.Assertions.*;

class PageComparatorTest {

    @Test
    void compare() {
        ObjectFactory factory = new ObjectFactory();
        Page page1 = factory.createPage();
        page1.setId("1");
        page1.setTitle("C");

        Page page2 = factory.createPage();
        page2.setId("2");
        page1.setTitle("B");

        Page page3 = factory.createPage();
        page3.setId("3");
        page2.setTitle("A");
        
        PageComparator comparator = new PageComparator();

        assertEquals(0, comparator.compare(page1, page1));
        assertTrue(comparator.compare(page1, page2) < 0);
        assertTrue(comparator.compare(page2, page3) < 0);
        assertTrue(comparator.compare(page1, page3) < 0);
        assertTrue(comparator.compare(page3, page2) > 0);
        assertTrue(comparator.compare(page2, page1) > 0);
        assertTrue(comparator.compare(page3, page1) > 0);
    }
}