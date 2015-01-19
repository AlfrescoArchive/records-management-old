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
package org.alfresco.test.regression.sanity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.browse.transfers.Transfers;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.details.category.DispositionBlock;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.DestroyConfirmationDialog;
import org.alfresco.po.rm.dialog.EditDispositionDateDialog;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.po.rm.disposition.DispositionLevel;
import org.alfresco.po.rm.disposition.PeriodUnit;
import org.alfresco.po.rm.disposition.edit.steps.DispositionStepBlock;
import org.alfresco.po.rm.disposition.edit.steps.EditDispositionSchedulePage;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Manage disposition schedule for Record
 *
 * @author Tatiana Kalinovskaya
 */
public class ManageDispositionScheduleForRecord extends BaseTest
{
    /** file plan     */
    @Autowired
    private FilePlan filePlan;

    /**edit disposition schedule page     */
    @Autowired
    private EditDispositionSchedulePage editDispositionSchedulePage;

    /** category details     */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

    /** record details page */
    @Autowired
    private RecordDetails recordDetails;

    /**edit disposition date dialog     */
    @Autowired
    private EditDispositionDateDialog editDispositionDateDialog;

    /** transfers */
    @Autowired
    private Transfers transfers;

    /** unfiled records */
    @Autowired
    private UnfiledRecords unfiledRecords;


    /** confirmation prompt */
    @Autowired
    private DestroyConfirmationDialog destroyConfirmationDialog;


    @BeforeMethod(alwaysRun=true)
    public void beforeMethod()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        // create new category
        filePlan
                .getToolbar()
                .clickOnNewCategory()
                .setName(CATEGORY_RECORD_DISPOSITION)
                .setTitle(TITLE)
                .clickOnSave();

    }

    /**
     * Main regression test execution
     */
    @Test
    (
            groups = { "RMA-2671", "sanity" },
            description = "Manage disposition schedule for Record",
            dependsOnGroups = { "RMA-2664" }
    )
    public void manageDispositionScheduleForRecord()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");

        //open category details page
        filePlan.getRecordCategory(CATEGORY_RECORD_DISPOSITION).clickOnViewDetails();

        //create disposition schedule
        categoryDetailsPage.createDispositionSchedule();

        // edit General information
        categoryDetailsPage
                .editDispositionGeneral()
                .setDispositionAuthority(DISPOSITION_AUTHORITY + RECORD)
                .setDispositionInstructions(DISPOSITION_INSTRUCTIONS + RECORD)
                .setDispositionLevel(DispositionLevel.RECORD)
                .clickOnSave();
        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify the category details page reflect the changes
        assertEquals(DISPOSITION_AUTHORITY + RECORD, dispositionBlock.getDispositionAuthority());
        assertEquals(DISPOSITION_INSTRUCTIONS + RECORD, dispositionBlock.getDispositionInstructions());
        assertEquals("Record", dispositionBlock.getAppliedTo());

        //edit steps
        editSteps();

        //assert the category details page reflect the changes
        {
            dispositionBlock = categoryDetailsPage.getDispositionBlock();
            //verify general information
            assertEquals(DISPOSITION_AUTHORITY + RECORD, dispositionBlock.getDispositionAuthority());
            assertEquals(DISPOSITION_INSTRUCTIONS + RECORD, dispositionBlock.getDispositionInstructions());
            assertEquals("Record", dispositionBlock.getAppliedTo());
            //verify disposition steps
            assertEquals(5, dispositionBlock.getStepsQuantity());
            assertEquals(CUTOFF_LABEL + " immediately" , dispositionBlock.getDispositionStepName(1));
            assertEquals(RETAIN_LABEL + " after 1 day(s)", dispositionBlock.getDispositionStepName(2));
            assertEquals(TRANSFER_LABEL, dispositionBlock.getDispositionStepName(3));
            assertEquals(ACCESSION_LABEL, dispositionBlock.getDispositionStepName(4));
            assertEquals(DESTROY_LABEL, dispositionBlock.getDispositionStepName(5));
        }
        //navigate inside the category
        categoryDetailsPage.navigateUp();

        //create folder
        // open new record folder dialog
        NewRecordFolderDialog dialog = filePlan.getToolbar().clickOnNewRecordFolder();
        // enter details of new record folder and click save
        dialog.setName(RECORD_FOLDER_ONE)
                .setTitle(TITLE)
                .clickOnSave();

        // navigate inside the folder
        filePlan.getRecordFolder(RECORD_FOLDER_ONE).clickOnLink();

        //file a record
        filePlan
                .getToolbar().clickOnFile()
                .clickOnElectronic()
                .uploadFile(RECORD);

        //complete the record
        filePlan.getRecord(RECORD).clickOnCompleteRecord();

        Record record = filePlan.getRecord(RECORD);
        //verify edit disposition date is available
        assertTrue(record.isActionClickable(Record.EDIT_DISPOSITION_DATE));
        //verify cut off is available
        assertTrue(record.isActionClickable(Record.CUTOFF));

        //click on cut off
        record.clickOnCutOff();

        record = filePlan.getRecord(RECORD);
        //verify record is cut off
        assertTrue(record.isCutOff());

        //verify available actions
        assertNull(record.isActionsClickable(
                Record.DOWNLOAD,
                Record.EDIT_METADATA,
                Record.UNDO_CUTOFF,
                Record.EDIT_DISPOSITION_DATE,
                Record.COPY,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT,
                Record.MANAGE_PERMISSIONS,
                Record.ADD_RELATIONSHIP));

        //edit disposition date - set it to today
        record.clickOnEditDispositionDate();
        editDispositionDateDialog
                .selectTodayCell()
                .clickOnUpdate(filePlan);

        record = filePlan.getRecord(RECORD);
        //verify end retention is available
        assertTrue(record.isActionClickable(Record.END_RETENTION));

        //TODO modify this step to wait for 5 minutes
        //retain the record
        record.clickOnEndRetention();

        //verify end retention is no longer available
        record = filePlan.getRecord(RECORD);
        assertNull(record.isActionsClickable(
                Record.DOWNLOAD,
                Record.EDIT_METADATA,
                Record.COPY,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT,
                Record.MANAGE_PERMISSIONS,
                Record.ADD_RELATIONSHIP));

        //navigate to details page of record
        recordDetails = record.clickOnLink();

        //verify Separation event is present
        assertEquals(1, recordDetails.getEventsQuantity());
        assertNotNull(recordDetails.getEventByName(SEPARATION));

        //complete the event
        recordDetails
                .clickOnCompleteEvent(SEPARATION)
                .clickOnOk(recordDetails);

        //verify available actions
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                Record.EDIT_METADATA,
                Record.TRANSFER,
                Record.COPY,
                Record.LINK,
                Record.DELETE,
                Record.MANAGE_PERMISSIONS,
                Record.VIEW_AUDIT,
                Record.ADD_RELATIONSHIP));

        ////navigate to browse view - inside the folder
        recordDetails.navigateUp();
        //transfer record
        filePlan.getRecord(RECORD).clickOnTransfer();

        //verify there is one transfer
        assertEquals(1, transfers.getList().size());
        //complete transfer
        transfers.getTransfer().clickOnCompleteTransfer();

        //navigate inside the folder
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(CATEGORY_RECORD_DISPOSITION, RECORD_FOLDER_ONE);

        record = filePlan.getRecord(RECORD);
        //verify the record is marked as transferred
        assertTrue(record.isTransferred());

        //open details page of record
        record.clickOnLink();

        //verify Accession action is not available
        assertFalse(recordDetails.getRecordActionsPanel().isActionAvailable(Record.ACCESSION));

        //verify "Study Complete" and "Training Complete" evens are present
        assertEquals(2, recordDetails.getEventsQuantity());
        assertNotNull(recordDetails.getEventByName(STUDY_COMPLETE));
        assertNotNull(recordDetails.getEventByName(TRAINING_COMPLETE));

        //complete the "Study Complete" event
        recordDetails
                .clickOnCompleteEvent(STUDY_COMPLETE)
                .clickOnOk(recordDetails);

        //verify Accession action is available
        assertTrue(recordDetails.getRecordActionsPanel().isActionAvailable(Record.ACCESSION));

        //accession the record
        recordDetails.getRecordActionsPanel().clickOnAction(Record.ACCESSION);

        //navigate to browse view - inside the folder
        recordDetails.navigateUp();
        //verify the awaiting accession indicator is displayed for record
        assertTrue(filePlan.getRecord(RECORD).isAccessionPending());

        //navigate to transfers
        filePlan
                .getFilterPanel()
                .clickOnTransfers();

        //verify there is one transfer
        assertEquals(1, transfers.getList().size());
        //complete accession
        transfers.getTransfer().clickOnCompleteAccession();

        //verify the accession is no longer displayed
        assertEquals(0,transfers.getList().size());

        //navigate inside folder
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(CATEGORY_RECORD_DISPOSITION, RECORD_FOLDER_ONE);

        //verify the record is marked as accessioned
        record = filePlan.getRecord(RECORD);
        assertTrue(record.isAccessioned());

        //open details page of record
        record.clickOnLink();

        //verify case closed and no longer needed events are displayed
        assertEquals(2,recordDetails.getEventsQuantity());
        assertNotNull(recordDetails.getEventByName(NO_LONGER_NEEDED));
        assertNotNull(recordDetails.getEventByName(CASE_CLOSED));

        //complete 'Case Closed' event
        recordDetails
                .clickOnCompleteEvent(CASE_CLOSED)
                .clickOnOk(recordDetails);

        //verify Destroy action is not available
        assertFalse(recordDetails.getRecordActionsPanel().isActionAvailable(Record.DESTROY));

        //complete the 'No longer needed' event
        recordDetails
                .clickOnCompleteEvent(NO_LONGER_NEEDED)
                .clickOnOk(recordDetails);

        //verify Destroy is now available
        assertTrue(recordDetails.getRecordActionsPanel().isActionClickable(Record.DESTROY));

        //click on Destroy and confirm
        recordDetails
                .getRecordActionsPanel()
                .clickOnAction(Record.DESTROY,destroyConfirmationDialog)
                .clickOnOkOk();

        //verify the record is marked as destroyed
        record = filePlan.getRecord(RECORD);
        assertTrue(record.isDestroyed());

        //generate destruction report
        record
                .clickOnGenerateDestructionReport()
                .clickOnFileReport();

        //navigate to Unfiled Records
        filePlan
                .getFilterPanel()
                .clickOnUnfiledRecords();

        //verify the report is present
        assertNotNull("Destruction report not present in unfiled records.", unfiledRecords.getList().getByPartialName("Destruction Report"));

        //TODO verify the information inside Destruction report
    }

    public  void editSteps()
    {
        editDispositionSchedulePage = categoryDetailsPage.editDispositionSteps();

        //From 'Add Steps' drop-down select 'Cutoff'
        DispositionStepBlock dispositionStep =
                editDispositionSchedulePage
                        .addStep(CUTOFF_INDEX) ;
        //check 'After a period of' check-box, select 'Immediately' value
        dispositionStep
                .getPeriodSection()
                .checkPeriod(true)
                .setPeriodUnit(PeriodUnit.IMMEDIATELY);
        assertEquals("Date Filed", dispositionStep.getPeriodSection().getPeriodAction());
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(CUTOFF_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Retain'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(RETAIN_INDEX);
        //check 'After a period of' check-box, select '1 day' value
        dispositionStep
                .getPeriodSection()
                .checkPeriod(true)
                .setPeriodUnit(PeriodUnit.DAY)
                .setPeriodAmount("1");
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(RETAIN_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Transfer'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(TRANSFER_INDEX);
        //check 'When event occurs' check-box, select 'Separation' event
        dispositionStep
                .checkEvents(true)
                .addEvent(SEPARATION);
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(TRANSFER_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Accession'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(ACCESSION_INDEX);
        //check 'When event occurs' check-box, select 2 events: "Study Complete" and "Training Complete", select 'Which ever event is earlier' value
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(STUDY_COMPLETE)
                .selectEvent(TRAINING_COMPLETE)
                .setEligibleOnFirstCompleteEvent(true);
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(ACCESSION_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Destroy'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(DESTROY_INDEX);
        //check 'When event occurs' check-box, select 2 events: "No longer needed" and "Case Closed", select 'When all events has occurred' value
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(NO_LONGER_NEEDED)
                .selectEvent(CASE_CLOSED)
                .setEligibleOnFirstCompleteEvent(false);
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(DESTROY_LABEL)
                .clickOnSave();

        //click Done
        editDispositionSchedulePage.clickOnDone();
    }

}
