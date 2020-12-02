package ua.yuriih.lab2;


import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTests {
    private static final File testFile = new File("src" + File.separator +
            "test" + File.separator + "resources" + File.separator + "site.xml");
    @Test
    public void parseXML_SAX() {
        parseXML_test(new SiteSAXParser(), testFile);
    }

    @Test
    public void parseXML_StAX() {
        parseXML_test(new SiteStAXParser(), testFile);
    }

    @Test
    public void parseXML_DOM() {
        parseXML_test(new SiteDOMParser(), testFile);
    }
    
    private void parseXML_test(SiteXMLParser parser, File file) {
        assertDoesNotThrow(() -> parser.parseXML(file));
    }
}