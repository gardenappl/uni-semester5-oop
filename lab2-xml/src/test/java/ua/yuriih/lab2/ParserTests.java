package ua.yuriih.lab2;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.yuriih.lab2.model.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserTests.class);
    
    private static final File testFileDir = new File("src" + File.separator + 
            "test" + File.separator + "resources");
    
    @Test
    public void parseXML_SAX() {
        parseXML_test(new SiteSAXParser());
    }

    @Test
    public void parseXML_StAX() {
        parseXML_test(new SiteStAXParser());
    }

    @Test
    public void parseXML_DOM() {
        parseXML_test(new SiteDOMParser());
    }
    
    private void parseXML_test(SiteXMLParser parser) {
        assertDoesNotThrow(() -> parseXML_expected(parser));
        parseXML_malformed(parser);
        parseXML_wrong_elements(parser);
        parseXML_wrong_count(parser);
    }
    
    private void parseXML_expected(SiteXMLParser parser) throws IOException, XMLParserException {
        Site site = parser.parseXML(new File(testFileDir, "site.xml"));
        Page page = site.getPage().get(0);
        assertEquals("main", page.getId());
        assertEquals("Main page", page.getTitle());
        assertEquals(Type.PORTAL, page.getType());
        assertFalse(page.isAuthorize());
        assertFalse(page.getChars().isPaid());
        assertEquals(Poll.AUTHORIZED, page.getChars().getPoll());
        
    }

    private void parseXML_wrong_count(SiteXMLParser parser) {
        assertDoesNotThrow(() -> parser.parseXML(new File(testFileDir, "site_wrong_count.xml")));
    }

    private void parseXML_wrong_elements(SiteXMLParser parser) {
        assertThrows(XMLParserException.class,
                () -> parser.parseXML(new File(testFileDir, "site_wrong_elements.xml")));
    }

    private void parseXML_malformed(SiteXMLParser parser) {
        assertThrows(XMLParserException.class,
                () -> parser.parseXML(new File(testFileDir, "site_malformed.xml")));
    }
}