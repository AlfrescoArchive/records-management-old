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

import static org.alfresco.po.common.util.Utils.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

import java.util.List;
import org.alfresco.po.share.console.ConsolePage;
import org.alfresco.po.share.page.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
    private static final By FOUND_GROUP_NAME = By.cssSelector("td *[class='itemname']");
    private static final By ADD_GROUP_BUTTON = By.cssSelector("td[class*='actions'] button");
    private static final By SELECTED_GROUP = By.cssSelector("div[class='groupselection-row'] span");
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
        waitForVisibilityOf(saveChangesButton);
        WebElement searchGroupInput = getVisibleElement(SEARCH_GROUP_INPUTS);
        WebElement searchButton = getVisibleElement(SEARCH_GROUP_BUTTONS);
    
        if (searchGroupInput != null && searchButton != null) 
        {
            searchGroupInput.sendKeys(groupName);
            searchButton.click();

            waitFor(visibilityOfAllElements(foundGroups));

            for (WebElement groupRow : foundGroups) 
            {
                // if the group is one of the found ones
                if (groupRow.findElement(FOUND_GROUP_NAME).getText().equals(groupName)) 
                {
                // click on Add button to select the group    
                    groupRow.findElement(ADD_GROUP_BUTTON).click();
                }
            }
            // wait for the visibility of the added group as selected in the bottom 
            waitForVisibilityOf(SELECTED_GROUP);
            clickOnSaveChanges();
        }
    }   
}
