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
import org.alfresco.po.rm.actions.edit.EditRecordFolderPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.FolderActions;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.folder.FolderActionsPanel;
import org.alfresco.po.rm.details.folder.FolderDetailsPage;
import org.alfresco.po.rm.details.record.PropertiesPanel;
import org.alfresco.po.rm.details.record.PropertiesPanel.Properties;
import org.alfresco.po.rm.dialog.GeneralConfirmationDialog;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.po.share.properties.Content;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * CreateFolders test
 * 
 * @author Oana Nechiforescu
 */
public class CreateFolders extends BaseTest
{  
    /** data prep services */
    @Autowired private RecordsManagementService service;
    
    @Autowired
    private FilePlan filePlan;  
    
    @Autowired
    private FolderDetailsPage folderDetailsPage;

    @Autowired
    private EditRecordFolderPage editRecordFolderPage;
    
    @Autowired
    private NewRecordFolderDialog newFolderDialog;

    @Autowired
    private GeneralConfirmationDialog confirmationDialog;
    
    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2757")
    public void createFolders()
    {     
        String category1 = "RM-2757 category1";
        String category2 = "RM-2757 category2";
        String folder1 = "folder 1 RM-2757";
        String editedFolder1 = "edited folder 1 RM-2757";
        String editedFolder1Title = "edited title folder 1 RM-2757";
        String editedFolder1Description = "edited description folder 1 RM-2757";
        
        // create "rm admin" user if it does not exist and assign it to RM Administrator role
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);

        // log in with the RM admin user
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");

        // create category 1
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(category1)
            .setTitle(TITLE)
            .clickOnSave();

        // create category 2
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(category2)
            .setTitle(TITLE)
            .clickOnSave();

        // navigate inside category 1
        filePlan.getRecordCategory(category1).clickOnLink();
        
        // open the new folder dialog
        newFolderDialog = filePlan.getToolbar().clickOnNewRecordFolder();
        // check required and optional fields
        assertTrue(newFolderDialog.isNameRequired());
        assertTrue(newFolderDialog.isTitleRequired());
        assertFalse(newFolderDialog.isDescriptionRequired());
        assertTrue(newFolderDialog.getIdentifierAndVitalInformation().isIdentifierRequired());
        
        // create the folder by completing the required fields
        newFolderDialog.setName(folder1);
        newFolderDialog.setTitle(TITLE);
        // change the default generated folder identifier
        newFolderDialog.getIdentifierAndVitalInformation().setIdentifier(folder1); 
        newFolderDialog.clickOnSave();

        // verify that the folder has been successfully created
        assertNotNull(filePlan.getRecordFolder(folder1));
        
        // check the folder's actions
        assertNull(filePlan.getRecordFolder(folder1).getUnclickableActions(
                FolderActions.COPY, FolderActions.DELETE, FolderActions.EDIT_METADATA, FolderActions.MANAGE_PERMISSIONS,
                FolderActions.MANAGE_RULES, FolderActions.MOVE, FolderActions.VIEW_AUDIT,
                FolderActions.VIEW_DETAILS, FolderActions.CLOSE_FOLDER));
        
        // go to folder 1 Details page
        folderDetailsPage = filePlan.getRecordFolder(folder1).clickOnViewDetails();
       
        // check the available actions from folder's details page
        FolderActionsPanel folderActions = folderDetailsPage.getFolderActionsPanel();
        assertFalse(folderActions.isActionAvailable(FolderActions.VIEW_DETAILS));
        assertTrue(folderActions.isActionsClickable(
                FolderActions.COPY, FolderActions.DELETE, FolderActions.EDIT_METADATA, FolderActions.MANAGE_PERMISSIONS,
                FolderActions.MANAGE_RULES, FolderActions.MOVE, FolderActions.VIEW_AUDIT, FolderActions.CLOSE_FOLDER));
        
        // check some folder's properties       
        assertEquals(PropertiesPanel.getPropertyValue(Properties.NAME), folder1);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.TITLE), TITLE);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.MIMETYPE_OR_IDENTIFIER), folder1);

        // edit some folder's properties
        folderDetailsPage.getFolderActionsPanel().clickOnAction(FolderActions.EDIT_METADATA, editRecordFolderPage);
        Content properties = editRecordFolderPage.getContent();
        properties.setNameValue(editedFolder1);
        properties.setTitle(editedFolder1Title);
        properties.setDescription(editedFolder1Description);
        editRecordFolderPage.clickOnSave();

        // navigate to category 1 level
        openPage(RM_ADMIN, DEFAULT_PASSWORD,filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", category1));

        // check the edited folder properties
        filePlan.getRecordFolder(editedFolder1).clickOnViewDetails();
        assertEquals(PropertiesPanel.getPropertyValue(Properties.NAME), editedFolder1);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.TITLE), editedFolder1Title);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.DESCRIPTION), editedFolder1Description);

        // navigate back to category 1 level
        folderDetailsPage.navigateUp();
        filePlan.navigateUp();
        // copy edited folder 1 to category 1 and category 2
        filePlan.getRecordFolder(editedFolder1).clickOnCopyTo().select(category1).clickOnCopy();
        filePlan.getRecordFolder(editedFolder1).clickOnCopyTo().select(category2).clickOnCopy();

        // check that the copy of the folder is displayed in category 1
        assertNotNull(filePlan.getRecordFolder("Copy of " + editedFolder1));

        // navigate to File Plan browse view level
        filePlan.navigateUp();

        // navigate inside category 2 and check edited folder is there
        filePlan.getRecordCategory(category2).clickOnLink();
        assertNotNull(filePlan.getRecordFolder(editedFolder1));

        // delete the edited folder 1 from category 2
        filePlan.getRecordFolder(editedFolder1).clickOnDelete().clickOnConfirm();
        assertNull(filePlan.getRecordFolder(editedFolder1));

        // navigate to File Plan browse view level
        filePlan.navigateUp();

        // navigate inside category 1
        filePlan.getRecordCategory(category1).clickOnLink();
        // move edited folder 1 to category 2 from category 1
        filePlan.getRecordFolder(editedFolder1).clickOnMoveTo().select(category2).clickOnMove();
        // check the folder is not displayed in category 1 anymore
        assertNull(filePlan.getRecordFolder(editedFolder1));

        // navigate to File Plan browse view level
        filePlan.navigateUp();
        // navigate inside category 2
        filePlan.getRecordCategory(category2).clickOnLink();
        // check the folder is displayed in category 2
        assertNotNull(filePlan.getRecordFolder(editedFolder1));

        // navigate to File Plan browse view level
        filePlan.navigateUp();
        // move category 2 to category 1
        filePlan.getRecordCategory(category2).clickOnMoveTo().select(category1).clickOnMove();
        // check that category 2 is not displayed anymore in File Plan
        assertNull(filePlan.getRecordCategory(category2));

        // navigate inside category 1 and check that category 2 has been moved there
        filePlan.getRecordCategory(category1).clickOnLink();
        assertNotNull(filePlan.getRecordCategory(category2));

        // navigate inside category 2 to edited folder 1 and close folder
        filePlan.getRecordCategory(category2).clickOnLink();
        filePlan.getRecordFolder(editedFolder1).clickOnAction(FolderActions.CLOSE_FOLDER);

        // check the available actions of a closed folder
        assertNull(filePlan.getRecordFolder(editedFolder1).getUnclickableActions(FolderActions.VIEW_DETAILS,
                FolderActions.COPY,
                FolderActions.VIEW_AUDIT, FolderActions.MANAGE_RULES,
                FolderActions.MANAGE_PERMISSIONS, FolderActions.REOPEN_FOLDER));

        // navigate to File Plan Level and delete the root category 1
        filePlan.navigateUp();
        filePlan.navigateUp();
        filePlan.getRecordCategory(category1).clickOnDelete().clickOnConfirm();
        assertNull(filePlan.getRecordCategory(category1));
    }
}