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
package org.alfresco.po.rm.details.folder;

import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.details.category.CategoryActionsPanel;
import org.alfresco.po.rm.details.category.DispositionBlock;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

/**
 * Folder Details page Unit Test
 *
 * @author Tatiana Kalinovskaya
 */
@Test(groups = {"unit-test"})
public class FolderDetailsPageUnitTest extends BaseRmUnitTest
{
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** folder details page*/
    @Autowired
    private FolderDetailsPage folderDetailsPage;

    /** audit log page*/
    @Autowired
    private AuditLogPage auditLogPage;

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
        // navigate inside the category
        filePlan.getRecordCategory(RECORD_CATEGORY_NAME).clickOnLink();
        //create new folder
        filePlan
                .getToolbar()
                .clickOnNewRecordFolder()
                .setName(RECORD_FOLDER_ONE)
                .setTitle(TITLE)
                .clickOnSave();
    }

    private void openFolderDetails(String folderName)
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary").navigateTo(RECORD_CATEGORY_NAME);
        //open folder details page
        filePlan
                .getRecordFolder(folderName)
                .clickOnViewDetails();
    }


    @Test
    public void verifyActionsPanel()
    {
        openFolderDetails(RECORD_FOLDER_ONE);
        //get actions panel
        FolderActionsPanel folderActionsPanel = folderDetailsPage.getFolderActionsPanel();
        //verify View Audit Log is available
        assertTrue(folderActionsPanel.isActionAvailable(RecordFolder.VIEW_AUDIT));
        //verify View Audit Log is clickable
        assertTrue(folderActionsPanel.isActionClickable(RecordFolder.VIEW_AUDIT));
        //verify click On Audit Log action
        folderActionsPanel.clickOnAction(RecordFolder.VIEW_AUDIT, auditLogPage);
        assertEquals("Audit log for " + RECORD_FOLDER_ONE, auditLogPage.getAuditPageHeader());
        auditLogPage.close();
    }

    @Test
    public void expandActionsPanel()
    {
        openFolderDetails(RECORD_FOLDER_ONE);
        //click on title of Actions Panel to collapse it
        folderDetailsPage.getFolderActionsPanel().clickOnTitle();
        //verify it is collapsed
        assertFalse(folderDetailsPage.getFolderActionsPanel().isPanelExpanded());

        //click on title of Actions Panel to expand it
        folderDetailsPage.getFolderActionsPanel().clickOnTitle();
        //verify it is expanded
        assertTrue(folderDetailsPage.getFolderActionsPanel().isPanelExpanded());
    }

    @Test
    public void noEventsAvailable()
    {
        openFolderDetails(RECORD_FOLDER_ONE);
        //verify there is no available events
        assertEquals(0, folderDetailsPage.getEventsQuantity());
    }

    @Test (dependsOnMethods = "noEventsAvailable")
    public void completeEvent()
    {
        createDispositionWithEvent();
        openFolderDetails(RECORD_FOLDER_TWO);
        //verify there is 1 available event
        assertEquals(1, folderDetailsPage.getEventsQuantity());
        //verify there is Abolished event
        assertNotNull(folderDetailsPage.getEventByName(ABOLISHED));
        //verify the 'Complete event' is available
        assertTrue(folderDetailsPage.isCompleteAvailable(ABOLISHED));
        //click on complete event and complete it
        folderDetailsPage
                .clickOnCompleteEvent(ABOLISHED)
                .clickOnOk(folderDetailsPage);
        //verify 'Undo' is now available
        assertFalse(folderDetailsPage.isCompleteAvailable(ABOLISHED));
    }

    @Test(dependsOnMethods = "completeEvent")
    public void undoEvent()
    {
        openFolderDetails(RECORD_FOLDER_TWO);
        //verify there is 1 available event
        assertEquals(1, folderDetailsPage.getEventsQuantity());
        //verify 'Undo' is available
        assertFalse(folderDetailsPage.isCompleteAvailable(ABOLISHED));
    }

    /**
     * Create Disposition Schedule: Cut Off after 'Abolished' and create new folder
     */
    private void createDispositionWithEvent()
    {
        // open file plan
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        //create disposition schedule
        filePlan
                .getRecordCategory(RECORD_CATEGORY_NAME)
                .clickOnViewDetails()
                .createDispositionSchedule()
                .editDispositionSteps() //edit disposition steps: add cut off  after Abolished event step
                .addStep(CUTOFF_INDEX)
                .setDescription(CUTOFF_LABEL + DESCRIPTION)
                .addEvent(ABOLISHED)
                .clickOnSave()
                .clickOnDone() //finish disposition creation
                .navigateUp(); // navigate to file plan
        //create new folder
        filePlan
                .getToolbar()
                .clickOnNewRecordFolder()
                .setName(RECORD_FOLDER_TWO)
                .setTitle(TITLE)
                .clickOnSave();
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
