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

import org.alfresco.po.rm.dialog.classification.ClassifyContentDialog;
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

    @Autowired
    private ClassifyContentDialog classifyContentDialog;

    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration", "classification" },
        description = "Verify Classify Document behaviour",
        dependsOnGroups = { "integration-dataSetup-collab" }
    )
    public void classifyDocument()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID, "documentlibrary");

        // verify document actions
        assertTrue(documentLibrary.getDocument(DOCUMENT)
            .isActionClickable(DocumentActions.CLASSIFY));

        // navigate to the document details page
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink();

        // verify that all the expected actions are available
        assertTrue(documentDetails.getDocumentActionsPanel()
            .isActionClickable(DocumentActionsPanel.CLASSIFY));

        // Open the classify document dialog
        documentDetails.getDocumentActionsPanel().clickOnAction(DocumentActionsPanel.CLASSIFY, classifyContentDialog);

        // Verify that a classify document dialog has appeared.
        assertTrue("Classify content dialog is not visible.", classifyContentDialog.isDisplayed());

        // Fill in the classification details.
        classifyContentDialog.setLevel(CLASSIFICATION_LEVEL_TEXT)
            .setAuthority(CLASSIFICATION_AUTHORITY)
            .addReason(CLASSIFICATION_REASON)
            .cancelDialog();

        // TODO: This test can currently only be run once as reclassification is not possible.
        // Presumably it would be good to add a tear-down method to delete and re-create the document.
    }
}