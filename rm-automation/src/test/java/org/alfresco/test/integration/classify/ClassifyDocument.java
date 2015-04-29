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

import static org.junit.Assert.assertTrue;

import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifyDocument extends BaseTest
{
    /**
     * Doc Lib
     */
    @Autowired
    private DocumentLibrary documentLibrary;

    /**
     * Doc Details page
     */
    @Autowired
    private DocumentDetails documentDetails;

    /**
     * Check that the classify action is available on the document library page and the document preview page.
     *
     * Nb. There is no explicit acceptance criteria for this, but it is clearly required for RM-2078.
     */
    @Test
    (
        groups = { "integration", "classification" },
        description = "Verify classify action is available",
        dependsOnGroups = { "integration-dataSetup-collab" }
    )
    public void checkClassifyActionAvailable()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID, "documentlibrary");
        Document document = documentLibrary.getDocument(DOCUMENT);

        // verify document actions
        assertTrue(document.isActionClickable(DocumentActions.CLASSIFY));

        // navigate to the document details page
        document.clickOnLink();

        // verify that all the expected actions are available
        assertTrue(documentDetails.getDocumentActionsPanel()
            .isActionClickable(DocumentActionsPanel.CLASSIFY));
    }

    /*
    RM-2078 Acceptance criteria currently without tests.

    User with no 'write' permission on unclassified content can not set the content classification

    Given that I do not have 'write' permissions on the unclassified content
    And may have a security clearance level above 'No Clearance'
    When I view the unclassified content
    Then I am unable to set a classification

    User with no security clearance can't set content classification

    Given that I have a security clearance level of 'No Clearance'
    And 'write' permission on the unclassified content
    When I view the unclassified content
    Then I can not set a classification

    Action doesn't appear for working copies, synced content, shared content or classified content

    Given that content is either a working copy
    Or content synced to the cloud
    Or content has been shared (via QuickShare)
    Or content that is already classified
    When I view the available actions
    Then the classify content action will not be available

    Can't sync classified content to the cloud

    Given that content has been synced to the cloud
    When I view the available actions
    Then the classify action will not be available

    Action appears for in-place record

    Given that content has been declared as a record
    And I have sufficient clearance to classify the record
    When I view the available actions
    Then the classify action will be available
    */
}