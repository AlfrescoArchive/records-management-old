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

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.UUID;

import org.alfresco.po.rm.console.usersandgroups.AddAuthorityDialog;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.share.console.users.NewUsersPage;
import org.alfresco.po.share.console.users.UserProfilePage;
import org.alfresco.po.share.console.users.UsersPage;
import org.alfresco.po.share.page.SharePage;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * Base class for all test classes. Each test class must extend this class.
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@ContextConfiguration(locations = {"classpath:rm-po-testContext.xml"})
@Listeners(ResourceTeardown.class)
public class BaseTest extends AbstractTestNGSpringContextTests implements TestData
{
    /** user dashboard page */
    @Autowired
    protected UserDashboardPage userDashboardPage;

    @Autowired
    private WebDriver webDriver;

    /** Module Properties */
    @Autowired
    private ModuleProperties moduleProperties;

    /** users page */
    @Autowired
    private UsersPage usersPage;

    /** new users page */
    @Autowired
    private NewUsersPage newUsersPage;

    /** users profile page */
    @Autowired
    private UserProfilePage userProfilePage;

    /** users and groups page */
    @Autowired
    private UsersAndGroupsPage usersAndGroupsPage;

    /**
     * Gets the module properties so that properties can be used in the classes
     *
     * @return {@link ModuleProperties} Gives the module properties
     */
    protected ModuleProperties getModuleProperties()
    {
        return moduleProperties;
    }

    /**
     * Gets the admin user name defined in the module.properties
     *
     * @return {@link String} The admin user name
     */
    protected String getAdminName()
    {
        return moduleProperties.getAdminName();
    }

    /**
     * Gets the admin password defined in the module.properties
     *
     * @return {@link String} The admin password
     */
    protected String getAdminPassword()
    {
        return moduleProperties.getAdminPassword();
    }

    /**
     * Before class
     */
    @BeforeClass(alwaysRun = true)
    public void beforeClass()
    {
        webDriver.manage().window().maximize();
    }

    /**
     * Helper to open page
     */
    protected <P extends SharePage> P openPage(P page)
    {
        return openPage(page, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * Helper to open page
     */
    protected <P extends SharePage> P openPage(P page, String ... context)
    {
        return openPage(getAdminName(), getAdminPassword(), page, context);
    }

    /**
     * Helper to open page
     */
    @SuppressWarnings("unchecked")
    protected <P extends SharePage> P openPage(String userName, String password, P page, String ... context)
    {
        return (P)page.open(moduleProperties.getShareURL(), userName, password, context);
    }

    /**
     * Generate random string suitable for property values
     */
    protected String generateText()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Helper method to create new users
     *
     * @param userName user name
     */
    protected void createUser(String userName)
    {
        createUser(userName, null);
    }

    /**
     * Helper method to create a new user with a specific RM role
     *
     * @param userName  user name
     * @param role      RM role
     */
    protected void createUser(String userName, String role)
    {
        // check that the user doesn't already exist
        if (!existsUser(userName))
        {
            // create the user
            openPage(newUsersPage)
                .setFirstName(userName)
                .setLastName(userName)
                .setEmail(userName + "@alfresco.com")
                .setUserName(userName)
                .setPassword(DEFAULT_PASSWORD)
                .setVerifyPassword(DEFAULT_PASSWORD)
                .clickOnCreateUser();

            // verify that user has been created
            // keep trying until SOLR has updated it's index!
            // TODO limit the retry
            usersPage.setSearch(userName).clickOnSearch();
            while(!usersPage.isUserFound(userName))
            {
                usersPage
                    .setSearch(userName)
                    .clickOnSearch();
            }

            // if a role is provided
            if (role != null)
            {
                // add user to role
                AddAuthorityDialog addAuthorityDialog = openPage(usersAndGroupsPage)
                    .selectRole(role)
                    .clickOnAddUser()
                    .setSearch(userName);

                // we may need to keep trying this until the SOLR index catches up
                addAuthorityDialog.clickOnSearch();
                while(addAuthorityDialog.isResultsEmpty())
                {
                    // wait and try again
                    try{Thread.sleep(1000);}catch(Exception exception){};
                    addAuthorityDialog.clickOnSearch();
                }

                // add the new user
                addAuthorityDialog.clickOnAdd(userName);
            }
        }
    }

    /**
     * Helper method to determine whether the user already exists or not
     */
    protected boolean existsUser(String userName)
    {
        return openPage(usersPage)
            .setSearch(userName)
            .clickOnSearch()
            .isUserFound(userName);
    }

    /**
     * Helper method to delete a user
     *
     * @param userName  user name
     */
    protected void deleteUser(String userName)
    {
        // check that user exists
        if (existsUser(userName))
        {
            // delete the user
            openPage(userProfilePage, userName)
                .clickOnDeleteUser()
                .clickOnConfirm();
        }
    }

    /**
     * Helper to improve the usefulness of the reported error when arrays are compared
     *
     * @param expected  expected array
     * @param actual    actual array
     */
    protected void compareArrays(String[] expected, String[] actual)
    {
        assertArrayEquals(
                "Expected " + Arrays.toString(expected) + ", but actual is " + Arrays.toString(actual),
                expected,
                actual);
    }
}
