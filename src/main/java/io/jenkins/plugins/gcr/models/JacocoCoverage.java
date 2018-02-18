package io.jenkins.plugins.gcr.models;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.stream.Stream;

@XmlRootElement(name = "report")
public class JacocoCoverage implements Coverage {

    public static final String TYPE_INSTRUCTION     = "INSTRUCTION";
    public static final String TYPE_BRANCH          = "BRANCH";
    public static final String TYPE_LINE            = "LINE";
    public static final String TYPE_COMPLEXITY      = "COMPLEXITY";

    // Fields

    @XmlElement(name = "counter")
    public List<JacocoCounter> counters;

    // Constructor

    public JacocoCoverage() {
    }

    // Coverage Interface

    private Stream<JacocoCounter> filterStreamFor(String type) {
        return counters.stream().filter(obj -> obj.type.equals(type));
    }

    private double rateForCounter(JacocoCounter counter) {
        return ((double)counter.covered / (double)counter.getTotal());
    }

    public double getLineRate() {
        JacocoCounter counter = filterStreamFor(TYPE_LINE).findAny().get();
        return rateForCounter(counter);
    }

    public double getBranchRate() {
        JacocoCounter counter = filterStreamFor(TYPE_BRANCH).findAny().get();
        return rateForCounter(counter);
    }

    // Other

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        builder.append(String.format("lineRate=%f, branchRate=%f", getLineRate(), getBranchRate()));
        builder.append(" ]");
        return builder.toString();
    }

}
