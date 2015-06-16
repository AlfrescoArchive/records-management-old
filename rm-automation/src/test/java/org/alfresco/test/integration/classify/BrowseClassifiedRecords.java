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
import static org.testng.AssertJUnit.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordIndicators;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.details.record.ClassifiedRecordDetails;
import org.alfresco.po.share.browse.documentlibrary.ContentBanner;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Tests related to browsing classified records.
 *
 * @author David Webster
 * @author tpage
 * @since 3.0
 */
public class BrowseClassifiedRecords extends BaseTest
{
    @Autowired
    private FilePlan filePlan;
    @Autowired
    private ClassifiedRecordDetails classifiedRecordDetails;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /**
     * Check how a classified record appears in the file plan view.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2001">RM-2001</a><pre>
     * Given that there is some classified content in the folder I am viewing in the document library
     * And that my security clearance is sufficient that I can see the classified content
     * When the classified content is presented in the list view
     * Then I can see a visual indicator that tells me it's classified
     * </pre>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2278">RM-2278</a><pre>
     * Given that a record has been classified
     * When I browse the file plan
     * Then the classification level of the record is clearly visible
     * </pre>
     */
    @Test
    (
        groups = { "integration", "ignored" },
        description = "Verify Classify Record behaviour",
        dependsOnGroups = { GROUP_CLASSIFIED_RECORD_EXISTS }
    )
    public void classifiedRecordIndicator()
    {
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE));

        Record record = filePlan.getRecord(CLASSIFIED_RECORD);
        assertTrue("Expected record to have classified indicator.", record.hasIndicator(RecordIndicators.CLASSIFIED));

        assertEquals("Expected 'Secret' classification banner to be visible.",
                    "Classified: " + SECRET_CLASSIFICATION_LEVEL_TEXT,
                    record.getBannerText(ContentBanner.CLASSIFICATION));
    }

    /**
     * Verify record details page displays classification information.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2083">RM-2083</a><pre>
     * Given that I have the appropriate security clearance to see a classified record
     * When I view the record details
     * Then I see information about the classification including:
     * * Initial classification
     * * Current classification
     * * Classification authority
     * * Classification reasons
     * </pre>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2282">RM-2282</a><pre>
     * Given that a record has been classified
     * When I view the record details page
     * Then the classification level of the record is clearly visible
     * </pre>
     */
    @Test
    (
        groups = { "integration", "ignored" },
        description = "Verify record details page displays classification information.",
        dependsOnGroups = { GROUP_CLASSIFIED_RECORD_EXISTS }
    )
    public void classifyRecordProperties()
    {
        // Open Collab site DocumentLibrary.
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME, RECORD_FOLDER_ONE));

        // navigate to the document details page
        filePlan.getRecord(CLASSIFIED_RECORD)
            .clickOnLink(classifiedRecordDetails);

        // verify that classification is as expected.
        assertEquals(SECRET_CLASSIFICATION_LEVEL_TEXT,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION));

        assertEquals(CLASSIFICATION_AUTHORITY,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_AUTHORITY));

        assertEquals(CLASSIFICATION_REASON,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_REASON));

        assertEquals("Expected 'Secret' classification banner to be visible.",
                    "Classified: " + SECRET_CLASSIFICATION_LEVEL_TEXT,
                    classifiedRecordDetails.getBannerText(ContentBanner.CLASSIFICATION));
    }
}
