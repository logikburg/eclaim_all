//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.05 at 04:38:55 PM CST 
//


package hk.org.ha.cid_login_ws.cidloginfunctions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="secureHttpFg" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="webServerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strProject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "secureHttpFg",
    "webServerName",
    "strProject"
})
@XmlRootElement(name = "showNewsIframe")
public class ShowNewsIframe {

    protected boolean secureHttpFg;
    protected String webServerName;
    protected String strProject;

    /**
     * Gets the value of the secureHttpFg property.
     * 
     */
    public boolean isSecureHttpFg() {
        return secureHttpFg;
    }

    /**
     * Sets the value of the secureHttpFg property.
     * 
     */
    public void setSecureHttpFg(boolean value) {
        this.secureHttpFg = value;
    }

    /**
     * Gets the value of the webServerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebServerName() {
        return webServerName;
    }

    /**
     * Sets the value of the webServerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebServerName(String value) {
        this.webServerName = value;
    }

    /**
     * Gets the value of the strProject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrProject() {
        return strProject;
    }

    /**
     * Sets the value of the strProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrProject(String value) {
        this.strProject = value;
    }

}
