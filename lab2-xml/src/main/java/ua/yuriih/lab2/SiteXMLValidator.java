package ua.yuriih.lab2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class SiteXMLValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteXMLValidator.class);
    
    private static final File SCHEMA_FILE = new File("src" + File.separator +
            "main" + File.separator + "resources" + File.separator + "site.xsd");
    
    private final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    public boolean isValid(File xmlFile) throws IOException {
        Schema schema;
        try {
            schema = factory.newSchema(SCHEMA_FILE);
        } catch (SAXException e) {
            LOGGER.error("Exception while creating schema!", e);
            throw new RuntimeException(e);
        }
        
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(xmlFile));
        } catch (SAXException e) {
            LOGGER.warn("Validator exception: ", e);
            return false;
        }
        return true;
    }
}
