/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
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

package org.alfresco.test.integration.dataSetup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Create File Plan for Integration tests
 *
 * @author Roy Wetherall, David Webster
 * @since 3.0
 * todo: Copied form org.alfresco.test.regression.sanity - needs modification.
 */
public class CreateFilePlan extends BaseTest
{
    /** file plan browse view */
    @Autowired
    private FilePlan filePlan;

    /**
     * Integration test execution
     */
    @Test
    (
        groups = { "integration", "GROUP_FILE_PLAN_EXISTS" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS" }
    )
    public void createFilePlan()
    {
        // open the RM site and navigate to file plan
        openPage(userDashboardPage).getMySitesDashlet()
            .clickOnRMSite(RM_SITE_ID)
            .getNavigation()
            .clickOnFilePlan();
    }

    @Test
    (
        groups = { "integration", "GROUP_CATEGORY_ONE_EXISTS" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_FILE_PLAN_EXISTS" }
    )
    public void createCategoryOne()
    {
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        createCategory(RECORD_CATEGORY_ONE);
    }

    @Test
    (
        groups = { "integration", "GROUP_SUB_CATEGORY_EXISTS" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_CATEGORY_ONE_EXISTS" }
    )
    public void createSubCategory()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE));
        createCategory(SUB_RECORD_CATEGORY_NAME);
    }

    @Test
    (
        groups = { "integration", "GROUP_RECORD_FOLDER_ONE_EXISTS" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_CATEGORY_ONE_EXISTS" }
    )
    public void createRecordFolderOne()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE));
        createRecordFolder(RECORD_FOLDER_ONE);
    }

    @Test
    (
        groups = { "integration", "GROUP_RECORD_FOLDER_TWO_EXISTS" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_CATEGORY_ONE_EXISTS" }
    )
    public void createRecordFolderTwo()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary", RECORD_CATEGORY_ONE));
        createRecordFolder(RECORD_FOLDER_TWO);
    }

    /** Create non-electronic record. */
    @Test
    (
        groups = { "integration", "GROUP_NON_ELECTRONIC_RECORD_EXISTS" },
        description = "Create non-electronic record.",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_ONE_EXISTS" }
    )
    public void createNonElectronicRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        // create non electronic record in folder 1
        filePlan.getToolbar()
            .clickOnFile()
            .clickOnNonElectronic()
            .setName(NON_ELECTRONIC_RECORD)
            .setTitle(TITLE)
            .clickOnSave();

        // check that the record has been created
        Record nonElectronicRecord = filePlan.getRecord(NON_ELECTRONIC_RECORD);
        assertNotNull(nonElectronicRecord);
    }

    /** Create electronic record. */
    @Test
    (
        groups = { "integration", "GROUP_ELECTRONIC_RECORD_EXISTS" },
        description = "Create electronic record.",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_ONE_EXISTS" }
    )
    public void createElectronicRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        // create electronic record in folder 1
        filePlan.getToolbar()
            .clickOnFile()
            .clickOnElectronic()
            .uploadFile(RECORD);

        // check that the record has been created
        Record record = filePlan.getRecord(RECORD);
        assertNotNull(record);
    }

    /** Create complete record. */
    @Test
    (
        groups = { "integration", "GROUP_COMPLETE_RECORD_EXISTS" },
        description = "Create complete record",
        dependsOnGroups = { "GROUP_RECORD_FOLDER_ONE_EXISTS" }
    )
    public void createCompleteRecord()
    {
        openPage(filePlan, RM_SITE_ID,
                    createPathFrom("documentlibrary", RECORD_CATEGORY_ONE, RECORD_FOLDER_ONE));

        filePlan.getToolbar()
            .clickOnFile()
            .clickOnElectronic()
            .uploadFile(COMPLETE_RECORD);

        filePlan.getRecord(COMPLETE_RECORD)
            .clickOnCompleteRecord();
    }

    /**
     * Create a category
     *
     * @param categoryName category name
     */
    private void createCategory(String categoryName)
    {
        createCategoryAndClickOnLink(categoryName, false);
    }

    /**
     * Create a category
     *
     * @param categoryName category name
     * @param clickOnLink  true if newly created record category link should be created, false otherwise
     */
    private void createCategoryAndClickOnLink(String categoryName, boolean clickOnLink)
    {
        // check that create category button is clickable
        assertTrue(filePlan.getToolbar()
            .isNewCategoryClickable());

        // create new category
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(categoryName)
            .setTitle(TITLE)
            .clickOnSave();

        // check that the newly created record category is in the display list
        RecordCategory recordCategory = filePlan.getRecordCategory(categoryName);
        assertNotNull(recordCategory);
        assertEquals(categoryName, recordCategory.getName());

        if (clickOnLink)
        {
            // click on link
            recordCategory.clickOnLink();
        }
    }

    /**
     * Create record folder
     *
     * @param folderName  folder name
     */
    private void createRecordFolder(String folderName)
    {
        createRecordFolderAndClickOnLink(folderName, false);
    }

    /**
     * Create record folder
     *
     * @param folderName  folder name
     * @param clickOnLink click on created folder link if true, otherwise false
     */
    private void createRecordFolderAndClickOnLink(String folderName, boolean clickOnLink)
    {
        // check that create category button is clickable
        assertTrue(filePlan.getToolbar()
            .isNewRecordFolderClickable());

        // open new record category dialog
        NewRecordFolderDialog dialog = filePlan.getToolbar()
            .clickOnNewRecordFolder();

        // enter details of new record folder and click save
        dialog.setName(folderName)
            .setTitle(TITLE)
            .clickOnSave();

        // check that the newly created record folder is in the display list
        RecordFolder recordFolder = filePlan.getRecordFolder(folderName);
        assertNotNull(recordFolder);
        assertEquals(folderName, recordFolder.getName());

        if (clickOnLink)
        {
            // click on link
            recordFolder.clickOnLink();
        }
    }
}
