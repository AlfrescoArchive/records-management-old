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

import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.alfresco.dataprep.SiteService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.console.users.SecurityClearancePage;
import org.alfresco.po.share.search.AdvancedSearchPage;
import org.alfresco.po.share.search.SearchResult;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
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
 * @since 3.0
 */
public class SearchClassifiedDocuments extends BaseTest
{
    /** test data */
    private static final String SEARCH_TEST_SITE_NAME = "SearchClassifiedDocuments";
    private static final String SEARCH_TEST_SITE_ID = "SearchClassifiedDocuments" + System.currentTimeMillis();
    private static final String SEARCH_TERM = "Giddywack";
    private static final String UNCLASSIFIED_DOCUMENT = SEARCH_TERM + " Unclassified";
    private static final String LOW_CLASSIFIED_DOCUMENT = SEARCH_TERM + " Confidential";
    private static final String MID_CLASSIFIED_DOCUMENT = SEARCH_TERM + " Secret";
    private static final String HIGH_CLASSIFIED_DOCUMENT = SEARCH_TERM + " Top Secret";
    private static final int NUMBER_OF_TEST_DOCUMENTS = 4;
    private static final String UNCLASSIFIED_USER = "SearchClassifiedDocumentsUnclassifiedUser";
    private static final String CONFIDENTIAL_USER = "SearchClassifiedDocumentsCondidentialUser";
    private static final String SECRET_USER = "SearchClassifiedDocumentsSecretUser";
    private static final String TOP_SECRET_USER = "SearchClassifiedDocumentsTopSecretUser";    
    
    /** page objects */
    @Autowired private AdvancedSearchPage advancedSearchPage;
    @Autowired private MySitesDashlet mySitesDashlet;
    @Autowired private DocumentLibrary documentLibrary;
    @Autowired private SecurityClearancePage securityClearancePage;
    
    /** data prep services */
    @Autowired private DataPrepHelper dataPrepHelper;
    @Autowired private SiteService siteService;
    @Autowired private UserService userService;

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
     * setup test data
     */
    @Test
    (
        groups = { "integration" }
    )
    public void setupTestData() throws Exception
    {        
        // create users
        dataPrepHelper.createUser(UNCLASSIFIED_USER);
        dataPrepHelper.createUser(CONFIDENTIAL_USER);
        dataPrepHelper.createUser(SECRET_USER);
        dataPrepHelper.createUser(TOP_SECRET_USER);
        
        // assign security clearance levels
        openPage(securityClearancePage)
            .setClearance(CONFIDENTIAL_USER, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage)
            .setClearance(SECRET_USER, SECRET_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage)
            .setClearance(TOP_SECRET_USER, TOP_SECRET_CLASSIFICATION_LEVEL_TEXT)
            .clickOnConfirm(securityClearancePage);
        
        // create collaboration site
        openPage(userDashboardPage);
        mySitesDashlet
            .clickOnCreateSite()
            .setSiteName(SEARCH_TEST_SITE_NAME)
            .setSiteURL(SEARCH_TEST_SITE_ID)
            .setSiteDescription(DESCRIPTION)
            .clickOnOk();
        
        // invite users to site as site managers
        userService.inviteUserToSiteAndAccept(UNCLASSIFIED_USER, getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, "SiteManager");
        userService.inviteUserToSiteAndAccept(CONFIDENTIAL_USER, getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, "SiteManager");
        userService.inviteUserToSiteAndAccept(SECRET_USER, getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, "SiteManager");
        userService.inviteUserToSiteAndAccept(TOP_SECRET_USER, getAdminName(), getAdminPassword(), SEARCH_TEST_SITE_ID, "SiteManager");
        
        // upload documents
        openPage(documentLibrary, SEARCH_TEST_SITE_ID);
        documentLibrary
            .getToolbar().clickOnUpload()
            .uploadFile(Utils.createTempFile(UNCLASSIFIED_DOCUMENT), documentLibrary)
            .getToolbar().clickOnUpload()
            .uploadFile(Utils.createTempFile(LOW_CLASSIFIED_DOCUMENT), documentLibrary)
            .getToolbar().clickOnUpload()
            .uploadFile(Utils.createTempFile(MID_CLASSIFIED_DOCUMENT), documentLibrary)
            .getToolbar().clickOnUpload()
            .uploadFile(Utils.createTempFile(HIGH_CLASSIFIED_DOCUMENT), documentLibrary);
        
        // classify documents
        classifyDocument(LOW_CLASSIFIED_DOCUMENT, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT);
        classifyDocument(MID_CLASSIFIED_DOCUMENT, SECRET_CLASSIFICATION_LEVEL_TEXT);
        classifyDocument(HIGH_CLASSIFIED_DOCUMENT, TOP_SECRET_CLASSIFICATION_LEVEL_TEXT);
                
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
    @AlfrescoTest(jira="RM-2104")
    public void unclassifiedUserSearch()
    {
        List<SearchResult> results = 
                    openPage(UNCLASSIFIED_USER, DEFAULT_PASSWORD, advancedSearchPage)
                        .setKeywords(SEARCH_TERM)
                        .clickOnSearch()
                        .getSearchResults();
        
        Assert.assertTrue("Unclassfied user should only see one document", 1 == results.size());        
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
                
        Assert.assertTrue("Confidential user should only see two documents", 2 == results.size());        
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
                
        Assert.assertTrue("Secret user should only see three documents", 3 == results.size());        
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
                
        Assert.assertTrue("Top secret user should only see four documents", 4 == results.size());        
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
        dataPrepHelper.deleteUser(UNCLASSIFIED_USER);
        dataPrepHelper.deleteUser(CONFIDENTIAL_USER);
        dataPrepHelper.deleteUser(SECRET_USER);
        dataPrepHelper.deleteUser(TOP_SECRET_USER);
    }
}