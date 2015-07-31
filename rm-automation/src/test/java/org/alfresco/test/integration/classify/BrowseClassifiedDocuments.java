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
import static org.junit.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanelField;
import org.alfresco.po.share.browse.documentlibrary.ContentBanner;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentIndicators;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.ClassifiedDocumentDetails;
import org.alfresco.test.BaseTest;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @author tpage
 * @since 3.0.a
 */
public class BrowseClassifiedDocuments extends BaseTest
{
    @Autowired
    private DocumentLibrary documentLibrary;
    @Autowired
    private ClassifiedDocumentDetails classifiedDocumentDetails;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /**
     * Check the classified document indicator.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2001">RM-2001</a><pre>
     * Given that there is some classified content in the folder I am viewing in the document library
     * And that my security clearance is sufficient that I can see the classified content
     * When the classified content is presented in the list view
     * Then I can see a visual indicator that tells me it's classified
     * </pre>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2245">RM-2245</a><pre>
     * Given that a document has been classified
     * When I browse the document library
     * Then the classification level of the document is clearly visible
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classify Document behaviour",
        dependsOnGroups = { "GROUP_SECRET_DOCUMENT_EXISTS" }
    )
    public void classifyDocumentIndicator()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        Document document = documentLibrary.getDocument(SECRET_DOCUMENT);
        assertTrue("Expected classified document icon to be visible.",
                    document.hasIndicator(DocumentIndicators.CLASSIFIED));

        assertEquals("Expected 'Secret' classification banner to be visible.",
                     SECRET_CLASSIFICATION_LEVEL_TEXT.toUpperCase(),
                     document.getBannerText(ContentBanner.CLASSIFICATION));
    }

    /**
     * Verify document details page displays classification information.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2002">RM-2002</a><pre>
     * Given that I have the appropriate security clearance to see a classified document
     * When I view the document details
     * Then I see information about the classification including:
     * * Initial classification
     * * Current classification
     * * Classification agency
     * * Classification reasons
     * </pre>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2282">RM-2282</a><pre>
     * Given that a document has been classified
     * When I view the document details page
     * Then the classification level of the document is clearly visible
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify document details page displays classification information.",
        dependsOnGroups = { "GROUP_SECRET_DOCUMENT_EXISTS" }
    )
    public void classifyDocumentProperties()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);

        // navigate to the document details page
        documentLibrary.getDocument(SECRET_DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);

        // verify that classification is as expected.
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.CURRENT_CLASSIFICATION, SECRET_CLASSIFICATION_LEVEL_TEXT);
        expectedFields.put(ClassifiedPropertiesPanelField.CLASSIFIED_BY, CLASSIFIED_BY);
        expectedFields.put(ClassifiedPropertiesPanelField.CLASSIFICATION_AGENCY, CLASSIFICATION_AGENCY);
        expectedFields.put(ClassifiedPropertiesPanelField.CLASSIFICATION_REASON, CLASSIFICATION_REASON);
        expectedFields.put(ClassifiedPropertiesPanelField.DOWNGRADE_DATE, DOWNGRADE_DATE_OUTPUT);
        expectedFields.put(ClassifiedPropertiesPanelField.DOWNGRADE_EVENT, DOWNGRADE_EVENT);
        expectedFields.put(ClassifiedPropertiesPanelField.DOWNGRADE_INSTRUCTIONS, DOWNGRADE_INSTRUCTIONS);
        expectedFields.put(ClassifiedPropertiesPanelField.DECLASSIFICATION_DATE, DECLASSIFICATION_DATE_OUTPUT);
        expectedFields.put(ClassifiedPropertiesPanelField.DECLASSIFICATION_EVENT, DECLASSIFICATION_EVENT);
        expectedFields.put(ClassifiedPropertiesPanelField.EXEMPTION_CATEGORIES, EXEMPTION_CATEGORY);
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        assertEquals("Expected 'Secret' classification banner to be visible.",
                    SECRET_CLASSIFICATION_LEVEL_TEXT.toUpperCase(),
                    classifiedDocumentDetails.getBannerText(ContentBanner.CLASSIFICATION));
    }

    /**
     * User with highest clearance can view all documents.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2286">RM-2286</a><pre>
     * Given that I am a user with the highest possible security clearance
     * When I browse the document library
     * Then I can see all unclassified documents
     * And I can see all classified documents
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "User with highest clearance can view all documents.",
        dependsOnGroups = { "GROUP_TOP_SECRET_DOCUMENT_EXISTS", "GROUP_SECRET_DOCUMENT_EXISTS", "GROUP_UNCLASSIFIED_DOCUMENT_EXISTS" }
    )
    public void highestClearanceUserCanViewEverything()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);
        // Check that we can access the documents by viewing their names.
        assertTrue(documentLibrary.getDocument(TOP_SECRET_DOCUMENT).getName().startsWith(TOP_SECRET_DOCUMENT));
        assertTrue(documentLibrary.getDocument(SECRET_DOCUMENT).getName().startsWith(SECRET_DOCUMENT));
        assertTrue(documentLibrary.getDocument(UNCLASSIFIED_DOCUMENT).getName().startsWith(UNCLASSIFIED_DOCUMENT));
    }

    /**
     * User with 'secret' clearance can view all documents classified with level at most 'secret'.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2133">RM-2133</a><pre>
     * Given that I am a user with mid-level security clearance
     * When I browse the document library
     * Then I can see all unclassified documents
     * And I can see all classified documents with a classification less than or equal to my clearance level
     * And I can't see any classified documents with a classification greater than my clearance level
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "User with 'secret' clearance can view all documents classified with level at most 'secret'.",
        dependsOnGroups = { "GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE", "GROUP_TOP_SECRET_DOCUMENT_EXISTS", "GROUP_SECRET_DOCUMENT_EXISTS", "GROUP_UNCLASSIFIED_DOCUMENT_EXISTS" }
    )
    public void secretClearanceUserCanViewUpToSecret()
    {
        openPage(RM_MANAGER, DEFAULT_PASSWORD, documentLibrary, COLLAB_SITE_ID);
        assertNull("Should not have been able to find the top secret document.", documentLibrary.getDocument(TOP_SECRET_DOCUMENT));
        // Check that we can access the documents by viewing their names.
        assertTrue(documentLibrary.getDocument(SECRET_DOCUMENT).getName().startsWith(SECRET_DOCUMENT));
        assertTrue(documentLibrary.getDocument(UNCLASSIFIED_DOCUMENT).getName().startsWith(UNCLASSIFIED_DOCUMENT));
    }

    /**
     * User with no clearance can only view unclassified documents.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2132">RM-2132</a><pre>
     * Given that I am a user with no security clearance
     * When I browse the document library
     * Then I can see all unclassified documents
     * And I cannot see any classified documents
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "User with no clearance can only view unclassified documents.",
        dependsOnGroups = { "GROUP_UNCLEARED_USER_EXISTS", "GROUP_TOP_SECRET_DOCUMENT_EXISTS", "GROUP_SECRET_DOCUMENT_EXISTS", "GROUP_UNCLASSIFIED_DOCUMENT_EXISTS" }
    )
    public void noClearanceUserCantViewClassifiedDocuments()
    {
        openPage(UNCLEARED_USER, DEFAULT_PASSWORD, documentLibrary, COLLAB_SITE_ID);
        assertNull("Should not have been able to find the top secret document.", documentLibrary.getDocument(TOP_SECRET_DOCUMENT));
        assertNull("Should not have been able to find the secret document.", documentLibrary.getDocument(SECRET_DOCUMENT));
        // Check that we can access this document by viewing the name.
        assertTrue(documentLibrary.getDocument(UNCLASSIFIED_DOCUMENT).getName().startsWith(UNCLASSIFIED_DOCUMENT));
    }

    /**
     * Check how a document explicitly marked 'Unclassified' is presented.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2279">RM-2279</a><pre>
     * Given that a document has been marked as unclassified
     * When I browse the document library
     * Then no classification banner is shown for the document
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "Check how a document explicitly marked 'Unclassified' is presented.",
        dependsOnGroups = { "GROUP_UNCLASSIFIED_DOCUMENT_EXISTS" }
    )
    public void unclassifiedDocumentPresentation()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        Document document = documentLibrary.getDocument(UNCLASSIFIED_DOCUMENT);
        // TODO Use ExceptionUtils when it's moved somewhere accessible to rm-automation
        try
        {
            document.getBannerText(ContentBanner.CLASSIFICATION);
            fail("The unclassified document should not have a classification banner.");
        }
        catch(TimeoutException e)
        {
            // NOOP - This is expected. Nb. Don't move the expected exception to cover the whole test as many places
            // throw TimeoutException.
        }
    }
}