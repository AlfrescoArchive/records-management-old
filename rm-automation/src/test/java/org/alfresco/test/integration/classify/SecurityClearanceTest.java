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
package org.alfresco.test.integration.classify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.alfresco.po.share.console.users.SecurityClearancePage;
import org.alfresco.po.share.page.SharePageNavigation;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * UI Integration tests for the Security Clearance admin page.
 *
 * @author tpage
 * @since 3.0
 */
@Test
public class SecurityClearanceTest extends BaseTest
{
    private static final List<String> CONFIGURED_CLEARANCES = Arrays.asList("Top Secret", "Secret", "Confidential", "No Clearance");

    @Autowired
    private SecurityClearancePage securityClearancePage;
    @Autowired
    private SharePageNavigation sharePageNavigation;

    /**
     * Check that the displayed users are sorted correctly.
     *
     * @param displayedUsers The list of user strings found on the page (in the format "First-name Surname (userid)").
     * @throws AssertionError If the list is sorted incorrectly.
     */
    protected void checkUserOrdering(List<String> displayedUsers)
    {
        String previousUserId = "";
        for (String user : displayedUsers)
        {
            String userId = user.split("[\\(\\)]")[1];
            assertTrue("Expected users to be sorted by user id, but found '"+previousUserId+"' and then '"+userId+"'",
                        userId.toLowerCase().compareTo(previousUserId.toLowerCase()) >= 0);
            previousUserId = userId;
        }
    }

    /**
     * Check the security clearance page loads and contains an ordered list of users and clearances.
     *
     * <pre>
     * Given that I am a compliance officer
     * When I view the admin tools UI
     * Then I can see the security clearance "tool" link under the Classification header
     *
     * Given that I am a compliance officer
     * When I click the "Security Clearance" link
     * Then the security clearance UI is displayed
     * And the user search filter is empty
     * And the first page of users are displayed ordered alphabetically
     * And their current clearance is displayed
     *
     * Given that I am a compliance officer
     * When I view the user security clearances
     * Then the current security clearance of each displayed user is shown as the default selection in a drop down against the user name in the list
     * </pre>
     */
    @Test
    (
        groups = { "integration", "security-clearance"},
        description = "Check the security clearance page loads and contains an ordered list of users and clearances",
        dependsOnGroups = { "integration-dataSetup-rmSite" }
    )
    public void loadSecurityClearancePage()
    {
        sharePageNavigation.clickOnAdminTools().clickOnSecurityClearanceLink();
        assertEquals("Expected the filter to be initially empty.", "", securityClearancePage.getNameFilter());

        // Check the displayed users are ordered alphabetically.
        List<String> displayedUsers = securityClearancePage.getDisplayedUsers();
        checkUserOrdering(displayedUsers);

        // Check each user has a valid clearance.
        for (String clearance : securityClearancePage.getUserClearances().values())
        {
            assertTrue("Unrecognised security clearance: " + clearance, CONFIGURED_CLEARANCES.contains(clearance));
        }
    }

    /**
     * Check the admin user is not found by using the filter.
     *
     * <pre>
     * Given that there is no filter set
     * When I start to type a filter
     * Then the users shown in the list automatically change to match the filter
     *
     * Given that there is not filter set
     * When I enter a filter that has no results
     * Then no results are shown in the list
     * And a message is shown in the list indicating no results where found
     * </pre>
     */
    @Test
    (
        groups = { "integration", "security-clearance"},
        description = "Check the admin user is not found by using the filter",
        dependsOnGroups = { "integration-dataSetup-rmSite" }
    )
    public void adminIsNotShown()
    {
        openPage(securityClearancePage);
        assertFalse(
           "Admin user unexpectedly present in results.", 
           securityClearancePage.isUserShown("admin"));        
    }

    // TODO We should provide a filter term that produces no matched users and confirm that the table
    //      contains a message that there are no matching users.

    /**
     * Give a user clearance and check the page reflects this. Note that this test has a side effect of providing the
     * user with "Secret" clearance, and is used as a dependency of other tests.
     *
     * <pre>
     * Given that there is no filter set
     * When I start to type a filter
     * Then the users shown in the list automatically change to match the filter
     *
     * Given that I am a compliance officer
     * When view the available options for a users security clearance level
     * Then I am presented with all the classification hierarchies and "no clearance"
     *
     * Given that I am a compliance officer
     * And a user has no clearance
     * Or any security clearance level
     * When I set their clearance level to something new
     * Then I am asked to confirm the change
     * And the new security clearance is show in the list
     * </pre>
     */
    @Test
    (
        groups = { "integration", "security-clearance" },
        description = "Give a user clearance, reload the page and then revoke it again",
        dependsOnGroups = { "integration-dataSetup-rmSite", "integration-dataSetup-users" }
    )
    public void giveUserClearance()
    {
        String clearance = openPage(securityClearancePage).getUserSecurityClearance(USER1);
        assertEquals("No Clearance", clearance);

        List<String> options = securityClearancePage.getClearanceOptions(USER1);
        assertEquals(Arrays.asList("Top Secret", "Secret", "Confidential", "No Clearance"), options);

        clearance = securityClearancePage
                        .setClearance(USER1, "Secret")
                        .clickOnConfirm(securityClearancePage)
                        .getUserSecurityClearance(USER1);
        assertEquals("Secret", clearance);
    }

    /*
    Acceptance criteria from RM-1842 which are not covered here.

    Paging through user list

    Given that the first page of users is currently being displayed
    When I page through the list
    Then the next page is loaded

    Ordering

    When a list of users are displayed
    Then they are ordered in ascending alphabetical order by default

    When I change the order to descending
    Then they are reordered accordingly

    Changing the filter

    Given that there is a filter set
    And results are shown
    When I change the filter
    Then the users and groups shown in the list automatically change to match the changed filter

    Removing the filter

    Given that there is a filter set
    And results are shown
    When I delete the filter
    Then the first page of all results is shown
    And the filter value prompts the user to enter a filter
     */
}
