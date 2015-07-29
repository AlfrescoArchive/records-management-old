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

import static org.alfresco.po.common.StreamHelper.zip;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Page object for the "Security Clearance" page within the Share Admin Console.
 * @author Neil Mc Erlean
 * @author David Webster
 */
@Component
public class SecurityClearancePage extends ConsolePage
{
    /** selectors */
    private static final By ROWS = By.cssSelector("tbody[id$='ITEMS'] tr[id*='Row']");
    private static final By SECURITY_CLEARANCE_SELECTOR = By.cssSelector(".control span[role=option]");
    private static final By PROFILE_LINK_SELECTOR = By.cssSelector(".security-clearance-user-name .inner");
    private static final By USER_NAME_SELECTOR = By.cssSelector(".security-clearance-user-name .value");
    private static final By VISIBLE_CLEARANCE_OPTIONS_SELECTOR = By.cssSelector("div:not([style*='display: none']).dijitMenuPopup");
    private static final By LOADING_SELECTOR = By.cssSelector(":not([class*='share-hidden'])[data-dojo-attach-point='dataLoadingNode']");
    private static final By NO_DATA = By.cssSelector("div[class$='no-data']");

    /** page url */
    private static final String PAGE_URL = "/page/console/admin-console/security-clearance";

    @FindBy(css=".security-clearance-filter .control input[name=nameFilter]")
    private TextInput nameFilterTextInput;

    @FindBy(css=".alfresco-lists-views-AlfListView")
    private WebElement clearanceTable;

    @Autowired
    private ConfirmationPrompt confirmationPrompt;

    /**
     * Page URL
     */
    public String getPageURL(String ... context)
    {
        return PAGE_URL;
    }

    /** Clears the name filter */
    public SecurityClearancePage clearNameFilter()
    {
        Utils.clear(nameFilterTextInput);
        Utils.waitForInvisibilityOf(LOADING_SELECTOR);
        return this;
    }

    /** Sets text in the name filter input. */
    public SecurityClearancePage setNameFilter(String filter)
    {
        Utils.clearAndType(nameFilterTextInput, filter);
        Utils.waitForInvisibilityOf(LOADING_SELECTOR);
        return this;
    }

    /** Get the text from the name filter box. */
    public String getNameFilter()
    {
        return nameFilterTextInput.getText();
    }

    /**
     * Indicates whether the result list is empty or not
     */
    public boolean isEmpty()
    {
        return Utils.elementExists(NO_DATA);
    }

    /**
     * Indicates whether the given user is shown in the current page of results
     */
    public boolean isUserShown(String userName)
    {
        boolean result = false;

        Utils.waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(ROWS));
        List<WebElement> rows = Utils.getWebDriver().findElements(ROWS);
        for (WebElement row : rows)
        {
            WebElement userNameControl = row.findElement(USER_NAME_SELECTOR);
            try
            {
                if (userNameControl.getText().contains(userName))
                {
                    result = true;
                    break;
                }
            }
            catch (StaleElementReferenceException exception)
            {
                // do nothing
            }
        }

        return result;
    }

    /**
     * Get the specified user's Security Clearance as a String
     */
    public String getUserSecurityClearance(String userName)
    {
        String result = null;

        List<WebElement> rows = Utils.getWebDriver().findElements(ROWS);
        for (WebElement row : rows)
        {
            WebElement userNameControl = row.findElement(USER_NAME_SELECTOR);
            if (userNameControl.getText().contains(userName))
            {
                WebElement securityClearance = row.findElement(SECURITY_CLEARANCE_SELECTOR);
                result = securityClearance.getText();
                break;
            }
        }

        return result;
    }

    /**
     * Get all the user names displayed on the page.
     */
    public List<String> getUserNames()
    {
        return clearanceTable
                    .findElements(USER_NAME_SELECTOR)
                    .stream()
                    .map(webElement -> webElement.getText().split("[\\(\\)]")[1])
                    .collect(Collectors.toList());
    }

    /**
     * Get all the names (first name, space, last name) displayed on the page.
     */
    public List<String> getNames()
    {
        return clearanceTable
                    .findElements(USER_NAME_SELECTOR)
                    .stream()
                    .map(webElement -> webElement.getText().split(" \\(")[0])
                    .collect(Collectors.toList());
    }

    /**
     * Get the text nodes of all elements found using the selector.
     *
     * @param container The node to search within.
     * @param selector The selector to use.
     * @return A stream of strings found in the text nodes of the matched elements.
     */
    private Stream<String> getTextNodesOfElements(WebElement container, By selector)
    {
        return container.findElements(selector).stream().map(webElement -> webElement.getText());
    }

    /**
     * Get all user clearances displayed on the page.
     *
     * @return A map from users to their clearances.
     */
    public Map<String, String> getUserClearances()
    {
        Stream<String> userStream = getTextNodesOfElements(clearanceTable, USER_NAME_SELECTOR);
        Stream<String> clearanceStream = getTextNodesOfElements(clearanceTable, SECURITY_CLEARANCE_SELECTOR);

        return zip(userStream, clearanceStream).collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
    }

    /**
     * Set a users security clearance
     */
    public ConfirmationPrompt setClearance(String userName, String clearance)
    {
        List<WebElement> rows = Utils.getWebDriver().findElements(ROWS);
        for (WebElement row : rows)
        {
            WebElement userNameControl = row.findElement(USER_NAME_SELECTOR);
            if (userNameControl.getText().contains(userName))
            {
                WebElement securityClearance = row.findElement(SECURITY_CLEARANCE_SELECTOR);
                securityClearance.click();

                WebElement visibleClearanceOptions = Utils.waitForVisibilityOf(VISIBLE_CLEARANCE_OPTIONS_SELECTOR);

                // get the options
                List<WebElement> clearanceOptions = visibleClearanceOptions.findElements(By.cssSelector("tr[role='option']"));
                for (WebElement clearanceOption : clearanceOptions)
                {
                    if (clearance.equals(clearanceOption.getAttribute("aria-label").trim()))
                    {
                        clearanceOption.click();
                        break;
                    }
                }

                break;
            }
        }

        return confirmationPrompt.render();
    }

    /**
     * Get the list of clearance options for the given user
     */
    public List<String> getClearanceOptions(String userName)
    {
        List<String> result = null;


        WebElement userRow = getUserRow(userName);

        WebElement securityClearance = userRow.findElement(SECURITY_CLEARANCE_SELECTOR);
        securityClearance.click();

        WebElement visibleClearanceOptions = Utils.waitForVisibilityOf(VISIBLE_CLEARANCE_OPTIONS_SELECTOR);

        // get the options
        List<WebElement> clearanceOptions = visibleClearanceOptions.findElements(By.cssSelector("tr[role='option']"));
        result = clearanceOptions.stream().map(option -> option.getAttribute("aria-label").trim())
                    .collect(Collectors.toList());

        securityClearance = userRow.findElement(SECURITY_CLEARANCE_SELECTOR);
        securityClearance.click();

        return result;
    }

    public WebElement getUserRow(String userName)
    {
        List<WebElement> rows = Utils.getWebDriver().findElements(ROWS);
        return rows.stream()
                .filter(row -> row.findElement(USER_NAME_SELECTOR).getText().contains(userName))
                .findFirst().get();
    }

    /**
     * Click on the User
     */
    public void clickOnUser(String userName)
    {
        WebElement userRow = getUserRow(userName);
        WebElement profileLink = userRow.findElement(PROFILE_LINK_SELECTOR);
        profileLink.click();
    }
}
