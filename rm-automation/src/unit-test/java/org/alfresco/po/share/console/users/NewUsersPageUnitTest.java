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

import java.util.UUID;

import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * New users page unit test
 * 
 * @author Roy Wetherall
 */
@Test (groups = {"unit-test"})
public class NewUsersPageUnitTest extends BaseTest
{
    /** new users page */
    @Autowired
    private NewUsersPage newUsersPage;
    
    /**
     * Open page before each test
     */
    @BeforeMethod
    public void openPage()
    {
        // open the users page
        openPage(newUsersPage);    
    }
    
    /**
     * Confirm that the expected elements of the page are present
     * when opened.
     */
    @Test
    public void confirmPageElements()
    {                
        // ensure all the fields are empty
        assertTrue(newUsersPage.getFirstName().isEmpty());
        assertTrue(newUsersPage.getLastName().isEmpty());
        assertTrue(newUsersPage.getEmail().isEmpty());
        assertTrue(newUsersPage.getUserName().isEmpty());
        assertTrue(newUsersPage.getPassword().isEmpty());
        assertTrue(newUsersPage.getVerifyPassword().isEmpty());
        
        // ensure the buttons are enabled/disabled accordingly
        assertTrue(newUsersPage.isEnabledCreateUser());
        assertTrue(newUsersPage.isEnabledCreateAndCreateAnother());
        assertTrue(newUsersPage.isEnabledCancel());
    }
    
    @Test(dependsOnMethods="confirmPageElements")
    public void clickOnCreateAndCreateAnother()
    {
        fillInUserDetails();
        assertNotNull(newUsersPage.clickOnCreateAndCreateAnother());
    }
    
    @Test(dependsOnMethods="clickOnCreateAndCreateAnother")
    public void clickOnCreateUser()
    {
        fillInUserDetails();
        UsersPage usersPage = newUsersPage.clickOnCreateUser();
        assertNotNull(usersPage);
    }
    
    @Test(dependsOnMethods="clickOnCreateUser")
    public void clickOnCancel()
    {
        UsersPage usersPage = newUsersPage.clickOnCancel();
        assertNotNull(usersPage);
    }
    
    private void fillInUserDetails()
    {
        newUsersPage.setFirstName(UUID.randomUUID().toString());
        newUsersPage.setLastName(UUID.randomUUID().toString());
        newUsersPage.setEmail(UUID.randomUUID().toString() + "@alfresco.com");
        newUsersPage.setUserName(UUID.randomUUID().toString());
        newUsersPage.setPassword("password");
        newUsersPage.setVerifyPassword("password");
    }
}
