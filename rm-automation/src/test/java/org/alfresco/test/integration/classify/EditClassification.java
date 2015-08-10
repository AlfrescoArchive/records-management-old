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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.rm.dialog.classification.EditClassifiedContentDialog;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Tests for the editing of classification
 *
 * @author Oana Nechiforescu
 */
public class EditClassification extends BaseTest
{
    @Autowired
    private FilePlan filePlan;
    @Autowired
    private ClassifyContentDialog classifyContentDialog;
    @Autowired
    private EditClassifiedContentDialog editContentClassificationDialog;
    @Autowired
    private DocumentLibrary documentLibrary;

    // initial selected exemption ID
    final String EXEMPTION_ID = "2";

    // classification meta-data used for editing
    final String EDITED_DOWNGRADE_DATE = "10/20/2080";
    final String EDITED_DOWNGRADE_EVENT = "not on this level anymore on the current project";
    final String EDITED_DOWNGRADE_INSTRUCTIONS = "1. a) Give Jack J. a notice.";
    final String EDITED_DECLASSIFICATION_DATE = "11/10/2081";
    final String EDITED_DECLASSIFICATION_EVENT = "made public";
    final String EDITED_EXEMPTION_INDEX = "1";

    /**
     * The Edit Classification dialog illustrates the current classification meta-data values for records
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-RM-2427">RM-2427</a><pre>
     * Initial state
     * Given that a record is classified
     * When I select the "Edit Classification" action
     * Then edit classification dialog is shown
     * And it reflects the current classification meta-data values
     * </pre>
     */
    @Test(
            groups = {"integration", "EDIT_CLASSIFICATION_INITIAL_STATE"},
            description = "The Edit Classification dialog illustrates the current classification metadata values for records",
            dependsOnGroups = {"EDIT_CLASSIFICATION_DATA_PREP"}
    )
    @AlfrescoTest(jira = "RM-2427")
    public void editRecordClassificationDialogInitialState()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        // Check that the Edit Classification button is available and open the Edit Classification dialog
        assertTrue(filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
        filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).clickOnEditClassification();
        assertTrue("The Edit Classification dialog is not displayed on click on Edit Classification button.", editContentClassificationDialog.isEditClassificationDialogDisplayed());
        // Verify that the title of the dialog is "Edit Classified Record"
        String editClassificationDialogTitle = editContentClassificationDialog.getEditClasificationTitle();
        assertEquals("The dialog title is not Edit Classified Record it is " + editClassificationDialogTitle, editContentClassificationDialog.EDIT_RECORD_CLASSIFICATION_TITLE, editClassificationDialogTitle);
        // Verify that the edit classification button label is "Edit"
        String editClassificationButtonLabel = editContentClassificationDialog.getEditButtonLabel();
        assertEquals("The edit classification button does not have the label Edit, it is " + editClassificationButtonLabel, editContentClassificationDialog.EDIT_BUTTON_LABEL, editClassificationButtonLabel);
        // Verify the previously set classification properties are selected and set by default
        assertEquals("The classification level value from Edit Classification dialog is not the one set in Classification Dialog.", SECRET_CLASSIFICATION_LEVEL_TEXT, editContentClassificationDialog.getLevel());
        assertEquals("The classified by value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFIED_BY, editContentClassificationDialog.getClassifiedBy());
        assertEquals("The classification agency value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFICATION_AGENCY, editContentClassificationDialog.getAgency());
        assertEquals("The classification reason value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFICATION_REASON, editContentClassificationDialog.getReasons().get(0));
        assertEquals("The downgrade date value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_DATE_EDIT, editContentClassificationDialog.getDowngradeDate());
        assertEquals("The downgrade event value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_EVENT, editContentClassificationDialog.getDowngradeEvent());
        assertEquals("The downgrade instructions value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_INSTRUCTIONS, editContentClassificationDialog.getDowngradeInstructions());
        assertEquals("The declassification date value from Edit Classification dialog is not the one set in Classification Dialog.", DECLASSIFICATION_DATE_EDIT, editContentClassificationDialog.getDeclassificationDate());
        assertEquals("The declassification event value from Edit Classification dialog is not the one set in Classification Dialog.", DECLASSIFICATION_EVENT, editContentClassificationDialog.getDeclassificationEvent());
        assertEquals("The exemption value from Edit Classification dialog is not the one set in Classification Dialog.", EXEMPTION_ID, editContentClassificationDialog.getExemptions().get(0));
        editContentClassificationDialog.clickOnCancel();
    }

    /**
     * The Edit Classification dialog illustrates the current classification meta-data values for documents
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-RM-2427">RM-2427</a><pre>
     * Initial state
     * Given that a document is classified
     * When I select the "edit classification" action
     * Then edit classification dialog is shown
     * And it reflects the current classification meta-data values
     * </pre>
     */
    @Test(
            groups = {"integration", "EDIT_CLASSIFICATION_INITIAL_STATE"},
            description = "The Edit Classification dialog illustrates the current classification metadata values for documents",
            dependsOnGroups = {"EDIT_CLASSIFICATION_DATA_PREP"}
    )
    @AlfrescoTest(jira = "RM-2427")
    public void editDocumentClassificationDialogInitialState()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        // Check that the Edit Classification button is available and open the Edit Classification dialog
        assertTrue(documentLibrary.getDocument(EDIT_CLASSIFICATION_DOCUMENT).isActionClickable(RecordActions.EDIT_CLASSIFIED_CONTENT));
        documentLibrary.getDocument(EDIT_CLASSIFICATION_DOCUMENT).clickOnEditClassification();
        assertTrue("The Edit Classification dialog is not displayed on click on Edit Classification button.", editContentClassificationDialog.isEditClassificationDialogDisplayed());
        // Verify that the title of the dialog is "Edit Classified File"
        String editClassificationDialogTitle = editContentClassificationDialog.getEditClasificationTitle();
        assertEquals("The dialog title is not Edit Classified Record it is " + editClassificationDialogTitle, editContentClassificationDialog.EDIT_DOCUMENT_CLASSIFICATION_TITLE, editClassificationDialogTitle);
        // Verify that the edit classification button label is "Edit"
        String editClassificationButtonLabel = editContentClassificationDialog.getEditButtonLabel();
        assertEquals("The edit classification button does not have the label Edit, it is " + editClassificationButtonLabel, editContentClassificationDialog.EDIT_BUTTON_LABEL, editClassificationButtonLabel);
        // Verify the previously set classification properties are selected and set by default
        assertEquals("The classification level value from Edit Classification dialog is not the one set in Classification Dialog.", SECRET_CLASSIFICATION_LEVEL_TEXT, editContentClassificationDialog.getLevel());
        assertEquals("The classified by value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFIED_BY, editContentClassificationDialog.getClassifiedBy());
        assertEquals("The classification agency value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFICATION_AGENCY, editContentClassificationDialog.getAgency());
        assertEquals("The classification reason value from Edit Classification dialog is not the one set in Classification Dialog.", CLASSIFICATION_REASON, editContentClassificationDialog.getReasons().get(0));
        assertEquals("The downgrade date value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_DATE_EDIT, editContentClassificationDialog.getDowngradeDate());
        assertEquals("The downgrade event value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_EVENT, editContentClassificationDialog.getDowngradeEvent());
        assertEquals("The downgrade instructions value from Edit Classification dialog is not the one set in Classification Dialog.", DOWNGRADE_INSTRUCTIONS, editContentClassificationDialog.getDowngradeInstructions());
        assertEquals("The declassification date value from Edit Classification dialog is not the one set in Classification Dialog.", DECLASSIFICATION_DATE_EDIT, editContentClassificationDialog.getDeclassificationDate());
        assertEquals("The declassification event value from Edit Classification dialog is not the one set in Classification Dialog.", DECLASSIFICATION_EVENT, editContentClassificationDialog.getDeclassificationEvent());
        assertEquals("The exemption value from Edit Classification dialog is not the one set in Classification Dialog.", EXEMPTION_ID, editContentClassificationDialog.getExemptions().get(0));
        editContentClassificationDialog.clickOnCancel();
    }

    /**
     * Successful editing of classification meta-data
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-RM-2428">RM-2428</a><pre>
     * Given that I am viewing the edit classification dialog
     * Then I am able to make changes to any of the classification meta-data values
     * And when I confirm these changes
     * Then the dialog closes
     * And the changes are committed
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "The verification of the successful editing of the classification meta-data of a document",
            dependsOnGroups = {"EDIT_CLASSIFICATION_INITIAL_STATE", "EDIT_CLASSIFICATION_DATA_PREP"}
    )
    @AlfrescoTest(jira = "RM-2428")
    public void editDocumentClassificationDetails()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);
        documentLibrary.getDocument(EDIT_CLASSIFICATION_DOCUMENT).clickOnEditClassification();
        // Edit the classification meta-data
        editContentClassificationDialog.setClassifiedBy(EDITED_CLASSIFIED_BY)
                .setAgency(EDITED_CLASSIFICATION_AGENCY)
                .removeSelectedReason(CLASSIFICATION_REASON)
                .addReason(EDITED_CLASSIFICATION_REASON)
                .setDowngradeDate(EDITED_DOWNGRADE_DATE)
                .setDowngradeEvent(EDITED_DOWNGRADE_EVENT)
                .setDowngradeInstructions(EDITED_DOWNGRADE_INSTRUCTIONS)
                .setDeclassificationDate(EDITED_DECLASSIFICATION_DATE)
                .setDeclassificationEvent(EDITED_DECLASSIFICATION_EVENT)
                .removeSelectedExemption(EXEMPTION_ID)
                .addExemptionCategory(EDITED_EXEMPTION_INDEX)
                .clickOnEdit();

        documentLibrary.getDocument(EDIT_CLASSIFICATION_DOCUMENT).clickOnEditClassification();
        assertEquals("The classified by value has not been edited succesfully in Edit Classification dialog.", EDITED_CLASSIFIED_BY, editContentClassificationDialog.getClassifiedBy());
        assertEquals("The classification agency value has not been edited succesfully in Edit Classification dialog.", EDITED_CLASSIFICATION_AGENCY, editContentClassificationDialog.getAgency());
        assertEquals("The classification reason value has not been edited succesfully in Edit Classification dialog.", EDITED_CLASSIFICATION_REASON, editContentClassificationDialog.getReasons().get(0));
        assertEquals("The downgrade date value has not been edited succesfully in Edit Classification dialog.", EDITED_DOWNGRADE_DATE, editContentClassificationDialog.getDowngradeDate());
        assertEquals("The downgrade event value has not been edited succesfully in Edit Classification dialog.", EDITED_DOWNGRADE_EVENT, editContentClassificationDialog.getDowngradeEvent());
        assertEquals("The downgrade instructions has not been edited succesfully in Edit Classification dialog.", EDITED_DOWNGRADE_INSTRUCTIONS, editContentClassificationDialog.getDowngradeInstructions());
        assertEquals("The declassification date has not been edited succesfully in Edit Classification dialog.", EDITED_DECLASSIFICATION_DATE, editContentClassificationDialog.getDeclassificationDate());
        assertEquals("The declassification event value has not been edited succesfully in Edit Classification dialog.", EDITED_DECLASSIFICATION_EVENT, editContentClassificationDialog.getDeclassificationEvent());
        assertEquals("The exemption value has not been edited succesfully in Edit Classification dialog.", EDITED_EXEMPTION_INDEX, editContentClassificationDialog.getExemptions().get(0));
        editContentClassificationDialog.clickOnCancel();
    }

    /**
     * Canceling the classification meta-data editing of a record
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-RM-2430">RM-2430</a><pre>
     * Given that I am viewing the edit classification dialog
     * And I make changes to the classification meta-data
     * When I cancel these changes
     * Then the dialog closes
     * And the changes are not committed
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "Verification of canceling the editing over the classification meta-data of a record",
            dependsOnGroups = {"EDIT_CLASSIFICATION_INITIAL_STATE", "EDIT_CLASSIFICATION_DATA_PREP"}
    )
    @AlfrescoTest(jira = "RM-2430")
    public void cancelRecordClassificationEditing()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).clickOnEditClassification();
        // Edit the classification meta-data of the record but cancel it afterwards
        editContentClassificationDialog.setLevel(CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
                .setClassifiedBy(EDITED_CLASSIFIED_BY)
                .setAgency(EDITED_CLASSIFICATION_AGENCY)
                .removeSelectedReason(CLASSIFICATION_REASON)
                .addReason(EDITED_CLASSIFICATION_REASON)
                .setDowngradeDate(EDITED_DOWNGRADE_DATE)
                .setDowngradeEvent(EDITED_DOWNGRADE_EVENT)
                .setDowngradeInstructions(EDITED_DOWNGRADE_INSTRUCTIONS)
                .setDeclassificationDate(EDITED_DECLASSIFICATION_DATE)
                .setDeclassificationEvent(EDITED_DECLASSIFICATION_EVENT)
                .removeSelectedExemption(EXEMPTION_ID)
                .addExemptionCategory(EDITED_EXEMPTION_INDEX)
                .clickOnCancel();

        filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).clickOnEditClassification();
        // Verify the metadata values have not changed
        assertEquals("The classification level value from Edit Classification dialog has changed after cancel in Classification Dialog.", SECRET_CLASSIFICATION_LEVEL_TEXT, editContentClassificationDialog.getLevel());
        assertEquals("The classified by value from Edit Classification dialog has changed after cancel in Classification Dialog.", CLASSIFIED_BY, editContentClassificationDialog.getClassifiedBy());
        assertEquals("The classification agency value from Edit Classification dialog has changed after cancel  in Classification Dialog.", CLASSIFICATION_AGENCY, editContentClassificationDialog.getAgency());
        assertEquals("The classification reason value from Edit Classification dialog has changed after cancel  in Classification Dialog.", CLASSIFICATION_REASON, editContentClassificationDialog.getReasons().get(0));
        assertEquals("The downgrade date value from Edit Classification dialog has changed after cancel in Classification Dialog.", DOWNGRADE_DATE_EDIT, editContentClassificationDialog.getDowngradeDate());
        assertEquals("The downgrade event value from Edit Classification dialog has changed after cancel in Classification Dialog.", DOWNGRADE_EVENT, editContentClassificationDialog.getDowngradeEvent());
        assertEquals("The downgrade instructions value from Edit Classification dialog has changed after cancel in Classification Dialog.", DOWNGRADE_INSTRUCTIONS, editContentClassificationDialog.getDowngradeInstructions());
        assertEquals("The declassification date value from Edit Classification dialog has changed after cancel in Classification Dialog.", DECLASSIFICATION_DATE_EDIT, editContentClassificationDialog.getDeclassificationDate());
        assertEquals("The declassification event value from Edit Classification dialog has changed after cancel in Classification Dialog.", DECLASSIFICATION_EVENT, editContentClassificationDialog.getDeclassificationEvent());
        assertEquals("The exemption value from Edit Classification dialog has changed after cancel in Classification Dialog.", EXEMPTION_ID, editContentClassificationDialog.getExemptions().get(0));
        editContentClassificationDialog.clickOnCancel();
    }

    /**
     * Check mandatory values
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-RM-2430">RM-2429</a><pre>
     * Given that I am viewing the edit classification dialog
     * And I clear any one of the mandatory properties
     * When I try to confirm these changes
     * Then I will be warned that mandatory information is missing
     * And the changes will not be committed until I provide the missing information
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "Check the mandatory fields from the Edit Classification dialog",
            dependsOnGroups = {"EDIT_CLASSIFICATION_DATA_PREP"}
    )
    @AlfrescoTest(jira = "RM-2429")
    public void checkMandatoryFieldsForEditClassificationDialog()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));
        filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).clickOnEditClassification();
        // Edit the mandatory fields to empty values
        editContentClassificationDialog.setClassifiedByEmpty()
                .setAgencyEmpty()
                .removeSelectedReason(CLASSIFICATION_REASON)
                .removeSelectedExemption(EXEMPTION_ID);

        assertFalse("The edit classification button is enabled even if the mandatory fields are not completed.", classifyContentDialog.isClassifyButtonEnabled());
        editContentClassificationDialog.clickOnCancel();
    }

     @Test(
            groups = {"integration", "EDIT_CLASSIFICATION_DATA_PREP"},
            description = "Method that creates and classifies the required content for the Edit Content Classification dialog tests",
            dependsOnGroups = {"CLASSIFICATION_ACTION", "GROUP_RECORD_FOLDER_ONE_EXISTS", "GROUP_COLLABORATION_SITE_EXISTS"}
    )
    public void editClassificationDataPrep()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        // Classify record
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(EDIT_CLASSIFICATION_RECORD);

        filePlan.getRecord(EDIT_CLASSIFICATION_RECORD).clickOnAction(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
                .setClassifiedBy(CLASSIFIED_BY)
                .setAgency(CLASSIFICATION_AGENCY)
                .addReason(CLASSIFICATION_REASON)
                .setDowngradeDate(DOWNGRADE_DATE_EDIT)
                .setDowngradeEvent(DOWNGRADE_EVENT)
                .setDowngradeInstructions(DOWNGRADE_INSTRUCTIONS)
                .setDeclassificationDate(DECLASSIFICATION_DATE_EDIT)
                .setDeclassificationEvent(DECLASSIFICATION_EVENT)
                .addExemptionCategory(EXEMPTION_ID)
                .clickOnClassify();

        openPage(documentLibrary, COLLAB_SITE_ID);
        documentLibrary.getToolbar()
                .clickOnUpload()
                .uploadFile(EDIT_CLASSIFICATION_DOCUMENT);

        // Classify document
        documentLibrary.getDocument(EDIT_CLASSIFICATION_DOCUMENT)
                .clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        classifyContentDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
                .setClassifiedBy(CLASSIFIED_BY)
                .setAgency(CLASSIFICATION_AGENCY)
                .addReason(CLASSIFICATION_REASON)
                .setDowngradeDate(DOWNGRADE_DATE_EDIT)
                .setDowngradeEvent(DOWNGRADE_EVENT)
                .setDowngradeInstructions(DOWNGRADE_INSTRUCTIONS)
                .setDeclassificationDate(DECLASSIFICATION_DATE_EDIT)
                .setDeclassificationEvent(DECLASSIFICATION_EVENT)
                .addExemptionCategory(EXEMPTION_ID)
                .clickOnClassify();
    }
}
