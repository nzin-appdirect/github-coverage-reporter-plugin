package io.jenkins.plugins.gcr.utils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.FileReader;

public class XmlUtils {

    public static SAXSource getSAXSource(String filepath) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        spf.setFeature("http://xml.org/sax/features/validation", false);
        spf.setValidating(false);

        XMLReader xmlReader = spf.newSAXParser().getXMLReader();
        InputSource inputSource = new InputSource(new FileReader(filepath));
        SAXSource source = new SAXSource(xmlReader, inputSource);

        return source;
    }
}
