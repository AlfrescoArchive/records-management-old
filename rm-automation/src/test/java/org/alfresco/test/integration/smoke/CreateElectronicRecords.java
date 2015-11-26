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
import org.alfresco.po.rm.actions.edit.EditElectronicRecordPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.record.PropertiesPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.copymovelinkfile.CopyDialog;
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
 * CreateElectronicRecords test
 * 
 * @author Oana Nechiforescu
 */
public class CreateElectronicRecords extends BaseTest
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
    private EditElectronicRecordPage editElectronicRecordPage;

    /** copy dialog */
    @Autowired
    private CopyDialog copyDialog;

    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2768")
    public void createElectronicRecord()
    {     
        String category1 = "RM-2768 category1";
        String folder1 = "RM-2768 folder1";
        String folder2 = "RM-2768 folder2";
        String record = "RM-2768 record";
        String editedRecord = "edited RM-2768 record";
        String editedRecordTitle = "edited RM-2768 record title";
        String editedRecordAuthor = "edited RM-2768 record author";
        String editedRecordDescription = "edited RM-2768 record description";
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

        // upload a file in folder 1
        filePlan.getRecordFolder(folder1).clickOnLink();       
        filePlan.getToolbar().clickOnFile()
                .clickOnElectronic().uploadFile(record);
        // check it is by default after creation in incomplete state
        assertTrue(filePlan.getRecord(record).isIncomplete());
        // verify electronic record actions
        assertNull(filePlan.getRecord(record).getUnclickableActions(
                Record.DOWNLOAD, Record.EDIT_METADATA, Record.COMPLETE_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));

        // navigate to the record details page
        filePlan.getRecord(record).clickOnLink(recordDetails);

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                Record.EDIT_METADATA, Record.COMPLETE_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));

        // check that download is available for the electronic record on record details page
        assertTrue(recordDetails.isDownloadButtonPresent());

        // check some record's properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(record));

        // copy the record to folder 1 from its Details page and check it is copied successfully
        recordDetails.getRecordActionsPanel().clickOnAction(RecordActions.COPY, copyDialog).select(folder1).clickOnCopy();
        recordDetails.navigateUp();
        assertNotNull(filePlan.getRecord("Copy of " + record));

        // move record to folder 2
        filePlan.getRecord(record).clickOnMoveTo().select(folder2).clickOnMove();
        // navigate to category level
        filePlan.navigateUp();
        // check the record has been moved successfully to folder 2
        filePlan.getRecordFolder(folder2).clickOnLink();
        assertNotNull(filePlan.getRecord(record));

        // navigate to category level
        filePlan.navigateUp();
        // delete the record copy from folder 1
        filePlan.getRecordFolder(folder1).clickOnLink();
        filePlan.getRecord("Copy of " + record).clickOnDelete().clickOnConfirm();
        // check it has been deleted
        assertNull(filePlan.getRecord("Copy of " + record));

        // navigate to category level
        filePlan.navigateUp();
        // complete the record from folder 2
        filePlan.getRecordFolder(folder2).clickOnLink();
        filePlan.getRecord(record).clickOnCompleteRecord();
        // check the incomplete banner is not displayed anymore
        assertFalse(filePlan.getRecord(record).isIncomplete());

        // check the record's available actions
        assertNull(filePlan.getRecord(record).getUnclickableActions(
                Record.DOWNLOAD, Record.EDIT_METADATA, Record.REOPEN_RECORD,
                Record.COPY, Record.MOVE, Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));
        assertFalse(filePlan.getRecord(record).isActionClickable(Record.COMPLETE_RECORD));

        // navigate to record's Details page and check its metadata can not be edited anymore
        filePlan.getRecord(record).clickOnEditMetadata(editElectronicRecordPage);
        assertFalse(editElectronicRecordPage.getContent().isNameEnabled());
        assertFalse(editElectronicRecordPage.getContent().isTitleEnabled());
        assertFalse(editElectronicRecordPage.getContent().isDescritionEnabled());
        assertFalse(editElectronicRecordPage.getContent().isAuthorEnabled());
        assertFalse(editElectronicRecordPage.getContent().isOwnerEnabled());
        editElectronicRecordPage.clickOnCancel();

        // re-open the record from its Details page
        filePlan.getRecord(record).clickOnLink(recordDetails).getRecordActionsPanel().clickOnAction(RecordActions.REOPEN_RECORD);
        // check the preview of the record is available
        assertTrue("The electronic record text file preview is not available", recordDetails.isContentAvailable());
        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                Record.EDIT_METADATA, Record.COMPLETE_RECORD,
                Record.COPY, Record.MOVE,
                Record.LINK, Record.DELETE,
                Record.VIEW_AUDIT, Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS, Record.ADD_RELATIONSHIP));
        assertFalse(recordDetails.getRecordActionsPanel().isActionAvailable(RecordActions.REOPEN_RECORD));

        // edit the metadata of the record
        recordDetails.getRecordActionsPanel().clickOnAction(RecordActions.EDIT_METADATA, editElectronicRecordPage);
        editElectronicRecordPage.getContent().setNameValue(editedRecord);
        editElectronicRecordPage.getContent().setTitle(editedRecordTitle);
        editElectronicRecordPage.getContent().setAuthor(editedRecordAuthor);
        editElectronicRecordPage.getContent().setDescription(editedRecordDescription);
        editElectronicRecordPage.clickOnSave();

        // check the edited record's properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(editedRecord));
        assertEquals(editedRecordTitle, PropertiesPanel.getPropertyValue(Properties.TITLE));
        assertEquals( editedRecordDescription, PropertiesPanel.getPropertyValue(Properties.DESCRIPTION));
        assertEquals(editedRecordAuthor, PropertiesPanel.getPropertyValue(Properties.AUTHOR));

        // navigate to folder 2 level and link the record to folder 1
        recordDetails.clickOnParentInBreadcrumb(folder2, filePlan);
        filePlan.getRecord(editedRecord).clickOnLinkTo().select(folder1).clickOnLink();
        // check the linked indicator is displayed for the record
        assertTrue(filePlan.getRecord(editedRecord).isLinked());

        // navigate inside folder 1 and verify that the record is displayed there as well
        filePlan.navigateUp();
        filePlan.getRecordFolder(folder1).clickOnLink();
        assertNotNull(filePlan.getRecord(editedRecord));
        // check that the folder 2 is displayed in the record Details page breadcrumb
        filePlan.getRecord(editedRecord).clickOnLink(recordDetails);
        assertTrue(recordDetails.getBreadcrumbPath().contains(folder2));

        // click on folder 2 in breadcrumb
        recordDetails.clickOnParentInBreadcrumb(folder2, filePlan);
        // check the navigation is made to folder 2 and that the record is inside it
        assertTrue(filePlan.getBreadcrumbPath().contains(folder2));
        assertNotNull(filePlan.getRecord(editedRecord));
        // navigate to FilePlan level and delete the category
        filePlan.navigateUp().navigateUp();
        filePlan.getRecordCategory(category1).clickOnDelete().clickOnConfirm();
    }
    
}
