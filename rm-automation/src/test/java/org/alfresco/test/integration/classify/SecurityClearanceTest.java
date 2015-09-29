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

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.users.SecurityClearancePage;
import org.alfresco.po.share.console.users.UserProfilePage;
import org.alfresco.po.share.page.SharePageNavigation;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * UI Integration tests for the Security Clearance admin page.
 *
 * @author tpage
 * @author David Webster
 * @since 2.4.a
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
     * Check that the displayed users are ordered correctly by default.
     */
    protected void checkUserOrdering(List<String> names)
    {
        String previousName = "";
        for (String name : names)
        {
            assertTrue("Expected users to be sorted case-insensitively by first name then surname, but found '"+previousName+"' and then '"+name+"'",
                        name.toLowerCase().compareTo(previousName.toLowerCase()) >= 0);
            previousName = name;
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
        List<String> names = securityClearancePage.getNames();
        checkUserOrdering(names);

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
     * Then I am not able to edit the admin's security clearance
     */
    @Test
    (
        groups = { "integration" },
        description = "Check the admin user is not found by using the filter",
        dependsOnGroups = { }
    )
    public void adminIsNotShown()
    {
        openPage(securityClearancePage);
        assertFalse("There should not be a dropdown to set the admin's security clearance.",
                    securityClearancePage.isUsersClearanceModifiable("admin"));
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
     *
     * Given that I cancel the security clearance change
     * Then the previous security clearance level value is still set
     * See RM-2231
     * </pre>
     */
    @Test
    (
        groups = { "integration", "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE" },
        description = "Give a user clearance, reload the page and then revoke it again",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS", "GROUP_RM_MANAGER_EXISTS" }
    )
    @AlfrescoTest(jira = "RM-2231")
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

        // check that the security clearance level returns to the previous value after cancelling the change
        clearance = securityClearancePage
                        .setClearance(RM_MANAGER, "Top Secret")
                        .clickOnCancel(securityClearancePage)
                        .getUserSecurityClearance(RM_MANAGER);
        assertEquals("Secret", clearance);
    }

    /**
     * Set the security clearance of a user with username containing space, filter using special characters.
     *
     * <pre>
     * Given that I change the security level of a user with username containing space
     * Then its security clearance is set successful
     * See RM-2547
     *
     * Given that I filter the users using a special character
     * Then the expected set of results is displayed
     * See RM-2217
     *
     * </pre>
     */
    @Test
    (
        groups = { "integration"},
        description = "Set the security clearance of a user with username containing space, filter using special caracters.",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS", "GROUP_SPACE_USER_EXISTS" }
    )
    @AlfrescoTest(jira = "RM-2547,RM-2217")
    public void testSecurityClearancePageSpecialCases()
    {
        // open Security Clearance page
        openPage(securityClearancePage);

        // set the security clearance of a user having its username containing space
        String setSecurityClearance = securityClearancePage
                .setClearance(SPACE_USER, "Confidential").clickOnConfirm(securityClearancePage)
                .getUserSecurityClearance(SPACE_USER);

        // check that the security clearance has been changed successfully
        assertEquals("Confidential", setSecurityClearance);

        // set the filter to "/" in order to verify that the filter works for special characters too
        securityClearancePage.setNameFilter("/");
        assertFalse(securityClearancePage.isErrorLoadingDataDisplayed());
        assertTrue(securityClearancePage.isEmpty());

        // assert that no result is displayed
        assertTrue(securityClearancePage.getUserNames().isEmpty());
    }

    /**
     * Check the Security Clearance page pagination cases.
     *
     * <pre>
     * Given that I click on the select page drop down
     * Then the available pages are revealed
     * See RM-2223
     *
     * Given that I change the value of the items per page
     * And I refresh the page
     * Then the selected value is the previous one which is sent from the URL
     * See RM-2266
     *
     * Given that I set currentPage and currentPageSize attributes to invalid values
     * Then the error page is displayed
     * See RM-2269
     *
     * </pre>
     */
    @Test
    (
        groups = { "integration"},
        description = "Check the Security Clearance page pagination cases.",
        dependsOnGroups = {"GROUP_RM_SITE_EXISTS"}
    )
    @AlfrescoTest(jira = "RM-2223,RM-2266,RM-2269")
    public void testSecurityClearancePagination()
    {
        // open Security Clearance page
        openPage(securityClearancePage);

        // check that the select page drop down reveals the pages to navigate to on click
        assertTrue(securityClearancePage.getNumberOfPages() >= 1);

        // verify the items per page options
        checkTheResultsPerPageOptionsAre(Arrays.asList("25", "50", "75", "100"));

        // assert the default items per page value to be 25
        assertTrue(securityClearancePage.getSelectedItemsPerPageValue().startsWith("25"));
        // select a different value
        securityClearancePage.selectResultsPerPageOption("50");
    // assert the set value to be 50
        assertTrue(securityClearancePage.getSelectedItemsPerPageValue().startsWith("50"));
        securityClearancePage.refreshCurrentPage();
        // assert the set value to be 50
        assertTrue(securityClearancePage.getSelectedItemsPerPageValue().startsWith("50"));
        // change the URL in order for the currentPage and currentPageSize to get invalid values
        openPage(securityClearancePage);
        Utils.getWebDriver().get(Utils.getWebDriver().getCurrentUrl() + "#currentPage=1000&currentPageSize=1000");
        // check that the error page is displayed
        assertTrue(securityClearancePage.isErrorLoadingDataDisplayed());
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
     *
     * Given that the filter has been removed
     * Then all the users are displayed
     * See RM-2517
     * </pre>
     */
    @Test
    (
        groups = { "integration"},
        description = "Check the admin user is not found by using the filter",
        dependsOnGroups = { "GROUP_RM_MANAGER_EXISTS", "GROUP_UNCLEARED_USER_EXISTS" }
    )
    @AlfrescoTest(jira = "RM-2517")
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

        // remove the filter
        securityClearancePage.clearNameFilter();
        assertTrue(securityClearancePage.getNameFilter().isEmpty());

        // partial filter
        securityClearancePage.setNameFilter("_user");

        // assert that the filter has been applied
        assertFalse(securityClearancePage.getNameFilter().isEmpty());
        assertTrue(securityClearancePage.getUserNames().size() >= 2);
        assertTrue(securityClearancePage.isUserShown(RM_MANAGER));
        assertTrue(securityClearancePage.isUserShown(UNCLEARED_USER));

        // invalid filter
        securityClearancePage.clearNameFilter();
        securityClearancePage.setNameFilter("monkey");

        // assert that no results are shown
        assertTrue(securityClearancePage.isEmpty());

        // clear filter and wait for the empty filter to be taken into consideration
         securityClearancePage.applyEmptyFilterAfterNoData();

        // assert that the empty filter has been applied and that all the users are displayed
        assertTrue(securityClearancePage.getUserNames().size() > 2);
        assertTrue(securityClearancePage.isUserShown(RM_MANAGER));
        assertTrue(securityClearancePage.isUserShown(UNCLEARED_USER));

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

    RM-2612 has to be covered as well
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

        assertEquals(securityClearancePage.getNameFilter(), RM_MANAGER);
    }

    private void checkTheResultsPerPageOptionsAre(List<String> expectedValues)
    {
        assertEquals(expectedValues, securityClearancePage.getResultsPerPageOptions());
    }

}
