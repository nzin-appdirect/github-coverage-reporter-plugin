package io.jenkins.plugins.gcr.sonar.parsers;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.DefaultCoverage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SonarCoverageParser {

    private final static String MEASURE_BRANCH  = "branch_coverage";

    private final static String MEASURE_LINE    = "line_coverage";

    private final static String MEASURE_OVERALL = "coverage";


    public SonarCoverageParser() {

    }

    public Coverage parse(JSONObject jsonResponse) {
        JSONArray jsonMeasures = jsonResponse.getJSONObject("component").getJSONArray("measures");

        double overallRate = 0.0;
        double lineRate = 0.0;
        double branchRate = 0.0;

        for (Object measure : jsonMeasures.toArray()) {
            final JSONObject jsonMeasure = (JSONObject)measure;
            final String measureName = jsonMeasure.getString("metric");
            final double measureValue = jsonMeasure.getDouble("value");
            if (measureName.equals(MEASURE_OVERALL)) {
                overallRate = measureValue;
            }
            if (measureName.equals(MEASURE_BRANCH)) {
                branchRate = measureValue;
            }
            if (measureName.equals(MEASURE_LINE)) {
                lineRate = measureValue;
            }
        }

        return new DefaultCoverage(
            overallRate / 100.0f,
            lineRate / 100.0f,
            branchRate / 100.0f
        );

    }

}
