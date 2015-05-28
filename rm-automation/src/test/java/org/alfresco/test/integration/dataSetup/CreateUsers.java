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

import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.test.BaseTest;
import org.testng.annotations.Test;

/**
 * Create the test users.
 *
 * @author tpage
 * @since 3.0
 */
public class CreateUsers extends BaseTest
{
    /**
     * Create users for use in tests.
     */
    @Test
    (
        groups = { "integration-dataSetup", "integration-dataSetup-users" },
        description = "Create users"
    )
    public void createUsers()
    {
        // create user
        createUser(USER1, UsersAndGroupsPage.ROLE_RM_MANAGER);
    }
}
