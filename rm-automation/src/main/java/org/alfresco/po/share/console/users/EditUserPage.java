/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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

import java.util.List;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.alfresco.po.share.page.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Edit user page
 * 
 * @author Oana Nechiforescu
 */
@Component
public class EditUserPage extends ConsolePage
{
    @Autowired
    private Message message;   
    @Autowired
    private UserProfilePage userProfile;
 
    /** page url */
    private static final String URL = "/page/console/admin-console/users#state=panel%3Dupdate%26userid%3D{0}";
    /** css selectors */
    private static final By SEARCH_GROUP_INPUTS = By.cssSelector("input[id$='groupfinder-search-text']");
    private static final By SEARCH_GROUP_BUTTONS = By.cssSelector("button[id$='groupfinder-group-search-button-button']");
    /** the groups found after search */        
    @FindBy(css="tbody[class*='data'] tr")
    private List<WebElement> foundGroups;
    
    /** save changes button */
    @FindBy(css="button[id$='updateuser-save-button-button']")
    private Button saveChangesButton;
    
    /**
     * @param context
     * @return page URL
     * @see org.alfresco.po.share.console.ConsolePage#getPageURL(java.lang.String[])
     */
    @Override
    public String getPageURL(String... context)
    {
        return URL;
    }
    
    /**
     * Click on save changes
     * @return the user profile page
     */
    public UserProfilePage clickOnSaveChanges()
    {
        // click on the save changes
        saveChangesButton.click();

        // wait until the message is visible
        message.waitUntillVisible();

        // return the rendered users profile page
        return userProfile.render();
    }
      
    /**
     * Add a user to a specified Alfresco group
     * @param groupName
     */
    public void addUserToGroup(String groupName)
    {
        Utils.waitForVisibilityOf(saveChangesButton);
        WebElement searchGroupInput = getVisibleElement(SEARCH_GROUP_INPUTS);
        WebElement searchButton = getVisibleElement(SEARCH_GROUP_BUTTONS);
    
        if (searchGroupInput != null && searchButton != null) 
        {
            searchGroupInput.sendKeys(groupName);
            searchButton.click();

            Utils.waitFor(ExpectedConditions.visibilityOfAllElements(foundGroups));

            for (WebElement groupRow : foundGroups) 
            {
                if (groupRow.findElement(By.cssSelector("td *[class='itemname']")).getText().equals(groupName)) 
                {
                    groupRow.findElement(By.cssSelector("td[class*='actions'] button")).click();
                }
            }
            Utils.waitForVisibilityOf(By.cssSelector("div[class='groupselection-row'] span"));
            clickOnSaveChanges();
        }
    }
    /**
     * Get the visible element from the list of elements that can be retrieved with the same selector
     * @param selector used to find elements
     * @return the visible element
     */
    private WebElement getVisibleElement(By selector)
    {
        List<WebElement> sameSelectorElements = Utils.getWebDriver().findElements(selector);
        
        for (WebElement element : sameSelectorElements) 
        {
            if (element.isDisplayed()) 
            {
                return element;
            }
        }
        return null;
    }    
}
