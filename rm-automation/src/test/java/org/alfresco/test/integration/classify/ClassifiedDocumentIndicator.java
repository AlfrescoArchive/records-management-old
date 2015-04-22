package org.alfresco.test.integration.classify;

import org.alfresco.po.share.browse.documentlibrary.DocumentIndicators;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
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
public class ClassifiedDocumentIndicator extends BaseTest
{
    /**
     * Doc Lib
     */
    @Autowired
    private DocumentLibrary documentLibrary;
    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classify Document behaviour"
        // FIXME: Add dataSetup method here, once work for RM-2051 is complete (e.g. req. classify content dialog PO)
        // ,
        // dependsOnGroups = { "integration-dataSetup-collab-classified" }
    )

    public void classifyDocumentIndicator()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID, "documentlibrary");

        // verify document actions
        assertTrue(documentLibrary.getDocument(CLASSIFIED_DOCUMENT)
            .hasIndicator(DocumentIndicators.CLASSIFIED));
    }
}