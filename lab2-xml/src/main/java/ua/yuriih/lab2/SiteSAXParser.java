package ua.yuriih.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ua.yuriih.lab2.model.Site;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.IllegalFormatException;
import java.util.zip.DataFormatException;

public class SiteSAXParser extends SiteXMLParser {
    private static Logger LOGGER = LoggerFactory.getLogger(SiteSAXParser.class);
    
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    
    @Override
    public Site parseXML(File xmlFile) throws IOException, XMLParserException {
        try {
            SAXParser parser = factory.newSAXParser();
            SiteHandler handler = new SiteHandler();

            parser.parse(xmlFile, handler);
            return handler.getCurrentSite();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new XMLParserException(e);
        }
    }
}
