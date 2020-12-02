package ua.yuriih.lab2;

import ua.yuriih.lab2.model.Site;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SiteStAXParser extends SiteXMLParser {
    private final XMLInputFactory factory = XMLInputFactory.newFactory();
    
    @Override
    public Site parseXML(File xmlFile) throws IOException, XMLParserException {
        SiteHandler siteHandler = new SiteHandler();
        try {
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(xmlFile));
            
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().toString();
                    Map<String, String> attributes = attributesToMap(startElement.getAttributes());
                    
                    XMLEvent nextEvent = eventReader.peek();
                    if (nextEvent != null && nextEvent.isCharacters()) {
                        Characters characters = eventReader.nextEvent().asCharacters();
                        
                        siteHandler.setValue(qName, characters.getData(), attributes);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new XMLParserException(e);
        }
        return siteHandler.getSite();
    }
    
    private Map<String, String> attributesToMap(Iterator<Attribute> attributes) {
        Map<String, String> attributeMap = new HashMap<>();
        attributes.forEachRemaining(attribute ->
            attributeMap.put(attribute.getName().toString(), attribute.getValue()));
        return attributeMap;
    }
}
