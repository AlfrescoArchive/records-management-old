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

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.rm.details.record.ClassifiedRecordDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifiedRecordProperties extends BaseTest
{
    @Autowired
    private FilePlan filePlan;
    @Autowired
    private ClassifiedRecordDetails classifiedRecordDetails;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classified Record Properties behaviour",
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
        assertEquals(CLASSIFICATION_LEVEL_ABBREVIATION,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION));

        assertEquals(CLASSIFICATION_AUTHORITY,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_AUTHORITY));

        assertEquals(CLASSIFICATION_REASON,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_REASON));
    }
}