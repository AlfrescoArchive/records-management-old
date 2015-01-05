/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
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
package org.alfresco.test.regression.sanity;

import static org.alfresco.po.rm.console.audit.AuditEntryTypes.RECORD_FOLDER;
import static org.alfresco.po.rm.console.audit.AuditEvents.CREATED_OBJECT;
import static org.alfresco.po.rm.console.audit.AuditEvents.MOVE_TO;
import static org.alfresco.po.rm.console.audit.AuditEvents.UPDATED_METADATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.actions.edit.EditRecordFolderPage;
import org.alfresco.po.rm.actions.viewaudit.AuditEntry;
import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.folder.FolderDetails;
import org.alfresco.po.rm.details.record.ActionsPanel;
import org.alfresco.po.rm.dialog.VitalReviewPeriod;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Manage Folders regression test
 *
 * @author Tatiana Kalinovskaya
 */
public class ManageFolders extends BaseTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** folder details page */
    @Autowired
    private FolderDetails folderDetails;

    /** edit non electronic record */
    @Autowired
    private EditRecordFolderPage editRecordFolderPage;

    /** audit log page*/
    @Autowired
    private AuditLogPage auditLogPage;

    /**
     * Main regression test execution
     */
    @Test
    (
        groups = {"RMA-2668", "sanity"},
        description = "Manage Folders",
        dependsOnGroups = {"RMA-2667"}
    )
    public void manageFolders()
    {
        // create user
        createUser(USER1, UsersAndGroupsPage.ROLE_RM_MANAGER);
        
        // TODO @Hema give user1 read-only permissions on Category1
        
        // open sub-category
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME);
        assertEquals("Both folders should be present in sub category", 2, filePlan.getList().size());

       // verify record folder one actions
        assertNull(filePlan.getRecordFolder(RECORD_FOLDER_ONE).isActionsClickable(
                RecordFolder.FOLDER_VIEW_DETAILS,
                RecordFolder.EDIT_METADATA,
                RecordFolder.CLOSE_FOLDER,
                RecordFolder.ADD_TO_HOLD,
                RecordFolder.COPY_FOLDER,
                RecordFolder.MOVE_FOLDER,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.DELETE,
                RecordFolder.VIEW_AUDIT));

        // navigate to the folder one details page
        filePlan.getRecordFolder(RECORD_FOLDER_ONE).clickOnViewDetails();

        // verify that all the expected actions are available
        assertTrue(folderDetails.getFolderActionsPanel().isActionsClickable(
                ActionsPanel.EDIT_METADATA,
                ActionsPanel.CLOSE_FOLDER,
                ActionsPanel.COPY_FOLDER,
                ActionsPanel.MOVE_FOLDER,
                ActionsPanel.ADD_TO_HOLD,
                ActionsPanel.MANAGE_PERMISSIONS,
                ActionsPanel.DELETE,
                ActionsPanel.VIEW_AUDIT));

        //edit metadata
        folderDetails.getFolderActionsPanel().clickOnAction(ActionsPanel.EDIT_METADATA, editRecordFolderPage);
        String folderName = RECORD_FOLDER_ONE + MODIFIED;
        editRecordFolderPage
                .getContent().setNameValue(RECORD_FOLDER_ONE + MODIFIED)
                .setTitle(TITLE + MODIFIED)
                .setDescription(DESCRIPTION + MODIFIED);
        editRecordFolderPage.getIdentifierAndVitalInformation()
                .checkVitalIndicator(true)
                .setReviewPeriod(VitalReviewPeriod.DAY)
                .setPeriodExpression("2");
        editRecordFolderPage.getLocation().setLocationField(LOCATION);
        editRecordFolderPage.clickOnSave();
        // TODO verify entered values are displayed on details page

        // navigate inside sub-category/ click on sub-category on breadcrumb
        folderDetails.navigateUp(2);

        // TODO try to copy folder1 to folder2

        //copy folder1 to category
        filePlan
                .getRecordFolder(folderName)
                .clickOnCopyTo()
                .select(RECORD_CATEGORY_ONE)
                .clickOnCopy();

        assertEquals("Both folders should be present in subcategory after copy", 2, filePlan.getList().size());

        // add folder-one to hold-1
        RecordFolder folder1 = filePlan.getRecordFolder(folderName);
        assertFalse(folder1.isHeld());
        folder1
                .clickOnAddToHold()
                .selectHold(HOLD1)
                .clickOnOk();
        folder1 = filePlan.getRecordFolder(folderName);
        assertTrue(folder1.isHeld());
        // check the correct actions for a held folder are being shown
        assertNull(folder1.isActionsClickable(
                RecordFolder.FOLDER_VIEW_DETAILS,
                RecordFolder.ADD_TO_HOLD,
                RecordFolder.REMOVE_FROM_HOLD,
                RecordFolder.VIEW_AUDIT));

        //view audit log
        auditLogPage = filePlan
                .getRecordFolder(folderName)
                .clickOnViewAuditLog();
        //verify audit log page
        verifyAuditLog(RECORD_FOLDER_ONE, folderName);
        auditLogPage.close();

        // remove from hold
        folder1 = filePlan.getRecordFolder(folderName);
        folder1
                .clickOnRemoveFromHold()
                .selectHold(HOLD1)
                .clickOnOk();
        folder1 = filePlan.getRecordFolder(folderName);
        assertFalse(folder1.isHeld());

        // delete folder-one from sub-category
        folder1
                .clickOnDelete()
                .clickOnConfirm();
        assertNull(filePlan.getRecordFolder(folderName));
        assertEquals("Only folder-two should be in sub-category", 1, filePlan.getList().size());

        //navigate up to category
        filePlan.navigateUp();

        //move folder1 to sub-category
        filePlan
                .getRecordFolder(folderName)
                .clickOnMoveTo()
                .select(SUB_RECORD_CATEGORY_NAME)
                .clickOnMove();
        assertEquals("Only one folder should be present in category after move", 1, filePlan.getList().size());

        //navigate inside sub-category
        filePlan.getRecordCategory(SUB_RECORD_CATEGORY_NAME).clickOnLink();

        //close folder-two
        filePlan.getRecordFolder(RECORD_FOLDER_TWO).clickOnCloseFolder();
        //verify the indicator is displayed
        assertTrue(filePlan.getRecordFolder(RECORD_FOLDER_TWO).isClosed());
        //verify the available actions
        assertNull(filePlan.getRecordFolder(RECORD_FOLDER_TWO).isActionsClickable(
                RecordFolder.FOLDER_VIEW_DETAILS,
                RecordFolder.REOPEN_FOLDER,
                RecordFolder.ADD_TO_HOLD,
                RecordFolder.COPY_FOLDER,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.VIEW_AUDIT));

        //reopen folder-two
        filePlan.getRecordFolder(RECORD_FOLDER_TWO).clickOnReopenFolder();
        //verify the indicator is not displayed
        assertFalse(filePlan.getRecordFolder(RECORD_FOLDER_TWO).isClosed());
        // verify that all the expected actions are available
        assertNull(filePlan.getRecordFolder(RECORD_FOLDER_TWO).isActionsClickable(
                RecordFolder.EDIT_METADATA,
                RecordFolder.CLOSE_FOLDER,
                RecordFolder.COPY_FOLDER,
                RecordFolder.MOVE_FOLDER,
                RecordFolder.ADD_TO_HOLD,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.DELETE,
                RecordFolder.VIEW_AUDIT));

    }

    /**
     * Verify the contents of the audit log
     */
    public void verifyAuditLog(String nameBefore, String nameAfter)
    {
        // Verify header information
        assertEquals("Audit log for " + nameAfter, auditLogPage.getAuditPageHeader());
        // verify Export and File As Record buttons are displayed and enabled
        assertTrue(auditLogPage.isExportButtonDisplayed());
        assertTrue(auditLogPage.isExportButtonEnabled());
        assertTrue(auditLogPage.isFileAsRecordButtonDisplayed());
        assertTrue(auditLogPage.isFileAsRecordButtonEnabled());

        //TODO specify all values as static elements
        // Verify 10 entries are displayed
        assertEquals(10, auditLogPage.getAuditEntryCount());

        // Verify the first 2 audit entries: when the folder was created
        AuditEntry auditEntry;
        for (int i=0; i<2; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to cm:created
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || (auditEntry.getAuditEntryEvent().equals(CREATED_OBJECT.toString())));
           // assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(RECORD_FOLDER.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + nameBefore,
                    auditEntry.getAuditEntryLocation());
        }

        //Verify next 4 audit entries: record was added to hold and removed from hold, deleted and linked
        for (int i=2; i<6; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to time when non-electronic record was added to hold and removed from hold, deleted and linked
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString()));
            // assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(RECORD_FOLDER.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + nameBefore,
                    auditEntry.getAuditEntryLocation());
        }

        // Verify next 2 entries: folder was renamed
        for (int i=6; i<8; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to time when folder was renamed
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || auditEntry.getAuditEntryEvent().equals(MOVE_TO.toString()));
          //  assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(RECORD_FOLDER.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + nameAfter,
                    auditEntry.getAuditEntryLocation());
        }

        // Verify next 2 entries: folder was renamed
        for (int i=8; i<10; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to time when folder was added to hold
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || auditEntry.getAuditEntryEvent().equals(ADD_TO_HOLD.toString()));
            //  assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(RECORD_FOLDER.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + nameAfter,
                    auditEntry.getAuditEntryLocation());
        }
    }
    
    /**
     * Delete user when suit is finished
     */
    @AfterSuite(alwaysRun=true)
    protected void deleteTestUser()
    {
        // delete user
        deleteUser(USER1);
    }
}
