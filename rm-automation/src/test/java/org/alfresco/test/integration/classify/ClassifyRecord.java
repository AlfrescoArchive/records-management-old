/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package org.alfresco.test.integration.classify;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifyRecord extends BaseTest
{
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

    /** The classify content dialog. */
    @Autowired
    private ClassifyContentDialog classifyContentDialog;

    /**
     * Check that the classify record action exists for electronic and non-electronic records.
     *
     * Note that there is no explicit acceptance criteria for this, but it is clearly required.
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify classify record action exists",
        dependsOnGroups = { GROUP_ELECTRONIC_RECORD_EXISTS, GROUP_NON_ELECTRONIC_RECORD_EXISTS }
    )
    public void checkClassifyActionExists()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE));
        Record electronicRecord = filePlan.getRecord(RECORD);

        // Verify that "Classify" is an available action.
        assertTrue(electronicRecord.isActionClickable(RecordActions.CLASSIFY));

        // navigate to the electronic details page
        electronicRecord.clickOnLink();

        // Verify that "Classify" is an available action.
        assertTrue(recordDetails.getRecordActionsPanel()
            .isActionClickable(RecordActionsPanel.CLASSIFY));

        // close the record details page
        recordDetails.navigateUp();

        // Navigate to the non-electronic details page.
        Record nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertNotNull(nonElectronicRecord);

        // Is the link available on the file plan for a non-electronic record?
        assertTrue(nonElectronicRecord.isActionClickable(RecordActions.CLASSIFY));

        // navigate to the record details page
        nonElectronicRecord.clickOnLink();

        // Is the action available on the record details page?
        assertTrue(recordDetails.getRecordActionsPanel()
            .isActionsClickable(RecordActionsPanel.CLASSIFY));
    }

    /** Check a record can be classified.  The classified record is used by other tests. */
    @Test
    (
        groups = { "integration", GROUP_CLASSIFIED_RECORD_EXISTS },
        description = "Verify classify record action exists",
        dependsOnGroups = { GROUP_FILE_PLAN_EXISTS }
    )
    public void classifyRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE));

        filePlan.getToolbar()
            .clickOnFile()
            .clickOnElectronic()
            .uploadFile(CLASSIFIED_RECORD);
        Record record = filePlan.getRecord(CLASSIFIED_RECORD);

        record.clickOnAction(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON)
            .submitDialog();
    }


    /**
     * Given that a record is held
     * And unclassified
     * When I view the available actions
     * Then classify is not available
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify classify record action exists",
        dependsOnGroups = { GROUP_ELECTRONIC_RECORD_EXISTS, GROUP_HOLDS_EXIST }
    )
    public void cantClassifyHeldRecord()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE));

        // show that the classify action is available
        assertFalse(filePlan.getRecord(RECORD).isHeld());
        assertTrue(filePlan.getRecord(RECORD).isActionClickable(RecordActions.CLASSIFY));

        // add record to hold
        filePlan
            .getRecord(RECORD)
            .clickOnAddToHold()
            .selectHold(HOLD1)
            .clickOnOk();

        // show that classify action is not available
        assertTrue(filePlan.getRecord(RECORD).isHeld());
        assertFalse(filePlan.getRecord(RECORD).isActionClickable(RecordActions.CLASSIFY));

        // remove record from hold
        filePlan
            .getRecord(RECORD)
            .clickOnRemoveFromHold()
            .selectHold(HOLD1)
            .clickOnOk();

        // show that the classify action is now available
        assertFalse(filePlan.getRecord(RECORD).isHeld());
        assertTrue(filePlan.getRecord(RECORD).isActionClickable(RecordActions.CLASSIFY));
    }

    /*
    RM-2052 Acceptance criteria currently without tests.
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
     */
}