package io.jenkins.plugins.gcr.parsers;

import io.jenkins.plugins.gcr.models.CoberturaCoverage;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.utils.XmlUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

public class CoberturaParser implements CoverageParser {

    public CoberturaParser() {

    }


    @Override
    public Coverage parse(String filepath) throws Exception {

        JAXBContext jaxbContext = JAXBContext.newInstance(CoberturaCoverage.class);
        SAXSource source = XmlUtils.getSAXSource(filepath);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Coverage coverage = (Coverage) jaxbUnmarshaller.unmarshal(source);
        System.out.println(coverage);

        return coverage;

    }

}
