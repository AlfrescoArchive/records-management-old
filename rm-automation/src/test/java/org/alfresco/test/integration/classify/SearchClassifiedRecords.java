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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordIndicators;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.rm.search.RecordsSearch;
import org.alfresco.po.rm.search.SearchConstants.SavedSearch;
import org.alfresco.po.rm.search.SearchConstants.SearchOption;
import org.alfresco.po.rm.search.SearchConstants.SearchOptionType;
import org.alfresco.po.rm.search.SearchRecordsResults;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * tests the search of classified records in Records Search page
 * @author Oana Nechiforescu
 */
public class SearchClassifiedRecords extends BaseTest
{
    /** The number of test records created by this set of tests in {@link #createClassifiedRecordsForSearch}. */
    private static final int NUMBER_OF_TEST_RECORDS = 3;

    @Autowired
    private FilePlan filePlan;

    @Autowired
    private RecordsSearch recordsSearch;

    @Autowired
    private SearchRecordsResults searchRecordsResults;

    @Autowired
    private ClassifyContentDialog classifyContentDialog;

    /** Predicate used to determine if records are ready for search (required to wait for SOLR) */
    private final Predicate<WebDriver> recordsAvailableForSearch = (w) ->
    {
        return (NUMBER_OF_TEST_RECORDS == openPage(recordsSearch)
                        .setKeywords("*" + RECORD_SEARCH_SUFFIX + "*")
                        .checkResultsComponentsOption(SearchOption.INCLUDE_INCOMPLETE, SearchOptionType.COMPONENTS, true)
                        .clickOnSearch()
                        .getResults()
                        .size());
    };

    /**
     * User with 'no clearance' clearance can view searched records with level at most 'no clearance'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2145">RM-2145</a><pre>
     * Given that I am a RM user with no security clearance
     * When I search for records
     * Then I can see all unclassified records in the search results
     * And I cannot see any classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'no clearance' clearance can view searched records with level at most 'no clearance'.",
            dependsOnGroups = { "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2145")
    public void unclearedUserSearchResultsForClassifiedData()
    {
        // navigate to Records Search
        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, recordsSearch);
        // include incomplete records
        recordsSearch.checkResultsComponentsOption(SearchOption.INCLUDE_INCOMPLETE, SearchOptionType.COMPONENTS, true);
        recordsSearch.clickOnSearch();

        assertFalse("The record with top secret security clearance is displayed in search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertFalse("The record with secret security clearance is displayed in search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertFalse("The record with confidential security clearance is displayed in search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'no clearance' clearance can view records from saved searches with level at most 'no clearance'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2145">RM-2145</a><pre>
     * Given that I am a RM user with no security clearance
     * When I search for records using the saved searches
     * Then I can see all unclassified records in the search results
     * And can not see any classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'no clearance' clearance can view saved searches records with level at most 'no clearance'.",
            dependsOnGroups = { "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2145")
    public void unclearedUserSearchResultsForSavedSearch()
    {
        // navigate to Records Search
        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, recordsSearch);
        //select Incomplete Records saved search
        recordsSearch.selectSavedRecordsSearch(SavedSearch.INCOMPLETE_RECORDS);
        recordsSearch.clickOnSearch();

        assertFalse("The record with top secret security clearance is displayed in Incomplete Records filter search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertFalse("The record with secret security clearance is displayed in Incomplete Records filter search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertFalse("The record with confidential security clearance is displayed in Incomplete Records filter search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in Incomplete Records filter search results for uncleared user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'no clearance' clearance can view records from File Plan saved searches with level at most 'no clearance'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2367">RM-2367</a><pre>
     * Given that I am a RM user with no security clearance
     * When I search for records using the File Plan saved searches
     * Then I can see all unclassified records in the search results
     * And can not see any classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'no clearance' clearance can view File Plan saved searches records with level at most 'no clearance'.",
            dependsOnGroups = { "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2367")
    public void unclearedUserFilePlanSearchResultsForSavedSearch()
    {
        // navigate to File Plan
        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        //select Incomplete Records saved search
        recordsSearch.selectSavedSearchFromFilePlan(SavedSearch.INCOMPLETE_RECORDS);

        assertNull("The record with top secret security clearance is displayed in Incomplete Records File Plan search results for uncleared user.", filePlan.getRecord(TOP_SECRET_RECORD_SEARCH));
        assertNull("The record with secret security clearance is displayed in Incomplete Records File Plan search results for uncleared user.", filePlan.getRecord(CLASSIFIED_RECORD));
        assertNull("The record with confidential security clearance is displayed in Incomplete Records File Plan search results for uncleared user.", filePlan.getRecord(CONFIDENTIAL_RECORD_SEARCH));
        assertNotNull("The record with unclassified security clearance is not displayed in Incomplete Records File Plan search results for uncleared user.", filePlan.getRecord(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'secret' clearance can view searched records with level at most 'secret'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2146">RM-2146</a><pre>
     * Given that I am a RM user with mid level security clearance
     * When I search for records
     * Then I can see all unclassified records in the search results
     * And I can see all classified records with a classification less than or equal to my security level in the search results
     * And I can not see any classified records with a classification higher than my security level in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'secret' clearance can view searched records with level at most 'secret'.",
            dependsOnGroups = { "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE", "GROUP_RM_MANAGER_READ_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
     @AlfrescoTest(jira="RM-2146")
    public void secretUserSearchResultsForClassifiedData()
    {
        // navigate to Records Search
        openPage(RM_MANAGER, DEFAULT_PASSWORD, recordsSearch);
        // include incomplete records
        recordsSearch.checkResultsComponentsOption(SearchOption.INCLUDE_INCOMPLETE, SearchOptionType.COMPONENTS, true);
        recordsSearch.clickOnSearch();

        assertFalse("The record with top secret security clearance is displayed in search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertTrue("The record with secret security clearance is not displayed in search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertTrue("The record with confidential security clearance is not displayed in search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

     /**
     * User with 'secret' clearance can view records from saved searches with level at most 'secret'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2146">RM-2146</a><pre>
     * Given that I am a RM user with mid level security clearance
     * When I search for records using saved searches
     * Then I can see all unclassified records in the search results
     * And I can see all classified records with a classification less than or equal to my security level in the search results
     * And I can not see any classified records with a classification higher than my security level in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'secret' clearance can view records from saved searches with level at most 'secret'.",
            dependsOnGroups = { "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE", "GROUP_RM_MANAGER_READ_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2146")
    public void secretUserSearchResultsForSavedSearch()
    {
        // navigate to Records Search
        openPage(RM_MANAGER, DEFAULT_PASSWORD, recordsSearch);
        //select Incomplete Records saved search
        recordsSearch.selectSavedRecordsSearch(SavedSearch.INCOMPLETE_RECORDS);
        recordsSearch.clickOnSearch();

        assertFalse("The record with top secret security clearance is displayed in Incomplete Records filter search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertTrue("The record with secret security clearance is not displayed in Incomplete Records filter search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertTrue("The record with confidential security clearance is not displayed in Incomplete Records filter search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in Incomplete Records filter search results for secret user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

     /**
     * User with 'secret' clearance can view records from File Plan saved searches with level at most 'secret'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2367">RM-2367</a><pre>
     * Given that I am a RM user with mid level security clearance
     * When I search for records using the File Plan saved searches
     * Then I can see all unclassified records in the search results
     * And I can see all classified records with a classification less than or equal to my security level in the search results
     * And I can not see any classified records with a classification higher than my security level in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'secret' clearance can view records from File Plan saved searches with level at most 'secret'.",
            dependsOnGroups = { "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE", "GROUP_RM_MANAGER_READ_CATEGORY_ONE", "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2367")
    public void secretUserFilePlanSearchResultsForSavedSearch()
    {
        // navigate to File Plan
        openPage(RM_MANAGER, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        //select Incomplete Records saved search
        recordsSearch.selectSavedSearchFromFilePlan(SavedSearch.INCOMPLETE_RECORDS);

        assertNull("The record with top secret security clearance is displayed in Incomplete Records File Plan search results for secret user.", filePlan.getRecord(TOP_SECRET_RECORD_SEARCH));
        assertNotNull("The record with secret security clearance is not displayed in Incomplete Records File Plan search results for secret user.", filePlan.getRecord(CLASSIFIED_RECORD));
        assertNotNull("The record with confidential security clearance is not displayed in Incomplete Records File Plan search results for secret user.", filePlan.getRecord(CONFIDENTIAL_RECORD_SEARCH));
        assertNotNull("The record with unclassified security clearance is not displayed in Incomplete Records File Plan search results for secret user.", filePlan.getRecord(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'top secret' clearance can view all searched classified and unclassified records.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2361">RM-2361</a><pre>
     * Given that I am a RM user with the highest security clearance
     * When I search for records
     * Then I can see all the unclassified records in the search results
     * And I can see all the classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'top secret' clearance can view all searched classified and unclassified records.",
            dependsOnGroups = { "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2361")
    public void topSecretUserSearchResultsForClassifiedData()
    {
        // navigate to Records Search
        openPage(recordsSearch);
        // include incomplete records
        recordsSearch.checkResultsComponentsOption(SearchOption.INCLUDE_INCOMPLETE, SearchOptionType.COMPONENTS, true);
        recordsSearch.clickOnSearch();

        assertTrue("The record with top secret security clearance is not displayed in search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertTrue("The record with secret security clearance is not displayed in search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertTrue("The record with confidential security clearance is not displayed in search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'top secret' clearance can view all classified and unclassified records from saved searches.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2361">RM-2361</a><pre>
     * Given that I am a RM user with the highest security clearance
     * When I search for records from saved searches
     * Then I can see all the unclassified records in the search results
     * And I can see all the classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'top secret' clearance can view all searched classified and unclassified records from saved searches.",
            dependsOnGroups = { "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2361")
    public void topSecretUserSearchResultsForSavedSearch()
    {
        // navigate to Records Search
        openPage(recordsSearch);
        //select Incomplete Records saved search
        recordsSearch.selectSavedRecordsSearch(SavedSearch.INCOMPLETE_RECORDS);
        recordsSearch.clickOnSearch();

        assertTrue("The record with top secret security clearance is not displayed in Incomplete Records filter search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(TOP_SECRET_RECORD_SEARCH));
        assertTrue("The record with secret security clearance is not displayed in Incomplete Records filter search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(CLASSIFIED_RECORD));
        assertTrue("The record with confidential security clearance is not displayed in Incomplete Records filter search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(CONFIDENTIAL_RECORD_SEARCH));
        assertTrue("The record with unclassified security clearance is not displayed in Incomplete Records filter search results for top secret user.", searchRecordsResults.recordIsDisplayedInResults(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * User with 'top secret' clearance can view all classified and unclassified records from File Plan saved searches.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2367">RM-2367</a><pre>
     * Given that I am a RM user with the highest security clearance
     * When I search for records from File Plan saved searches
     * Then I can see all the unclassified records in the search results
     * And I can see all the classified records in the search results
     * </pre>
     */
    @Test(
            groups = {"integration"},
            description = "User with 'top secret' clearance can view all searched classified and unclassified records from File Plan saved searches.",
            dependsOnGroups = { "GROUP_CLASSIFIED_RECORD_EXISTS", "GROUP_SEARCH_RECORDS_EXIST" }
    )
    @AlfrescoTest(jira="RM-2367")
    public void topSecretUserFilePlanSearchResultsForSavedSearch()
    {
        // navigate to Records Search
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        //select Incomplete Records saved search
        recordsSearch.selectSavedSearchFromFilePlan(SavedSearch.INCOMPLETE_RECORDS);

        assertNotNull("The record with top secret security clearance is not displayed in Incomplete Records File Plan search results for top secret user.", filePlan.getRecord(TOP_SECRET_RECORD_SEARCH));
        assertNotNull("The record with secret security clearance is not displayed in Incomplete Records File Plan search results for top secret user.", filePlan.getRecord(CLASSIFIED_RECORD));
        assertNotNull("The record with confidential security clearance is not displayed in Incomplete Records File Plan search results for top secret user.", filePlan.getRecord(CONFIDENTIAL_RECORD_SEARCH));
        assertNotNull("The record with unclassified security clearance is not displayed in Incomplete Records File Plan search results for top secret user.", filePlan.getRecord(UNCLASSIFIED_RECORD_SEARCH));
    }

    /**
     * Create top secret, confidential and unclassified records for search.
     */
    @Test(
            groups = { "integration", "GROUP_SEARCH_RECORDS_EXIST" },
            description = "Create and classify records for search",
            dependsOnGroups = { "GROUP_RECORD_FOLDER_SEARCH_EXISTS"}
    )
    public void createClassifiedRecordsForSearch()
    {
        // upload records
        openPage(filePlan, RM_SITE_ID,
                createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_SEARCH));
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(TOP_SECRET_RECORD_SEARCH);
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(UNCLASSIFIED_RECORD_SEARCH);
        filePlan.getToolbar()
                .clickOnFile()
                .clickOnElectronic()
                .uploadFile(CONFIDENTIAL_RECORD_SEARCH);

        // classify record to Top Secret clearance level
        Record topSecretRecord = filePlan.getRecord(TOP_SECRET_RECORD_SEARCH);
        topSecretRecord.clickOnLink();
        topSecretRecord.clickOnActionFromDetailsPage(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(TOP_SECRET_CLASSIFICATION_LEVEL_TEXT)
                .setClassifiedBy(CLASSIFIED_BY)
                .setAgency(CLASSIFICATION_AGENCY)
                .addReason(CLASSIFICATION_REASON)
                .clickOnClassifyFromDetailsPage();
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_SEARCH));
        filePlan.getRecord(TOP_SECRET_RECORD_SEARCH)
                .hasIndicator(RecordIndicators.CLASSIFIED);

        // classify record to Confidential clearance level
        Record confidentialRecord = filePlan.getRecord(CONFIDENTIAL_RECORD_SEARCH);
        confidentialRecord.clickOnLink();
        confidentialRecord.clickOnActionFromDetailsPage(RecordActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
                .setClassifiedBy(CLASSIFIED_BY)
                .setAgency(CLASSIFICATION_AGENCY)
                .addReason(CLASSIFICATION_REASON)
                .clickOnClassifyFromDetailsPage();
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_SEARCH));
        filePlan.getRecord(CONFIDENTIAL_RECORD_SEARCH)
                .hasIndicator(RecordIndicators.CLASSIFIED);

        // wait for documents to be available for search
        new FluentWait<WebDriver>(Utils.getWebDriver())
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(1, TimeUnit.SECONDS)
            .until(recordsAvailableForSearch);
    }
}
