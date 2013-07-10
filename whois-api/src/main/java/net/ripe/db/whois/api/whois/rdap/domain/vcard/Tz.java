package net.ripe.db.whois.api.whois.rdap.domain.vcard;

import net.ripe.db.whois.api.whois.rdap.VCardProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "parameters",
    "type",
    "value"
})
@XmlRootElement(name = "tz")
public class Tz
    extends VCardProperty
    implements Serializable
{
    @XmlElement(defaultValue = "tz")
    protected String name;
    protected HashMap parameters;
    @XmlElement(defaultValue = "utc-offset")
    protected String type;
    protected String value;

    public void setName(String value) {
        this.name = value;
    }

    public HashMap getParameters() {
        return parameters;
    }

    public void setParameters(HashMap value) {
        this.parameters = value;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        if (null == name) {
            return "tz";
        }
        return name;
    }

    public String getType() {
        if (null == type) {
            return "utc-offset";
        }
        return type;
    }
}
