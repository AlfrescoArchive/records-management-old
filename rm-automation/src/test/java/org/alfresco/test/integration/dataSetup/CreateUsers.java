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
 * @since 2.4.a
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
                    RM_MANAGER,
                    RM_MANAGER);
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
                    UNCLEARED_USER,
                    UNCLEARED_USER);
    }

    @Test
    (
        groups = { "integration", "GROUP_SPACE_USER_EXISTS" },
        description = "Create users"
    )
    public void createSpaceUser()
    {
        userService.create(getAdminName(), getAdminPassword(), SPACE_USER, DEFAULT_PASSWORD, DEFAULT_EMAIL, SPACE_USER, SPACE_USER);
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
        // TODO This string replace should be removed when updating to dataprep 1.6+. A fix was added here:
        // https://github.com/AlfrescoTestAutomation/dataprep/commit/89a2db6f7d5a471b3f42ad8461b3021cc0ac9614
        String encodedSpaceUser = SPACE_USER.replaceAll(" ", "%20");
        if (userService.userExists(getAdminName(), getAdminPassword(), encodedSpaceUser))
        {
            userService.delete(getAdminName(), getAdminPassword(), encodedSpaceUser);
        }
    }
}
