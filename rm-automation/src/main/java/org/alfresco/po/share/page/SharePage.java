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

import org.alfresco.po.common.Page;
import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.share.login.LoginPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
    /** page footer */
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
    public synchronized SharePage open(String server, String userName, String password, String ... context)
    {
        // get the page URL
        String url = server + getPageURL(context);
        
        // check whether we need to log out or not
        if (loginPage.isUserLoggedIn() && !userName.equals(loginPage.getCurrentUser()))
        {
            // logout
            sharePageNavigation.openUserDropdownMenu().logout();
            
            // login
            loginPage.login(userName, password);  
        }
        
        // Try the naive method of getting to the given URL.
        navigateToUrl(url);

        // determine whether the login form is shown or not
        if (!loginPage.isUserLoggedIn() || loginPage.isShown())
        {
            // login specifying destination renderable (ie this) to save an unnecessary render of the user dashboard
            loginPage.login(userName, password, this);            
        }
        
        return this.render();
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
