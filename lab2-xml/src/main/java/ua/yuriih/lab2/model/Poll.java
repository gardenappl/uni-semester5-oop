//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.12.02 at 10:21:04 PM EET 
//


package ua.yuriih.lab2.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Poll.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Poll">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="anonymous"/>
 *     &lt;enumeration value="authorized"/>
 *     &lt;enumeration value="no"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Poll")
@XmlEnum
public enum Poll {

    @XmlEnumValue("anonymous")
    ANONYMOUS("anonymous"),
    @XmlEnumValue("authorized")
    AUTHORIZED("authorized"),
    @XmlEnumValue("no")
    NO("no");
    private final String value;

    Poll(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Poll fromValue(String v) {
        for (Poll c: Poll.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
