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

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordActions;
import org.alfresco.po.rm.browse.fileplan.RecordIndicators;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @since 3.0
 */
public class ClassifiedRecordIndicator extends BaseTest
{
    /**
     * file plan
     */
    @Autowired
    private FilePlan filePlan;
    /**
     * Main test execution
     */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classify Record behaviour",
        dependsOnGroups = { "integration-dataSetup-rmSite", "integration-dataSetup-collab",
            "integration-dataSetup-fileplan"
            // FIXME: Add dataSetup method here, once work for RM-2051 is complete (e.g. req. classify content dialog PO)
            //    , "integration-dataSetup-fileplan-classified"
        }
    )

    public void classifiedRecordIndicator()
    {

        // open record folder one
        openPage(filePlan, RM_SITE_ID, "documentlibrary").navigateTo(RECORD_CATEGORY_ONE, SUB_RECORD_CATEGORY_NAME,
            RECORD_FOLDER_ONE);

        // Is the indicator available on the classified record
        assertTrue(filePlan.getRecord(CLASSIFIED_RECORD)
            .hasIndicator(RecordIndicators.CLASSIFIED));

        // Is the indicator available on the classified non electronic record
        assertTrue(filePlan.getRecord(CLASSIFIED_NON_ELECTRONIC_RECORD)
            .hasIndicator(RecordIndicators.CLASSIFIED));

    }
}