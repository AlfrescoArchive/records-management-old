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

import java.util.Arrays;
import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.po.rm.browse.FilePlanFilterPanel;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.record.PropertiesPanel;
import org.alfresco.po.rm.details.record.PropertiesPanel.Properties;
import org.alfresco.po.rm.dialog.GeneralConfirmationDialog;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.alfresco.test.DataPrepHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * DeclareInPlaceRecord test
 *
 * @author Oana Nechiforescu
 */
public class DeclareInPlaceRecord extends BaseTest
{   
   
    /** document library browse view */
    @Autowired
    private DocumentLibrary documentLibrary;
    
    /** document details page */
    @Autowired
    private DocumentDetails documentDetails;
    
    @Autowired
    private FilePlan filePlan;
    
    @Autowired
    private FilePlanFilterPanel filePlanPanel;
    
    @Autowired
    private GeneralConfirmationDialog confirmationDialog;
    
    /** data prep services */
    @Autowired private RecordsManagementService service;
    @Autowired private DataPrepHelper dataPrep;
    
    String uploadedInplaceRecord = "in-place record";
    String createdInplaceRecord = "created in-place record";
    
    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira="RM-2366")
    public void declareInplaceRecord()
    {
        // create "rm admin" user if it does not exist and assign it to RM Administrator role
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
        // create Collaboration site
        dataPrep.createSite(COLLAB_SITE, COLLAB_SANITY_ID);
        // create collab_user
        dataPrep.createUser(COLLABORATOR);
        // invite collab_user to Collaboration site with Contributor role
        service.inviteUserToSite(getAdminName(), getAdminPassword(), COLLABORATOR, COLLAB_SANITY_ID, "SiteContributor");

        openPage(COLLABORATOR, DEFAULT_PASSWORD, documentLibrary, COLLAB_SANITY_ID);
        // upload document
        documentLibrary
                .getToolbar()
                .clickOnUpload()
                .uploadFile(uploadedInplaceRecord);

        // create document
        documentLibrary
                .getToolbar()
                .clickOnCreateTextFile()
                .createTextFile(createdInplaceRecord, "default content").clickOnParentInBreadcrumb("Documents", documentLibrary);

        Document uploadedDoc = documentLibrary.getDocument(uploadedInplaceRecord);
        String[] uploadedDocClickableActions = uploadedDoc.getClickableActions();

        // check the uploaded document available actions
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));

        // check the created document available actions
        Document createdDoc = documentLibrary.getDocument(createdInplaceRecord);
        String[] createdDocClickableActions = createdDoc.getClickableActions();
        assertTrue(Arrays.asList(createdDocClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertFalse(Arrays.asList(createdDocClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertFalse(Arrays.asList(createdDocClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));

        // declare as record the uploaded document
        uploadedDoc.clickOnAction(DocumentActions.DECLARE_AS_RECORD);

        // check the uploaded document declared now as record available actions
        String[] uploadedRecordClickableActions = documentLibrary.getInplaceRecord(uploadedInplaceRecord).getClickableActions();
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));

        // navigate to Document Details page
        documentLibrary.getInplaceRecord(uploadedInplaceRecord).clickOnLink(documentDetails);
        // check the preview of the file is available
        assertTrue("The in-place record text file preview is not available", documentDetails.isContentAvailable());

        // check the uploaded record available actions
        DocumentActionsPanel actionsPanel = documentDetails.getDocumentActionsPanel();
        assertTrue(actionsPanel.isActionClickable(RecordActions.DOWNLOAD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.EDIT_METADATA));
        assertTrue(actionsPanel.isActionClickable(RecordActions.HIDE_RECORD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.MOVE_INPLACE));

        // check some of the uploaded record properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(uploadedInplaceRecord));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.CREATOR).equals(COLLABORATOR));

        // check the record location is still in Document Library by clicking on its "Documents" container
        documentDetails.clickOnParentInBreadcrumb("Documents", documentLibrary);

        // declare as record the created document from its Document Details page
        DocumentDetails createdDocumentDetails = documentLibrary.getDocument(createdInplaceRecord).clickOnLink(documentDetails);
        createdDocumentDetails.getDocumentActionsPanel().clickOnAction(DocumentActions.DECLARE_AS_RECORD);

         // check the created record available actions
        assertTrue(actionsPanel.isActionClickable(RecordActions.DOWNLOAD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.EDIT_METADATA));
        assertTrue(actionsPanel.isActionClickable(RecordActions.HIDE_RECORD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.MOVE_INPLACE));

        // check some of the created record properties
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(createdInplaceRecord));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.CREATOR).equals(COLLABORATOR));

        // check the record location is still in Document Library by clicking on its "Documents" container
        documentDetails.clickOnParentInBreadcrumb("Documents", documentLibrary);

        // hide the uploaded record
        documentLibrary.getInplaceRecord(uploadedInplaceRecord).hideRecordFromDocumentLibrary(documentLibrary);

        // hide the created record
        documentLibrary.getInplaceRecord(createdInplaceRecord).hideRecordFromDocumentLibrary(documentLibrary);

        // check the records are not visible anymore in Document Library
        assertNull("The uploaded record is still visible after being hidden.", documentLibrary.getInplaceRecord(uploadedInplaceRecord));
        assertNull("The created record is still visible after being hidden.", documentLibrary.getInplaceRecord(createdInplaceRecord));

         String encodedRMAdminUser = RM_ADMIN.replaceAll(" ", "");
        // log in with the RM admin user
        openPage(encodedRMAdminUser, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");

        // check the hidden records above are displayed in Unfiled Records
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(uploadedInplaceRecord));
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(createdInplaceRecord));

        // delete the records
        filePlanPanel.clickOnUnfiledRecords().getRecord(uploadedInplaceRecord).clickOnDelete().clickOnConfirm();
        filePlanPanel.clickOnUnfiledRecords().getRecord(createdInplaceRecord).clickOnDelete().clickOnConfirm();

        // check the records have been successfully deleted
        assertNull("The uploaded inplace record could not be deleted.",filePlanPanel.clickOnUnfiledRecords().getRecord(uploadedInplaceRecord));
        assertNull("The created inplace record could not be deleted.", filePlanPanel.clickOnUnfiledRecords().getRecord(createdInplaceRecord));
    }      
}