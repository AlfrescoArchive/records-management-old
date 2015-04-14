package org.alfresco.test.integration.classify;

import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.DocumentActionsPanel;
import org.alfresco.po.share.details.document.DocumentDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;

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
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
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
        // FIXME: this breaks...
        documentLibrary.getDocument(DOCUMENT)
            .clickOnLink();

        // verify that all the expected actions are available
        assertTrue(documentDetails.getDocumentActionsPanel()
            .isActionClickable(DocumentActionsPanel.CLASSIFY));
    }
}