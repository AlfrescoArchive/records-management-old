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
    private static final String CURRENT_LOGGED_IN_USER_LOCK = new String("CURRENT_LOGGED_IN_USER_LOCK");
    /** This restricts the amount of effort that can be spent trying to access a URL. */
    private static final int MAXIMUM_URL_LOOP_RETRIES = 20;
    /**
     * The currently logged-in user. This is only suitable for testing against a single client, if we want to support
     * testing against a Selenium grid then we need to add some code to track which user is logged in on which client.
     */
    private static String currentLoggedInUser;

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
     * Wait for some length of time between iterations of the open URL loop. Don't wait at all for the first few loops,
     * then increase the wait time by a second each loop.
     *
     * @param iteration The iteration number.
     */
    private void iterationWait(int iteration)
    {
        if (iteration < 3)
        {
            return;
        }
        try
        {
            int duration = iteration - 3;
            Reporter.log("Waiting for " + duration + " seconds");
            TimeUnit.SECONDS.sleep(duration);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

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
        // Try the naive method of getting to the given URL.
        navigateToUrl(url);

        // This addresses the race condition of two threads logging out/in at the same time. It's a little bit pointless
        // as we only support testing against a single client anyway.
        synchronized(CURRENT_LOGGED_IN_USER_LOCK)
        {
            int iteration = 0;
            boolean navigationComplete = false;
            while (!navigationComplete && iteration < MAXIMUM_URL_LOOP_RETRIES)
            {
                iterationWait(iteration);

                String title = webDriver.getTitle();
                String currentUrl = webDriver.getCurrentUrl();
                Reporter.log("Time " + System.currentTimeMillis() + ": Currently at '" + currentUrl + "', with title '" + title + "' and current user " + currentLoggedInUser + "<br>");

                if (title.contains("Login"))
                {
                    boolean success = login(userName, password);
                    if (success)
                    {
                        currentLoggedInUser = userName;
                    }
                }
                else if (currentLoggedInUser != null && !userName.equals(currentLoggedInUser))
                {
                    boolean success = logout();
                    if (success)
                    {
                        currentLoggedInUser = null;
                    }
                }
                else if (!url.equals(currentUrl))
                {
                    navigateToUrl(url);
                }
                else
                {
                    navigationComplete = true;
                }
                iteration++;
            }

            if (!navigationComplete)
            {
                throw new RuntimeException("Failed to access " + url + " after " + iteration + " loops");
            }
        }

        return this.render();
    }

    /**
     * Try to logout.
     *
     * @return {@code true} ii the user was logged out successfully.
     */
    private boolean logout()
    {
        Reporter.log("Attempting to log out");
        try
        {
            sharePageNavigation.openUserDropdownMenu().logout();
            Utils.waitFor(ExpectedConditions.titleContains("Login"));
        }
        catch (Exception e)
        {
            Reporter.log("Failed to logout with exception message: " + e.getMessage());
            return false;
        }
        Reporter.log("Log out attempt complete, title is " + webDriver.getTitle());
        return true;
    }

    /**
     * Try to login.
     *
     * @param userName The user to log in as.
     * @param password The password to use.
     * @return {@code true} if the user was logged in successfully.
     */
    private boolean login(String userName, String password)
    {
        Reporter.log("Logging in as " + userName + " (" + password + ")");
        try
        {
            loginPage.render();
            loginPage
                .setUsername(userName)
                .setPassword(password)
                .clickOnLoginButton();
        }
        catch (Exception e)
        {
            Reporter.log("Failed to log in - exception was " + e.getMessage());
            return false;
        }
        Reporter.log("Logged in as " + userName);
        return true;
    }

    /**
     * Navigate to the specified URL.
     *
     * @param url The URL to access.
     */
    private void navigateToUrl(String url)
    {
        Reporter.log("Opening " + url);
        webDriver.get(url);
        Reporter.log("Opened " + url);
    }
}
