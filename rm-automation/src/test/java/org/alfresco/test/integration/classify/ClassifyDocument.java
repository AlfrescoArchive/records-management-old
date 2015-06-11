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
import static org.testng.Assert.assertTrue;

import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.browse.documentlibrary.InplaceRecord;
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
        groups = { "integration" },
        description = "Verify classify action is available",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS }
    )
    public void checkClassifyActionAvailable()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);
        Document document = documentLibrary.getDocument(DOCUMENT);

        // verify document actions
        assertTrue(document.isActionClickable(DocumentActions.CLASSIFY));

        // navigate to the document details page
        document.clickOnLink();

        // verify that all the expected actions are available
        assertTrue(documentDetails.getDocumentActionsPanel()
            .isActionClickable(DocumentActionsPanel.CLASSIFY));
    }

    /**
     * Check that the classify action is not available on the document in several cases.
     *
     * Given that content is either a working copy
     * TODO Or content synced to the cloud
     * Or content has been shared (via QuickShare)
     * Or content that is already classified (covered by {@link ContentClassificationDialogTests#classifyDocument})
     * When I view the available actions
     * Then the classify content action will not be available
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify classify action is not available for various cases",
        dependsOnGroups = { GROUP_LOCKED_DOCUMENT_EXISTS, GROUP_SHARED_DOCUMENT_EXISTS }
    )
    public void checkClassifyActionUnavailableInVariousCases()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        Document sharedDocument = documentLibrary.getDocument(SHARED_DOCUMENT);
        assertFalse(sharedDocument.isActionClickable(DocumentActions.CLASSIFY));

        // TODO Create test for document that has been synced to the cloud.

        Document lockedDocument = documentLibrary.getDocument(LOCKED_DOCUMENT);
        assertFalse(lockedDocument.isActionClickable(DocumentActions.CLASSIFY));
    }

    /**
     * Check that a user with no security clearance doesn't see the 'Classify' action.
     *
     * Given that I have a security clearance level of 'No Clearance'
     * And 'write' permission on the unclassified content
     * When I view the unclassified content
     * Then I cannot set a classification
     */
    @Test
    (
        groups = { "integration", "ignored" }, // TODO Re-enable when we've decided logging out/in doesn't break tests on Bamboo.
        description = "Check that a user with no security clearance doesn't see the 'Classify' action",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS, GROUP_UNCLEARED_USER_EXISTS }
    )
    public void checkUnclearedUserCannotClassify()
    {
        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, documentLibrary, COLLAB_SITE_ID);

        Document document = documentLibrary.getDocument(DOCUMENT);
        assertFalse(document.isActionClickable(DocumentActions.CLASSIFY));
    }

    /**
     * Check that the 'Classify' action exists for an in-place record.
     *
     * Given that content has been declared as a record
     * And I have sufficient clearance to classify the record
     * When I view the available actions
     * Then the classify action will be available
     */
    @Test
    (
        groups = { "integration" },
        description = "Check that the 'Classify' action exists for an in-place record",
        dependsOnGroups = { GROUP_IN_PLACE_RECORD_EXISTS }
    )
    public void checkInplaceRecordCanBeClassified()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        InplaceRecord inPlaceRecord = documentLibrary.getInplaceRecord(IN_PLACE_RECORD);
        assertTrue(inPlaceRecord.isActionClickable(DocumentActions.CLASSIFY));
    }

    /**
     * Check that I can't sync classified content to the cloud
     *
     * Given that content has been classified
     * When I view the available options
     * Then I am unable to sync the content
    */
    @Test
    (
        groups = {"integration"},
        description = "Check that we can't sync classified content",
        dependsOnGroups = {GROUP_SECRET_DOCUMENT_EXISTS}
    )
    public void checkWeCannotSyncClassifiedContent()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        // On Document Library Page
        assertFalse(documentLibrary.getDocument(SECRET_DOCUMENT)
            .isShareDocumentAvailable());

        // On Document Details Page
        assertFalse(documentLibrary.getDocument(SECRET_DOCUMENT)
            .clickOnLink()
            .getSocialActions()
            .isShareDocumentAvailable());
    }

    /*
    RM-2078 Acceptance criteria currently without tests.

    User with no 'write' permission on unclassified content can not set the content classification

    Given that I do not have 'write' permissions on the unclassified content
    And may have a security clearance level above 'No Clearance'
    When I view the unclassified content
    Then I am unable to set a classification
    */
}