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
package org.alfresco.po.rm.disposition.edit.steps;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.details.category.DispositionBlock;
import org.alfresco.po.rm.disposition.PeriodUnit;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Edit disposition steps page Unit Test
 *
 * @author Tatiana Kalinovskaya
 */
@Test(groups = {"unit-test"})
public class EditDispositionScheduleUnitTest extends BaseRmUnitTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** category details */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

    /** edit general information */
    @Autowired
    private EditDispositionSchedulePage editDispositionSchedulePage;

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
                .createDispositionSchedule();
    }

    @BeforeMethod
    public void beforeMethod()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        //open category details page
        filePlan.getRecordCategory(RECORD_CATEGORY_NAME).clickOnViewDetails();
        //open edit general information page
        categoryDetailsPage
                .editDispositionSteps();
    }

    @Test
    public void clickOnAddStep()
    {
        //expand Add steps drop-box (click on Add step)
        editDispositionSchedulePage.clickOnAddStep();
        //verify the Add steps drop-box is expanded
        assertTrue(editDispositionSchedulePage.isDropdownExpanded());
        //verify Cutoff and Retain are enabled
        assertTrue(editDispositionSchedulePage.isActionEnabled(CUTOFF_INDEX) && editDispositionSchedulePage.isActionEnabled(RETAIN_INDEX));
        //collapse Add steps drop-box (click on Add step)
        editDispositionSchedulePage.clickOnAddStep();
        //verify the Add steps dropdown is collapsed
        assertFalse(editDispositionSchedulePage.isDropdownExpanded());
    }

    @Test
    public void clickOnSave()
    {
        //add step and click on Save
        editDispositionSchedulePage
                .addStep(CUTOFF_INDEX)
                .setDescription(DESCRIPTION)
                .clickOnSave();
        //verify the step was added
        assertEquals(1, editDispositionSchedulePage.getDispositionStepsQuantity());
        //delete added step
        deleteStep(1);
    }

    @Test
    public void clickOnCancel()
    {
        //add step and click on Save
        editDispositionSchedulePage
                .addStep(CUTOFF_INDEX)
                .setDescription(DESCRIPTION)
                .clickOnCancel();
        //verify the step was not added
        assertEquals(0, editDispositionSchedulePage.getDispositionStepsQuantity());
    }

    @Test
    public void clickOnEditStep()
    {
        List<String> addedEvents = new ArrayList<>();
        //add step
       addedEvents = addCutOffStep();
        //click on edit for the first (just added) step
        DispositionStepBlock dispositionStep =  editDispositionSchedulePage.clickOnEdit(1);

        //verify the step information
        assertEquals("Cut off after 2 day(s)", dispositionStep.getStepTitle());
        //period information
        PeriodSection period = dispositionStep.getPeriodSection();
        assertTrue(period.isPeriodCheckboxSelected());
        assertEquals("Day", period.getPeriodUnit());
        assertEquals("2", period.getPeriodAmount());
        assertEquals("Created Date", period.getPeriodAction());
        //event information
        assertTrue(dispositionStep.isEventsCheckboxSelected());
        EventsSection events = dispositionStep.getEventsSection();
        assertEquals(addedEvents, events.getAddedEventsNames());
        assertFalse(events.isEligibleOnFirstCompleteEvent());

        //click on Cancel
        dispositionStep.clickOnCancel();
        //delete added step
        deleteStep(1);
    }

    @Test
    public void clickOnDeleteStep()
    {
        addCutOffStep();
        editDispositionSchedulePage
                .clickOnDelete(1)
                .clickOnConfirm();
        //verify the step was deleted
        assertEquals(0, editDispositionSchedulePage.getDispositionStepsQuantity());
    }

    @Test
    public void clickOnDone()
    {
        addCutOffStep();
        editDispositionSchedulePage.clickOnDone();

        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify one step is displayed on category details page
        assertEquals(1, dispositionBlock.getStepsQuantity());
        //verify disposition step name
        assertEquals("Cut off after 2 day(s)", dispositionBlock.getDispositionStepName(1));

        //delete te step
        categoryDetailsPage.editDispositionSteps();
        deleteStep(1);
    }

    @Test
    public void checkGhostOnDestroy()
    {
        addCutOffStep();
        //From 'Add Steps' drop-down select 'Destroy'
        DispositionStepBlock dispositionStep =
                editDispositionSchedulePage
                        .addStep(DESTROY_INDEX);
        dispositionStep
                .checkGhostOnDestroy(false)//uncheck GhostOnDestroy checkbox
                .setDescription(DESCRIPTION) //set description
                .clickOnSave(); //click Save
        //click on edit for the destroy step
        dispositionStep =  editDispositionSchedulePage.clickOnEdit(2);
        //verify the GhostOnDestroy checkbox is not selected
        assertFalse(dispositionStep.isGhostOnDestroyCheckboxSelected());

        //click on Cancel
        dispositionStep.clickOnCancel();
        //delete added step
        deleteStep(2);
        deleteStep(1);
    }

    /**
     * helper method to add first step
     * @return list of event names that were added to the step
     */
    private List <String> addCutOffStep()
    {
        //From 'Add Steps' drop-down select 'Cutoff'
        DispositionStepBlock dispositionStep =
                editDispositionSchedulePage
                        .addStep(CUTOFF_INDEX) ;
        //check 'After a period of' check-box, select '1 Day' value
        dispositionStep
                .getPeriodSection()
                .checkPeriod(true)
                .setPeriodUnit(PeriodUnit.DAY)
                .setPeriodAmount("2");
        //check 'When event occurs' check-box, select 2 events: "No longer needed" and "Case Closed", select 'When all events has occurred' value
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(NO_LONGER_NEEDED)
                .selectEvent(CASE_CLOSED)
                .setEligibleOnFirstCompleteEvent(false); //When all events have occurred
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(CUTOFF_LABEL)
                .clickOnSave();
        List<String> addedEvents = new ArrayList<>();
        addedEvents.add(NO_LONGER_NEEDED);
        addedEvents.add(CASE_CLOSED);
        return addedEvents;
    }

    /**
     * helper method to delete step
     */
    private void deleteStep(int number)
    {
        editDispositionSchedulePage
                .clickOnDelete(number)
                .clickOnConfirm();
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
