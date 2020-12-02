package ua.yuriih.lab2;

import org.xml.sax.SAXException;
import ua.yuriih.lab2.model.Site;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.zip.DataFormatException;

public abstract class SiteXMLParser {
    public abstract Site parseXML(File xmlFile) throws IOException, XMLParserException;
}
