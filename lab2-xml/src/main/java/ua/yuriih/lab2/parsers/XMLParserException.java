package ua.yuriih.lab2.parsers;

import org.xml.sax.SAXException;

public class XMLParserException extends SAXException {
    public XMLParserException(Exception e) {
        super(e);
    }

    public XMLParserException(String message) {
        super(message);
    }
}
