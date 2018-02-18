package io.jenkins.plugins.gcr.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "counter")
public class JacocoCounter {

    @XmlAttribute(name = "type")
    public String type;

    @XmlAttribute(name = "missed")
    public int missed;

    @XmlAttribute(name = "covered")
    public int covered;

    public int getTotal() {
        return covered + missed;
    }

}