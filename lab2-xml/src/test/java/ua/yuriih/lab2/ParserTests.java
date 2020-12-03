package ua.yuriih.lab2;


import org.junit.jupiter.api.Test;
import ua.yuriih.lab2.model.*;
import ua.yuriih.lab2.parsers.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {
    private static final String TEST_FILE_DIR = "src" + File.separator +
            "test" + File.separator + "resources";
    
    @Test
    public void parseXML_SAX() {
        parseXML(new SiteSAXParser());
    }

    @Test
    public void parseXML_StAX() {
        parseXML(new SiteStAXParser());
    }

    @Test
    public void parseXML_DOM() {
        parseXML(new SiteDOMParser());
    }
    
    private void parseXML(SiteXMLParser parser) {
        assertDoesNotThrow(() -> parseXML_expected(parser, "site.xml"));
        assertDoesNotThrow(() -> parseXML_expected(parser, "site_wrong_order.xml"));
        assertThrows(XMLParserException.class,
                () -> parser.parseXML(new File(TEST_FILE_DIR, "site_malformed.xml")));
        assertThrows(XMLParserException.class,
                () -> parser.parseXML(new File(TEST_FILE_DIR, "site_wrong_elements.xml")));
        assertDoesNotThrow(() ->
                parser.parseXML(new File(TEST_FILE_DIR, "site_wrong_count.xml")));
    }
    
    private void parseXML_expected(SiteXMLParser parser, String fileName) throws IOException, XMLParserException {
        Site site = parser.parseXML(new File(TEST_FILE_DIR, fileName));
        assertEquals(3, site.getPage().size());

        Page page = site.getPage().get(1);
        assertEquals("main", page.getId());
        assertEquals("Main page", page.getTitle());
        assertEquals(Type.PORTAL, page.getType());
        assertFalse(page.isAuthorize());
        assertFalse(page.getChars().isPaid());
        assertEquals(Poll.AUTHORIZED, page.getChars().getPoll());
    }
}