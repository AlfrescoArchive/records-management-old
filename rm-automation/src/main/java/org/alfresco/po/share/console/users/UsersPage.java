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

import java.util.List;

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * User console page
 * 
 * @author Roy Wetherall
 */
@Component
public class UsersPage extends ConsolePage
{
    /** page url */
    private static final String PAGE_URL = "/page/console/admin-console/users";
    
    /** selectors */
    private static final By USER_NAME_SELECTOR = By.cssSelector("td[class*='userName'] div");
    
    /** new users page */
    @Autowired
    private NewUsersPage newUsersPage;
    
    /** new user button */
    @FindBy(css="button[id$='newuser-button-button']")
    private Button newUserButton;
    
    /** data table */
    @FindBy(css="div[id$='default-datatable']")
    private WebElement datatable;
    
    /** search input text */
    @FindBy(css="input[id$='default-search-text']")
    private TextInput searchTextInput;
    
    /** search button */
    @FindBy(css="div.search-button button")
    private Button searchButton;
    
    /** message */
    @FindBy(css ="tbody[class$='message'] div")
    private WebElement message;  
    
    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        return PAGE_URL;
    }
    
    /**
     * @return  true if the new user button is enabled, false otherwise
     */
    public boolean isEnabledNewUser()
    {
        return newUserButton.isEnabled();
    }
    
    /**
     * Click on new user button
     */
    public NewUsersPage clickOnNewUser()
    {
       newUserButton.click();
       return newUsersPage.render();
    }
    
    /**
     * Sets the search text
     */
    public UsersPage setSearch(String search)
    {
        Utils.clearAndType(searchTextInput, search);
        return this;
    }
    
    /**
     * @return  true if search button is enabled, false otherwise
     */
    public boolean isEnabledSearch()
    {
        return searchButton.isEnabled();
    }
    
    /**
     * Click on the search button
     */
    public UsersPage clickOnSearch()
    {
        // click search button
        searchButton.click();
        
        // wait for search
        Utils.webDriverWait().until(searchFinished());
        
        return this;
    }
    
    /**
     * Helper method to return custom expected condition that returns true when 
     * the search is complete.
     */
    private ExpectedCondition<Boolean> searchFinished()
    {
        return new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver arg0)
            {
                boolean result = false;
                if (message.isDisplayed() && message.getText().contains("view"))
                {
                    result = true;
                }
                else
                {
                    result = true;
                }
                return result;
            }            
        };
    }
    
    /**
     * @param true if the user is shown on the search results, false otherwise
     */
    public boolean isUserFound(String userName)
    {
        boolean result = false;
        List<WebElement> users = datatable.findElements(USER_NAME_SELECTOR);
        for (WebElement user : users)
        {
            if (userName.equals(user.getText()))
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
    
}
