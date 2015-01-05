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


import static org.alfresco.po.common.util.Utils.clearAndType;
import static org.alfresco.po.common.util.Utils.waitForStalenessOf;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Add authority dialog
 * 
 * @author Roy Wetherall
 */
public abstract class AddAuthorityDialog extends Dialog
{
    /**
     * selectors
     */
    private static final By RESULTS_SELECTOR = By.cssSelector("tbody[class$='data'] tr");

    private static final By ADD_BUTTON_SELECTOR = By.cssSelector("button");

    /**
     * search text input
     */
    @FindBy(css = "div[style*='visibility: visible'] input[id$='search-text']")
    private TextInput searchTextInput;

    /**
     * search button
     */
    @FindBy(css = "div[style*='visibility: visible'] button[id$='search-button-button']")
    private Button searchButton;

    /**
     * message
     */
    @FindBy(css = "div[style*='visibility: visible'] tbody[class$='message'] div")
    private WebElement message;

    /**
     * close
     */
    @FindBy(css = "a.container-close")
    private Link closeLink;

    /**
     * users and groups page
     */
    @Autowired
    private UsersAndGroupsPage usersAndGroupsPage;

    @Autowired
    private Message waitMessage;

    /**
     * Get results web element
     */
    protected abstract WebElement getResultsWebElement();

    /**
     * Get the authority name for a given result
     */
    protected abstract String getAuthorityName(WebElement result);

    /**
     * @return String  current search value
     */
    public String getSearch()
    {
        return searchTextInput.getText();
    }

    /**
     * Set search string
     */
    public AddAuthorityDialog setSearch(String search)
    {
        clearAndType(searchTextInput, search);
        return this;
    }

    /**
     * Click on search
     */
    public AddAuthorityDialog clickOnSearch()
    {
        // mouse over and click button
        Utils.mouseOver(searchButton);
        searchButton.click();

        // wait for searching message to be hidden
        Utils.webDriverWait().until(searchFinished());

        return this;
    }

    /**
     * Helper method to return custom expected condition that returns true when
     * the search is complete.
     */
    protected ExpectedCondition<Boolean> searchFinished()
    {
        return new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver arg0)
            {
                boolean result = false;
                if (message.isDisplayed() && message.getText().contains("Searching"))
                {
                    result = false;
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
     * Determines whether the results are empty or not
     */
    public boolean isResultsEmpty()
    {
        return getResultsWebElement().findElements(RESULTS_SELECTOR).isEmpty();
        
    }

    /**
     * Get the results
     */
    public List<String> getResults()
    {
        List<WebElement> elements = getResultsWebElement().findElements(RESULTS_SELECTOR);
        List<String> result = new ArrayList<String>(elements.size());
        for (WebElement element : elements)
        {
            result.add(getAuthorityName(element));
        }
        return result;
    }
    
    /**
     * Click on add
     */
    public UsersAndGroupsPage clickOnAdd(String authorityName)
    {
        List<WebElement> elements = getResultsWebElement().findElements(RESULTS_SELECTOR);
        if (elements.isEmpty())
        {
            throw new RuntimeException("Can't add " + authorityName + ", because the list is empty.");
        }
        else
        {
            boolean matchFound = false;
            for (WebElement element : elements)
            {
                if (authorityName.equals(getAuthorityName(element)))
                {
                    WebElement action = element.findElement(ADD_BUTTON_SELECTOR);
                    action.click();
                    waitMessage.waitUntillVisible();
                    matchFound = true;
                    break;
                }
            }
            
            if (!matchFound)
            {
                throw new RuntimeException("Can't add " + authorityName + ", because it was not found in the list.");
            }
        }
        
        // render page
        return usersAndGroupsPage.render();
    }
    
    /**
     * Click on close
     */
    public UsersAndGroupsPage clickOnClose()
    {
        closeLink.click();
        waitForStalenessOf(searchButton);
        return usersAndGroupsPage.render();
    }
}
