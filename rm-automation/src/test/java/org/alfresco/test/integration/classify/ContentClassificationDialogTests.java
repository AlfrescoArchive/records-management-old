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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.dialog.DeleteConfirmationDialog;
import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Acceptance tests relating to the classify content dialog.
 *
 * @author tpage
 */
public class ContentClassificationDialogTests extends BaseTest
{
    @Autowired
    private DocumentLibrary documentLibrary;
    @Autowired
    private ClassifyContentDialog classifyContentDialog;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;
    @Autowired
    private DocumentDetails documentDetails;
    @Autowired
    private DeleteConfirmationDialog deleteConfirmationDialog;

    /**
     * Opening the classify content dialog and classifying a document. This test covers the following two (very related)
     * acceptance criteria. This test has the side effect of creating and classifying the document CLASSIFIED_DOCUMENT,
     * which is used by other tests.
     *
     * <pre>
     * Given that I have clicked the classify content action
     * When the classify content dialog opens
     * Then all the fields will be blank *** TODO Check that having the level prepopulated is ok. ***
     * And the "ok" button will be disabled
     * Until valid values have been entered for all the mandatory fields
     * Then the "ok" button will be enabled
     *
     * Given that I have opened the classify content dialog
     * And have entered valid values for all the mandatory fields
     * When I click "ok"
     * Then the content is classified
     * And the classify content action is no longer available
     * </pre>
     */
    @Test
    (
        groups = { "integration", GROUP_CLASSIFIED_DOCUMENT_EXISTS },
        description = "Use the classify content dialog to classify a document.",
        dependsOnGroups = { GROUP_COLLABORATION_SITE_EXISTS }
    )
    public void classifyDocument()
    {
        // Open collaboration site document library, upload the document and click on the "Classify" action.
        openPage(documentLibrary, COLLAB_SITE_ID);
        documentLibrary.getToolbar()
            .clickOnFile()
            .uploadFile(CLASSIFIED_DOCUMENT);
        documentLibrary.getDocument(CLASSIFIED_DOCUMENT)
            .clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        // Verify that a classify document dialog has appeared.
        assertTrue("Classify content dialog is not visible.", classifyContentDialog.isDisplayed());

        // Check all the fields start blank (except the classification level).
        assertEquals("Authority should be initially blank.", "", classifyContentDialog.getAuthority());
        assertEquals("There should be no reasons selected initially.", 0, classifyContentDialog.getReasons().size());

        // Check the "Create" button is disabled.
        assertFalse("Create button should not initially be enabled.", classifyContentDialog.isCreateButtonEnabled());

        // Fill in the classification details.
        classifyContentDialog.setLevel(CLASSIFICATION_LEVEL_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON);

        // Check the "Create" button is now enabled.
        assertTrue("Create button should be enabled now form is filled.", classifyContentDialog.isCreateButtonEnabled());

        // Classify the content.
        classifyContentDialog.submitDialog();

        // Check that the classify content action is no longer available.
        String[] clickableActions = documentLibrary.getDocument(CLASSIFIED_DOCUMENT).getClickableActions();
        assertFalse("The classify action should no longer be available.",
                    Arrays.asList(clickableActions).contains(DocumentActionsPanel.CLASSIFY));
    }

    /**
     * Check that cancelling the classification dialog works.
     *
     * <pre>
     * Given that I have clicked the classify content action
     * And the classify content dialog opens
     * Even if data has been set in the inputs (or options have been selected)
     * When clicking on Cancel then the dialog is closed and the Classify option is still available.
     * Also when opening again the dialog, the previous inserted/selected data is not displayed, the inputs are blank
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "Check that cancelling the classification dialog works.",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS }
    )
    public void cancelClassifyDialog()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);
        Document document = documentLibrary.getDocument(DOCUMENT);
        // Open the classify document dialog
        document.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        // Fill in the classification details.
        classifyContentDialog.setLevel(CLASSIFICATION_LEVEL_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON);

        // Click the cancel button.
        classifyContentDialog.cancelDialog();

        // Check the classify document dialog has closed.
        assertFalse("Classify content dialog should have closed.", classifyContentDialog.isDisplayed());
        // Check that the classify content action is still available.
        String[] clickableActions = document.getClickableActions();
        assertTrue("The classify action should still be available.",
                    Arrays.asList(clickableActions).contains(DocumentActionsPanel.CLASSIFY));

        // Open the dialog again and check the fields are as they were initially.
        document.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);
        assertEquals("The level should be back to the default.", DEFAULT_CLASSIFICATION_LEVEL_TEXT,
                    classifyContentDialog.getLevel());
        assertEquals("Authority should be initially blank.", "", classifyContentDialog.getAuthority());
        assertEquals("There should be no reasons selected initially.", 0, classifyContentDialog.getReasons().size());
    }

    /**
     * Check that content can be "classified" with a level of "Unclassified".
     *
     * <pre>
     * Given that I have opened the classify content dialog
     * And have selected "Unclassified"
     * And entered all other values required
     * When I click "ok"
     * Then the content is classified as "Unclassified"
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "Check that content can be classified with a level of Unclassified.",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS }
    )
    public void classifyAsUnclassified()
    {
        // Open the document preview page.
        openPage(documentLibrary, COLLAB_SITE_ID);
        documentLibrary.getDocument(DOCUMENT).clickOnLink();

        // Classify the document with a level of "No Clearance".
        DocumentActionsPanel documentActionsPanel = documentDetails.getDocumentActionsPanel();
        documentActionsPanel.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);
        classifyContentDialog.setLevel(UNCLASSIFIED_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON)
            .submitDialog();

        // Check that the document is classified and has level "Unclassified".
        assertNotNull("Classified properties not found.", classifiedPropertiesPanel);
        String level = classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION);
        assertEquals("Unexpected current classification.", UNCLASSIFIED_TEXT, level);

        // Delete the document and recreate it again.
        documentDetails.navigateUp();
        recreateDocument();
    }

    /**
     * Check that a user with 'Top Secret' clearance sees all the levels.
     *
     * <pre>
     * Given that I am a user with the highest security clearance
     * When I classify content
     * Then all levels in the classification hierarchy are available to me
     * </pre>
     */
    @Test
    (
        groups = { "integration", "ignored" }, // TODO Re-enable when we've decided logging out/in doesn't break tests on Bamboo.
        description = "Check that a user with 'Top Secret' clearance sees all the levels.",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS }
    )
    public void clearedUserSeesAllLevels()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);
        Document document = documentLibrary.getDocument(DOCUMENT);
        // Open the classify document dialog
        document.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        List<String> availableLevels = classifyContentDialog.getAvailableLevels();
        assertEquals(Arrays.asList("Top Secret", "Secret", "Confidential", "Unclassified"), availableLevels);

        // Click the cancel button.
        classifyContentDialog.cancelDialog();
    }

    /**
     * Check that a user with 'Secret' clearance sees only the levels up to 'Secret'.
     *
     * <pre>
     * Given that I am a user with mid level security clearance
     * When I classify content
     * Then only classifications that match my security clearance and those below are available to me
     * And those above my security clearance level are not visible to me
     * </pre>
     */
    @Test
    (
        groups = { "integration", "ignored" }, // TODO Re-enable when we've decided logging out/in doesn't break tests on Bamboo.
        description = "Check that a user with 'Secret' clearance sees only the levels up to 'Secret'.",
        dependsOnGroups = { GROUP_DOCUMENT_EXISTS, GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE }
    )
    public void secretUserSeesSomeLevels()
    {
        // Open Collab site DocumentLibrary.
        openPage(RM_MANAGER, DEFAULT_PASSWORD, documentLibrary, COLLAB_SITE_ID);
        Document document = documentLibrary.getDocument(DOCUMENT);
        // Open the classify document dialog
        document.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        List<String> availableLevels = classifyContentDialog.getAvailableLevels();
        assertEquals(Arrays.asList("Secret", "Confidential", "Unclassified"), availableLevels);

        // Click the cancel button.
        classifyContentDialog.cancelDialog();
    }

    /**
     * Delete and re-upload a document. This is used to allow resetting after classifying a document. TODO: Implement
     * removal of classification and use that instead.
     */
    private void recreateDocument()
    {
        documentLibrary
            .getDocument(DOCUMENT)
            .clickOnDelete()
            .confirmDelete();

        openPage(documentLibrary, COLLAB_SITE_ID);
        documentLibrary.getToolbar()
            .clickOnFile()
            .uploadFile(DOCUMENT);
    }
}
