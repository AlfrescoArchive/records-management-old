package org.alfresco.test.integration.classify;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
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
public class ClassifyRecord extends BaseTest {
    /**
     * file plan
     */
    @Autowired
    private FilePlan filePlan;

    /**
     * record details
     */
    @Autowired
    private RecordDetails recordDetails;

    /**
     * Main test execution
     */
    @Test
            (
                    groups = {"integration"},
                    description = "Verify Classify Record behaviour",
                    dependsOnGroups = {"integration-dataSetup-rmSite", "integration-dataSetup-collab", "integration-dataSetup-fileplan"}
            )
    public void classifyRecord() {

        /*
            STORY (RM-2052):
                As a classification officer
                    TODO: This test assumes admin === classification officer
                I want to be able to classify records for the first time
                So that only users with the appropriate security clearance can see the record
                (record applies to electronic and non electronic records)
        */

        // open record folder one
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE);

        // verify electronic record actions
        assertTrue(filePlan.getRecord(RECORD).isActionClickable(RecordActions.CLASSIFY));

        // navigate to the electronic details page
        filePlan.getRecord(RECORD).clickOnLink();

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionClickable(RecordActionsPanel.CLASSIFY));

        // close the record details page
        recordDetails.navigateUp();

        // verify non-electronic record actions
        Record nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertNotNull(nonElectronicRecord);

        // Is the link available on the file plan for a non-electronic record?
        assertTrue(recordDetails.getRecordActionsPanel().isActionClickable(RecordActions.CLASSIFY));

        // navigate to the record details page
        nonElectronicRecord.clickOnLink();

        // Is the action available on the record details page?
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(RecordActionsPanel.CLASSIFY));

        /*
        Acceptance Criteria from RM-2052
            - Records user with no 'read & file' permission on and unclassified record can not set the content classification
                Given that I do not have 'read & file' permissions on the unclassified record
                And have a security clearance level above 'No Clearance'
                When I view the unclassified record
                Then I am unable to set a classification
                    TODO: non admin user with security clearance, but without read & file permissions
            - User with no security clearance can't set record classification
                Given that I have a security clearance level of 'No Clearance'
                And have 'read & file' permission on the unclassified record
                When I view the unclassified record
                Then I can not set a classification
                    TODO: non admin user with read & file permissions, but without security clearance
            - A held record can still be classified
                Given that I am a cleared user
                And have 'read & file' permission on the unclassified record
                And the record is held
                When I view the unclassified record
                Then I can set a classification
                    TODO: held record test
         */

    }
}