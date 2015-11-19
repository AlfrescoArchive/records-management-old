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
import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.po.rm.browse.fileplan.CategoryActions;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import static org.alfresco.test.TestData.DEFAULT_EMAIL;
import static org.alfresco.test.TestData.DEFAULT_PASSWORD;
import static org.alfresco.test.TestData.LAST_NAME;
import static org.alfresco.test.TestData.RM_ADMIN;
import static org.alfresco.test.TestData.TITLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * CreateCategories test
 * 
 * @author Oana Nechiforescu
 */
public class CreateCategories extends BaseTest
{
    /** data prep services */
    @Autowired private RecordsManagementService service;
   
    @Autowired
    private FilePlan filePlan;  
    
    @Autowired
    private RecordCategory recordCategoryLevel;
    
    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2756")
    public void createCategories() 
    {     
        String category1 = "RM-2756 category1";
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
        
        recordCategory1.clickOnCopyTo().clickOnCopy();
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
        
    }
    
    
}   
