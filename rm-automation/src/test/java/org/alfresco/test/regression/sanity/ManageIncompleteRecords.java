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

import static org.alfresco.po.rm.console.audit.AuditEntryTypes.NON_ELECTRONIC_DOCUMENT;
import static org.alfresco.po.rm.console.audit.AuditEvents.CREATED_OBJECT;
import static org.alfresco.po.rm.console.audit.AuditEvents.FILE_TO;
import static org.alfresco.po.rm.console.audit.AuditEvents.MOVE_TO;
import static org.alfresco.po.rm.console.audit.AuditEvents.UPDATED_METADATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.common.util.Retry;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.actions.edit.EditNonElectronicRecordPage;
import org.alfresco.po.rm.actions.viewaudit.AuditEntry;
import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.AuthoritySelectDialog;
import org.alfresco.po.rm.dialog.RequestInformationDialog;
import org.alfresco.po.rm.managepermissions.ManagePermissions;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Manage incomplete records regression test
 *
 * @author Roy Wetherall
 */
public class ManageIncompleteRecords extends BaseTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** edit non electronic record */
    @Autowired
    private EditNonElectronicRecordPage editNonElectronicRecordPage;

    /** audit log page*/
    @Autowired
    private AuditLogPage auditLogPage;

    /**request information dialog*/
    @Autowired
    private RequestInformationDialog requestInformationDialog;
    /**Manage permissions*/
    @Autowired
    private ManagePermissions managePermissions;

    /**Select dialog*/
    @Autowired
    private AuthoritySelectDialog authoritySelectDialog;

    private static String userName = "user1";

    /**
     * Main regression test execution
     */
    @Test
    (
        groups = { "RMA-2666", "sanity" },
        description = "Manage Incomplete Records",
        dependsOnGroups = { "RMA-2665" }
    )
    public void manageIncompleteRecords()
    {
        createUser(userName);
        // open record folder one
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
            .navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE);

        // verify electronic record actions
        compareArrays(RecordActions.INCOMPLETE_RECORD_ACTIONS_WITH_DOWNLOAD, filePlan.getRecord(RECORD).getClickableActions());

        // navigate to the electronic details page
        filePlan.getRecord(RECORD).clickOnLink();

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                RecordActionsPanel.EDIT_METADATA,
                RecordActionsPanel.COMPLETE_RECORD,
                RecordActionsPanel.ADD_TO_HOLD,
                RecordActionsPanel.COPY,
                RecordActionsPanel.MOVE,
                RecordActionsPanel.LINK,
                RecordActionsPanel.DELETE,
                RecordActionsPanel.VIEW_AUDIT,
                RecordActionsPanel.REQUEST_INFORMATION));

        // check that download is available for electronic record on record
        // details page
        assertTrue(recordDetails.isDownloadButtonPresent());

        // close the record details page
        recordDetails.navigateUp();

        // verify non-electronic record actions
        Record nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertNotNull(nonElectronicRecord);
        compareArrays(RecordActions.INCOMPLETE_RECORD_ACTIONS, nonElectronicRecord.getClickableActions());

        //remember the non-electronic record name (before editing) and identifier to verify it on Audit log page
        String nameBefore = nonElectronicRecord.getName();
        String identifier = nonElectronicRecord.getIdentifier();

        // navigate to the record details page
        nonElectronicRecord.clickOnLink();

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(RecordActions.INCOMPLETE_RECORD_ACTIONS));

        // check download is not available on records detail page for
        // non-electronic record
        assertFalse(recordDetails.isDownloadButtonPresent());

        // edit non-electronic metadata
        recordDetails.getRecordActionsPanel().clickOnAction(RecordActionsPanel.EDIT_METADATA, editNonElectronicRecordPage);
        editNonElectronicRecordPage
            .getContent().setNameValue(NON_ELECTRONIC_RECORD + MODIFIED)
            .setTitle(TITLE + MODIFIED)
            .setDescription(DESCRIPTION + MODIFIED);
        editNonElectronicRecordPage.getNonElectronicRecord()
             .setPhysicalSize(PHYSICALSIZE)
             .setNumberOfCopies(NUMBEROFCOPIES)
             .setStorageLocation(STORAGELOCATION)
             .setShelf(SHELF)
             .setBox(BOX)
             .setFile(FILE);
        editNonElectronicRecordPage.getLocation().setLocationField(LOCATION);
        editNonElectronicRecordPage.clickOnSave();

        // TODO verify entered values are displayed on details page

        // close record details page
        recordDetails.navigateUp();

        // copy non-electronic record to folder2
        filePlan
           .getRecord(NON_ELECTRONIC_RECORD)
           .clickOnCopyTo()
           .select(RECORD_FOLDER_TWO)
           .clickOnCopy();

        assertEquals("Both records should be present in record folder one after copy", 2, filePlan.getList().size());

        // view audit log
         auditLogPage = filePlan
              .getRecord(NON_ELECTRONIC_RECORD)
              .clickOnViewAuditLog();
        //verify audit log page
        verifyAuditLog(nameBefore, NON_ELECTRONIC_RECORD + MODIFIED, identifier);
        auditLogPage.close();

        // add non-electronic record to hold 1
        nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertFalse(nonElectronicRecord.isHeld());
        nonElectronicRecord
            .clickOnAddToHold()
            .selectHold(HOLD1)
            .clickOnOk();
        nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertTrue(nonElectronicRecord.isHeld());

        // check the correct actions for a held record are being shown
        assertNull(nonElectronicRecord.isActionsClickable(
                Record.ADD_TO_HOLD,
                Record.REMOVE_FROM_HOLD,
                Record.VIEW_AUDIT));

        // remove from hold
        nonElectronicRecord
            .clickOnRemoveFromHold()
                .selectHold(HOLD1)
            .clickOnOk();
        nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertFalse(nonElectronicRecord.isHeld());

        // delete non-electronic record
        filePlan
            .getRecord(NON_ELECTRONIC_RECORD)
            .clickOnDelete()
            .clickOnConfirm();
        assertNull(filePlan.getRecord(NON_ELECTRONIC_RECORD));
        assertEquals("Only electronic record should be in folder one", 1, filePlan.getList().size());

        // open folder 2
        filePlan
            .navigateUp()
            .navigateTo(RECORD_FOLDER_TWO);
        assertEquals("Copied non-electronic record is not present in folder two", 1, filePlan.getList().size());

        // move non-electronic record in folder2 back to folder 1
        filePlan
            .getRecord(NON_ELECTRONIC_RECORD)
            .clickOnMoveTo()
            .select(RECORD_FOLDER_ONE)
            .clickOnMove();
        assertEquals("Non-electronic record was not moved to record folder one from folder two", 0, filePlan.getList().size());

        // open folder 1
        filePlan
            .navigateUp()
            .navigateTo(RECORD_FOLDER_ONE);
        assertEquals("Both records should be in record folder one ready for completion", 2, filePlan.getList().size());

        // link non-electronic record to folder
        nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertFalse("Non-electronic record should not yet be linked", nonElectronicRecord.isLinked());
        nonElectronicRecord
            .clickOnLinkTo()
            .select(RECORD_FOLDER_TWO)
            .clickOnLink();
        nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertTrue("Non-electronic record should be linked", nonElectronicRecord.isLinked());

        // complete non-electronic record
        assertTrue(filePlan.getRecord(NON_ELECTRONIC_RECORD).isIncomplete());
        filePlan.getRecord(NON_ELECTRONIC_RECORD).clickOnCompleteRecord();
        assertFalse(filePlan.getRecord(NON_ELECTRONIC_RECORD).isIncomplete());

        // request information
        Record electronicRecord = filePlan.getRecord(RECORD);
        requestInformationDialog = electronicRecord.clickOnRequestInformation();
        String name =  electronicRecord.getName();

        //verify the name of the record (the information is requested for) is correct
        assertEquals(name, requestInformationDialog.getRecordName());

        //add Administrator to 'Request information from' select
        requestInformationDialog.clickOnSelectUsersAndGroups().search("Administrator").clickAddIcon().clickOnOk();

        // fill in Requested information
        requestInformationDialog.setRequestedInfoArea("What is that?");

        // click Request Information
        requestInformationDialog.clickRequestInformation(filePlan);

        // verify the information requested indicator is displayed
        electronicRecord = filePlan.getRecord(RECORD);
        assertTrue("Information should be requested for electronic record", electronicRecord.isInformationRequested());

        //manage permissions
        managePermissions = electronicRecord.clickOnManagePermission();
        authoritySelectDialog = managePermissions.clickOnSelectUsersAndGroups();
        // add test authority
        String testUserName = "user1";
        authoritySelectDialog.authoritySearch(testUserName).clickAddButton();
        managePermissions.clickOnOK();

        //Manage permissions for user1
        String actualPermissionsName;
        String expectedPermissionName = "Read Only";
        electronicRecord = filePlan.getRecord(RECORD);
        //open manage permissions for electronic record
        managePermissions = electronicRecord.clickOnManagePermission();
        //get default permission set for test user
        actualPermissionsName = managePermissions.getPermission(testUserName);
        //verify the default settings
        assertTrue(actualPermissionsName.equals(expectedPermissionName));
        //change the permission to read and file
        expectedPermissionName = "Read and File";
        //set the authority permission type
        managePermissions.setPermissions(testUserName, expectedPermissionName);
        //get the test authority permission type and assert ther results
        actualPermissionsName = managePermissions.getPermission(testUserName);
        //verify the permissions set
        assertTrue(actualPermissionsName.equals(expectedPermissionName));
        managePermissions.clickOnOK();

        // complete electronic record
        filePlan.getRecordFolder(RECORD_FOLDER_ONE);
        electronicRecord = filePlan.getRecord(RECORD);
        assertTrue(electronicRecord.isIncomplete());
        electronicRecord.clickOnCompleteRecord();
        assertFalse(filePlan.getRecord(RECORD).isIncomplete());
    }

    /**
     * Verify the contents of the audit log
     */
    public void verifyAuditLog(String nameBefore, String nameAfter, String identifier)
    {
        // Verify header information
        assertEquals("Audit log for " + nameAfter, auditLogPage.getAuditPageHeader());
        /** verify Export and File As Record buttons are displayed and enabled */
        assertTrue(auditLogPage.isExportButtonDisplayed());
        assertTrue(auditLogPage.isExportButtonEnabled());
        assertTrue(auditLogPage.isFileAsRecordButtonDisplayed());
        assertTrue(auditLogPage.isFileAsRecordButtonEnabled());

        //TODO specify all values as static elements

        Utils.retry(new Retry<Void>()
        {
            public Void execute()
            {
                // Verify 5 entries are displayed
                int auditEntriesCount = auditLogPage.getAuditEntryCount();
                assertEquals(5, auditEntriesCount);
                return null;
            }
        }, 5);

        /** Verify the first 3 audit entries*/
        AuditEntry auditEntry;
        for (int i=0; i<5 - 2; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to cm:created
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || (auditEntry.getAuditEntryEvent().equals(FILE_TO.toString()))
                    || (auditEntry.getAuditEntryEvent().equals(CREATED_OBJECT.toString())));
            assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(NON_ELECTRONIC_DOCUMENT.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + RECORD_FOLDER_ONE + "/"
                            + nameBefore,
                    auditEntry.getAuditEntryLocation());
        }

        /** Verify the last 2 entries*/
        for (int i=5 - 2; i<5; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to cm:modified
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || auditEntry.getAuditEntryEvent().equals(MOVE_TO.toString()));
            assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(NON_ELECTRONIC_DOCUMENT.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + RECORD_FOLDER_ONE + "/"
                            + nameAfter,
                    auditEntry.getAuditEntryLocation());
        }
    }
}
