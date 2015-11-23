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

import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.po.rm.actions.edit.EditRecordCategoryPage;
import org.alfresco.po.rm.browse.fileplan.CategoryActions;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.category.CategoryActionsPanel;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.details.record.PropertiesPanel;
import org.alfresco.po.rm.details.record.PropertiesPanel.Properties;
import org.alfresco.po.rm.dialog.GeneralConfirmationDialog;
import org.alfresco.po.rm.properties.CommonProperties;
import org.alfresco.po.share.properties.Content;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;


/**
 * CreateCategories test
 * 
 * @author Oana Nechiforescu
 */
public class CreateCategories extends BaseTest
{
    /** data prep service */
    @Autowired private RecordsManagementService service;

    @Autowired
    private FilePlan filePlan;
    @Autowired
    private EditRecordCategoryPage editRecordCategoryPage;
    @Autowired
    private CategoryDetailsPage category1DetailsPage;
    @Autowired
    private GeneralConfirmationDialog confirmationDialog;
    
    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2756")
    public void createCategories() 
    {     
        String category1 = "RM-2756 category1";
        String editedCategory1 = "edited RM-2756 category1";
        String editedCategory1Title = "category1 title";
        String editedCategory1Description = "this is the category 1 description";
        
        String category2 = "RM-2756 category2";
        // create "rm admin" user
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
        // log in with the RM admin user
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        // check that create category button is clickable
        assertTrue(filePlan.getToolbar().isNewCategoryClickable());

        // create category 1
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(category1)
            .setTitle(TITLE)
            .setDescription(DESCRIPTION)
            .clickOnSave();

        // check that the newly created record category 1 is in the display list
        RecordCategory recordCategory1 = filePlan.getRecordCategory(category1);
        assertNotNull(recordCategory1);
        assertEquals(category1, recordCategory1.getName());
        
        String[] category1Actions = recordCategory1.getClickableActions();
   
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.COPY));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.DELETE));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.EDIT_METADATA));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.MANAGE_PERMISSIONS));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.MANAGE_RULES));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.MOVE));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.VIEW_AUDIT));
        assertTrue(Arrays.asList(category1Actions).contains(CategoryActions.VIEW_DETAILS));
        
        // copy category 1 to File Plan
        recordCategory1.clickOnCopyTo().clickOnCopy();
        // check that a copy of the category has been created
        assertNotNull(filePlan.getRecordCategory("Copy of " + category1));
        
        // create category 2
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(category2)
            .setTitle(TITLE)
            .setDescription(DESCRIPTION)    
            .clickOnSave();
        
        // check that the newly created record category 2 is in the display list
        RecordCategory recordCategory2 = filePlan.getRecordCategory(category2);
        assertNotNull(recordCategory2);
        assertEquals(category2, recordCategory2.getName());
        
        // copy category 1 to category 2
        filePlan.getRecordCategory(category1).clickOnCopyTo().select(category2).clickOnCopy();
        
        // navigate inside category 2
        filePlan.getRecordCategory(category2).clickOnLink();
        
        // check category 1 is displayed
        RecordCategory recordCategory1Copy = filePlan.getRecordCategory(category1);
        assertNotNull(recordCategory1Copy);
        
        // navigate to category 1 details page
        category1DetailsPage = recordCategory1Copy.clickOnViewDetails();
        
        // check the category 1 available actions    
        CategoryActionsPanel actionsPanel = category1DetailsPage.getCategoryActionsPanel();
        assertTrue(actionsPanel.isActionClickable(CategoryActions.EDIT_METADATA));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.MANAGE_PERMISSIONS));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.COPY));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.MOVE));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.DELETE));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.VIEW_AUDIT));
        assertTrue(actionsPanel.isActionClickable(CategoryActions.MANAGE_RULES));
              
        // check some of category's properties  
        assertTrue(PropertiesPanel.getPropertyValue(Properties.NAME).equals(category1));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.TITLE).equals(TITLE));
        assertTrue(PropertiesPanel.getPropertyValue(Properties.DESCRIPTION).equals(DESCRIPTION));
        
        // edit category1's name, title and description 
        category1DetailsPage.getCategoryActionsPanel().clickOnAction(CategoryActions.EDIT_METADATA, editRecordCategoryPage);
        Content properties = editRecordCategoryPage.getContent();
        properties.setNameValue(editedCategory1);
        properties.setTitle(editedCategory1Title);
        properties.setDescription(editedCategory1Description);
        editRecordCategoryPage.saveChanges(category1DetailsPage);

        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        // navigate inside category 2
        filePlan.getRecordCategory(category2).clickOnLink();

        // move category 1 edited copy to File Plan
        filePlan.getRecordCategory(editedCategory1).clickOnMoveTo().select("File Plan").clickOnMove();
        
        // check the category 1 edited copy is no more displayed in category 2
        assertNull(filePlan.getRecordCategory(editedCategory1));  
        
        // navigate to File Plan browse level
        filePlan.navigateUp();
        
        // check the edited copy of category 1 is displayed in File Plan
        assertNotNull(filePlan.getRecordCategory(editedCategory1));

        // delete category 1
        filePlan.getRecordCategory(category1).clickOnAction(CategoryActions.DELETE,confirmationDialog).confirm();
        filePlan.render();
        // delete category 2
        filePlan.getRecordCategory(category2).clickOnAction(CategoryActions.DELETE,confirmationDialog).confirm();
        filePlan.render();
        // delete edited copy of category 1 
        filePlan.getRecordCategory(editedCategory1).clickOnAction(CategoryActions.DELETE, confirmationDialog).confirm();
        filePlan.render();
        // delete initial copy of category 1
        filePlan.getRecordCategory("Copy of " + category1).clickOnAction(CategoryActions.DELETE, confirmationDialog).confirm();
        filePlan.render();

        // check the categories have been successfully deleted
        assertNull(filePlan.getRecordCategory(category1));  
        assertNull(filePlan.getRecordCategory(category2));  
        assertNull(filePlan.getRecordCategory(editedCategory1));       
    }
    
    
}   
