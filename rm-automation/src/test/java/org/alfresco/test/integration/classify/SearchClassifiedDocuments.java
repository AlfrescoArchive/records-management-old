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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.ContentService;
import org.alfresco.dataprep.SiteService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.console.users.SecurityClearancePage;
import org.alfresco.po.share.search.AdvancedSearchPage;
import org.alfresco.po.share.search.SearchResult;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.alfresco.test.DataPrepHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;


/**
 * Search classified document integration test
 *
 * @author Roy Wetherall
 * @since 2.4.a
 */
public class SearchClassifiedDocuments extends BaseTest
{
    /** test data */
    private static final String SEARCH_TEST_SITE_NAME = "SearchClassifiedDocuments";
    private static final String SEARCH_TEST_SITE_ID = "SearchClassifiedDocuments" + System.currentTimeMillis();
    private static final String SEARCH_TERM = "Giddywack";
    private static final String UNCLASSIFIED_DOCUMENT = SEARCH_TERM + " Unclassified";
    private static final String CONFIDENTIAL_DOCUMENT = SEARCH_TERM + " Confidential";
    private static final String SECRET_DOCUMENT = SEARCH_TERM + " Secret";
    private static final String TOP_SECRET_DOCUMENT = SEARCH_TERM + " Top Secret";
    private static final int NUMBER_OF_TEST_DOCUMENTS = 4;
    private static final String UNCLASSIFIED_USER = "SearchClassifiedDocumentsUnclassifiedUser";
    private static final String CONFIDENTIAL_USER = "SearchClassifiedDocumentsCondidentialUser";
    private static final String SECRET_USER = "SearchClassifiedDocumentsSecretUser";
    private static final String TOP_SECRET_USER = "SearchClassifiedDocumentsTopSecretUser";

    /** page objects */
    @Autowired private AdvancedSearchPage advancedSearchPage;
    @Autowired private DocumentLibrary documentLibrary;
    @Autowired private SecurityClearancePage securityClearancePage;

    /** data prep services */
    @Autowired private DataPrepHelper dataPrepHelper;
    @Autowired private SiteService siteService;
    @Autowired private UserService userService;
    @Autowired private ContentService contentService;

    /** predicate used to determine if documents are ready for search (required to wait for SOLR) */
    private final Predicate<WebDriver> documentsAvailableForSearch = (w) ->
    {
        return (NUMBER_OF_TEST_DOCUMENTS == openPage(advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults()
                        .size());
    };

    /**
     * Set up four users with different clearances and a site containing four documents at different classification levels.
     */
    @Test
    (
        groups = { "integration" }
    )
    public void setupTestData() throws Exception
    {
        // create users
        for (String user : Arrays.asList(UNCLASSIFIED_USER, CONFIDENTIAL_USER, SECRET_USER, TOP_SECRET_USER))
        {
            // create user
            dataPrepHelper.createUser(user);
        }

        // assign security clearance levels
        openPage(securityClearancePage)
            .setClearance(CONFIDENTIAL_USER, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage)
            .setClearance(SECRET_USER, SECRET_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage)
            .setClearance(TOP_SECRET_USER, TOP_SECRET_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage);

        // create collaboration site
        dataPrepHelper.createSite(SEARCH_TEST_SITE_NAME, SEARCH_TEST_SITE_ID);

        // invite users to site as site manager
        for (String user : Arrays.asList(UNCLASSIFIED_USER, CONFIDENTIAL_USER, SECRET_USER, TOP_SECRET_USER))
        {
            // invite user to site as site manager
            userService.inviteUserToSiteAndAccept(user, getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, "SiteManager");
        }

        // upload documents
        openPage(documentLibrary, SEARCH_TEST_SITE_ID);
        for (String documentName : Arrays.asList(UNCLASSIFIED_DOCUMENT, CONFIDENTIAL_DOCUMENT, SECRET_DOCUMENT, TOP_SECRET_DOCUMENT))
        {
            contentService.createDocument(getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, DocumentType.TEXT_PLAIN, documentName, TEST_CONTENT);
        }

        // classify documents
        openPage(documentLibrary, SEARCH_TEST_SITE_ID);
        classifyDocument(CONFIDENTIAL_DOCUMENT, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT);
        classifyDocument(SECRET_DOCUMENT, SECRET_CLASSIFICATION_LEVEL_TEXT);
        classifyDocument(TOP_SECRET_DOCUMENT, TOP_SECRET_CLASSIFICATION_LEVEL_TEXT);

        // wait for documents to be available for search
        new FluentWait<WebDriver>(Utils.getWebDriver())
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(1, TimeUnit.SECONDS)
            .until(documentsAvailableForSearch);
    }

    /**
     * Helper method to classify document
     */
    private void classifyDocument(String name, String level)
    {
        documentLibrary
            .getDocument(name)
            .clickOnClassify()
            .setLevel(level)
            .setClassifiedBy(CLASSIFIED_BY)
            .addReason(CLASSIFICATION_REASON)
            .clickOnClassify();
    }

    @Test
    (
       groups = { "integration" },
       description = "Search as unclassified user.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2143")
    public void unclassifiedUserSearch()
    {
        List<SearchResult> results =
                    openPage(UNCLASSIFIED_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();

        assertEquals("Unclassfied user should only see one document", 1, results.size());

        assertTrue( "Unclassified user should be able to see the unclassfied document",
                    searchResultsContainDocument(UNCLASSIFIED_DOCUMENT, results));
        assertFalse("Unclassified user should not be able to see the confidential document",
                    searchResultsContainDocument(CONFIDENTIAL_DOCUMENT, results));
        assertFalse("Unclassified user should not be able to see the secret document",
                    searchResultsContainDocument(SECRET_DOCUMENT, results));
        assertFalse("Unclassified user should not be able to see the top secret document",
                    searchResultsContainDocument(TOP_SECRET_DOCUMENT, results));

    }

    @Test
    (
       groups = { "integration" },
       description = "Search as confidential user.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2144")
    public void confidentialUserSearch()
    {
        List<SearchResult> results =
                    openPage(CONFIDENTIAL_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();

        assertEquals("Confidential user should only see two documents", 2, results.size());

        assertTrue( "Confidential user should be able to see the unclassfied document",
                    searchResultsContainDocument(UNCLASSIFIED_DOCUMENT, results));
        assertTrue("Confidential user should be able to see the confidential document",
                    searchResultsContainDocument(CONFIDENTIAL_DOCUMENT, results));
        assertFalse("Confidential user should not be able to see the secret document",
                    searchResultsContainDocument(SECRET_DOCUMENT, results));
        assertFalse("Confidential user should not be able to see the top secret document",
                    searchResultsContainDocument(TOP_SECRET_DOCUMENT, results));
    }

    @Test
    (
       groups = { "integration" },
       description = "Search as secret user.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2144")
    public void secretUserSearch()
    {
        List<SearchResult> results =
                    openPage(SECRET_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();

        assertEquals("Secret user should only see three documents", 3, results.size());

        assertTrue( "Secret user should be able to see the unclassfied document",
                    searchResultsContainDocument(UNCLASSIFIED_DOCUMENT, results));
        assertTrue("Secret user should be able to see the confidential document",
                    searchResultsContainDocument(CONFIDENTIAL_DOCUMENT, results));
        assertTrue("Secret user should be able to see the secret document",
                    searchResultsContainDocument(SECRET_DOCUMENT, results));
        assertFalse("Confidential user should not be able to see the top secret document",
                    searchResultsContainDocument(TOP_SECRET_DOCUMENT, results));
    }

    @Test
    (
       groups = { "integration" },
       description = "Search as top secret user.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2360")
    public void topSecretUserSearch()
    {
        List<SearchResult> results =
                    openPage(TOP_SECRET_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();

        assertEquals("Top secret user should see all four documents", 4, results.size());

        assertTrue( "Top Secret user should be able to see the unclassfied document",
                    searchResultsContainDocument(UNCLASSIFIED_DOCUMENT, results));
        assertTrue("Top Secret user should be able to see the confidential document",
                    searchResultsContainDocument(CONFIDENTIAL_DOCUMENT, results));
        assertTrue("Top Secret user should be able to see the secret document",
                    searchResultsContainDocument(SECRET_DOCUMENT, results));
        assertTrue("Top Secret user should be able to see the top secret document",
                    searchResultsContainDocument(TOP_SECRET_DOCUMENT, results));
    }

    /**
     * Helper method to determine whether the search results contain a specified document.
     * <p>
     * Document name is supplied as partial and 'contains' used to match.
     */
    private boolean searchResultsContainDocument(String partialDocumentName, List<SearchResult> searchResults)
    {
        boolean result = false;

        for (SearchResult searchResult : searchResults)
        {
            if (searchResult.getName().contains(partialDocumentName))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    @Test
    (
        groups = {"integration"},
        description = "Search results contain classification",
        dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2487, RM-2488")
    public void classifiedSearchLabels()
    {
        List<SearchResult> results =
                openPage(TOP_SECRET_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();

        assertTrue("Unclassified document should not contain a classified label",
                searchResultsContainClassifiedLabel(UNCLASSIFIED_DOCUMENT, results, ""));
        assertTrue("Confidential document should contain confidential level",
                searchResultsContainClassifiedLabel(CONFIDENTIAL_DOCUMENT, results, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT));
        assertTrue("Secret document should contain secret label",
                searchResultsContainClassifiedLabel(SECRET_DOCUMENT, results, SECRET_CLASSIFICATION_LEVEL_TEXT));
    }

    private boolean searchResultsContainClassifiedLabel(String partialDocumentName, List<SearchResult> searchResults, String classificationLevel)
    {
        boolean result = false;

        for (SearchResult searchResult : searchResults) {
            if (searchResult.getName().contains(partialDocumentName)) {
                result = searchResult.getClassifiedLabel().toLowerCase().equals(classificationLevel.toLowerCase());
                break;
            }
        }

        return result;
    }

    /** tear down data */
    @AfterSuite(alwaysRun=true)
    public void tearDownTestData() throws Exception
    {
        // delete site
        if (siteService.exists(SEARCH_TEST_SITE_ID, getAdminName(), getAdminPassword()))
        {
            siteService.delete(getAdminName(), getAdminPassword(), "", SEARCH_TEST_SITE_ID);
        }

        // delete users
        for (String user : Arrays.asList(UNCLASSIFIED_USER, CONFIDENTIAL_USER, SECRET_USER, TOP_SECRET_USER))
        {
            dataPrepHelper.deleteUser(user);
        }
    }
}