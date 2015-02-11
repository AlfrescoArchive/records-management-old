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
package org.alfresco.po.share.console.users;

import static org.junit.Assert.*;

import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Users page unit test
 * 
 * @author Roy Wetherall
 */
@Test (groups = {"unit-test"})
public class UsersPageUnitTest extends BaseTest
{
    /** users page */
    @Autowired
    private UsersPage usersPage;
    
    /**
     * Open page before each test
     */
    @BeforeMethod
    public void openPage()
    {
        // open the users page
        openPage(usersPage);        
    }
    
    /**
     * Confirm that the expected elements of the page are present
     * when opened.
     */
    @Test
    public void confirmPageElements()
    {        
        // check that the buttons are enabled
        assertTrue(usersPage.isEnabledNewUser());
    }
    
    /**
     * Ensure that the new users page is opened when clicking on the new
     * user button
     */
    @Test(dependsOnMethods="confirmPageElements")
    public void clickOnNewUser()
    {
        // click on the new users button
        NewUsersPage newUsersPage = usersPage.clickOnNewUser();
        assertNotNull(newUsersPage);
    }
}
