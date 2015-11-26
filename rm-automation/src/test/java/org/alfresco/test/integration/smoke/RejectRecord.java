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
import org.alfresco.po.rm.browse.FilePlanFilterPanel;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.dialog.GeneralConfirmationDialog;
import org.alfresco.po.rm.dialog.RejectedRecordInformationDialog;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.alfresco.test.DataPrepHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * RejectRecord test
 *
 * @author Oana Nechiforescu
 */
public class RejectRecord extends BaseTest
{
    /** document library browse view */
    @Autowired
    private DocumentLibrary documentLibrary;

    /** document details page */
    @Autowired
    private DocumentDetails documentDetails;

    @Autowired
    private UnfiledRecords unfiledRecords;

    @Autowired
    private FilePlan filePlan;

    @Autowired
    private FilePlanFilterPanel filePlanPanel;

    @Autowired
    private GeneralConfirmationDialog confirmationDialog;

    @Autowired
    private RejectedRecordInformationDialog rejectedRecordInformationDialog;

    /** data prep services */
    @Autowired private RecordsManagementService service;
    @Autowired private DataPrepHelper dataPrep;

    String rejectedUploadedInplaceRecord = "rejected uploaded in-place record";
    String rejectedCreatedInplaceRecord = "rejected created in-place record";
    String defaultReason = "default reason";

    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira="RM-2748")
    public void rejectRecords() {
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
                .uploadFile(rejectedUploadedInplaceRecord);

        // create document
        documentLibrary
                .getToolbar()
                .clickOnCreateTextFile()
                .createTextFile(rejectedCreatedInplaceRecord, "default content").clickOnParentInBreadcrumb("Documents", documentLibrary);

        // declare the uploaded document as record
        documentLibrary.getDocument(rejectedUploadedInplaceRecord).clickOnDeclareAsRecord();
        // hide it from Document Library
        documentLibrary.getInplaceRecord(rejectedUploadedInplaceRecord).hideRecordFromDocumentLibrary(documentLibrary);

        // declare the created document as record
        documentLibrary.getDocument(rejectedCreatedInplaceRecord).clickOnDeclareAsRecord();
        // hide it from Document Library
        documentLibrary.getInplaceRecord(rejectedCreatedInplaceRecord).hideRecordFromDocumentLibrary(documentLibrary);

        // log in with rm admin and reject both documents
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        unfiledRecords = filePlan.getFilterPanel().clickOnUnfiledRecords();
        unfiledRecords.rejectRecordWithReason(rejectedUploadedInplaceRecord, defaultReason, unfiledRecords);
        unfiledRecords.rejectRecordWithReason(rejectedCreatedInplaceRecord, defaultReason, unfiledRecords);

        // log in with collaborator user
        openPage(COLLABORATOR, DEFAULT_PASSWORD, documentLibrary, COLLAB_SANITY_ID);

        Document rejectedCreatedDocument = documentLibrary.getDocument(rejectedCreatedInplaceRecord);
        Document rejectedUploadedDocument = documentLibrary.getDocument(rejectedUploadedInplaceRecord);

        String[] rejectedCreatedRecordActions = rejectedCreatedDocument.getClickableActions();
        String[] rejectedUploadedRecordActions = rejectedUploadedDocument.getClickableActions();

        // check the declare as record option is not available
        assertFalse(Arrays.asList(rejectedCreatedRecordActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertFalse(Arrays.asList(rejectedUploadedRecordActions).contains(DocumentActions.DECLARE_AS_RECORD));

        // check the records have the rejected banner displayed
        assertTrue(rejectedCreatedDocument.isRejectedBannerDisplayed());
        assertTrue(rejectedUploadedDocument.isRejectedBannerDisplayed());

        // check the rejection information for created record
        rejectedRecordInformationDialog = rejectedCreatedDocument.seeInfoAboutRejection();
        assertTrue(rejectedRecordInformationDialog.getDescription().contains(rejectedCreatedInplaceRecord));
        assertEquals(RM_ADMIN, rejectedRecordInformationDialog.getByUser());
        assertEquals(defaultReason, rejectedRecordInformationDialog.getReason());
        rejectedRecordInformationDialog.close();

        // check the rejection information for uploaded record
        rejectedRecordInformationDialog = rejectedUploadedDocument.seeInfoAboutRejection();
        assertTrue(rejectedRecordInformationDialog.getDescription().contains(rejectedUploadedInplaceRecord));
        assertEquals(RM_ADMIN, rejectedRecordInformationDialog.getByUser());
        assertEquals(defaultReason, rejectedRecordInformationDialog.getReason());
        rejectedRecordInformationDialog.close();

        // remove the rejected warning for both records
        documentLibrary.getDocument(rejectedCreatedInplaceRecord).removeRejectedBanner();
        documentLibrary.getDocument(rejectedUploadedInplaceRecord).removeRejectedBanner();
        documentLibrary.render();
        // check the records do not have the rejected banner displayed anymore
        assertFalse(documentLibrary.getDocument(rejectedCreatedInplaceRecord).isRejectedBannerDisplayed());
        assertFalse(documentLibrary.getDocument(rejectedUploadedInplaceRecord).isRejectedBannerDisplayed());

        String[] newCreatedRecordActions = documentLibrary.getDocument(rejectedCreatedInplaceRecord).getClickableActions();
        String[] newUploadedRecordActions =  documentLibrary.getDocument(rejectedUploadedInplaceRecord).getClickableActions();

        // check the declare as record option is now available
        assertTrue(Arrays.asList(newCreatedRecordActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertTrue(Arrays.asList(newUploadedRecordActions).contains(DocumentActions.DECLARE_AS_RECORD));

        // delete the documents
        documentLibrary.getDocument(rejectedCreatedInplaceRecord).clickOnDelete().confirm();
        documentLibrary.render();
        assertNull(documentLibrary.getDocument(rejectedCreatedInplaceRecord));
        documentLibrary.getDocument(rejectedUploadedInplaceRecord).clickOnDelete().confirm();
        documentLibrary.render();
        assertNull(documentLibrary.getDocument(rejectedUploadedInplaceRecord));
    }
}