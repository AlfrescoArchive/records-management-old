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

import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.ClassifiedDocumentDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifiedDocumentProperties extends BaseTest
{
    @Autowired
    private DocumentLibrary documentLibrary;
    @Autowired
    private ClassifiedDocumentDetails classifiedDocumentDetails;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classified Document Properties behaviour",
        dependsOnGroups = { GROUP_CLASSIFIED_DOCUMENT_EXISTS }
    )
    public void classifyDocumentProperties()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID, "documentlibrary");

        // navigate to the document details page
        documentLibrary.getDocument(CLASSIFIED_DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);

        // verify that classification is as expected.
        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION)
            .equals(CLASSIFICATION_LEVEL_ABBREVIATION));

        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_AUTHORITY)
            .equals(CLASSIFICATION_AUTHORITY));

        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_REASON)
            .equals(CLASSIFICATION_REASON));
    }
}