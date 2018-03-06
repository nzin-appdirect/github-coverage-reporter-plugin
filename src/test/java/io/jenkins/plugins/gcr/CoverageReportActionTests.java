package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.DefaultCoverage;
import org.junit.Assert;
import org.junit.Test;

public class CoverageReportActionTests {

    @Test
    public void testBranchRateDescription() {
        Coverage coverage1 = new DefaultCoverage(0.0, 0.25);
        Coverage coverage2 = new DefaultCoverage(0.0, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String description = action.getBranchRateDescription();

        Assert.assertEquals("25.00%", description);
    }

    @Test
    public void testLineRateDescription() {
        Coverage coverage1 = new DefaultCoverage(0.5, 0.0);
        Coverage coverage2 = new DefaultCoverage(0.0, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String description = action.getLineRateDescription();

        Assert.assertEquals("50.00%", description);
    }

    @Test
    public void testLineRateDifferenceDecreased() {
        Coverage coverage1 = new DefaultCoverage(0.25, 0.0);
        Coverage coverage2 = new DefaultCoverage(0.5, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String difference = action.getLineRateDifference();

        Assert.assertEquals("-25.00%", difference);
    }

    @Test
    public void testLineRateDifferenceIncreased() {
        Coverage coverage1 = new DefaultCoverage(0.5, 0.0);
        Coverage coverage2 = new DefaultCoverage(0.25, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String difference = action.getLineRateDifference();

        Assert.assertEquals("+25.00%", difference);
    }

    @Test
    public void testStatusDescriptionDescreased() {
        Coverage coverage1 = new DefaultCoverage(0.25, 0.0);
        Coverage coverage2 = new DefaultCoverage(0.5, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String status = action.getStatusDescription();

        Assert.assertEquals("Coverage of 25.00% is lower than expected 50.00%.", status);
    }


    @Test
    public void testStatusDescriptionInscreased() {
        Coverage coverage1 = new DefaultCoverage(0.5, 0.0);
        Coverage coverage2 = new DefaultCoverage(0.25, 0.0);
        CoverageReportAction action = new CoverageReportAction(coverage1, coverage2);

        String status = action.getStatusDescription();

        Assert.assertEquals("Coverage of 50.00% is greater than or equal to expected 25.00%.", status);
    }

}
