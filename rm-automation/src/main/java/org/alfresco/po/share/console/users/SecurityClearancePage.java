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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.TextInput;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import org.openqa.selenium.Keys;

/**
 * Page object for the "Security Clearance" page within the Share Admin Console.
 * @author Neil Mc Erlean
 * @author David Webster
 * @author Oana Nechiforescu
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
    private static final By LOADING_SELECTOR = By.cssSelector(".data-loading ");
    private static final By NO_DATA = By.cssSelector("div[class$='no-data']");
    private static final By ERROR_LOADING_DATA = By.cssSelector("div[data-dojo-attach-point='dataFailureNode']");
    
    /** predicates */
    private Predicate<WebDriver> waitTillHidden = (webDriver) ->
    {
        WebElement loadingMessage = webDriver.findElement(LOADING_SELECTOR);
        return loadingMessage.getAttribute("class").contains("share-hidden");
    };    
    
    /** page url */
    private static final String PAGE_URL = "/page/console/admin-console/security-clearance";

    @FindBy(css=".security-clearance-filter .control input[name=nameFilter]")
    private TextInput nameFilterTextInput;

    @FindBy(css=".alfresco-lists-views-AlfListView")
    private WebElement clearanceTable;
    
    @FindBy(css="div[id$='_PAGE_BACK']")
    private WebElement backButton;
    
    @FindBy(css="div[id$='_PAGE_FORWARD']")
    private WebElement nextButton;
    
    @FindBy(css="div[class*='page-selector']")
    private WebElement pageDropDownSelector;
    
    @FindBy(xpath="//div[not(contains(@id,'RESULTS_PER_PAGE_SELECTOR_dropdown')) and contains(@id,'PAGE_SELECTOR_dropdown')]")
    private WebElement pagesContainerSelector;
    
    @FindBy(css="div[id$='_RESULTS_PER_PAGE_SELECTOR']")
    private WebElement resultsPerPageSelector;
    
    @FindBy(css="div[aria-labelledby*='_RESULTS_PER_PAGE_SELECTOR'] table tbody tr td[id*='_text']")
    private List<WebElement> resultsPerPageItems;
    
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
        // set the text
        Utils.clearAndType(nameFilterTextInput, filter);
        
        // no easy way to wait for the loading message to appear
        try { Thread.sleep(1000); } catch (InterruptedException e){}
        
        // wait for the loading message to disappear
        new FluentWait<WebDriver>(Utils.getWebDriver())
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(1, TimeUnit.SECONDS)
            .until(waitTillHidden); 
        
        return this;
    }

    /** Get the text from the name filter box. */
    public String getNameFilter()
    {
        return nameFilterTextInput.getText();
    }
    
    /** Get the available pages. */
    public int getNumberOfPages()
    {
        pageDropDownSelector.click();
        Utils.waitForVisibilityOf(pagesContainerSelector);
        return pagesContainerSelector.findElements(By.cssSelector("tr")).size();
    }

    /** Get the available results per page options.
     * @return a list of Strings representing the numeric values of the options displayed for results per page.
     * The regex below is used in order to remove all the non-numeric values from the displayed options
     * The value of "25 items per page" would be kept as "25" to avoid language dependency
     */
    public List<String> getResultsPerPageOptions()
    {
        List<String> options = new ArrayList<>();
        resultsPerPageSelector.click();
        Utils.waitFor(ExpectedConditions.visibilityOfAllElements(resultsPerPageItems));
        
        // retain the numeric value only from the label
        for(WebElement option : resultsPerPageItems)
        {
        options.add(option.getText().replaceAll("[^0-9]", ""));
        }    
        return options;
    }        
    
    /**
     * Indicates whether the result list is empty or not
     */
    public boolean isEmpty()
    {
        return Utils.elementExists(NO_DATA);
    }

    /**
     * Checks if the "There was an error loading the data" error is displayed
     * Waits for the loading message to disappear to make sure that the error page would have time to be displayed
     */
    public boolean isErrorLoadingDataDisplayed()
    {
        // no easy way to wait for the loading message to disappear
        try { Thread.sleep(1000); } catch (InterruptedException e){}
        
        // wait for the loading message to disappear
        new FluentWait<WebDriver>(Utils.getWebDriver())
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(1, TimeUnit.SECONDS)
            .until(waitTillHidden); 
        return Utils.getWebDriver().findElement(ERROR_LOADING_DATA).isDisplayed();
    }
    
    /**
     * Checks if the Back button is enabled
     */
    public boolean isBackButtonEnabled()
    {
        return backButton.isEnabled();
    }
    
    /**
     * Checks if the Next button is enabled
     */
    public boolean isNextButtonEnabled()
    {
        return nextButton.isEnabled();
    }
    
    /**
     * Clears filter and waits for no data message to disappear in order to be sure that the empty filter has been applied
     * Can be used after the "No matching users were found. Try changing your search criteria." error has been displayed
     */
    public void applyEmptyFilterAfterNoData()
    {
        clearFilter();
        Utils.waitForInvisibilityOf(NO_DATA);
        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e)
        {
        }
        // wait for the loading message to disappear
        new FluentWait<WebDriver>(Utils.getWebDriver())
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .until(waitTillHidden);
    }
    
    /**
     * Clears the filter using CONTROL + a keys, then BACKSPACE to remove all content at once
     */
    private void clearFilter()
    {
        nameFilterTextInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        nameFilterTextInput.sendKeys(Keys.BACK_SPACE);
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
    
    /**
     * Select results per page option
     * @param optionLabel is the numeric value of option displayed, to avoid language dependency, so to select "50 per page" the parameter would be "50"
     */
    public void selectResultsPerPageOption(String optionLabel)
    {
        resultsPerPageSelector.click();
        Utils.waitFor(ExpectedConditions.visibilityOfAllElements(resultsPerPageItems));

        for (WebElement option : resultsPerPageItems) 
        {
            if (option.getText().contains(optionLabel)) 
            {
                option.click();
                return;
            }
        }   
    }
    
    /**
     * Get the selected items per page value
     */
    public String getSelectedItemsPerPageValue()
    {
        return resultsPerPageSelector.getText();
    }        
}
