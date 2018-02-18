package io.jenkins.plugins.gcr.parsers;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.JacocoCoverage;
import io.jenkins.plugins.gcr.utils.XmlUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

public class JacocoParser implements CoverageParser {

    @Override
    public Coverage parse(String filepath) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(JacocoCoverage.class);
        SAXSource source = XmlUtils.getSAXSource(filepath);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Coverage coverage = (Coverage) jaxbUnmarshaller.unmarshal(source);

        return coverage;
    }

}
