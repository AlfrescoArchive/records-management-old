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
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.browse.holds.Hold;
import org.alfresco.po.rm.browse.holds.Holds;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecordFolder;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.po.rm.site.RMSiteDashboard;
import org.alfresco.po.share.admin.usertrashcan.UserTrashcanPage;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.browse.documentlibrary.InplaceRecord;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * RMA-2665: Create File Plan
 * 
 * @author Roy Wetherall
 */
public class CreateFilePlan extends BaseTest
{
    /** rm site dashboard */
    @Autowired
    private RMSiteDashboard rmSiteDashboard;
    
    /** file plan browse view*/
    @Autowired
    private FilePlan filePlan;
    
    /** document library browse view */
    @Autowired
    private DocumentLibrary documentLibrary;
    
    /** unfiled records browse view */
    @Autowired
    private UnfiledRecords unfiledRecords;
    
    /** holds browse view */
    @Autowired
    private Holds holds;
    
    /** collab site dashboard */
    @Autowired
    private CollaborationSiteDashboard siteDashboard;
    
    /** user trashcan */
    @Autowired
    private UserTrashcanPage userTrashcan;
    
    /**
     * Regression test execution
     */
    @Test
    (
        groups = {"RMA-2665", "sanity"}, 
        description = "Create File Plan",
        dependsOnGroups = "RMA-2664"
    )
    public void createFilePlan()
    {
        // create collaboration site
        openPage(userDashboardPage)
            .getMySitesDashlet()
            .clickOnCreateSite()
            .setSiteName(COLLAB_SITE_NAME)
            .setSiteURL(COLLAB_SITE_ID)
            .setSiteDescription(DESCRIPTION)
            .clickOnOk();
        
        // upload document
        siteDashboard
            .getNavigation()
            .clickOnDocumentLibrary()
            .getToolbar()
            .clickOnFile()
            .uploadFile(DOCUMENT);
        
        // open the RM site and navigate to file planS
        openPage(userDashboardPage)
            .getMySitesDashlet().clickOnRMSite(RM_SITE_ID)
            .getNavigation().clickOnFilePlan(); 
        
        // create root category
        createCategoryAndClickOnLink(RECORD_CATEGORY_ONE, true);
        
        // create sub-category
        createCategoryAndClickOnLink(SUB_RECORD_CATEGORY_NAME, true);
        
        // create folder 2
        createRecordFolderAndClickOnLink(RECORD_FOLDER_TWO, false);
      
        // create folder 1
        createRecordFolderAndClickOnLink(RECORD_FOLDER_ONE, true);
        
        // create non electronic record in folder 1
        filePlan
            .getToolbar().clickOnFile()
            .clickOnNonElectronic()
            .setName(NON_ELECTRONIC_RECORD)               
            .setTitle(TITLE)
            .clickOnSave();
    
        // check that the record has been created
        Record nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertNotNull(nonElectronicRecord);
        
        // create electronic record in folder 1        
        filePlan
            .getToolbar().clickOnFile()
            .clickOnElectronic()
            .uploadFile(RECORD);
        
        // check that the record has been created
        Record record = filePlan.getRecord(RECORD);
        assertNotNull(record);    
      
        // declare in-place record
        declareInplaceRecord();
        
        // create unfiled record folder
        unfiledRecords
            .getToolbar().clickOnNewUnfiledRecordFolder()
            .setName(UNFILED_RECORD_FOLDER)
            .setTitle(TITLE)
            .clickOnSave();
        
        // check that the unfiled record folder has been created
        UnfiledRecordFolder unfiledRecordFolder = unfiledRecords.getList().get(UNFILED_RECORD_FOLDER, UnfiledRecordFolder.class);
        assertNotNull(unfiledRecordFolder);
        
        // create holds
        unfiledRecords.getFilterPanel().clickOnHolds();
        createHold(HOLD1);
        createHold(HOLD2);
    }
    
    /**
     * delete collaboration site
     */
    @AfterSuite
    protected void deleteCollaborationSite()
    {   
        MySitesDashlet mySites = openPage(userDashboardPage).getMySitesDashlet();
        
        // check for existance of site
        if (mySites.siteExists(COLLAB_SITE_ID))
        {       
            // delete site
            mySites.clickOnDeleteSite(COLLAB_SITE_ID);
            assertFalse(mySites.siteExists(COLLAB_SITE_ID));
            
            // open the user trash can and empty it
            openPage(userTrashcan, getAdminName())
                .clickOnEmpty()
                .clickOnConfirm(userTrashcan);
        }
    }

    /**
     * Create a category
     * 
     * @param categoryName  category name
     * @param clickOnLink   true if newly created record category link should be created, false otherwise
     */
    private void createCategoryAndClickOnLink(String categoryName, boolean clickOnLink)
    {
        // check that create category button is clickable
        assertTrue(filePlan.getToolbar().isNewCategoryClickable());
        
        // create new category
        filePlan
            .getToolbar()
            .clickOnNewCategory()
            .setName(categoryName)
            .setTitle(TITLE)
            .clickOnSave();
        
        // check that the newly created record category is in the display list
        RecordCategory recordCategory = filePlan.getRecordCategory(categoryName);
        assertNotNull(recordCategory);
        assertEquals(categoryName, recordCategory.getName());
        
        if (clickOnLink)
        {
            // click on link
            recordCategory.clickOnLink();
        }        
    }
    
    /** 
     * Create record folder
     * 
     * @param folderName    folder name
     * @param clickOnLink   click on created folder link if true, otherwise false
     */
    private void createRecordFolderAndClickOnLink(String folderName, boolean clickOnLink)
    {
        // check that create category button is clickable
        assertTrue(filePlan.getToolbar().isNewRecordFolderClickable());
        
        // open new record category dialog
        NewRecordFolderDialog dialog = filePlan.getToolbar().clickOnNewRecordFolder();
        
        // enter details of new record folder and click save
        dialog.setName(folderName)
               .setTitle(TITLE)
               .clickOnSave();
        
        // check that the newly created record folder is in the display list
        RecordFolder recordFolder = filePlan.getRecordFolder(folderName);
        assertNotNull(recordFolder);
        assertEquals(folderName, recordFolder.getName());
        
        if (clickOnLink)
        {
            // click on link
            recordFolder.clickOnLink();
        }           
    }
    
    /**
     * Declare inplace record
     */
    private void declareInplaceRecord()
    {
        // navigate to the collaboration site
        openPage(userDashboardPage)
           .getMySitesDashlet()
           .clickOnCollaborationSite(COLLAB_SITE_ID);
        
        // get document
        Document document = siteDashboard
                                        .getNavigation()
                                        .clickOnDocumentLibrary()
                                        .getList()
                                        .getByPartialName(DOCUMENT, Document.class);
        assertNotNull(document);
        String origionalName = document.getName();
        
        // declare the document as a record
        document.clickOnDeclareAsRecord();
        
        // refresh the document item
        InplaceRecord inplaceRecord = documentLibrary.getList().getByPartialName(DOCUMENT, InplaceRecord.class);
        
        // check that the document name has changed
        String recordName = inplaceRecord.getName();
        assertFalse(recordName.equals(origionalName));
        
        // naviagate to the unfiled records
        openPage(userDashboardPage)
            .getMySitesDashlet()
            .clickOnRMSite(RM_SITE_ID)
            .getNavigation().clickOnFilePlan()
            .getFilterPanel().clickOnUnfiledRecords(); 
        
        Record record = unfiledRecords.getList().get(recordName, Record.class);
        assertNotNull("Unfiled record not found.", record);
    }
    
    /**
     * Create hold
     *     
     * @param holdName  hold name
     */
    private void createHold(String holdName)
    {
        holds
            .getToolbar().clickOnNewHold()
            .setName(holdName)
            .setReason(REASON)
            .clickOnSave();
    
        Hold hold = holds.getList().get(holdName, Hold.class);
        assertNotNull(hold);
    }
}
