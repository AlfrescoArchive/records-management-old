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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.ContentService;
import org.alfresco.dataprep.SiteService;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanelField;
import org.alfresco.po.rm.dialog.classification.EditClassifiedContentDialog;
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
 * @since 2.4.a
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
    @Autowired private ContentService contentService;

    /**
     * Create a classified document (at "Secret") in a new collaboration site that can be upgraded, downgraded and
     * declassified.
     */
    @Test
    (
        groups = { "integration" }
    )
    public void setUpReclassificationData() throws Exception
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
       dependsOnMethods = "setUpReclassificationData"
    )
    @AlfrescoTest(jira="RM-2441, RM-2454")
    public void upgradeDocument()
    {
        // Upgrade document to "Top Secret".
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
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, "Upgrade person");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, "Upgrade reason");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "Upgrade");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        // To avoid complications with running in different timezones, just check that the date has been set.
        String reclassifyDate = classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_DATE);
        checkValidPropertiesPanelDate(reclassifyDate, "reclassified date");
    }

    /**
     * Downgrade the classification of a document and check the reclassification fields are set. This test 'depends on'
     * the upgrade test to ensure they run in a predictable order.
     */
    @Test
    (
       groups = { "integration" },
       description = "Downgrade the classification of the document to Confidential.",
       dependsOnMethods = "upgradeDocument"
    )
    @AlfrescoTest(jira="RM-2442")
    public void downgradeDocument()
    {
        // Downgrade document to "Confidential".
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
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, "Downgrade person");
        String expectedDate = DateTimeFormatter.ofPattern("EEE d MMM yyyy").withZone(ZoneOffset.UTC).format(Instant.now());
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_DATE, expectedDate);
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, "Downgrade reason");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "Downgrade");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        // To avoid complications with running in different timezones, just check that the date has been set.
        String reclassifyDate = classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_DATE);
        checkValidPropertiesPanelDate(reclassifyDate, "reclassified date");
    }

    /**
     * Declassify a document and check the reclassification fields are set. This must not be done before the downgrade
     * test and so it 'depends' on it. Also test that when editing the classification of the reclassified document, the
     * reclassification fields are initially disabled, but can be set after setting the level.
     */
    @Test
    (
       groups = { "integration" },
       description = "Declassify the document.",
       dependsOnMethods = "downgradeDocument"
    )
    @AlfrescoTest(jira="RM-2443, RM-2446")
    public void declassifyDocument()
    {
        // Open the edit classification dialog.
        openPage(documentLibrary, SITE_ID);
        EditClassifiedContentDialog dialog = documentLibrary
            .getDocument(DOCUMENT)
            .clickOnEditClassification();

        // Check that the reclassification fields are disabled and set to the values from the last test.
        assertEquals("Downgrade person", dialog.getLastReclassifiedBy());
        assertEquals("Downgrade reason", dialog.getLastReclassifiedReason());
        assertFalse("Expected 'Reclassified By' to be disabled.", dialog.isReclassifiedByEnabled());
        assertFalse("Expected 'Reclassification Reason' to be disabled.", dialog.isReclassificationReasonEnabled());

        // Declassify the document by setting the classification to "Unclassified".
        dialog.setLevel(UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT);

        // Check that once the classification level is changed, the reclassification fields are reset.
        assertEquals("Administrator", dialog.getReclassifiedBy());
        assertTrue("Reclassification reason should be cleared when level is changed.",
                    dialog.getReclassifiedReason().isEmpty());

        // Set the reclassification fields.
        dialog.setReclassifiedBy("Declassify person")
            .setReclassifyReason("Declassify reason")
            .clickOnClassify();

        // Check the upgrade fields in the properties panel.
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.CURRENT_CLASSIFICATION, UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT);
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, "Declassify person");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, "Declassify reason");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "Declassify");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        // To avoid complications with running in different timezones, just check that the date has been set.
        String reclassifyDate = classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_DATE);
        checkValidPropertiesPanelDate(reclassifyDate, "reclassified date");
    }

    /**
     * Classify a document as secret and verify in the Edit classification page that Reclassified by and Reclassification reason are empty and disabled,
     * Last reclassified by and Last reclassification reason are not displayed
     * Also check that when we reclassify it as confidential the "Reclassified By" field is
     * pre-populated with the current user.
     */
    @Test
    (
       groups = { "integration" },
       description = "Check the initial fields when trying to reclassify a document.",
       dependsOnMethods = "setUpReclassificationData"
    )
    @AlfrescoTest(jira="RM-2444,RM-2539,RM-2540")
    public void checkInitialReclassificationFields() throws Exception
    {
        // Upload document specifically for this test.
        String document = "checkInitialReclassificationFields document";
        openPage(documentLibrary, SITE_ID);
        contentService.createDocument(getAdminName(), getAdminPassword(), SITE_ID, DocumentType.TEXT_PLAIN, document, TEST_CONTENT);

        // Classify document
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(document)
            .clickOnClassify()
            .setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setClassifiedBy(CLASSIFIED_BY)
            .addReason(CLASSIFICATION_REASON)
            .clickOnClassify();

        openPage(documentLibrary, SITE_ID);

        EditClassifiedContentDialog dialog = (EditClassifiedContentDialog) documentLibrary
            .getDocument(document)
            .clickOnEditClassification();
        // Verify that the Edit Classification page displays the Reclassified By and the Reclassification reason disabled and empty for the document that has never been reclassified.
        assertFalse("The Reclassified By input is not disabled for content that has not been yet reclassified.", dialog.isReclassifiedByEnabled());
        assertEquals("The Reclassified By input is not empty by default.", "", dialog.getReclassifiedBy());
        assertFalse("The Reclassification Reason input is not disabled for content that has not been yet reclassified.", dialog.isReclassificationReasonEnabled());
        assertEquals("The Reclassification Reason input is not empty by default.", "", dialog.getReclassifiedReason());

        // Verify that the Last reclassified by and Last reclassification reason are not displayed.
        assertFalse("The Last Reclassified By input is displayed even if the document has never been reclassified.", dialog.isLastReclassifiedByDisplayed());
        assertFalse("The Last Reclassification reason input is displayed even if the document has never been reclassified.", dialog.isLastReclassificationReasonDisplayed());

        // Attempt to reclassify and check the initial value of the "reclassified by" field.
        dialog.setLevel(CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT);
        String reclassifiedBy = dialog.getReclassifiedBy();
        assertEquals("'Reclassified by' should default to the current user full name.", "Administrator", reclassifiedBy);

        // Verify that Reclassified by and Reclassification reason are enabled for editing.
        assertTrue("The Reclassified By input is not editable.", dialog.isReclassifiedByEnabled());
        assertTrue("The Reclassification reason input is not editable.", dialog.isReclassificationReasonEnabled());

        dialog.clickOnCancel();

        // Verify the Last Reclassified by, Last reclassification date, reason and action are empty in the Document details panel
        documentLibrary.getDocument(document)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, "(None)");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_DATE, "(None)");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, "(None)");
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "(None)");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));
    }

    /**
     * Given that content has been classified and has been reclassified at least once
     * When the edit classification dialog is displayed
     * The the following properties are displayed as disabled and empty : Reclassified by, Reclassification reason
     * and the properties Last Reclassified by and Last Reclassification reason are editable, populated with values from the previous reclassification
     * When changing the classification value (upgrade/downgrade/declassify)
     * Then the following properties are shown as enabled for editing: Reclassified by, Reclassification reason
     * And the value of 'Reclassified By' defaults to current user's full name, but is editable
     * When changing the values of the Last reclassification reason and last reclassified by
     * Then those changes are saved when Save button is clicked
     */
    @Test
    (
       groups = { "integration" },
       description = "Editing the classification of content that has been classified, reclassify it, change the reclassification information.",
       dependsOnMethods = "setUpReclassificationData"
    )
    @AlfrescoTest(jira="RM-2541,RM-2542,RM-2543")
    public void checkLastReclassificationProperties() throws Exception
    {
        String firstReclassificationUser = "Reclassifier User Name 1";
        String firstReclassificationReason = "Reclassification Reason 1";
        String secondReclassificationUser = "Reclassifier User Name 2";
        String secondReclassificationReason = "Reclassification Reason 2";

     // Upload document
        String document = "checkLastReclassificationProperties document";
        openPage(documentLibrary, SITE_ID);
        contentService.createDocument(getAdminName(), getAdminPassword(), SITE_ID, DocumentType.TEXT_PLAIN, document, TEST_CONTENT);

        // Classify document
        openPage(documentLibrary, SITE_ID);
        documentLibrary
            .getDocument(document)
            .clickOnClassify()
            .setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setClassifiedBy(CLASSIFIED_BY)
            .addReason(CLASSIFICATION_REASON)
            .clickOnClassify();

        // Reclassify it for the first time
        openPage(documentLibrary, SITE_ID);
        documentLibrary.getDocument(document)
            .clickOnEditClassification().setLevel(TOP_SECRET_CLASSIFICATION_LEVEL_TEXT)
            .setReclassifiedBy(firstReclassificationUser).setReclassifyReason(firstReclassificationReason).clickOnEdit();

        EditClassifiedContentDialog reopenedDialog = documentLibrary
            .getDocument(document)
            .clickOnEditClassification();

        // Verify that the Reclassified by, Reclassification reason are displayed as disabled and empty
        assertFalse("The Reclassified By field is enabled for editing.", reopenedDialog.isReclassifiedByEnabled());
        assertFalse("The Reclassification reason is enabled for editing.", reopenedDialog.isReclassificationReasonEnabled());
        assertEquals("The Reclassified By field is not empty.", "", reopenedDialog.getReclassifiedBy());
        assertEquals("The Reclassification reason is not empty.", "", reopenedDialog.getReclassifiedReason());

        // Verify the properties Last Reclassified by and Last Reclassification reason are editable, populated with values from the previous reclassification
        assertTrue("The Last Reclassified By field is not enabled for editing.", reopenedDialog.isLastReclassifiedByEnabled());
        assertTrue("The Last Reclassification reason is not enabled for editing.", reopenedDialog.isLastReclassificationReasonEnabled());
        assertEquals(firstReclassificationUser, reopenedDialog.getLastReclassifiedBy());
        assertEquals(firstReclassificationReason, reopenedDialog.getLastReclassifiedReason());

        reopenedDialog.clickOnCancel();
        // Check the Last Reclassification fields in the properties panel.
        documentLibrary.getDocument(document)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedFields = new HashMap<>();
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, firstReclassificationUser);
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, firstReclassificationReason);
        expectedFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "Upgrade");
        expectedFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        openPage(documentLibrary, SITE_ID);

        // Change the last reclassification data, verify the values are updated
        EditClassifiedContentDialog updatedDialog = documentLibrary
            .getDocument(document)
            .clickOnEditClassification();
        // Change the Reclassified By and Reclassification reason values
        updatedDialog.setLevel(SECRET_CLASSIFICATION_LEVEL_TEXT).setReclassifiedBy("not saved user").setReclassifyReason("not saved reason");
        // Change the security level back to the original one and verify that the Reclassified By and Reclassification reason fields are disabled
        updatedDialog.setLevel(TOP_SECRET_CLASSIFICATION_LEVEL_TEXT);
        assertFalse("The Reclassified By field is enabled for editing after changing back the security level to the original one.", updatedDialog.isReclassifiedByEnabled());
        assertFalse("The Reclassification reason is enabled for editing after changing back the security level to the original one.", updatedDialog.isReclassificationReasonEnabled());

        // Change the Last Reclassification fields values
        updatedDialog.setLastReclassifiedBy(secondReclassificationUser).setLastReclassifyReason(secondReclassificationReason).clickOnEdit();

        // Check the Last Reclassification fields have the values set above, not the ones from Reclassified By and Reclassification reason that were in the dialog before save
        documentLibrary.getDocument(document)
            .clickOnLink(classifiedDocumentDetails);
        Map<ClassifiedPropertiesPanelField, String> expectedLastClassificationFields = new HashMap<>();
        expectedLastClassificationFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFIED_BY, secondReclassificationUser);
        expectedLastClassificationFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_REASON, secondReclassificationReason);
        expectedLastClassificationFields.put(ClassifiedPropertiesPanelField.LAST_RECLASSIFICATION_ACTION, "Upgrade");
        expectedLastClassificationFields.forEach(
                    (field, value) -> assertEquals(value, classifiedPropertiesPanel.getClassifiedProperty(field)));

        openPage(documentLibrary, SITE_ID);

        // Change the security clearance level
        EditClassifiedContentDialog dialog = documentLibrary
            .getDocument(document)
            .clickOnEditClassification();
        dialog.setLevel(UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT);

        // Verify that the Reclassified By and Reclassification Reason are shown as enabled for editing
        assertTrue("The Reclassified By field is not enabled for editing after changing the security clearance level.", dialog.isReclassifiedByEnabled());
        assertEquals("The Reclassified By value does not default to the current user's full name.", "Administrator", dialog.getReclassifiedBy());
        assertTrue("The Reclassification Reason is not enabled for editing after changing the security clearance level.", dialog.isReclassificationReasonEnabled());

        // Verify that the Last Reclassified By and Last Reclassification Reason values are the ones previously saved.
        assertEquals("The Last Reclassified By value is not the one previously stored.", secondReclassificationUser, dialog.getLastReclassifiedBy());
        assertEquals("The Last Reclassification Reason is not the one previously stored.", secondReclassificationReason, dialog.getLastReclassifiedReason());

    }


    /**
     * Validate that a date is correctly displayed in the properties panel (i.e. is similar to "Wed 5 Aug 2015").
     *
     * @param dateString The date string to validate.
     * @param dateName The name of the date; to be output in error messages.
     * @throws AssertionError if the date is not in the correct format.
     */
    private void checkValidPropertiesPanelDate(String dateString, String dateName)
    {
        try
        {
            TemporalAccessor parsedDate = DateTimeFormatter.ofPattern("EEE d MMM yyyy").parse(dateString);
            assertNotNull("Expected " + dateName + " to be populated.", parsedDate);
        }
        catch (DateTimeParseException e)
        {
            fail("Invalid " + dateName + " found: " + dateString);
        }
    }

    /** Remove the test site. */
    @AfterSuite(alwaysRun=true)
    public void tearDownReclassificationData() throws Exception
    {
        // delete site
        if (siteService.exists(SITE_ID, getAdminName(), getAdminPassword()))
        {
            siteService.delete(getAdminName(), getAdminPassword(), "", SITE_ID);
        }
    }

}
