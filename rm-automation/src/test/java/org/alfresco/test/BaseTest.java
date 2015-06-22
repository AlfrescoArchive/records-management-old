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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.util.Arrays;
import java.util.UUID;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FatalStartupException;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.console.usersandgroups.AddAuthorityDialog;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.share.console.users.NewUsersPage;
import org.alfresco.po.share.console.users.UserProfilePage;
import org.alfresco.po.share.console.users.UsersPage;
import org.alfresco.po.share.page.SharePage;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/**
 * Base class for all test classes. Each test class must extend this class.
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@ContextConfiguration(locations = {"classpath:rm-po-testContext.xml"})
@Listeners({ResourceTeardown.class, ScreenshotListener.class})
public class BaseTest extends AbstractTestNGSpringContextTests implements TestData
{
    /** user dashboard page */
    @Autowired
    protected UserDashboardPage userDashboardPage;

    /** Is WireMock enabled? */
    private static final boolean WIREMOCK_ENABLED = false;
    private static final String WIREMOCK_PATH = "wiremock";

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

    /** Wiremock **/
    // TODO: @autowire these?
    private WireMockServer wireMockServer;

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
     * Create a path from a page and folder structure.
     *
     * @param page The page that acts as the root folder.
     * @param pathComponents The folders in the order they should be navigated.
     * @return The path that can be used as part of the URL when opening a page.
     */
    protected String createPathFrom(String page, String... pathComponents)
    {
        StringBuilder path = new StringBuilder(page + "#filter=path|%2F");
        for (String pathComponent : pathComponents)
        {
            path.append("%2F" + pathComponent);
        }
        path.append("|");
        return path.toString();
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
            //
            // We use a rather arbitrary retry maximum here just to ensure that the retry doesn't run forever.
            Utils.retryUntil(() -> usersPage.setSearch(userName)
                                            .clickOnSearch(),
                             () -> usersPage.isUserFound(userName), 25);

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
                    try{Thread.sleep(1000);}catch(Exception exception){}
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

    /**
     * Helper method to take screenshots
     *
     * @param screenshotName The name of the screenshot
     * @throws IOException if the source or destination is invalid or if an IO error occurs during copying
     */
    protected void takeScreenshot(String screenshotName) throws IOException
    {
        if (isBlank(screenshotName))
        {
            throw new IllegalArgumentException("Screenshot name cannot be blank.");
        }

        WebDriver augmentedDriver = new Augmenter().augment(webDriver);
        File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        File destination = new File("target/screenshots/" + screenshotName + ".png");
        copyFile(screenshot, destination);
    }

    /**
     * WireMock config:
     */
    @BeforeSuite
    public void setUpWiremock()
    {
        if (WIREMOCK_ENABLED)
        {
            try
            {
                wireMockServer = new WireMockServer(wireMockConfig().usingFilesUnderClasspath(WIREMOCK_PATH));
                wireMockServer.start();
            }
            catch (FatalStartupException fatalStartupException)
            {
                // BindException is thrown if the address is already in use. i.e. if a repo is running.
                // Assume that BindExceptions are caused by already having a repo running.
                // If the repo is running, we'll use that.
                Throwable exception = fatalStartupException;
                boolean rethrow = true;
                while (exception.getCause() != null)
                {
                    exception = exception.getCause();
                    if (exception instanceof BindException)
                    {
                        rethrow = false;
                        break;
                    }
                }
                if (rethrow)
                {
                    throw fatalStartupException;
                }
            }
        }
    }

    @AfterSuite
    public void tearDownWiremock() throws InterruptedException
    {
        if (wireMockServer != null && wireMockServer.isRunning())
        {
            wireMockServer.stop();
        }
    }

}
