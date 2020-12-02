package ua.yuriih.lab2.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.yuriih.lab2.model.Site;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SiteSAXParser extends SiteXMLParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteDOMParser.class);
    private static SAXParserFactory factory = SAXParserFactory.newInstance();
    
    @Override
    public Site parseXML(File xmlFile) throws IOException, XMLParserException {
        try {
            SAXParser parser = factory.newSAXParser();
            SiteSAXHandler handler = new SiteSAXHandler();

            parser.parse(xmlFile, handler);
            return handler.getCurrentSite();
        } catch (ParserConfigurationException e) {
            LOGGER.error("Error in parser configuration", e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new XMLParserException(e);
        }
    }


    private static class SiteSAXHandler extends DefaultHandler {
        private SiteHandler siteHandler;
        private String currentData;

        @Override
        public void startDocument() throws SAXException {
            siteHandler = new SiteHandler();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (siteHandler.shouldSetAtStart(qName))
                siteHandler.setValue(qName, null, attributesToMap(attributes));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentData = new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!siteHandler.shouldSetAtStart(qName))
                siteHandler.setValue(qName, currentData, null);
        }

        public Site getCurrentSite() {
            return siteHandler.getSite();
        }

        private Map<String, String> attributesToMap(Attributes attributes) {
            HashMap<String, String> attributeMap = new HashMap<>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                attributeMap.put(attributes.getQName(i), attributes.getValue(i));
            }
            return attributeMap;
        }
    }
}
