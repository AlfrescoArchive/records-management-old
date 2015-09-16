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

import static org.alfresco.po.common.util.RMCollectionUtils.tail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.UUID;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.browse.fileplan.RecordIndicators;
import org.alfresco.po.rm.details.record.ClassifiedRecordDetails;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.rm.dialog.classification.EditClassifiedContentDialog;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 2.4.a
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

    /** The edit classified content dialog. */
    @Autowired
    private EditClassifiedContentDialog editClassifiedContentDialog;

    @Autowired
    private ClassifiedRecordDetails classifiedRecordDetails;

    /**
     * Check that the classify record action exists for electronic and non-electronic records.
     *
     * Note that there is no explicit acceptance criteria for this, but it is clearly required.
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Verify classify record action exists",
        dependsOnGroups = { "GROUP_ELECTRONIC_RECORD_EXISTS", "GROUP_NON_ELECTRONIC_RECORD_EXISTS" }
    )
    public void checkClassifyActionExists()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
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

    /** Check a record can be classified. Also check the Edit Classification button availability before and after classification
     * The classified record is used by other tests.
     */
    @Test
    (
        groups = { "integration", "GROUP_CLASSIFIED_RECORD_EXISTS", "CLASSIFICATION_ACTION"},
        description = "Verify classify record action exists",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_ONE_EXISTS" }
    )
    public void classifyRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        filePlan.getToolbar()
            .clickOnFile()
            .clickOnElectronic()
            .uploadFile(CLASSIFIED_RECORD);
        Record record = filePlan.getRecord(CLASSIFIED_RECORD);

        String[] clickableActions = record.getClickableActions();
         // Check that the Edit Classification button is not available before record classification
        assertFalse("Expected the Edit Classification button not to be available for record before classification",
                    Arrays.asList(clickableActions).contains(RecordActions.EDIT_CLASSIFIED_CONTENT));
        record.clickOnLink(recordDetails);
        assertFalse("Expected the Edit Classification button not to be available before classification in Record Details",
                recordDetails.getRecordActionsPanel().isActionAvailable(RecordActionsPanel.EDIT_CLASSIFIED_CONTENT));
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        filePlan.getRecord(CLASSIFIED_RECORD).clickOnAction(RecordActionsPanel.CLASSIFY, classifyContentDialog);

        final String currentUserFullName = "Administrator";
        assertEquals("'Classified By' field should be preset to current user's full name",
                currentUserFullName, classifyContentDialog.getClassifiedBy());

        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setClassifiedBy(CLASSIFIED_BY)
            .setAgency(CLASSIFICATION_AGENCY)
            .addReason(CLASSIFICATION_REASON)
            .setDowngradeDate(DOWNGRADE_DATE_INPUT)
            .setDowngradeEvent(DOWNGRADE_EVENT)
            .setDowngradeInstructions(DOWNGRADE_INSTRUCTIONS)
            .setDeclassificationDate(DECLASSIFICATION_DATE_INPUT)
            .setDeclassificationEvent(DECLASSIFICATION_EVENT)
            .addExemptionCategory(EXEMPTION_CATEGORY)
            .clickOnClassify();

        // Check that the Edit Classification button is available after classification
        assertTrue("Expected the Edit Classification button to be available and clickable after classification",
                filePlan.getRecord(CLASSIFIED_RECORD).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));

        // Now go to doc details and check the Edit Classification button is available.
        filePlan.getRecord(CLASSIFIED_RECORD).clickOnLink(classifiedRecordDetails);
        assertTrue("Expected the Edit Classification button to be available and clickable after classification in Record Details",
                     recordDetails.getRecordActionsPanel().isActionClickable(RecordActionsPanel.EDIT_CLASSIFIED_CONTENT));
    }

    /**
     * Given that a record is held
     * And unclassified
     * When I view the available actions
     * Then classify is not available
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Verify classify record action exists",
        dependsOnGroups = { "GROUP_ELECTRONIC_RECORD_EXISTS", "GROUP_HOLDS_EXIST" }
    )
    public void cantClassifyHeldRecord()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

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

     /**
     * Given that a classified record is held
     * When I view the available actions
     * Then Edit Classification is not available
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Verify classified but held record can't have the classification edited",
        dependsOnGroups = { "GROUP_COMPLETE_RECORD_IS_CLASSIFIED", "GROUP_HOLDS_EXIST" }
    )
    public void cantEditClassificationOfAHeldRecord()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        // add record to hold
        filePlan
            .getRecord(COMPLETE_RECORD)
            .clickOnAddToHold()
            .selectHold(HOLD1)
            .clickOnOk();

        // show that Edit Classification action is not available
        assertFalse(filePlan.getRecord(COMPLETE_RECORD).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));

        // remove record from hold
        filePlan
            .getRecord(COMPLETE_RECORD)
            .clickOnRemoveFromHold()
            .selectHold(HOLD1)
            .clickOnOk();

        // show that the Edit Classification action is now available
        assertFalse(filePlan.getRecord(COMPLETE_RECORD).isHeld());
        assertTrue(filePlan.getRecord(COMPLETE_RECORD).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
    }

    /**
     * Check that a completed record can be classified.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2186">RM-2186</a><pre>
     * Given that a record is complete
     * When I classify the record
     * Then the record is marked as classified.
     * </pre>
     */
    @Test
    (
        groups = { "integration", "GROUP_COMPLETE_RECORD_IS_CLASSIFIED", "CLASSIFICATION_ACTION"},
        description = "Check that a completed record can be classified.",
        dependsOnGroups = { "GROUP_COMPLETE_RECORD_EXISTS" }
    )
    public void checkCanClassifyCompleteRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        filePlan.getRecord(COMPLETE_RECORD)
            .clickOnAction(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setClassifiedBy(CLASSIFIED_BY)
            .setAgency(CLASSIFICATION_AGENCY)
            .addReason(CLASSIFICATION_REASON)
            .clickOnClassify();
        filePlan.getRecord(COMPLETE_RECORD)
            .hasIndicator(RecordIndicators.CLASSIFIED);
    }

    /**
     * Check that a user with no security clearance can't classify a record.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2078">RM-2078</a><pre>
     * Given that I have a security clearance level of 'No Clearance'
     * And have 'read & file' permission on the unclassified record
     * When I view the unclassified record
     * Then I cannot set a classification
     * </pre>
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Check that a user with no security clearance doesn't see the 'Classify' action",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_ONE_EXISTS", "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE" }
    )
    public void checkUnclearedUserCannotClassifyRecord()
    {
        String recordName = UUID.randomUUID().toString();

        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        filePlan.getToolbar()
            .clickOnFile()
            .clickOnElectronic()
            .uploadFile(recordName);

        Record record = filePlan.getRecord(recordName);
        assertFalse("Classify action should not be shown to uncleared user.",
                record.isActionClickable(RecordActions.CLASSIFY));
    }

    /**
     * Check that a user with intermediate security clearance cannot reclassify a record above their clearance.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2440">RM-2440</a><pre>
     * Given that I am a user with mid-level clearance (for example Secret)
     * And content is already classified
     * When I edit the content classification
     * Then I am only able to reclassify the content up to my security clearance (for example I would have the options
     *     Unclassified, Confidential and Secret, but Top Secret would not be available to me)
     * </pre>
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Check that a user with Secret clearance cannot reclassify above that level",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_THREE_EXISTS",
                            "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE",
                            "GROUP_RM_MANAGER_FILE_CATEGORY_THREE" }
    )
    public void checkUserWithIntermediateClearanceHasLimitedReclassificationOptions() throws Exception
    {
        final String recordName = UUID.randomUUID().toString();
        final String folderOnePath = createPathFrom("documentlibrary", RECORD_CATEGORY_THREE, RECORD_FOLDER_THREE);

        // Log in as admin ...
        openPage(filePlan, RM_SITE_ID, folderOnePath);

        // ... and upload a file...
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(recordName);

        // ... and classify it as Confidential ...
        filePlan.getRecord(recordName).clickOnAction(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
                             .setClassifiedBy(CLASSIFIED_BY)
                             .setAgency(CLASSIFICATION_AGENCY)
                             .addReason(CLASSIFICATION_REASON)
                             .clickOnClassify();

        // Implementation note. We now need to ensure that the admin user has permission to reclassify to any level
        // and that the RM Manager user can only reclassify up to Secret.
        // However we will switch to the RM Manager user first in order to work around an issue whereby Selenium
        // elements become stale if a dialog is used & dismissed multiple times within a test.

        // Then log in as a user with more limited clearance ...
        openPage(RM_MANAGER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, folderOnePath);

        // ... and ensure that the record can be reclassified only up to the current user's clearance level
        assertTrue("Edit classification action should be shown.",
                filePlan.getRecord(recordName).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
        filePlan.getRecord(recordName).clickOnAction(RecordActionsPanel.EDIT_CLASSIFIED_CONTENT, editClassifiedContentDialog);

        assertEquals("Unexpected reclassification levels offered",
                tail(CLASSIFICATION_LEVELS_TEXT), editClassifiedContentDialog.getAvailableLevels());

        editClassifiedContentDialog.clickOnCancel();

        // ... and log in again as a user with full reclassification options ...
        openPage(filePlan, RM_SITE_ID, folderOnePath);

        // ... and check that admin can reclassify at any level ...
        assertTrue("Edit classification action should be shown.",
                filePlan.getRecord(recordName).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
        filePlan.getRecord(recordName).clickOnAction(RecordActionsPanel.EDIT_CLASSIFIED_CONTENT, editClassifiedContentDialog);

        assertEquals("Unexpected reclassification levels offered",
                     CLASSIFICATION_LEVELS_TEXT, editClassifiedContentDialog.getAvailableLevels());

        editClassifiedContentDialog.clickOnCancel();
    }

    /**
     * Check that a user with 'Secret' clearance can't classify a record if it does not have read & file permission over it
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2078">RM-2078</a><pre>
     * Given that I do not have 'read & file' permissions on the unclassified record
     * And have a security clearance level above 'No Clearance'
     * When I view the unclassified record
     * Then I am unable to set a classification
     * </pre>
     */
    @Test
    (
        groups = { "integration" , "CLASSIFICATION_ACTION"},
        description = "Check that a user with 'Secret' clearance can't classify a record if it does not have read & file permission over it.",
        dependsOnGroups = { "GROUP_ELECTRONIC_RECORD_EXISTS", "GROUP_RM_MANAGER_READ_CATEGORY_ONE", "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE" }
    )
    public void cantClassifyRecordWithoutReadAndFile()
    {
        openPage(RM_MANAGER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        Record record = filePlan.getRecord(RECORD);
        assertFalse("Classify action should not be shown to a user that does not have read & file permission over it.",
                    record.isActionClickable(RecordActions.CLASSIFY));
    }

    /**
     * Check that a user with 'Secret' clearance can't edit the classification of a classified record if it does not have read & file permission over it <pre>
     * Given that I do not have 'read & file' permissions on the classified record
     * And have a security clearance level above 'No Clearance'
     * When I view the classified record
     * Then I am unable to edit its classification
     * <pre>
     */
     @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Check that a user with 'Secret' clearance can't edit a classified record clasification if it does not have read & file permission over it.",
        dependsOnGroups = { "GROUP_COMPLETE_RECORD_IS_CLASSIFIED", "GROUP_RM_MANAGER_READ_CATEGORY_ONE", "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE" }
    )
    public void cantEditRecordClassificationWithoutReadAndFile()
    {
        openPage(RM_MANAGER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        Record record = filePlan.getRecord(COMPLETE_RECORD);
        assertFalse("Edit Classification action should not be shown to a user that does not have read & file permission over the classified record.",
                    record.isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
        record.clickOnLink(recordDetails);
        assertFalse("Edit Classification action should not be shown in Record Details to a user that does not have read & file permission over the classified record.",
                recordDetails.getRecordActionsPanel().isActionAvailable(RecordActions.EDIT_CLASSIFIED_CONTENT));
    }
    
    /**
     * Downgrade instructions mandatory when downgrade on (or when) set
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2409">RM-2409</a><pre>
     * Given I am a cleared user
     * And I am classifying the content for the first time
     * And I enter a downgrade date and/or event
     * When I attempt to save the classification information
     * Then I will be informed that downgrade instructions are mandatory when the downgrade date and/or event are set
     * And the save will not be successful
     * Note that downgrade instructions can be set without a date or event specified.
     * </pre>
     */
    @Test
    (
        groups = { "integration", "CLASSIFICATION_ACTION"},
        description = "Check the mandatory and non-mandatory states of the Instructions field.",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_THREE_EXISTS" }
    )
    @AlfrescoTest(jira = "RM-2409")
    public void checkInstructionsFieldStates()
    {
        String recordName = "never-classified-record";
        
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_THREE, RECORD_FOLDER_THREE));
        
        // upload a file
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(recordName);
        
        // click on classify
        classifyContentDialog = filePlan.getRecord(recordName).clickOnClassifyAction();
        
        // complete the default required fields so that the classification to be possible
        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setAgency(CLASSIFICATION_AGENCY)
            .addReason(CLASSIFICATION_REASON);
        
        // set the downgrade date   
        classifyContentDialog.setDowngradeDate(DOWNGRADE_DATE_INPUT);
        
        // verify that the Instructions field becomes required and that the Classify button is disabled
        assertTrue("The instructions are not required when the Downgrade date is completed.", classifyContentDialog.isInstructionsFieldRequired());  
        assertFalse("The Clasify button is not disabled when setting the Downgrade date.", classifyContentDialog.isClassifyButtonEnabled());
        
        // clear the Downgrade date 
        classifyContentDialog.clearDowngradeDate();

        // set the downgrade event
        classifyContentDialog.setDowngradeEvent(DOWNGRADE_EVENT);
        
        // verify that the Instructions field becomes required and that the Classify button is disabled
        assertTrue("The instructions are not required when the Downgrade event is completed.", classifyContentDialog.isInstructionsFieldRequired());  
        assertFalse("The Clasify button is not disabled when setting the Downgrade event.", classifyContentDialog.isClassifyButtonEnabled());
  
        // set the downgrade date   
        classifyContentDialog.setDowngradeDate(DOWNGRADE_DATE_INPUT);
        
        // verify that the Instructions field remains required and that the Classify button is still disabled when having both Downgrade date and event set
        assertTrue("The instructions are not required when the Downgrade date and event are both completed.", classifyContentDialog.isInstructionsFieldRequired());  
        assertFalse("The Clasify button is not disabled when setting the Downgrade date and event.", classifyContentDialog.isClassifyButtonEnabled());
 
        // complete the instructions
        classifyContentDialog.setDowngradeInstructions(DOWNGRADE_INSTRUCTIONS);
        
        // verify that the Classify button is enabled after the Instructions completion
        assertTrue("The Clasify button is not enabled when setting the Downgrade date, event and Instructions.", classifyContentDialog.isClassifyButtonEnabled());
        
        classifyContentDialog.clearDowngradeDate().clearDowngradeEvent();
        
        // verify that the Classify button is enabled even if the Downgrade Date and Event are not completed, but only the Instructions field
        assertTrue("The Clasify button is not enabled when not setting the Downgrade date and event, but only the Instructions field.", classifyContentDialog.isClassifyButtonEnabled());
       
    }

}