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
package org.alfresco.test.integration.dataSetup;

import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Create the test users.
 *
 * @author tpage
 * @since 3.0
 */
public class CreateUsers extends BaseTest
{
    /** data prep services */
    @Autowired protected RecordsManagementService recordsManagementService;
    @Autowired protected UserService userService;
    
    /**
     * Create users for use in tests.
     */
    @Test
    (
        groups = { "integration", "GROUP_RM_MANAGER_EXISTS" },
        description = "Create users",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS" }
    )
    public void createRMManager()
    {
        recordsManagementService.createUserAndAssignToRole(
                    getAdminName(), 
                    getAdminPassword(), 
                    RM_MANAGER, 
                    DEFAULT_PASSWORD, 
                    DEFAULT_EMAIL, 
                    UsersAndGroupsPage.ROLE_RM_MANAGER,
                    FIRST_NAME,
                    LAST_NAME);
    }

    @Test
    (
        groups = { "integration", "GROUP_UNCLEARED_USER_EXISTS" },
        description = "Create users",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS" }
    )
    public void createUnclearedUser()
    {
        recordsManagementService.createUserAndAssignToRole(
                    getAdminName(), 
                    getAdminPassword(), 
                    UNCLEARED_USER, 
                    DEFAULT_PASSWORD, 
                    DEFAULT_EMAIL, 
                    UsersAndGroupsPage.ROLE_RM_MANAGER,
                    FIRST_NAME,
                    LAST_NAME);
    }

    /** delete users on test teardown */
    @AfterSuite(alwaysRun = true)
    protected void deleteUsers()
    {
        if (userService.userExists(getAdminName(), getAdminPassword(), RM_MANAGER))
        {
            userService.delete(getAdminName(), getAdminPassword(), RM_MANAGER);
        }
        if (userService.userExists(getAdminName(), getAdminPassword(), UNCLEARED_USER))
        {
            userService.delete(getAdminName(), getAdminPassword(), UNCLEARED_USER);
        }
    }
}
