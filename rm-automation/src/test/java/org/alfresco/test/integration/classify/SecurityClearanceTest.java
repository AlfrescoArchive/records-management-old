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
import org.alfresco.po.share.console.users.UserProfilePage;
import org.alfresco.po.share.page.SharePageNavigation;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * UI Integration tests for the Security Clearance admin page.
 *
 * @author tpage
 * @author David Webster
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
    @Autowired
    private UserProfilePage userProfilePage;

    /**
     * Check that the displayed users are sorted correctly.
     */
    protected void checkUserOrdering(List<String> userNames)
    {
        String previousUserName = "";
        for (String userName : userNames)
        {
            assertTrue("Expected users to be sorted by user id, but found '"+previousUserName+"' and then '"+userName+"'",
                        userName.toLowerCase().compareTo(previousUserName.toLowerCase()) >= 0);
            previousUserName = userName;
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
        groups = { "integration" },
        description = "Check the security clearance page loads and contains an ordered list of users and clearances",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS", "GROUP_RM_MANAGER_EXISTS", "GROUP_UNCLEARED_USER_EXISTS" }
    )
    public void loadSecurityClearancePage()
    {
        sharePageNavigation.clickOnAdminTools().clickOnSecurityClearanceLink();
        assertEquals("Expected the filter to be initially empty.", "", securityClearancePage.getNameFilter());

        // check the user names on the page
        List<String> userNames = securityClearancePage.getUserNames();
        assertTrue(userNames.contains(RM_MANAGER));
        assertTrue(userNames.contains(UNCLEARED_USER));
        checkUserOrdering(userNames);

        // check the individual users are displayed
        assertTrue(securityClearancePage.isUserShown(RM_MANAGER));
        assertTrue(securityClearancePage.isUserShown(UNCLEARED_USER));

        // Check each user has a valid clearance.
        for (String clearance : securityClearancePage.getUserClearances().values())
        {
            assertTrue("Unrecognised security clearance: " + clearance, CONFIGURED_CLEARANCES.contains(clearance));
        }
    }

    /**
     * When I view the classification security clearances
     * Then I am not able to see or edit the admins users security clearance
     */
    @Test
    (
        groups = { "integration", "ignored" }, //TODO Re-enable (and de-quarantine) when RM-2210 is fixed.
        description = "Check the admin user is not found by using the filter",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS" }
    )
    public void adminIsNotShown()
    {
        openPage(securityClearancePage);
        assertFalse(
                "Admin user unexpectedly present in results.",
                securityClearancePage.isUserShown("admin"));

        securityClearancePage.setNameFilter("admin");
        assertTrue(securityClearancePage.isEmpty());

    }

    /**
     * Give a user clearance and check the page reflects this. Note that this test has a side effect of providing the
     * RM_MANAGER with "Secret" clearance, and is used as a dependency of other tests.
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
        groups = { "integration", "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE" },
        description = "Give a user clearance, reload the page and then revoke it again",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS", "GROUP_RM_MANAGER_EXISTS" }
    )
    public void giveUserClearance()
    {
        String clearance = openPage(securityClearancePage).getUserSecurityClearance(RM_MANAGER);
        assertEquals("No Clearance", clearance);

        List<String> options = securityClearancePage.getClearanceOptions(RM_MANAGER);
        assertEquals(Arrays.asList("Top Secret", "Secret", "Confidential", "No Clearance"), options);

        clearance = securityClearancePage
                        .setClearance(RM_MANAGER, "Secret")
                        .clickOnConfirm(securityClearancePage)
                        .getUserSecurityClearance(RM_MANAGER);
        assertEquals("Secret", clearance);
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
        groups = { "integration"},
        description = "Check the admin user is not found by using the filter",
        dependsOnGroups = { "GROUP_RM_MANAGER_EXISTS", "GROUP_UNCLEARED_USER_EXISTS" }
    )
    public void userNameFilter()
    {
        openPage(securityClearancePage);

        // assert that the filter is empty
        assertTrue(securityClearancePage.getNameFilter().isEmpty());

        // set filter
        securityClearancePage.setNameFilter(RM_MANAGER);

        // assert that the filter has been applied
        assertFalse(securityClearancePage.getNameFilter().isEmpty());
        assertEquals(1, securityClearancePage.getUserNames().size());
        assertTrue(securityClearancePage.isUserShown(RM_MANAGER));
        assertFalse(securityClearancePage.isUserShown(UNCLEARED_USER));

        // partial filter
        securityClearancePage.clearNameFilter();
        securityClearancePage.setNameFilter("_user");

        // assert that the filter has been cleared
        assertFalse(securityClearancePage.getNameFilter().isEmpty());
        assertTrue(securityClearancePage.getUserNames().size() >= 2);
        assertTrue(securityClearancePage.isUserShown(RM_MANAGER));
        assertTrue(securityClearancePage.isUserShown(UNCLEARED_USER));

        // invalid filter
        securityClearancePage.clearNameFilter();
        securityClearancePage.setNameFilter("monkey");

        // assert that no results are shown
        assertTrue(securityClearancePage.isEmpty());
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
    */

    /**
     *
     * Access user profile page
     * Given that I am viewing the security clearance management page
     * When I click on a user in the list (icon or name)
     * Then the users profile page is displayed
     *
     * Closing the user profile page
     * Given that I am viewing a users profile via the security clearance management page
     * When I click on the "Go Back" button
     * Then I am returned back to the security clearance management page where the user filter is in the same state as it was when we left the page for the users profile
     */
    @Test (
            groups = {"integration"},
            description = "Check access to the user profile page works as expected",
            dependsOnGroups = {"GROUP_RM_SITE_EXISTS", "GROUP_RM_MANAGER_EXISTS"}
    )
    public void userProfileLink()
    {
        openPage(securityClearancePage);
        // assert that the filter is empty
        assertTrue(securityClearancePage.getNameFilter().isEmpty());

        // Click on a user
        securityClearancePage.setNameFilter(RM_MANAGER);
        securityClearancePage.clickOnUser(RM_MANAGER);

        // Check we're on the right profile page:
        assertEquals(userProfilePage.getUserName(), RM_MANAGER);

        // Go back and verify filter still applied.
        userProfilePage.clickOnBack();

        // FIXME: previous filter should be displayed, but won't due to an Aikau bug. See AKU-415.
        // This temporary line checks the page has loaded replace with one below once AKU-415 is fixed.
        assertTrue(securityClearancePage.getNameFilter().isEmpty());
        // assertEquals(securityClearancePage.getNameFilter(), RM_MANAGER);
    }
}
