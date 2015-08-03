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

import static org.testng.AssertJUnit.assertEquals;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.ContentService;
import org.alfresco.dataprep.SiteService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanelField;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.ClassifiedDocumentDetails;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.alfresco.test.DataPrepHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Tests related to reclassification of content.
 *
 * @author Tom Page
 * @since 3.0.a
 */
public class Reclassification extends BaseTest
{
    private static final String SITE_NAME = "ReclassificationDocuments";
    private static final String SITE_ID = "ReclassificationDocuments" + System.currentTimeMillis();
    private static final String DOCUMENT = "Reclassification Document " + System.currentTimeMillis();

    /* Page objects */
    @Autowired private DocumentLibrary documentLibrary;
    @Autowired private ClassifiedDocumentDetails classifiedDocumentDetails;
    @Autowired private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /* Data prep services */
    @Autowired private DataPrepHelper dataPrepHelper;
    @Autowired private SiteService siteService;
    @Autowired private UserService userService;
    @Autowired private ContentService contentService;

    /**
     * Create a classified document (at "Secret") in a new collaboration site that can be upgraded, downgraded and
     * declassified.
     */
    @Test
    (
        groups = { "integration" }
    )
    public void setupTestData() throws Exception
    {
        // Create collaboration site
        dataPrepHelper.createSite(SITE_NAME, SITE_ID);

        // Upload document
        openPage(documentLibrary, SITE_ID);
        contentService.createDocument(getAdminName(), getAdminPassword(), SITE_ID, DocumentType.TEXT_PLAIN, DOCUMENT, TEST_CONTENT);

        // Classify document
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(DOCUMENT)
            .clickOnClassify()
            .setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setClassifiedBy(CLASSIFIED_BY)
            .addReason(CLASSIFICATION_REASON)
            .clickOnClassify();
    }

    /** Upgrade the classification of a document and check the reclassification fields are set. */
    @Test
    (
       groups = { "integration" },
       description = "Upgrade the classification of the document to Top Secret.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2441, RM-2454")
    public void upgradeDocument()
    {
        // Upgrade document to Top Secret.
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(DOCUMENT)
            .clickOnEditClassification()
            .setLevel(TOP_SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setReclassifiedBy("Upgrade person")
            .setReclassifyReason("Upgrade reason")
            .clickOnClassify();

        // Check the upgrade fields in the properties panel.
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.CURRENT_CLASSIFICATION, TOP_SECRET_CLASSIFICATION_LEVEL_TEXT);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_BY, "Upgrade person");
        String expectedDate = DateTimeFormatter.ofPattern("EEE d MMM yyyy").withZone(ZoneOffset.UTC).format(Instant.now());
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_DATE, expectedDate);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_REASON, "Upgrade reason");
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_ACTION, "Upgrade");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));
    }

    /** Downgrade the classification of a document and check the reclassification fields are set. */
    @Test
    (
       groups = { "integration" },
       description = "Downgrade the classification of the document to Confidential.",
       dependsOnMethods = "setupTestData"
    )
    @AlfrescoTest(jira="RM-2442")
    public void downgradeDocument()
    {
        // Downgrade document to Confidential.
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(DOCUMENT)
            .clickOnEditClassification()
            .setLevel(CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT)
            .setReclassifiedBy("Downgrade person")
            .setReclassifyReason("Downgrade reason")
            .clickOnClassify();

        // Check the upgrade fields in the properties panel.
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.CURRENT_CLASSIFICATION, CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_BY, "Downgrade person");
        String expectedDate = DateTimeFormatter.ofPattern("EEE d MMM yyyy").withZone(ZoneOffset.UTC).format(Instant.now());
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_DATE, expectedDate);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_REASON, "Downgrade reason");
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_ACTION, "Downgrade");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));
    }

    /**
     * Declassify a document and check the reclassification fields are set. This must not be done before the downgrade
     * test and so it 'depends' on it.
     */
    @Test
    (
       groups = { "integration" },
       description = "Declassify the document.",
       dependsOnMethods = "downgradeDocument"
    )
    @AlfrescoTest(jira="RM-2443")
    public void declassifyDocument()
    {
        // Upgrade document to Top Secret.
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(DOCUMENT)
            .clickOnEditClassification()
            .setLevel(UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT)
            .setReclassifiedBy("Declassify person")
            .setReclassifyReason("Declassify reason")
            .clickOnClassify();

        // Check the upgrade fields in the properties panel.
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.CURRENT_CLASSIFICATION, UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_BY, "Declassify person");
        String expectedDate = DateTimeFormatter.ofPattern("EEE d MMM yyyy").withZone(ZoneOffset.UTC).format(Instant.now());
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_DATE, expectedDate);
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_REASON, "Declassify reason");
        expectedFields.put(ClassifiedPropertiesPanelField.RECLASSIFY_ACTION, "Declassify");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));
    }

    /** Remove the test site. */
    @AfterSuite(alwaysRun=true)
    public void tearDownTestData() throws Exception
    {
        // delete site
        if (siteService.exists(SITE_ID, getAdminName(), getAdminPassword()))
        {
            siteService.delete(getAdminName(), getAdminPassword(), "", SITE_ID);
        }
    }
}
