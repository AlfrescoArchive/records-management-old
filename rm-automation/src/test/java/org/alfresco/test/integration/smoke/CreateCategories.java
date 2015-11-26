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
import org.alfresco.po.share.properties.Content;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

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

        createTestPrecondition();
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

        // check the available actions
        checkCategoryActions(recordCategory1.getClickableActions());

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

        // copy category 1 to category 2 and navigate to the copy of category 1 details page
        category1DetailsPage = copyCategory1ToCategory2(category1, category2).clickOnViewDetails();
        
        // check the category 1 available actions    
        checkCategoryActionsFromDetailsPage(category1DetailsPage.getCategoryActionsPanel());
              
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

        // check some of the edited category 1 properties
        assertEquals(PropertiesPanel.getPropertyValue(Properties.NAME), editedCategory1);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.TITLE), editedCategory1Title);
        assertEquals(PropertiesPanel.getPropertyValue(Properties.DESCRIPTION), editedCategory1Description);

        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        // navigate inside category 2
        filePlan.getRecordCategory(category2).clickOnLink(filePlan);

        // move category 1 edited copy to File Plan
        filePlan.getRecordCategory(editedCategory1).clickOnMoveTo().select("File Plan").clickOnMove();
        
        // check the category 1 edited copy is no more displayed in category 2
        assertNull(filePlan.getRecordCategory(editedCategory1));  
        
        // navigate to File Plan browse level
        filePlan.navigateUp();
        
        // check the edited copy of category 1 is displayed in File Plan
        assertNotNull(filePlan.getRecordCategory(editedCategory1));

        // delete categories
        deleteCategories(Arrays.asList(category1, category2, editedCategory1, "Copy of "+ category1));
    }

    private void createTestPrecondition()
    {
        // create "rm admin" user if it does not exist and assign it to RM Administrator role
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
    }

    private void checkCategoryActions(String[] actualActions)
    {
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.COPY));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.DELETE));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.EDIT_METADATA));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.MANAGE_PERMISSIONS));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.MANAGE_RULES));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.MOVE));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.VIEW_AUDIT));
        assertTrue(Arrays.asList(actualActions).contains(CategoryActions.VIEW_DETAILS));
    }

    private void checkCategoryActionsFromDetailsPage(CategoryActionsPanel categoryActionsPanel)
    {
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.EDIT_METADATA));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.MANAGE_PERMISSIONS));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.COPY));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.MOVE));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.DELETE));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.VIEW_AUDIT));
        assertTrue(categoryActionsPanel.isActionClickable(CategoryActions.MANAGE_RULES));

    }

    private RecordCategory copyCategory1ToCategory2(String category1Name, String category2Name)
    {
        // copy category 1 to category 2
        filePlan.getRecordCategory(category1Name).clickOnCopyTo().select(category2Name).clickOnCopy();

        // navigate inside category 2
        filePlan.getRecordCategory(category2Name).clickOnLink();

        // check category 1 is displayed
        RecordCategory recordCategory1Copy = filePlan.getRecordCategory(category1Name);
        assertNotNull(recordCategory1Copy);
        return recordCategory1Copy;
    }

    private void deleteCategories(List<String> categories)
    {
        for(String category : categories)
        {
            filePlan.getRecordCategory(category).clickOnAction(CategoryActions.DELETE, confirmationDialog).confirm();
            filePlan.render();
            assertNull(filePlan.getRecordCategory(category));
        }

    }
    
}   
