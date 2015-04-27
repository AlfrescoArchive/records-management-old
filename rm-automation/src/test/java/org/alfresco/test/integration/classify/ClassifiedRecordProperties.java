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
    /**
     * Doc Lib
     */
    @Autowired
    private FilePlan filePlan;

    @Autowired
    private ClassifiedRecordDetails classifiedRecordDetails;

    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;
    /**
     * Main test execution
     */
    @Test(
        groups = { "integration" },
        description = "Verify Classified Record Properties behaviour"
        // FIXME: Add dataSetup method here, once work for RM-2051 is complete (e.g. req. classify content dialog PO)
        // ,
        // dependsOnGroups = { "integration-dataSetup-fileplan-classified" }
    )

    public void classifyRecordProperties()
    {
        // Open Collab site DocumentLibrary.
        openPage(filePlan, RM_SITE_ID, "documentlibrary").navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME,
            RECORD_FOLDER_ONE);

        // navigate to the document details page
        filePlan.getRecord(CLASSIFIED_RECORD)
            .clickOnLink(classifiedRecordDetails);

        // verify that classification is as expected.
        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION)
            .equals(CLASSIFICATION_LEVEL_ABBREVIATION));

        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_AUTHORITY)
            .equals(CLASSIFICATION_AUTHORITY));

        assertTrue(classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_REASON)
            .equals(CLASSIFICATION_REASON));
    }
}