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
package org.alfresco.test.integration.record;

import static org.junit.Assert.assertNotNull;

import org.alfresco.dataprep.UserService;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * @author Roy Wetherall
 * @since 2.4.a
 */
public class DeclareInPlaceRecords extends BaseTest
{
    /** page objects */
    @Autowired private DocumentLibrary documentLibrary;
    
    /** data prep services */
    @Autowired private UserService userService;
    
    /**
     * Given that a user is the owner of a document
     * And that user has been deleted
     * When admin tries to declare the document as a record
     * Then the document becomes an inplace record
     */
    @Test
    (
        groups = { "integration"},
        description = "Use the classify content dialog to classify a document.",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS", "GROUP_COLLABORATION_SITE_EXISTS" }
    ) 
    @AlfrescoTest(jira="RM-2584")
    public void DeclareRecordOwnerDeleted() throws Exception
    {
        // create test user
        String userName = generateText();
        userService.create(getAdminName(), getAdminPassword(), userName, DEFAULT_PASSWORD, DEFAULT_EMAIL, FIRST_NAME, LAST_NAME);
        userService.inviteUserToSiteAndAccept(getAdminName(), getAdminPassword(), userName, COLLAB_SITE_ID, "SiteManager");
        
        // upload a new document as the user 
        String fileName = generateText();
        openPage(userName, DEFAULT_PASSWORD, documentLibrary, COLLAB_SITE_ID)
            .getToolbar().clickOnUpload()
            .uploadFile(fileName, documentLibrary)
            .getSharePageNavigation().logout();
        
        // delete the test user
        userService.delete(getAdminName(), getAdminPassword(), userName);
        
        // get the document
        Document document = openPage(documentLibrary, COLLAB_SITE_ID)
            .getDocument(fileName);
        
        assertNotNull(document);
        
        // declare document as record
        document.clickOnDeclareAsRecord();
        
        // assert that the document is now a record
        assertNotNull(documentLibrary.getInplaceRecord(fileName));
    }
}
