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
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
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
    private UnfiledRecords unfiledRecords;
    
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
        openPage(COLLABORATOR, DEFAULT_PASSWORD, documentLibrary, COLLAB_SANITY_ID);
        Document uploadedDoc = documentLibrary.getDocument(uploadedInplaceRecord);
        String[] uploadedDocClickableActions = uploadedDoc.getClickableActions();
       
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertTrue(Arrays.asList(uploadedDocClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));
         
        Document createdDoc = documentLibrary.getDocument(createdInplaceRecord);
        String[] createdDocClickableActions = createdDoc.getClickableActions();
        assertTrue(Arrays.asList(createdDocClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertFalse(Arrays.asList(createdDocClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertFalse(Arrays.asList(createdDocClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));
        
        uploadedDoc.clickOnAction(DocumentActions.DECLARE_AS_RECORD);
        
        String[] uploadedRecordClickableActions = documentLibrary.getInplaceRecord(uploadedInplaceRecord).getClickableActions();
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.DECLARE_AS_RECORD));
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.DECLARE_VERSION_AS_RECORD));
        assertFalse(Arrays.asList(uploadedRecordClickableActions).contains(DocumentActions.AUTO_DECLARE_OPTIONS));
        
        documentLibrary.getInplaceRecord(uploadedInplaceRecord).clickOnLink(documentDetails);
        assertTrue("The in-place record text file preview is not available", documentDetails.isPreviewAvailable());
        DocumentActionsPanel actionsPanel = documentDetails.getDocumentActionsPanel();
        
        assertTrue(actionsPanel.isActionClickable(RecordActions.DOWNLOAD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.EDIT_METADATA));
        assertTrue(actionsPanel.isActionClickable(RecordActions.HIDE_RECORD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.MOVE_INPLACE));
        
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(uploadedInplaceRecord));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.CREATOR).equals(COLLABORATOR));
      
        DocumentLibrary docLibrary = documentDetails.navigateUpToDocumentsBrowseView();
       
        DocumentDetails createdDocumentDetails = docLibrary.getDocument(createdInplaceRecord).clickOnLink(documentDetails);
        createdDocumentDetails.getDocumentActionsPanel().clickOnAction(DocumentActions.DECLARE_AS_RECORD);

        assertTrue(actionsPanel.isActionClickable(RecordActions.DOWNLOAD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.EDIT_METADATA));
        assertTrue(actionsPanel.isActionClickable(RecordActions.HIDE_RECORD));
        assertTrue(actionsPanel.isActionClickable(RecordActions.MOVE_INPLACE));  
               
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).startsWith(createdInplaceRecord));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.CREATOR).equals(COLLABORATOR));
        
        documentDetails.navigateUpToDocumentsBrowseView();
        
        documentLibrary.getInplaceRecord(uploadedInplaceRecord).clickOnAction(RecordActions.HIDE_RECORD, confirmationDialog).confirm();
        documentLibrary.render();
       
        documentLibrary.getInplaceRecord(createdInplaceRecord).clickOnAction(RecordActions.HIDE_RECORD, confirmationDialog).confirm();
        documentLibrary.render();
        
        assertNull("The uploaded record is still visible after being hidden.", documentLibrary.getInplaceRecord(uploadedInplaceRecord));
        assertNull("The created record is still visible after being hidden.", documentLibrary.getInplaceRecord(createdInplaceRecord)); 
        
        openPage(getAdminName(), getAdminPassword(), filePlan, RM_SITE_ID, "documentlibrary");
        
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(uploadedInplaceRecord));
        assertNotNull(filePlanPanel.clickOnUnfiledRecords().getRecord(createdInplaceRecord));
    }          
}