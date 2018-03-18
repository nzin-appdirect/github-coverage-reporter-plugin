package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.CoverageRateType;
import io.jenkins.plugins.gcr.models.DefaultCoverage;
import org.junit.Assert;
import org.junit.Test;

public class CoverageReportActionTests {

    Coverage higherCoverage = new DefaultCoverage(0.4,0.5, 0.3);
    Coverage lowerCoverage  = new DefaultCoverage(0.2,0.3, 0.1);

    // Line

    @Test
    public void testStatusForLineRateDecreased() {
        CoverageReportAction action = new CoverageReportAction(lowerCoverage, higherCoverage, CoverageRateType.LINE);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("failure", status);
        Assert.assertEquals("Coverage of 30.00% is lower than expected 50.00%.", description);
    }


    @Test
    public void testStatusForLineRateIncreased() {
        CoverageReportAction action = new CoverageReportAction(higherCoverage, lowerCoverage, CoverageRateType.LINE);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("success", status);
        Assert.assertEquals("Coverage of 50.00% is greater than or equal to expected 30.00%.", description);
    }

    // Branch

    @Test
    public void testStatusBranchRateDecreased() {
        CoverageReportAction action = new CoverageReportAction(lowerCoverage, higherCoverage, CoverageRateType.BRANCH);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("failure", status);
        Assert.assertEquals("Coverage of 10.00% is lower than expected 30.00%.", description);
    }


    @Test
    public void testStatusBranchRateIncreased() {
        CoverageReportAction action = new CoverageReportAction(higherCoverage, lowerCoverage, CoverageRateType.BRANCH);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("success", status);
        Assert.assertEquals("Coverage of 30.00% is greater than or equal to expected 10.00%.", description);
    }


    @Test
    public void testStatusOverallRateDecreased() {
        CoverageReportAction action = new CoverageReportAction(lowerCoverage, higherCoverage, CoverageRateType.OVERALL);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("failure", status);
        Assert.assertEquals("Coverage of 20.00% is lower than expected 40.00%.", description);
    }


    @Test
    public void testStatusOverallRateIncreased() {
        CoverageReportAction action = new CoverageReportAction(higherCoverage, lowerCoverage, CoverageRateType.OVERALL);

        String status = action.getStatusName();
        String description = action.getStatusDescription();

        Assert.assertEquals("success", status);
        Assert.assertEquals("Coverage of 40.00% is greater than or equal to expected 20.00%.", description);
    }

}
