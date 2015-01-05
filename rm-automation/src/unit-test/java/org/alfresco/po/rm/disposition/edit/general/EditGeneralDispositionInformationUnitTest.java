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
package org.alfresco.po.rm.disposition.edit.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.details.category.CategoryDetails;
import org.alfresco.po.rm.details.category.DispositionBlock;
import org.alfresco.po.rm.disposition.DispositionLevel;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Edit General disposition properties page Unit Test
 *
 * @author Tatiana Kalinovskaya
 */
@Test(groups = {"unit-test"})
public class EditGeneralDispositionInformationUnitTest extends BaseRmUnitTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** category details */
    @Autowired
    private CategoryDetails categoryDetails;

    /** edit general information */
    @Autowired
    private EditGeneralDispositionInformationPage editGeneralDispositionInformationPage;

    @BeforeClass
    public void beforeClass()
    {
        // create RM site
        createRMSite();

        // create new category
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .getToolbar()
                .clickOnNewCategory()
                .setName(RECORD_CATEGORY_NAME)
                .setTitle(TITLE)
                .clickOnSave();

        //create disposition schedule
        filePlan
                .getRecordCategory(RECORD_CATEGORY_NAME)
                .clickOnViewDetails()
                .clickOnCreateDispositionSchedule();
    }
    
    @BeforeMethod
    public void beforeMethod()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        //open category details page
        filePlan.getRecordCategory(RECORD_CATEGORY_NAME).clickOnViewDetails();
        //open edit general information page
        categoryDetails
                .clickEditDispositionGeneral();
    }

    @Test
    public void clickOnSave()
    {
        //verify the disposition level is enabled
        assertTrue(editGeneralDispositionInformationPage.isDispositionLevelEnabled());
        editGeneralDispositionInformationPage
                .setDispositionAuthority(DISPOSITION_AUTHORITY)
                .setDispositionInstructions(DISPOSITION_INSTRUCTIONS)
                .setDispositionLevel(DispositionLevel.RECORD)
                .clickOnSave();
        DispositionBlock dispositionBlock = categoryDetails.getDispositionBlock();
        //verify general information
        assertEquals(DISPOSITION_AUTHORITY, dispositionBlock.getDispositionAuthority());
        assertEquals(DISPOSITION_INSTRUCTIONS, dispositionBlock.getDispositionInstructions());
        assertEquals("Record", dispositionBlock.getAppliedTo());
    }

    @Test(dependsOnMethods="clickOnSave")
    public void gettingValues()
    {
        // verify Disposition Authority is empty
        assertEquals(DISPOSITION_AUTHORITY, editGeneralDispositionInformationPage.getDispositionAuthority());
        // verify Disposition Instructions is empty
        assertEquals(DISPOSITION_INSTRUCTIONS, editGeneralDispositionInformationPage.getDispositionInstructions());
        //verify the disposition level is enabled
        assertTrue(editGeneralDispositionInformationPage.isDispositionLevelEnabled());
        //verify disposition level is set to Record Folder
        assertEquals(DispositionLevel.RECORD_FOLDER, editGeneralDispositionInformationPage.getDispositionLevel());
    }

    @Test(dependsOnMethods="clickOnSave")
    public void clickOnCancel()
    {
        //verify the disposition level is enabled
        assertTrue(editGeneralDispositionInformationPage.isDispositionLevelEnabled());
        editGeneralDispositionInformationPage
                .setDispositionAuthority(DISPOSITION_AUTHORITY + MODIFIED)
                .setDispositionInstructions(DISPOSITION_INSTRUCTIONS + MODIFIED)
                .setDispositionLevel(DispositionLevel.RECORD_FOLDER)
                .clickOnCancel();
        DispositionBlock dispositionBlock = categoryDetails.getDispositionBlock();
        //verify general information hasn't changed
        assertEquals(DISPOSITION_AUTHORITY, dispositionBlock.getDispositionAuthority());
        assertEquals(DISPOSITION_INSTRUCTIONS, dispositionBlock.getDispositionInstructions());
        assertEquals("Record", dispositionBlock.getAppliedTo());
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
