package io.jenkins.plugins.gcr.utils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class XmlUtils {

    public static SAXSource getSAXSource(String filepath) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        spf.setFeature("http://xml.org/sax/features/validation", false);
        spf.setValidating(false);

        XMLReader xmlReader = spf.newSAXParser().getXMLReader();
        FileInputStream inputStream = new FileInputStream(filepath);
        Reader fileReader = new InputStreamReader(inputStream, "UTF-8");
        InputSource inputSource = new InputSource(fileReader);
        SAXSource source = new SAXSource(xmlReader, inputSource);

        return source;
    }
}
