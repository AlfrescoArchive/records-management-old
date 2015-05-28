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

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Page object for the "Security Clearance" page within the Share Admin Console.
 * @author Neil Mc Erlean
 */
@Component
public class SecurityClearancePage extends ConsolePage
{
    private static final String PAGE_URL = "/page/console/admin-console/security-clearance";

    /** Selects the "User Name" text from within the table showing users and their clearances.
     * Example value: "Alice Beecher (abeecher)".
     * <p/>
     * Note that this actually selects the span containing the value for user name.
     * Note that this selector assumes that the table only has one row. Therefore the table
     * must be restricted to one row by {@link #setNameFilter(String) adding some filter text}. */
    private static final By USER_NAME_SELECTOR = By.cssSelector(".security-clearance-user-name .value");

    /** Selects the "Security Clearance" text from within the table showing users and their clearances.
     * Example value: "No Clearance".
     * <p/>
     * Note that this actually selects the span containing the value for security clearance.
     * A click on this span should cause the Security Clearance dropdown to appear.
     */
    private static final By SECURITY_CLEARANCE_SELECTOR =
            By.cssSelector(".alfresco-lists-views-AlfListView tr.alfresco-lists-views-layouts-Row .control span[role=option]");

    /** Selects the dropdown cell for the specified clearance level.
     *  <p/>
     *  Note that the provided clearance string must match the text in the UI e.g. "Top Secret".
     *  A click on this element will select that clearance level.
     */
    private By getSecurityClearanceDropDownSelector(String clearance)
    {
        // The trailing space after the clearance (%s) is intentional here. It seems to be added by dojo/dijit.
        return By.cssSelector(String.format("tr[aria-label='%s ']", clearance));
    }

    @FindBy(css=".security-clearance-filter .control input[name=nameFilter]")
    private TextInput nameFilterTextInput;

    @FindBy(css=".alfresco-lists-views-AlfListView")
    private WebElement clearanceTable;

    public String getPageURL(String ... context)
    {
        return PAGE_URL;
    }


    /** Sets text in the name filter input. */
    public SecurityClearancePage clearNameFilter()
    {
        Utils.clear(nameFilterTextInput);
        return this;
    }

    /** Sets text in the name filter input. */
    public SecurityClearancePage setNameFilter(String filter)
    {
        Utils.clearAndType(nameFilterTextInput, filter);
        return this;
    }

    /** Get the text from the name filter box. */
    public String getNameFilter()
    {
        return nameFilterTextInput.getText();
    }

    /** Get the specified user's Security Clearance as a String.
     *
     *  @throws TimeoutException if the provided {@code filterTerm} does not match exactly one user.
     */
    public String getUserSecurityClearance(String filterTerm)
    {
        filterTableToOneUser(filterTerm);

        final List<WebElement> secClearanceSpans = webDriver.findElements(SECURITY_CLEARANCE_SELECTOR);

        if (secClearanceSpans == null || secClearanceSpans.size() != 1)
        {
            throw new IllegalArgumentException(String.format("Expected exactly 1 user for filterTerm '%s' but found %s",
                                                             filterTerm,
                                                             secClearanceSpans == null ? "null" : secClearanceSpans.size()));
        }

        return secClearanceSpans.get(0).getText();
    }

    private void filterTableToOneUser(String filterTerm)
    {
        // Reset the table to show all results. We must do this so that the waiting code below will work.
        // If we don't reset the table, then there is the chance that the table has only one row in it
        // before this search is performed, which would cause an error when we wait for its size to be 1.
        this.clearNameFilter();

        // Now wait for the table to be populated. Arbitrarily we need more than 1 user.
        Utils.waitFor(driver -> driver.findElements(USER_NAME_SELECTOR).size() > 1);

        // Restrict the table - hopefully to only a single row this time.
        this.setNameFilter(filterTerm);

        // We will wait for the table to have a single result row.
        Utils.waitFor(driver -> driver.findElements(USER_NAME_SELECTOR).size() == 1);

        // TODO It would be nice here to be able to also wait for the appearance of "Could not find any users..."
        //          But there is no css class on that div which makes it tricky.
    }

    /**
     * Get all the user names displayed on the page.
     *
     * @return The list of users.
     */
    public List<String> getDisplayedUsers()
    {
        return clearanceTable
                    .findElements(USER_NAME_SELECTOR)
                    .stream()
                    .map(webElement -> webElement.getText())
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
     * TODO
     */
    public SecurityClearancePage setClearance(String string)
    {
        return this;
    }

    /**
     * TODO
     */
    public SecurityClearancePage andConfirm()
    {
        return this;
    }


    /**
     * TODO
     */
    public List<String> getClearanceOptions(String userName)
    {
        return null;
    }


    // TODO Get available Security Clearance dropdown options.
}
