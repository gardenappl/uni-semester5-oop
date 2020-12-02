package ua.yuriih.lab2.parsers;

import ua.yuriih.lab2.model.Site;

import java.io.File;
import java.io.IOException;

public abstract class SiteXMLParser {
    public abstract Site parseXML(File xmlFile) throws IOException, XMLParserException;
}
