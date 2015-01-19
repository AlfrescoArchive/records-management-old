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
package org.alfresco.po.rm.details.category;

import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.disposition.DispositionLevel;
import org.alfresco.po.rm.disposition.edit.general.EditGeneralDispositionInformationPage;
import org.alfresco.po.rm.disposition.edit.steps.EditDispositionSchedulePage;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.junit.Assert.*;

/**
 * Category Details page Unit Test
 *
 * @author Tatiana Kalinovskaya
 */
@Test(groups = {"unit-test"})
public class CategoryDetailsPageUnitTest extends BaseRmUnitTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** audit log page*/
    @Autowired
    private AuditLogPage auditLogPage;

    /** category details page */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

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
    }

    @BeforeMethod
    public void beforeMethod()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        //open category details page
        filePlan
                .getRecordCategory(RECORD_CATEGORY_NAME)
                .clickOnViewDetails();
    }

    @Test
    public void verifyActionsPanel()
    {
        //get actions panel
        CategoryActionsPanel categoryActionsPanel = categoryDetailsPage.getCategoryActionsPanel();
        //verify View Audit Log is available
        assertTrue(categoryActionsPanel.isActionAvailable(RecordCategory.VIEW_AUDIT));
        //verify View Audit Log is clickable
        assertTrue(categoryActionsPanel.isActionClickable(RecordCategory.VIEW_AUDIT));
        //verify click On Audit Log action
        categoryActionsPanel.clickOnAction(RecordCategory.VIEW_AUDIT, auditLogPage);
        assertEquals("Audit log for " + RECORD_CATEGORY_NAME, auditLogPage.getAuditPageHeader());
        auditLogPage.close();
    }

    @Test
    public void expandActionsPanel()
    {
        //click on title of Actions Panel to collapse it
        categoryDetailsPage.getCategoryActionsPanel().clickOnTitle();
        //verify it is collapsed
        assertFalse(categoryDetailsPage.getCategoryActionsPanel().isPanelExpanded());

        //click on title of Actions Panel to expand it
        categoryDetailsPage.getCategoryActionsPanel().clickOnTitle();
        //verify it is expanded
        assertTrue(categoryDetailsPage.getCategoryActionsPanel().isPanelExpanded());
    }

    @Test
    public void noDispositionCreated()
    {
        //get disposition block
        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify "Craete Disposition Schedule" button is enabled
        assertTrue(dispositionBlock.isCreateDispositionScheduleEnabled());
        //verify disposition is not created
        assertFalse(dispositionBlock.isDispositionScheduleCreated());
    }

    @Test(dependsOnMethods="noDispositionCreated")
    public void createDispositionSchedule()
    {
        categoryDetailsPage.createDispositionSchedule();
        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify disposition is created
        assertTrue(dispositionBlock.isDispositionScheduleCreated());
        //verify unpublished updated aren't available
        assertFalse(dispositionBlock.isUnpublishedUpdateAvailable());
    }

    @Test (dependsOnMethods = "createDispositionSchedule")
    public void editDispositionGeneral()
    {
        // click on edit General information
        EditGeneralDispositionInformationPage editGeneralDispositionInformationPage = categoryDetailsPage.editDispositionGeneral();
        //verify the edit disposition page has opened
        assertTrue(editGeneralDispositionInformationPage.isDispositionLevelEnabled());
    }

    @Test (dependsOnMethods = "editDispositionGeneral")
    public void editDispositionSteps()
    {
        // click on edit General information
        EditDispositionSchedulePage editDispositionSchedulePage = categoryDetailsPage.editDispositionSteps();
        //verify the edit disposition steps page has opened
        assertFalse(editDispositionSchedulePage.isDropdownExpanded());
    }
    
    @Test (dependsOnMethods = "createDispositionSchedule")
    public void verifyGeneralInformation()
    {
        // edit General information
        categoryDetailsPage
                .editDispositionGeneral()
                .setDispositionAuthority(DISPOSITION_AUTHORITY + FOLDER)
                .setDispositionInstructions(DISPOSITION_INSTRUCTIONS + FOLDER)
                .setDispositionLevel(DispositionLevel.RECORD_FOLDER)
                .clickOnSave();
        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify the General information
        assertEquals(DISPOSITION_AUTHORITY + FOLDER, dispositionBlock.getDispositionAuthority());
        assertEquals(DISPOSITION_INSTRUCTIONS + FOLDER, dispositionBlock.getDispositionInstructions());
        assertEquals("Folder", dispositionBlock.getAppliedTo());
    }
    
    @Test (dependsOnMethods = "createDispositionSchedule")
    public void verifyStepsInformation()
    {
        //add cutoff step with description
        EditDispositionSchedulePage editDispositionSchedulePage = categoryDetailsPage.editDispositionSteps();
        editDispositionSchedulePage.addStep(CUTOFF_INDEX).setDescription(CUTOFF_LABEL + DESCRIPTION).clickOnSave().clickOnDone();

        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify disposition steps information
        assertEquals(1, dispositionBlock.getStepsQuantity());
        assertEquals(CUTOFF_LABEL, dispositionBlock.getDispositionStepName(1));
        assertEquals(CUTOFF_LABEL + DESCRIPTION, categoryDetailsPage.viewStepDescription(1));
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
