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

package org.alfresco.test.integration.legacy;

import java.util.Arrays;
import org.alfresco.po.rm.browse.FilePlanFilterPanel;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
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
import org.springframework.beans.factory.annotation.Autowired;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

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
    
    String uploadedInplaceRecord = "in-place record";
    String createdInplaceRecord = "created in-place record";
    
    @Test(
            groups = {"legacy"},
            dependsOnGroups = {"preconditionForSanity"}
    )
    @AlfrescoTest(jira="RM-2366")
    public void declareInplaceRecord() 
    {
        // log in with collaborator user
        openPage(COLLABORATOR, DEFAULT_PASSWORD, documentLibrary, COLLAB_SANITY_ID);
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
        assertTrue("The in-place record text file preview is not available", documentDetails.isPreviewAvailable());
       
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
        DocumentLibrary docLibrary = documentDetails.navigateUpToDocumentsBrowseView();
       
        // declare as record the created document from its Document Details page
        DocumentDetails createdDocumentDetails = docLibrary.getDocument(createdInplaceRecord).clickOnLink(documentDetails);
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
        documentDetails.navigateUpToDocumentsBrowseView();
        
        // hide the uploaded record
        documentLibrary.getInplaceRecord(uploadedInplaceRecord).clickOnAction(RecordActions.HIDE_RECORD, confirmationDialog).confirm();
        documentLibrary.render();
       
        // hide the created record
        documentLibrary.getInplaceRecord(createdInplaceRecord).clickOnAction(RecordActions.HIDE_RECORD, confirmationDialog).confirm();
        documentLibrary.render();
        
        // check the records are not visible anymore in Document Library
        assertNull("The uploaded record is still visible after being hidden.", documentLibrary.getInplaceRecord(uploadedInplaceRecord));
        assertNull("The created record is still visible after being hidden.", documentLibrary.getInplaceRecord(createdInplaceRecord)); 
        
        // log in with the RM admin user
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        
        // check the hiden records above are displayed in Unfiled Records
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(uploadedInplaceRecord));
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(createdInplaceRecord));
    }          
}