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
package org.alfresco.po.rm.console.usersandgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Users and groups page unit test
 * 
 * @author Roy Wetherall
 */
@Test (groups = {"unit-test"})
public class UsersAndGroupsPageUnitTest extends BaseRmUnitTest
{
    /** authority names */
    private static final String USER = "peter";
    private static final String GROUP = "EMAIL_CONTRIBUTORS";
    
    /** users and groups page */
    @Autowired
    private UsersAndGroupsPage usersAndGroupsPage;
    
    @BeforeClass
    public void beforeClass()
    {
        // create RM site
        createRMSite();
        
        // create users
        createUser(USER);        
    }
    
    /**
     * Before method
     */
    @BeforeMethod
    public void beforeMethod()
    {        
        // open users and groups page
        openPage(usersAndGroupsPage);
    }
    
    /**
     * Check initial state of page
     */
    @Test
    public void checkInitialState()
    {
        // check button states
        assertTrue(usersAndGroupsPage.isEnabledAddGroup());
        assertFalse(usersAndGroupsPage.isEnabledRemoveGroup());
        assertTrue(usersAndGroupsPage.isEnabledAddUser());
        assertFalse(usersAndGroupsPage.isEnabledRemoveUser());
        
        // check available roles
        List<String> roles = usersAndGroupsPage.getRoles();
        assertEquals(5, roles.size());
        assertTrue(roles.contains(UsersAndGroupsPage.ROLE_RM_ADMIN));
        assertTrue(roles.contains(UsersAndGroupsPage.ROLE_RM_MANAGER));
        assertTrue(roles.contains(UsersAndGroupsPage.ROLE_RM_POWER_USER));
        assertTrue(roles.contains(UsersAndGroupsPage.ROLE_RM_SECURITY_OFFICER));
        assertTrue(roles.contains(UsersAndGroupsPage.ROLE_RM_USER));
        
        // check selected role
        String selectedRole = usersAndGroupsPage.getSelectedRole();
        assertEquals(UsersAndGroupsPage.ROLE_RM_ADMIN, selectedRole);
        
        // check the role groups
        List<String> groups = usersAndGroupsPage.getRoleGroups();
        assertNotNull(groups);
        assertTrue(groups.isEmpty());
        assertNull(usersAndGroupsPage.getSelectedRoleGroup());
        
        // check the role users
        List<String> users = usersAndGroupsPage.getRoleUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertTrue(users.contains("admin"));                
        assertNull(usersAndGroupsPage.getSelectedRoleUser());
    }
    
    /**
     * Add group
     */
    @Test(dependsOnMethods="checkInitialState")
    public void addGroup()
    {
        // show that the group is not present
        assertFalse(usersAndGroupsPage.getRoleGroups().contains(GROUP));
        
        // add group
        usersAndGroupsPage
            .clickOnAddGroup()
            .search(GROUP)
            .clickOnAdd(GROUP);
     
        // check that the group has been added
        assertTrue(usersAndGroupsPage.getRoleGroups().contains(GROUP));
    }
    
    /**
     * Remove group
     */
    @Test(dependsOnMethods="addGroup")
    public void removeGroup()
    {
        // show the initial state
        assertTrue(usersAndGroupsPage.getRoleGroups().contains(GROUP));
        assertFalse(usersAndGroupsPage.isEnabledRemoveGroup());
        
        // select the group
        usersAndGroupsPage.selectRoleGroup(GROUP);
        assertTrue(usersAndGroupsPage.isEnabledRemoveGroup());        
        assertEquals(GROUP, usersAndGroupsPage.getSelectedRoleGroup());
        
        // remove the group
        usersAndGroupsPage
            .clickOnRemoveGroup()
            .clickOnConfirm(usersAndGroupsPage);
        
        // show that the group has been removed
        assertFalse(usersAndGroupsPage.getRoleGroups().contains(GROUP));    
        assertFalse(usersAndGroupsPage.isEnabledRemoveGroup());
    }
    
    /**
     * Add user
     */
    @Test(dependsOnMethods="removeGroup")
    public void addUser()
    {
        // show that the user is not present
        assertFalse(usersAndGroupsPage.getRoleUsers().contains(USER));
        
        // add user
        usersAndGroupsPage
            .clickOnAddUser()
            .search(USER)
            .clickOnAdd(USER);
        
       // check user has been added
        assertTrue(usersAndGroupsPage.getRoleUsers().contains(USER));
    }
    
    /**
     * Remove user
     */
    @Test(dependsOnMethods="addUser")    
    public void removeUser()
    {
        // show the initial state
        assertTrue(usersAndGroupsPage.getRoleUsers().contains(USER));
        assertFalse(usersAndGroupsPage.isEnabledRemoveUser());
        
        // select the user
        usersAndGroupsPage.selectRoleUser(USER);
        assertTrue(usersAndGroupsPage.isEnabledRemoveUser());        
        assertEquals(USER, usersAndGroupsPage.getSelectedRoleUser());
        
        // remove the user
        usersAndGroupsPage
            .clickOnRemoveUser()
            .clickOnConfirm();
        
        // show that the user has been removed
        assertFalse(usersAndGroupsPage.getRoleUsers().contains(USER));    
        assertFalse(usersAndGroupsPage.isEnabledRemoveUser());
    }
    
    /**
     * Select role
     */
    @Test(dependsOnMethods="removeUser")
    public void selectRole()
    {
        // check initial selection
        assertEquals(UsersAndGroupsPage.ROLE_RM_ADMIN, usersAndGroupsPage.getSelectedRole());
        
        // select power user
        usersAndGroupsPage.selectRole(UsersAndGroupsPage.ROLE_RM_POWER_USER);
        
        // show selection has changed
        assertEquals(UsersAndGroupsPage.ROLE_RM_POWER_USER, usersAndGroupsPage.getSelectedRole());
        assertTrue(usersAndGroupsPage.getRoleGroups().isEmpty());
        assertTrue(usersAndGroupsPage.getRoleUsers().isEmpty());
    }
    
    /**
     * Clean up
     */
    @AfterClass
    public void afterClass()
    {
        // delete user
        deleteUser(USER);
        
        // delete rm site
        deleteRMSite();
    }
}
