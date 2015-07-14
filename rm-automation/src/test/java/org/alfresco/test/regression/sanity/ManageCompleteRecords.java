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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.alfresco.po.rm.actions.edit.EditElectronicRecordPage;
import org.alfresco.po.rm.actions.edit.EditNonElectronicRecordPage;
import org.alfresco.po.rm.actions.viewaudit.AuditEntry;
import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.console.audit.AuditEntryTypes;
import org.alfresco.po.rm.console.audit.AuditEvents;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.AuthoritySelectDialog;
import org.alfresco.po.rm.managepermissions.ManagePermissions;
import org.alfresco.po.share.properties.Content;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;



/**
 * Manage complete records regression test
 *
 * @author Roy Wetherall
 */
public class ManageCompleteRecords extends BaseTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** edit electronic record */
    @Autowired
    private EditElectronicRecordPage editElectronicRecordPage;

    /** edit non electronic record */
    @Autowired
    private EditNonElectronicRecordPage editNonElectronicRecordPage;

    /** audit log page*/
    @Autowired
    private AuditLogPage auditLogPage;

    @Autowired
    private ManagePermissions managePermissions;

    /**Select dialog*/
    @Autowired
    private AuthoritySelectDialog authoritySelectDialog;


    /**
     * Main regression test execution
     */
    @Test
    (
        groups = {"RMA-2667", "sanity"},
        description = "Manage Complete Records",
        dependsOnGroups = {"RMA-2666"}
    )
    public void manageCompleteRecords()
    {
        // open record folder one
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
            .navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE);

        assertEquals("Both records should be present in record folder one", 2, filePlan.getList().size());

        // verify electronic record actions
        assertNull(filePlan.getRecord(RECORD).isActionsClickable(
                Record.DOWNLOAD,
                Record.EDIT_METADATA,
                Record.REOPEN_RECORD,
                Record.ADD_TO_HOLD,
                Record.COPY,
                Record.MOVE,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT));
        assertFalse(filePlan.getRecord(RECORD).isActionClickable(Record.COMPLETE_RECORD));
        assertFalse(filePlan.getRecord(RECORD).isActionClickable(Record.REQUEST_INFORMATION));

        // navigate to the electronic details page
        filePlan.getRecord(RECORD).clickOnLink();

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                RecordActionsPanel.EDIT_METADATA,
                RecordActionsPanel.REOPEN_RECORD,
                RecordActionsPanel.ADD_TO_HOLD,
                RecordActionsPanel.COPY,
                RecordActionsPanel.MOVE,
                RecordActionsPanel.LINK,
                RecordActionsPanel.DELETE,
                RecordActionsPanel.VIEW_AUDIT));

        // check that download is available for electronic record on
        // record details page
        assertTrue(recordDetails.isDownloadButtonPresent());

        // close the record details page
        recordDetails.navigateUp();

        // verify non-electronic record actions
        assertNull(filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionsClickable(
                Record.EDIT_METADATA,
                Record.REOPEN_RECORD,
                Record.ADD_TO_HOLD,
                Record.COPY,
                Record.MOVE,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT));
        assertFalse(filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionClickable(Record.COMPLETE_RECORD));
        assertFalse(filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionClickable(Record.REQUEST_INFORMATION));
        assertFalse(filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionClickable(Record.DOWNLOAD));

        // navigate to the record details page
        filePlan.getRecord(NON_ELECTRONIC_RECORD).clickOnLink();

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                RecordActionsPanel.EDIT_METADATA,
                RecordActionsPanel.REOPEN_RECORD,
                RecordActionsPanel.ADD_TO_HOLD,
                RecordActionsPanel.COPY,
                RecordActionsPanel.MOVE,
                RecordActionsPanel.LINK,
                RecordActionsPanel.DELETE,
                RecordActionsPanel.VIEW_AUDIT));

        // check download is not available on records detail page for
        // non-electronic record
        assertFalse(recordDetails.isDownloadButtonPresent());

        // close details page
        recordDetails.navigateUp();

        //edit electronic record
        Record electronicRecord = filePlan.getRecord(RECORD);
        //remember the electronic record name (before editing) and identifier to verify it on Audit log page
        String name = electronicRecord.getName();
        String identifier = electronicRecord.getIdentifier();

        electronicRecord.clickOnEditMetadata(editElectronicRecordPage);
        //verify properties from content section are disabled
        Content contentSection = editElectronicRecordPage.getContent();
        assertTrue(!contentSection.isNameEnabled()
                && !contentSection.isTitleEnabled()
                && !contentSection.isDescritionEnabled()
                && !contentSection.isAuthorEnabled()
                && !contentSection.isOwnerEnabled());
        //verify mimetype RM-687
        // assertFalse(contentSection.isMimetypeEnabled());
        //verify properties from record section are enabled
        assertTrue(editElectronicRecordPage.getLocation().isLocationEnabled());

        //edit location and save
        editElectronicRecordPage.getLocation().setLocationField(LOCATION);
        editElectronicRecordPage.clickOnSave();

        // copy electronic record to folder two
        filePlan
            .getRecord(RECORD)
            .clickOnCopyTo()
            .select(RECORD_FOLDER_TWO)
            .clickOnCopy();

        // navigate to record folder two
        filePlan.navigateUp().navigateTo(RECORD_FOLDER_TWO);

        // check that the copy is incomplete and unheld
        assertEquals("Electronic record has not been copied to record folder two.", 2, filePlan.getList().size());
        assertTrue("Electronic record should be incomplete", filePlan.getRecord(RECORD).isIncomplete());
        assertFalse("Electronic record should not be held", filePlan.getRecord(RECORD).isHeld());

        //complete electronic record
        filePlan.getRecord(RECORD).clickOnCompleteRecord();
        //verify the record is complete
        assertFalse(filePlan.getRecord(RECORD).isIncomplete());

        //delete electronic record in folder two
        filePlan.getRecord(RECORD).clickOnDelete().clickOnConfirm();
        assertEquals("Electronic record has not been deleted from folder two", 1, filePlan.getList().size());

        // navigate to record folder one
        filePlan.navigateUp().navigateTo(RECORD_FOLDER_ONE);

        // add the record to hold 1
        filePlan
            .getRecord(RECORD)
            .clickOnAddToHold()
            .selectHold(HOLD1)
            .clickOnOk();

        // check that the record is held
        assertTrue("Electronic record should be held", filePlan.getRecord(RECORD).isHeld());

        // check the available actions on the held record
        assertNull(filePlan.getRecord(RECORD).isActionsClickable(
                Record.DOWNLOAD,
                Record.ADD_TO_HOLD,
                Record.REMOVE_FROM_HOLD,
                Record.VIEW_AUDIT));

        // remove the record from the hold
        filePlan
            .getRecord(RECORD)
            .clickOnRemoveFromHold()
                .selectHold(HOLD1)
            .clickOnOk();

        // check that the record is no longer held
        assertFalse("Electronic record should not be held", filePlan.getRecord(RECORD).isHeld());

        // check the list of actions available on the record after it has been removed from the hold
        assertEquals(null, filePlan.getRecord(RECORD).isActionsClickable(
                Record.DOWNLOAD,
                Record.EDIT_METADATA,
                Record.REOPEN_RECORD,
                Record.ADD_TO_HOLD,
                Record.COPY,
                Record.MOVE,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT));

        // view audit log
        auditLogPage = filePlan
                .getRecord(RECORD)
                .clickOnViewAuditLog();
        //verify audit log page
        verifyAuditLog(name, identifier);
        auditLogPage.close();

        // move electronic record from folder 1 to folder 2
        filePlan
            .getRecord(RECORD)
            .clickOnMoveTo()
            .select(RECORD_FOLDER_TWO)
            .clickOnMove();
        assertEquals("Electronic record has not been moved from folder one to folder two", 1, filePlan.getList().size());

        // navigate to folder 2
        filePlan.navigateUp().navigateTo(RECORD_FOLDER_TWO);
        assertEquals("Electronic record has not been moved from folder one to folder two", 2, filePlan.getList().size());
        assertFalse("Electronic record should be complete", filePlan.getRecord(RECORD).isIncomplete());

        // link electronic record from folder 2 to folder 1
        filePlan
            .getRecord(RECORD)
            .clickOnLinkTo()
            .select(RECORD_FOLDER_ONE)
            .clickOnLink();
        assertTrue("Electronic record should be linked", filePlan.getRecord(RECORD).isLinked());

        // navigate to folder 1
        filePlan.navigateUp().navigateTo(RECORD_FOLDER_ONE);

        // edit non-electronic record meta-data
        filePlan.getRecord(NON_ELECTRONIC_RECORD).clickOnEditMetadata(editNonElectronicRecordPage);
        //verify properties from content section are disabled
        contentSection = editNonElectronicRecordPage.getContent();
        assertTrue(!contentSection.isNameEnabled()
                && !contentSection.isTitleEnabled()
                && !contentSection.isDescritionEnabled());
        //TODO verify non-electronic properties are also disabled
        //verify properties from record section are enabled
        assertTrue(editElectronicRecordPage.getLocation().isLocationEnabled());
        //edit location and save
        editElectronicRecordPage.getLocation().setLocationField(LOCATION);
        editElectronicRecordPage.clickOnSave();

        // reopen the non-electronic record
        assertFalse("Record should be complete", filePlan.getRecord(NON_ELECTRONIC_RECORD).isIncomplete());
        filePlan
            .getRecord(NON_ELECTRONIC_RECORD)
            .clickOnReopenRecord();
        assertTrue("Record should be incomplete", filePlan.getRecord(NON_ELECTRONIC_RECORD).isIncomplete());

        // check the record actions
        assertTrue("Complete record action should be clickable", filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionClickable(Record.COMPLETE_RECORD));
        assertFalse("Reopen record action should not be clickable", filePlan.getRecord(NON_ELECTRONIC_RECORD).isActionClickable(Record.REOPEN_RECORD));

        // complete the non-electronic record
        filePlan
            .getRecord(NON_ELECTRONIC_RECORD)
            .clickOnCompleteRecord();
        assertFalse("Record should be complete", filePlan.getRecord(RECORD).isIncomplete());

        // check the record actions
        assertFalse("Complete record action should not be clickable", filePlan.getRecord(RECORD).isActionClickable(Record.COMPLETE_RECORD));
        assertTrue("Reopen record action should be clickable", filePlan.getRecord(RECORD).isActionClickable(Record.REOPEN_RECORD));

        //manage permissions
        managePermissions = filePlan
                .getRecord(NON_ELECTRONIC_RECORD)
                .clickOnManagePermission();
        authoritySelectDialog = managePermissions.clickOnSelectUsersAndGroups();

        // add test authority
        String testUserName ="user1";
        authoritySelectDialog.authoritySearch(testUserName).clickAddButton();
        managePermissions.clickOnOK();

        //Manage permissions for user1
        String actualPermissionsName;
        String expectedPermissionName = "Read Only";
        Record nelectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        //open manage permissions for electronic record
        managePermissions = nelectronicRecord.clickOnManagePermission();
        //get default permission set for test user
        actualPermissionsName = managePermissions.getPermission(testUserName, testUserName, testUserName);
        //verify the default settings
        assertTrue(actualPermissionsName.equals(expectedPermissionName));
        //change the permission to read and file
        expectedPermissionName = "Read and File";
        //set the authority permission type
        managePermissions.setPermissions(testUserName, testUserName, testUserName, expectedPermissionName);
        //get the test authority permission type and assert ther results
        actualPermissionsName = managePermissions.getPermission(testUserName, testUserName, testUserName);
        //verify the permissions set
        assertTrue(actualPermissionsName.equals(expectedPermissionName));
        managePermissions.clickOnOK();
    }




    /**
     * Verify the contents of the audit log
     */
    public void verifyAuditLog(String name, String identifier)
    {
        // Verify header information
        assertEquals("Audit log for " + name, auditLogPage.getAuditPageHeader());
        /** verify Export and File As Record buttons are displayed and enabled */
        assertTrue(auditLogPage.isExportButtonDisplayed());
        assertTrue(auditLogPage.isExportButtonEnabled());
        assertTrue(auditLogPage.isFileAsRecordButtonDisplayed());
        assertTrue(auditLogPage.isFileAsRecordButtonEnabled());

        // Verify 8 entries are displayed
        int auditEntriesCount = auditLogPage.getAuditEntryCount();
        assertEquals(8, auditEntriesCount);

        /** Verify the first 2 audit entries - record was filed*/
        AuditEntry auditEntry;
        for (int i=0; i<2; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to cm:created
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(AuditEvents.UPDATED_METADATA.toString())
                    || (auditEntry.getAuditEntryEvent().equals(AuditEvents.FILE_TO.toString())));
            assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(AuditEntryTypes.CONTENT.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + RECORD_FOLDER_ONE + "/"
                            + name,
                    auditEntry.getAuditEntryLocation());
        }

        /** Verify next 2 entries - record was completed*/
        for (int i=2; i<4; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to time the record was completed
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed

            // check audit entry
            String auditEntryEvent = auditEntry.getAuditEntryEvent();
            if (!auditEntryEvent.equals(AuditEvents.UPDATED_METADATA.toString()) &&
                !auditEntryEvent.equals(AuditEvents.COMPLETE_RECORD.toString()))
            {
                fail("Expected audit entry to be 'updated' or 'complete', but was '" + auditEntryEvent + "'");
            }

            assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(AuditEntryTypes.CONTENT.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + RECORD_FOLDER_ONE + "/"
                            + name,
                    auditEntry.getAuditEntryLocation());
        }

        /** Verify next entry - record metadata was edited*/
        auditEntry = auditLogPage.getAuditEntry(4);
        assertNotNull(auditEntry.getAuditEntryTimestamp());
        //TODO verify the timestamp equals to time the record was edited
        assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
        //TODO verify the currently logged in user is displayed
        assertTrue(auditEntry.getAuditEntryEvent().equals(AuditEvents.UPDATED_METADATA.toString()));
        assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
        assertEquals(AuditEntryTypes.CONTENT.toString(), auditEntry.getAuditEntryType());
        assertEquals("/" + DOCUMENT_LIBRARY + "/"
                        + RECORD_CATEGORY_ONE + "/"
                        + SUB_RECORD_CATEGORY_NAME + "/"
                        + RECORD_FOLDER_ONE + "/"
                        + name,
                auditEntry.getAuditEntryLocation());

        /** Verify next 2 entries - record was added to hold*/
        for (int i=5; i<7; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to time the record was added to hold
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(AuditEvents.UPDATED_METADATA.toString())
                    || auditEntry.getAuditEntryEvent().equals(AuditEvents.ADD_TO_HOLD.toString()));
            assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(AuditEntryTypes.CONTENT.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_ONE + "/"
                            + SUB_RECORD_CATEGORY_NAME + "/"
                            + RECORD_FOLDER_ONE + "/"
                            + name,
                    auditEntry.getAuditEntryLocation());
        }

        /** Verify last entry - record was removed from hold*/
        auditEntry = auditLogPage.getAuditEntry(7);
        assertNotNull(auditEntry.getAuditEntryTimestamp());
        //TODO verify the timestamp equals to time the record was removed from hold
        assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
        //TODO verify the currently logged in user is displayed
        assertTrue(auditEntry.getAuditEntryEvent().equals(AuditEvents.REMOVE_FROM_HOLD.toString()));
        assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
        assertEquals(AuditEntryTypes.CONTENT.toString(), auditEntry.getAuditEntryType());
        assertEquals("/" + DOCUMENT_LIBRARY + "/"
                        + RECORD_CATEGORY_ONE + "/"
                        + SUB_RECORD_CATEGORY_NAME + "/"
                        + RECORD_FOLDER_ONE + "/"
                        + name,
                auditEntry.getAuditEntryLocation());
    }
}
