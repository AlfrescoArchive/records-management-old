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
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
import org.alfresco.po.share.browse.documentlibrary.Document;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
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

    /**
     * Opening the classify content dialog
     *
     * Given that I have clicked the classify content action
     * When the classify content dialog opens
     * Then all the fields will be blank *** TODO Check that having the level prepopulated is ok. ***
     * And the "ok" button will be disabled
     * Until valid values have been entered for all the mandatory fields
     * Then the "ok" button will be enabled
     */
    @Test
    (
        groups = { "integration", "classification-irreversible" },
        description = "Verify Classify Document behaviour",
        dependsOnGroups = { "integration-dataSetup-collab" }
    )
    public void classifyDocument()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID, "documentlibrary");
        Document document = documentLibrary.getDocument(DOCUMENT);
        // Open the classify document dialog
        document.clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        // Verify that a classify document dialog has appeared.
        assertTrue("Classify content dialog is not visible.", classifyContentDialog.isDisplayed());

        // Check all the fields start blank (except the classification level).
        assertEquals("Authority should be initially blank.", "", classifyContentDialog.getAuthority());
        assertEquals("There should be no reasons selected initially.", 0, classifyContentDialog.getReasons().size());

        // Check the "ok" button is disabled.
        assertFalse("Create button should not initially be enabled.", classifyContentDialog.isCreateButtonEnabled());

        // Fill in the classification details.
        classifyContentDialog.setLevel(CLASSIFICATION_LEVEL_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON);

        // Check the "ok" button is now enabled.
        assertTrue("Create button should be enabled now form is filled.", classifyContentDialog.isCreateButtonEnabled());

        // Classify the content.
        classifyContentDialog.submitDialog();

        // TODO: This test can currently only be run once as reclassification is not possible.
        // Presumably it would be good to add a tear-down method to delete and re-create the document.
    }

    /*
    RM-1845 Acceptance criteria currently without tests.

    Classifying content

    Given that I have opened the classify content dialog
    And have entered valid values for all the mandatory fields
    When I click "ok"
    Then the content is classified
    And the classify content action is no longer available

    Unclassified content

    Given that I have opened the classify content dialog
    And have selected "Unclassified"
    And entered all other values required
    When I click "ok"
    Then the content is classified as "Unclassified"

    Cancelling the classification

    Given that I have clicked the classify content action
    And the classify content dialog opens
    Even if data has been set in the inputs (or options have been selected)
    When clicking on Cancel then the dialog is closed and the Classify option is still available.
    Also when opening again the dialog, the previous inserted/selected data is not displayed, the inputs are blank
    */

}
