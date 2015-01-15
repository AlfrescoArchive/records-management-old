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
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.browse.transfers.Transfers;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.details.category.DispositionBlock;
import org.alfresco.po.rm.details.folder.FolderDetailsPage;
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
 * Manage disposition schedule for Folder
 *
 * @author Tatiana Kalinovskaya
 */
public class ManageDispositionScheduleForFolder extends BaseTest
{
    /**
     * file plan
     */
    @Autowired
    private FilePlan filePlan;

    /**
     * edit disposition schedule page
     */
    @Autowired
    private EditDispositionSchedulePage editDispositionSchedulePage;

    /**
     * category details
     */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

    /** folder details page */
    @Autowired
    private FolderDetailsPage folderDetailsPage;

    /**
     * edit disposition date dialog
     */
    @Autowired
    private EditDispositionDateDialog editDispositionDateDialog;

    /** transfers */
    @Autowired
    private Transfers transfers;

    /** unfiled records */
    @Autowired
    private UnfiledRecords unfiledRecords;

    @BeforeMethod(alwaysRun=true)
    public void beforeMethod()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        // create new category
        filePlan
                .getToolbar()
                .clickOnNewCategory()
                .setName(CATEGORY_FOLDER_DISPOSITION)
                .setTitle(TITLE)
                .clickOnSave();

    }

    /**
     * Main regression test execution
     */
    @Test
    (
            groups = { "RMA-2670", "sanity" },
            description = "Manage disposition schedule for Folder",
            dependsOnGroups = { "RMA-2664" }
    )
    public void manageDispositionScheduleForFolder()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");

        //open category details page
        filePlan.getRecordCategory(CATEGORY_FOLDER_DISPOSITION).clickOnViewDetails();

        //create disposition schedule
        categoryDetailsPage.createDispositionSchedule();

        // edit General information
        categoryDetailsPage
                .editDispositionGeneral()
                .setDispositionAuthority(DISPOSITION_AUTHORITY + FOLDER)
                .setDispositionInstructions(DISPOSITION_INSTRUCTIONS + FOLDER)
                .setDispositionLevel(DispositionLevel.RECORD_FOLDER)
                .clickOnSave();
        DispositionBlock dispositionBlock = categoryDetailsPage.getDispositionBlock();
        //verify the category details page reflect the changes
        assertEquals(DISPOSITION_AUTHORITY + FOLDER, dispositionBlock.getDispositionAuthority());
        assertEquals(DISPOSITION_INSTRUCTIONS + FOLDER, dispositionBlock.getDispositionInstructions());
        assertEquals("Folder", dispositionBlock.getAppliedTo());

        //edit steps
        editSteps();

        //assert the category details page reflect the changes
        {
            dispositionBlock = categoryDetailsPage.getDispositionBlock();
            //verify general information
            assertEquals(DISPOSITION_AUTHORITY + FOLDER, dispositionBlock.getDispositionAuthority());
            assertEquals(DISPOSITION_INSTRUCTIONS + FOLDER, dispositionBlock.getDispositionInstructions());
            assertEquals("Folder", dispositionBlock.getAppliedTo());
            //verify disposition steps
            assertEquals(5, dispositionBlock.getStepsQuantity());
            assertEquals(CUTOFF_LABEL + " after 1 day(s)", dispositionBlock.getDispositionStepName(1));
            assertEquals(TRANSFER_LABEL, dispositionBlock.getDispositionStepName(2));
            assertEquals(RETAIN_LABEL, dispositionBlock.getDispositionStepName(3));
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

        //navigate to category
        filePlan.navigateUp();

        RecordFolder folder = filePlan.getRecordFolder(RECORD_FOLDER_ONE);
        //verify edit disposition date is available
        assertTrue(folder.isActionClickable(RecordFolder.EDIT_DISPOSITION_DATE));
        //verify cut off is not available
        assertFalse(folder.isActionClickable(RecordFolder.CUTOFF));

        //edit disposition date - set it to today
        folder.clickOnEditDispositionDate();
        editDispositionDateDialog
                .selectTodayCell()
                .clickOnUpdate(filePlan);

        folder = filePlan.getRecordFolder(RECORD_FOLDER_ONE);
        //verify edit disposition date is available
        assertTrue(folder.isActionClickable(RecordFolder.EDIT_DISPOSITION_DATE));
        //verify cut off is available
        assertTrue(folder.isActionClickable(RecordFolder.CUTOFF));

        //TODO modify this step to wait for 5 minutes
        //click on cut off
        folder.clickOnCutOff();

        folder = filePlan.getRecordFolder(RECORD_FOLDER_ONE);
        //verify folder is cut off and closed
        assertTrue(folder.isCutOff());
        assertTrue(folder.isClosed());
        //verify available actions
        assertNull(folder.isActionsClickable(
                RecordFolder.VIEW_DETAILS,
                RecordFolder.UNDO_CUTOFF,
                RecordFolder.COPY,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.MANAGE_RULES,
                RecordFolder.VIEW_AUDIT));

        //navigate to details page of folder
        folderDetailsPage = folder.clickOnViewDetails();

        //verify Abolished event is present
        assertEquals(1, folderDetailsPage.getEventsQuantity());
        assertNotNull(folderDetailsPage.getEventByName(ABOLISHED));

        //complete the event
        folderDetailsPage
                .clickOnCompleteEvent(ABOLISHED)
                .clickOnOk();

        //verify available actions
        assertTrue(folderDetailsPage.getFolderActionsPanel().isActionsClickable(
                RecordFolder.UNDO_CUTOFF,
                RecordFolder.TRANSFER,
                RecordFolder.COPY,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.MANAGE_RULES,
                RecordFolder.VIEW_AUDIT));

        //transfer folder
        folderDetailsPage.getFolderActionsPanel().clickOnAction(RecordFolder.TRANSFER);

        //verify the awaiting transfer indicator is displayed for folder
        folderDetailsPage.navigateUp(2);
        assertTrue(filePlan.getRecordFolder(RECORD_FOLDER_ONE).isTransferPending());

        //navigate to transfers
        filePlan
                .getFilterPanel()
                .clickOnTransfers();

        //verify there is one transfer
        assertEquals(1, transfers.getList().size());
        //complete transfer
        transfers.getTransfer().clickOnCompleteTransfer();

        //navigate inside category
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(CATEGORY_FOLDER_DISPOSITION);

        //verify the folder is marked as transferred
        assertTrue(filePlan.getRecordFolder(RECORD_FOLDER_ONE).isTransferred());

        //open details page of folder
        filePlan
                .getRecordFolder(RECORD_FOLDER_ONE)
                .clickOnViewDetails();

        //verify Case Complete event is present
        assertEquals(1, folderDetailsPage.getEventsQuantity());
        assertNotNull(folderDetailsPage.getEventByName(CASE_COMPLETE));

        //complete the event
        folderDetailsPage
                .clickOnCompleteEvent(CASE_COMPLETE)
                .clickOnOk();

        //verify available actions
        assertTrue(folderDetailsPage.getFolderActionsPanel().isActionsClickable(
                RecordFolder.END_RETENTION,
                RecordFolder.COPY,
                RecordFolder.MANAGE_PERMISSIONS,
                RecordFolder.MANAGE_RULES,
                RecordFolder.VIEW_AUDIT));

        //end retention
        folderDetailsPage.getFolderActionsPanel().clickOnAction(RecordFolder.END_RETENTION);

        //verify obsolete and superseded events are displayed
        assertEquals(2, folderDetailsPage.getEventsQuantity());
        assertNotNull(folderDetailsPage.getEventByName(OBSOLETE));
        assertNotNull(folderDetailsPage.getEventByName(SUPERSEDED));

        //verify Accession action is not available
        assertFalse(folderDetailsPage.getFolderActionsPanel().isActionAvailable(RecordFolder.ACCESSION));

        //complete obsolete event
        folderDetailsPage
                .clickOnCompleteEvent(OBSOLETE)
                .clickOnOk();

        //navigate to browse view - inside the category
        folderDetailsPage.navigateUp(2);

        //verify Accession is available
        folder = filePlan.getRecordFolder(RECORD_FOLDER_ONE);
        assertTrue(folder.isActionClickable(RecordFolder.ACCESSION));

        //Accession the folder
        folder.clickOnAccession();

        //verify there is one transfer
        assertEquals(1, transfers.getList().size());
        //complete accession
        transfers.getTransfer().clickOnCompleteAccession();

        //verify the accession is no longer displayed
        assertEquals(0,transfers.getList().size());

        //navigate inside category
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(CATEGORY_FOLDER_DISPOSITION);

        //verify the folder is marked as accessioned
        assertTrue(filePlan.getRecordFolder(RECORD_FOLDER_ONE).isAccessioned());

        //open details page of folder
        filePlan
                .getRecordFolder(RECORD_FOLDER_ONE)
                .clickOnViewDetails();

        //verify case closed and no longer needed events are displayed
        assertEquals(2, folderDetailsPage.getEventsQuantity());
        assertNotNull(folderDetailsPage.getEventByName(NO_LONGER_NEEDED));
        assertNotNull(folderDetailsPage.getEventByName(CASE_CLOSED));

        //complete the 'No longer needed' event
        folderDetailsPage
                .clickOnCompleteEvent(NO_LONGER_NEEDED)
                .clickOnOk();

        //verify Destroy action is not available
        assertFalse(folderDetailsPage.getFolderActionsPanel().isActionAvailable(RecordFolder.DESTROY));

        //complete 'Case Closed' event
        folderDetailsPage
                .clickOnCompleteEvent(CASE_CLOSED)
                .clickOnOk();

        //verify Destroy is now available
        assertTrue(folderDetailsPage.getFolderActionsPanel().isActionClickable(RecordFolder.DESTROY));

        //navigate to browse view
        folderDetailsPage.navigateUp(2);

        //click on Destroy and confirm
        filePlan
                .getRecordFolder(RECORD_FOLDER_ONE)
                .clickOnDestroy()
                .clickOnOkOk();

        //verify the folder is marked as destroyed
        assertTrue(filePlan.getRecordFolder(RECORD_FOLDER_ONE).isDestroyed());

        //generate destruction report
        filePlan
                .getRecordFolder(RECORD_FOLDER_ONE)
                .clickOnGenerateDestructionReport()
                .clickOnFileReport();

        //navigate to Unfiled Records
        filePlan
                .getFilterPanel()
                .clickOnUnfiledRecords();

        //verify the report is present
        assertNotNull(unfiledRecords.getList().getByPartialName("Destruction Report"));

        //TODO verify the information inside Destruction report
    }

    public  void editSteps()
    {
        editDispositionSchedulePage = categoryDetailsPage.editDispositionSteps();

        //From 'Add Steps' drop-down select 'Cutoff'
        DispositionStepBlock dispositionStep =
                editDispositionSchedulePage
                        .addStep(CUTOFF_INDEX) ;
        //check 'After a period of' check-box, select '1 Day' value
        dispositionStep
                .getPeriodSection()
                .checkPeriod(true)
                .setPeriodUnit(PeriodUnit.DAY)
                .setPeriodAmount("1");
        assertEquals("Created Date", dispositionStep.getPeriodSection().getPeriodAction());
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(CUTOFF_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Transfer'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(TRANSFER_INDEX);
        //check 'When event occurs' check-box, select 'Abolished' event
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(ABOLISHED);
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(TRANSFER_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Retain'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(RETAIN_INDEX);
        //check 'When event occurs' check-box, select 'Case Complete' event
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(CASE_COMPLETE);
        //fill 'Step Description' and click 'Save' button
        dispositionStep
                .setDescription(RETAIN_LABEL)
                .clickOnSave();

        //From 'Add Steps' drop-down select 'Accession'
        dispositionStep =
                editDispositionSchedulePage
                        .addStep(ACCESSION_INDEX);
        //check 'When event occurs' check-box, select 2 events: "Obsolete" and "Superseded", select 'Which ever event is earlier' value
        dispositionStep
                .checkEvents(true)
                .getEventsSection()
                .selectEvent(OBSOLETE)
                .selectEvent(SUPERSEDED)
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
