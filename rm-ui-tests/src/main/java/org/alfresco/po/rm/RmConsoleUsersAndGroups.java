/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.rm;

import static org.alfresco.webdrone.WebDroneUtil.checkMandotaryParam;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.share.ShareLink;
import org.alfresco.webdrone.ElementState;
import org.alfresco.webdrone.RenderElement;
import org.alfresco.webdrone.RenderTime;
import org.alfresco.webdrone.WebDrone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Records management users and groups page.
 *
 * @author Polina Lushchinskaya
 * @version 1.1
 * @since 2.2
 */
public class RmConsoleUsersAndGroups extends RmSitePage
{
    protected static final By ADD_BUTTON = By.xpath("//button[@id='addUser-button']");
    protected static final By ADD_USER_FORM = By.cssSelector("div[id$='peoplepicker']");
    protected static final By SEARCH_USER_INPUT = By.cssSelector("input[id$='rm-search-peoplefinder-search-text']");
    protected static final By SEARCH_USER_BUTTON = By.xpath("//button[contains(@id, 'search-button')]");
    protected static final By CREATED_ALERT = By.xpath(".//*[@id='message']/div/span");
    protected static final By USER_LIST = By.cssSelector("#users-list>li>a");

    /** logger */
    private static Log logger = LogFactory.getLog(RmConsoleUsersAndGroups.class);

    public enum SystemRoles
    {
        /*
         * These value are unique and even if the browser language changes we'll
         * be able to use them, so no reason to extract them to language properties
         */
        RECORDS_MANAGEMENT_ADMINISTRATOR("Administrator"),
        RECORDS_MANAGEMENT_POWER_USER("PowerUser"),
        RECORDS_MANAGEMENT_RECORDS_MANAGER("RecordsManager"),
        RECORDS_MANAGEMENT_SECURITY_OFFICER("SecurityOfficer"),
        RECORDS_MANAGEMENT_User("User");

        private final String cssSelector;

        SystemRoles(String cssSelector)
        {
            this.cssSelector = cssSelector;
        }

        public String getValue()
        {
            return cssSelector;
        }
    }

    /**
     * Constructor.
     *
     * @param drone {@link org.alfresco.webdrone.WebDrone}
     */
    public RmConsoleUsersAndGroups(WebDrone drone)
    {
        super(drone);
    }

    /**
     * @see org.alfresco.webdrone.Render#render(org.alfresco.webdrone.RenderTime)
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmConsoleUsersAndGroups render(RenderTime timer)
    {
        checkMandotaryParam("timer", timer);

        while (true)
        {
            timer.start();
            try
            {
                // if search body is found we are rendered
                By roles = By.cssSelector("#roleSelection");
                WebElement rmConsoleElement = drone.find(roles);
                if (rmConsoleElement.isDisplayed())
                {
                    break;
                }
            }
            catch (NoSuchElementException e)
            {
                logger.error(e.getMessage());
            }
            finally
            {
                timer.end();
            }
        }
        return this;
    }

    /**
     * @see org.alfresco.webdrone.Render#render(long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmConsoleUsersAndGroups render(long time)
    {
        checkMandotaryParam("time", time);

        RenderTime timer = new RenderTime(time);
        return render(timer);
    }

    /**
     * @see org.alfresco.webdrone.Render#render()
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmConsoleUsersAndGroups render()
    {
        RenderTime timer = new RenderTime(maxPageLoadingTime);
        return render(timer);
    }

    /**
     * Action click on group value
     *
     * @param drone {@link org.alfresco.webdrone.WebDrone}
     * @param groupName Name of group
     */
    public static void selectGroup(final WebDrone drone, String groupName)
    {
        checkMandotaryParam("drone", drone);
        checkMandotaryParam("groupName", groupName);

        WebElement group = drone.findAndWait(By.cssSelector("#role-" + groupName));
        group.click();
    }

    /**
     * Helper method find Add button by xpath according username
     *
     * @param userName Name of Created user
     * @return By locator add button locator by xpath
     */
    protected By addUserButton(String userName)
    {
        checkMandotaryParam("userName", userName);

        return By.xpath("//span[contains(text(), '" + userName + "')]/ancestor::tr//button");
    }

    /**
     * Helper method wait until created alert disappears
     */
    public void waitUntilCreatedAlert()
    {
        drone.waitUntilElementPresent(CREATED_ALERT, 5);
        drone.waitUntilElementDeletedFromDom(CREATED_ALERT, 5);
    }

    /**
     * Helper method return user Link on Users And Groups Page in RM console
     *
     * @param userName Name of user
     * @return locator of user link
     */
    public static By userLinkRmConsole(String userName)
    {
        checkMandotaryParam("userName", userName);

        return By.cssSelector("div[id$='roleUsers'] a[id$='user-" + userName + "']");
    }

    /**
     * Click the add user button
     */
    public void clickAdd()
    {
        WebElement element = drone.find(ADD_BUTTON);
        drone.mouseOverOnElement(element);
        element.click();
    }

    /**
     * Check is add button is visible.
     * 
     * @author Michael Suzuki
     * @return true if visible
     */
    public boolean isAddButtonDisplayed()
    {
        try
        {
            return drone.find(ADD_BUTTON).isDisplayed();
        }
        catch (NoSuchElementException e)
        {
        }
        return false;
    }

    /**
     * Click the search user button
     */
    public void clickSearchUser()
    {
        WebElement element = drone.findAndWait(SEARCH_USER_BUTTON);
        drone.mouseOverOnElement(element);
        element.click();
    }

    /**
     * Action assigned user to role
     *
     * @param userName Name of assigbed user
     * @param roleName Role name applied to user
     * @return {@link RmConsoleUsersAndGroups}
     */
    public RmConsoleUsersAndGroups assignUserToRole(String userName, String roleName)
    {
        checkMandotaryParam("userName", userName);
        checkMandotaryParam("roleName", roleName);

        selectGroup(drone, roleName);

        // Add user
        clickAdd();
        drone.findAndWait(ADD_USER_FORM).isDisplayed();

        // Search for user
        WebElement searchInput = drone.findAndWait(SEARCH_USER_INPUT);
        searchInput.clear();
        searchInput.sendKeys(userName);
        clickSearchUser();

        // wait until the user is shown
        RenderElement addUserButton = new RenderElement(addUserButton(userName), ElementState.CLICKABLE);
        elementRender(new RenderTime(drone.getDefaultWaitTime()), addUserButton);

        // click the add user button
        drone.find(addUserButton(userName)).click();

        // for (int i=0; i<3; i++)
        // {
        // if (!isElementPresent(addUserButton(userName)))
        // {
        // clickSearchUser();
        // }
        // else
        // {
        // break;
        // }
        // }

        // click(addUserButton(userName));

        waitUntilCreatedAlert();
        return new RmConsoleUsersAndGroups(drone).render();
    }

    public List<ShareLink> getUSerlist()
    {
        List<WebElement> elements = drone.findAndWaitForElements(USER_LIST);
        List<ShareLink> userlist = new ArrayList<ShareLink>();

        for (WebElement webElement : elements)
        {
            userlist.add(new ShareLink(webElement, drone));
        }
        return userlist;

    }
}
