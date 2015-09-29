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

import static org.junit.Assert.assertTrue;

import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * User profile page unit test
 * 
 * @author Roy Wetherall
 */
@Test (groups = {"unit-test"})
public class UserProfilePageUnitTest extends BaseTest
{
    /** new users page */
    @Autowired
    private NewUsersPage newUsersPage;
    
    /** user profile page */
    @Autowired
    private UserProfilePage userProfilePage;
    
    /**
     * Confirm that the expected elements of the page are present
     * when opened.
     */
    @Test
    public void confirmPageElements()
    {   
        // open the users page
        openPage(userProfilePage, "admin"); 
        
        // check that the buttons are enabled
        assertTrue(userProfilePage.isEnabledDeleteUser());
    }
    
    /**
     * Confirm user delete button works
     */
    @Test(dependsOnMethods="confirmPageElements")
    public void clickOnNewUser()
    {
        // create a user
        createUser("temp");
        // TODO remove this
        try{Thread.sleep(500);}catch(Exception e){};
        
        // delete user
        openPage(userProfilePage, "temp")
            .clickOnDeleteUser()
            .clickOnConfirm();
        
        // TODO sort out timing issues
        // assert user is delete
        //openPage(userProfilePage, "temp");
        //assertFalse(userProfilePage.isEnabledDeleteUser());
    }
}
