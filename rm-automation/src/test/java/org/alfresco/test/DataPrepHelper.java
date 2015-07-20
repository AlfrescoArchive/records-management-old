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
package org.alfresco.test;

import org.alfresco.dataprep.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 * @since 3.0.a
 */
@Component
public class DataPrepHelper implements TestData
{
    @Autowired private ModuleProperties moduleProperties;

    /** data prep services */
    @Autowired private UserService userService;

    /**
     * Helper method to create user if it doesn't already exist.
     */
    public void createUser(String userName)
    {
        if (!userService.userExists(
                    moduleProperties.getAdminName(),
                    moduleProperties.getAdminPassword(),
                    userName))
        {
            userService.create(
                        moduleProperties.getAdminName(),
                        moduleProperties.getAdminPassword(),
                        userName,
                        DEFAULT_PASSWORD,
                        DEFAULT_EMAIL,
                        FIRST_NAME,
                        LAST_NAME);
        }
    }

    /**
     * Helper method to delete user if it exists.
     */
    public void deleteUser(String userName)
    {
        if (userService.userExists(
                    moduleProperties.getAdminName(),
                    moduleProperties.getAdminPassword(),
                    userName))
        {
            userService.delete(
                        moduleProperties.getAdminName(),
                        moduleProperties.getAdminPassword(),
                        userName);
        }
    }
}
