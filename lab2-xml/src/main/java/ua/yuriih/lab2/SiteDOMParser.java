package ua.yuriih.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import ua.yuriih.lab2.model.Site;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SiteDOMParser extends SiteXMLParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteDOMParser.class);
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

    @Override
    public Site parseXML(File xmlFile) throws IOException, XMLParserException {
        SiteHandler handler = new SiteHandler();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.normalizeDocument();

            parseElementsRecursive(document.getDocumentElement(), handler);

        } catch (ParserConfigurationException e) {
            LOGGER.error("Error in parser configuration", e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new XMLParserException(e);
        }
        return handler.getSite();
    }
    
    private void parseElementsRecursive(Node element, SiteHandler handler) {
        Map<String, String> attributeMap = attributesToMap(element.getAttributes());
        handler.setValue(element.getNodeName(), element.getTextContent(), attributeMap);
        
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
                parseElementsRecursive(child, handler);
        }
    }
    
    private Map<String, String> attributesToMap(NamedNodeMap attributes) {
        HashMap<String, String> attributeMap = new HashMap<>(attributes.getLength());
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            attributeMap.put(node.getNodeName(), node.getNodeValue());
        }
        return attributeMap;
    }
}
