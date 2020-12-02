package ua.yuriih.lab2;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SiteXMLValidatorTest {
    private static final String TEST_FILE_DIR = "src" + File.separator +
            "test" + File.separator + "resources";

    @Test
    void isValid() throws IOException {
        SiteXMLValidator validator = new SiteXMLValidator();
        assertTrue(validator.isValid(new File(TEST_FILE_DIR, "site.xml")));
        assertFalse(validator.isValid(new File(TEST_FILE_DIR, "site_malformed.xml")));
        assertFalse(validator.isValid(new File(TEST_FILE_DIR, "site_wrong_elements.xml")));
        assertFalse(validator.isValid(new File(TEST_FILE_DIR, "site_wrong_count.xml")));
        assertFalse(validator.isValid(new File(TEST_FILE_DIR, "site_wrong_order.xml")));
    }
}