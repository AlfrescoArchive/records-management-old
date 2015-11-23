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
package org.alfresco.test.integration.legacy;

import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.po.rm.actions.edit.EditElectronicRecordPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.Record;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.test.AlfrescoTest;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;


/**
 * CreateElectronicRecords test
 * 
 * @author Oana Nechiforescu
 */
public class CreateElectronicRecords extends BaseTest
{
    /** data prep services */
    @Autowired private RecordsManagementService service;
    
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** edit electronic record */
    @Autowired
    private EditElectronicRecordPage editElectronicRecordPage;

    @Test(dependsOnGroups="createRMSite")
    @AlfrescoTest(jira = "RM-2768")
    public void createElectronicRecord()
    {     
        String category1 = "RM-2768 category1";
        String folder1 = "RM-2768 folder1";
        String folder2 = "RM-2768 folder2";
        String record = "RM-2768 record";
        // create "rm admin" user
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
        // log in with the RM admin user
        openPage(RM_ADMIN, DEFAULT_PASSWORD, filePlan, RM_SITE_ID, "documentlibrary");
        
          // create category 1
        filePlan.getToolbar()
            .clickOnNewCategory()
            .setName(category1)
            .setTitle(TITLE)
            .clickOnSave();
        // navigate inside category 1
        filePlan.getRecordCategory(category1).clickOnLink();
        // create folder 1
        filePlan.getToolbar().clickOnNewRecordFolder()
                .setName(folder1).setTitle(TITLE).clickOnSave();
        // create folder 2
        filePlan.getToolbar().clickOnNewRecordFolder()
                .setName(folder2).setTitle(TITLE).clickOnSave();
        // upload a file in folder 1
        filePlan.getRecordFolder(folder1).clickOnLink();       
        filePlan.getToolbar().clickOnFile()
                .clickOnElectronic().uploadFile(record);
        // verify electronic record actions
        assertNull(filePlan.getRecord(record).isActionsClickable(
                Record.DOWNLOAD,
                Record.EDIT_METADATA,
                Record.COPY,
                Record.MOVE,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT,
                Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS,
                Record.ADD_RELATIONSHIP));

        // navigate to the record details page
        filePlan.getRecord(record).clickOnLink(recordDetails);

        // verify that all the expected actions are available
        assertTrue(recordDetails.getRecordActionsPanel().isActionsClickable(
                Record.EDIT_METADATA,
                Record.COMPLETE_RECORD,
                Record.COPY,
                Record.MOVE,
                Record.LINK,
                Record.DELETE,
                Record.VIEW_AUDIT,
                Record.REQUEST_INFORMATION,
                Record.MANAGE_PERMISSIONS,
                Record.ADD_RELATIONSHIP));

        // check that download is available for electronic record on record details page
        assertTrue(recordDetails.isDownloadButtonPresent());
    }
    
}
