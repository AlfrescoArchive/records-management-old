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
package org.alfresco.po.share.page;

import java.util.concurrent.TimeUnit;

import org.alfresco.po.common.Page;
import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.annotations.WaitFor;
import org.alfresco.po.common.annotations.WaitForStatus;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.login.LoginPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;

/**
 * Share page implementation
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
public abstract class SharePage extends Page
{
    /** A lock used when accessing the {@link #currentLoggedInUser}. */
    public static final String CURRENT_LOGGED_IN_USER_LOCK = new String("CURRENT_LOGGED_IN_USER_LOCK");
    /**
     * The currently logged-in user. This is only suitable for testing against a single client, if we want to support
     * testing against a Selenium grid then we need to add some code to track which user is logged in on which client.
     */
    protected static String currentLoggedInUser;

    /** page footer */
    @WaitFor(status=WaitForStatus.VISIBLE)
    @FindBy(css="div.sticky-footer")
    private WebElement footer;

    /** share page navigation */
    @Autowired
    @RenderableChild
    private SharePageNavigation sharePageNavigation;

    /** login page */
    @Autowired
    private LoginPage loginPage;

    @Autowired
    private Message message;

    /**
     * Get share page navigation
     */
    public SharePageNavigation getSharePageNavigation()
    {
        return sharePageNavigation;
    }

    /**
     * @see org.alfresco.po.common.Page#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        if (!(getLastRendered() instanceof SharePage))
        {
            message.waitUntillHidden();
        }
        return super.render();
    }

    /**
     * Get the URL of the page
     */
    protected abstract String getPageURL(String ... context);

    /**
     * Open this page, login to Share if required.
     *
     * @param server        base server URL
     * @param userName      user name
     * @param password      password
     * @param context       context
     * @return {@link SharePage} rendered page
     */
    public SharePage open(String server, String userName, String password, String ... context)
    {
        // get the page URL
        String url = server + getPageURL(context);

        // This addresses the race condition of two threads logging out/in at the same time. It's a little bit pointless
        // as we only support testing against a single client anyway.
        synchronized(CURRENT_LOGGED_IN_USER_LOCK)
        {
            Reporter.log("Request to open page as "+userName+ " ("+password+"), previously "+currentLoggedInUser);
            // Logout if previously logged in as someone else.
            if (currentLoggedInUser != null && !userName.equals(currentLoggedInUser))
            {
                Utils.retryUntil(
                            () ->
                            {
                                Reporter.log("Attempting to log out");
                                try
                                {
                                    sharePageNavigation.openUserDropdownMenu().logout();
                                    Reporter.log("Log out attempt complete.");
                                }
                                catch (IllegalStateException e)
                                {
                                    Reporter.log("Failed to logout - assuming already logged out. Exception message: "
                                                + e.getMessage());
                                }
                                catch (Exception e)
                                {
                                    Reporter.log("Failed to logout with exception message: " + e.getMessage());
                                    throw e;
                                }
                                return null;
                            },
                            () ->
                            {
                                // Make sure we give the "Login" page a chance to load.
                                try
                                {
                                    Utils.waitFor(ExpectedConditions.titleContains("Login"));
                                }
                                catch (TimeoutException e)
                                {
                                    Reporter.log("Timed out waiting for title to contain 'Login'.");
                                }
                                String title = webDriver.getTitle();
                                Reporter.log("Title is now '" + title + "'");
                                return title.contains("Login");
                            },
                            3);

                String title = webDriver.getTitle();
                if (title.contains("Login"))
                {
                    currentLoggedInUser = null;
                    Reporter.log("Successfully logged out");
                }
                else
                {
                    Reporter.log("Failed to log out - title is now '" + title
                                + "'.  Assuming currentlyLoggedInUser is still " + currentLoggedInUser);
                }
            }

            Reporter.log("Opening "+url);

            // open the page
            webDriver.get(url);

            String title = webDriver.getTitle();
            Reporter.log("Opened "+url+ ". Title is '"+title+"'");

            // if redirected to the login page
            // TODO Replace this with a helper method like Utils.retryWhile().
            int numberOfRetries = 0;
            while (title.contains("Login") && numberOfRetries < 3)
            {
                Reporter.log("Logging in as "+userName);
                // login
                loginPage.render();
                // Wait for the FADEIN to finish (0.25 seconds).
                try
                {
                    TimeUnit.MILLISECONDS.sleep(250);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                loginPage.render();
                loginPage
                    .setUsername(userName)
                    .setPassword(password)
                    .clickOnLoginButton();

                title = webDriver.getTitle();
                if (title.contains("Login"))
                {
                    numberOfRetries += 1;
                    Reporter.log("Failed to log in as " + userName + ". Attempt number " + numberOfRetries
                                + ". New title is '" + title + "'");
                }
                else
                {
                    currentLoggedInUser = userName;
                    Reporter.log("Logged in as " + userName + ". New title is '" + title + "'");
                }
            }
        }
        Reporter.log("About to render page (currentlyLoggedInUser is now "+currentLoggedInUser+")");

        return this.render();
    }
}
