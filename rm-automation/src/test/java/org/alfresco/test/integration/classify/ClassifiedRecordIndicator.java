package org.alfresco.test.integration.classify;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.browse.fileplan.RecordIndicators;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifiedRecordIndicator extends BaseTest
{
    /**
     * file plan
     */
    @Autowired
    private FilePlan filePlan;
    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classify Record behaviour",
        dependsOnGroups = { "integration-dataSetup-rmSite", "integration-dataSetup-collab",
            "integration-dataSetup-fileplan"
            // FIXME: Add dataSetup method here, once work for RM-2051 is complete (e.g. req. classify content dialog PO)
            //    , "integration-dataSetup-fileplan-classified"
        }
    )

    public void classifiedRecordIndicator()
    {

        // open record folder one
        openPage(filePlan, RM_SITE_ID, "documentlibrary").navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME,
            RECORD_FOLDER_ONE);

        // Is the indicator available on the classified record
        assertTrue(filePlan.getRecord(CLASSIFIED_RECORD)
            .hasIndicator(RecordIndicators.CLASSIFIED));

        // Is the indicator available on the classified non electronic record
        assertTrue(filePlan.getRecord(CLASSIFIED_NON_ELECTRONIC_RECORD)
            .hasIndicator(RecordIndicators.CLASSIFIED));

    }
}