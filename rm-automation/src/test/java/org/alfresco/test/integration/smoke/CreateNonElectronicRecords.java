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
package org.alfresco.test.integration.smoke;

import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.po.rm.actions.edit.EditNonElectronicRecordPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.record.PropertiesPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.copymovelinkfile.CopyDialog;
import org.alfresco.po.rm.dialog.create.NewNonElectronicRecordDialog;
import org.alfresco.po.share.properties.Content;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import static org.alfresco.po.rm.details.record.PropertiesPanel.Properties;

/**
 * CreateNonElectronicRecords test
 *
 * @author Oana Nechiforescu
 */
public class CreateNonElectronicRecords extends BaseTest
{
    /** data prep service */
    @Autowired private RecordsManagementService service;

    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** edit electronic record */
    @Autowired
    private EditNonElectronicRecordPage editNonElectronicRecordPage;

    @Autowired
    private NewNonElectronicRecordDialog newNonElectronicRecordDialog;

    /** copy dialog */
    @Autowired
    private CopyDialog copyDialog;

    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2777")
    public void createNonElectronicRecord() {
        String category1 = "RM-2777 category1";
        String folder1 = "RM-2777 folder1";
        String folder2 = "RM-2777 folder2";
        String record = "RM-2777 record";
        String recordTitle = record + " title";
        String recordDescription = record + " description";
        String editedRecord = "edited RM-2777 record";
        String editedRecordTitle = "edited RM-2777 record title";
        String editedRecordDescription = "edited RM-2777 record description";

        // add test precondition
        createTestPrecondition(category1, folder1, folder2);

        filePlan.getRecordFolder(folder1).clickOnLink();
        // create a non-electronic record by completing some of the fields
        newNonElectronicRecordDialog = filePlan.getToolbar().clickOnFile().clickOnNonElectronic();
        newNonElectronicRecordDialog.setName(record);
        newNonElectronicRecordDialog.setTitle(recordTitle);
        newNonElectronicRecordDialog.setDescription(recordDescription);
        newNonElectronicRecordDialog.clickOnSave();

        // check the non-electronic record has been created
        assertNotNull(filePlan.getRecord(record));
        // check it is incomplete by default at creation
        assertTrue(filePlan.getRecord(record).isIncomplete());
        // verify the non-electronic record actions
        assertNull(filePlan.getRecord(record).getUnclickableActions(
                Record.EDIT_METADATA, Record.COMPLETE_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));

        // navigate to the non-electronic record details page
        filePlan.getRecord(record).clickOnLink(recordDetails);

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                Record.EDIT_METADATA, Record.COMPLETE_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));

        // check that download is not available for the non-electronic record on record details page
        assertFalse(recordDetails.isDownloadButtonPresent());

        // check the record has no preview available
        assertFalse(recordDetails.isContentAvailable());

        // check some of the non-electronic record's properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(record));
        assertEquals(recordTitle, PropertiesPanel.getPropertyValue(Properties.TITLE));
        assertEquals(recordDescription, PropertiesPanel.getPropertyValue(Properties.DESCRIPTION));

        // edit the non-electronic record properties
        recordDetails.getRecordActionsPanel().clickOnAction(RecordActions.EDIT_METADATA, editNonElectronicRecordPage);
        Content editMetadataProperties = editNonElectronicRecordPage.getContent();
        editMetadataProperties.setNameValue(editedRecord);
        editMetadataProperties.setTitle(editedRecordTitle);
        editMetadataProperties.setDescription(editedRecordDescription);
        editNonElectronicRecordPage.clickOnSave();

        // check the record's edited properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(editedRecord));
        assertEquals(editedRecordTitle, PropertiesPanel.getPropertyValue(Properties.TITLE));
        assertEquals(editedRecordDescription, PropertiesPanel.getPropertyValue(Properties.DESCRIPTION));

        // complete the record from its Details page and check is does not have the incomplete banner anymore
        recordDetails.getRecordActionsPanel().clickOnAction(RecordActions.COMPLETE_RECORD);
        recordDetails.clickOnParentInBreadcrumb(folder1, filePlan);
        assertFalse(filePlan.getRecord(editedRecord).isIncomplete());

        // check its metadata can not be edited anymore after completion
        filePlan.getRecord(editedRecord).clickOnEditMetadata(editNonElectronicRecordPage);
        assertFalse(editNonElectronicRecordPage.getContent().isNameEnabled());
        assertFalse(editNonElectronicRecordPage.getContent().isTitleEnabled());
        assertFalse(editNonElectronicRecordPage.getContent().isDescritionEnabled());
        editNonElectronicRecordPage.clickOnCancel();

        // copy record to folder 2 and check its state would be by default incomplete
        filePlan.getRecord(editedRecord).clickOnCopyTo().select(folder2).clickOnCopy();
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder2).clickOnLink();
        assertTrue(filePlan.getRecord(editedRecord).isIncomplete());

        // complete the record copy from folder 2
        filePlan.getRecord(editedRecord).clickOnCompleteRecord();
        assertFalse(filePlan.getRecord(editedRecord).isIncomplete());

        // verify that all the expected actions are available
        assertNull(filePlan.getRecord(editedRecord).getUnclickableActions(
                Record.EDIT_METADATA, Record.REOPEN_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.MANAGE_PERMISSIONS,
                Record.ADD_RELATIONSHIP));

        // check its metadata can not be edited anymore after completion
        filePlan.getRecord(editedRecord).clickOnEditMetadata(editNonElectronicRecordPage);
        assertFalse(editNonElectronicRecordPage.getContent().isNameEnabled());
        assertFalse(editNonElectronicRecordPage.getContent().isTitleEnabled());
        assertFalse(editNonElectronicRecordPage.getContent().isDescritionEnabled());
        editNonElectronicRecordPage.clickOnCancel();

        // delete the record from folder 1 and check it is not displayed anymore there after deletion
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder1).clickOnLink(filePlan);
        filePlan.getRecord(editedRecord).clickOnDelete().clickOnConfirm();
        assertNull(filePlan.getRecord(editedRecord));

        // move folder from folder 2 to folder 1 and check it is not displayed anymore there after move
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder2).clickOnLink(filePlan);
        filePlan.getRecord(editedRecord).clickOnMoveTo().select(folder1).clickOnMove();
        assertNull(filePlan.getRecord(editedRecord));

        // navigate inside folder 1 and check the moved record state is complete
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder1).clickOnLink(filePlan);
        assertFalse(filePlan.getRecord(editedRecord).isIncomplete());

        // link the record to folder 2
        filePlan.getRecord(editedRecord).clickOnLinkTo().select(folder2).clickOnLink();
        assertTrue(filePlan.getRecord(editedRecord).isLinked());
        // check it is displayed in folder 2 as well
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder2).clickOnLink(filePlan);
        assertNotNull(filePlan.getRecord(editedRecord));

        // check folder 1 is displayed in the breadcrumb for the record and click on it
        filePlan.getRecord(editedRecord).clickOnLink(recordDetails);
        assertTrue(recordDetails.getBreadcrumbPath().contains(folder1));
        recordDetails.clickOnParentInBreadcrumb(folder1, filePlan);
        // check the record is displayed in folder 1
        assertNotNull(filePlan.getRecord(editedRecord));

        // delete the category 1
        deleteCategory1(category1);
    }

    private void createTestPrecondition(String category1, String folder1, String folder2)
    {
        // create "rm admin" user
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
        // log in with the RM admin user
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");

        // create category 1
        filePlan.getToolbar()
                .clickOnNewCategory()
                .setName(category1)
                .setTitle(TITLE)
                .clickOnSave();
        // navigate inside category 1
        filePlan.getRecordCategory(category1).clickOnLink();
        // create folder 1
        filePlan.getToolbar().clickOnNewRecordFolder()
                .setName(folder1).setTitle(TITLE).clickOnSave();
        // create folder 2
        filePlan.getToolbar().clickOnNewRecordFolder()
                .setName(folder2).setTitle(TITLE).clickOnSave();
    }

    private void deleteCategory1(String category1)
    {
        filePlan.navigateUp().navigateUp();
        filePlan.getRecordCategory(category1).clickOnDelete().clickOnConfirm();
        assertNull(filePlan.getRecordCategory(category1));
    }
}

