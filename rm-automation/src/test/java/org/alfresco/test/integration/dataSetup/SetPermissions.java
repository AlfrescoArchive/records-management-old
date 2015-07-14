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
package org.alfresco.test.integration.dataSetup;

import static org.junit.Assert.assertEquals;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.managepermissions.ManagePermissions;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Set the permissions for users to read and file in the file plan.
 *
 * @author tpage
 * @since 3.0
 */
public class SetPermissions extends BaseTest
{
    @Autowired
    private FilePlan filePlan;
    @Autowired
    private ManagePermissions managePermissions;

    /** Give RM_MANAGER permission to read records in CATEGORY_ONE. */
    @Test
    (
        groups = { "integration", "GROUP_RM_MANAGER_READ_CATEGORY_ONE" },
        description = "Give RM_MANAGER permission to read records in CATEGORY_ONE.",
        dependsOnGroups = { "GROUP_RM_MANAGER_EXISTS", "GROUP_CATEGORY_ONE_EXISTS" }
    )
    public void rmManagerCanReadCategoryOne()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary"));
        filePlan.getRecordCategory(RECORD_CATEGORY_ONE).clickonManagePermissions();
        managePermissions.setPermissions(RM_MANAGER, RM_MANAGER, RM_MANAGER,  "Read Only");
        assertEquals("Read Only", managePermissions.getPermission(RM_MANAGER, RM_MANAGER, RM_MANAGER));
        managePermissions.clickOnOK();
    }

    /** Give UNCLEARED_USER permission to file records in CATEGORY_ONE. */
    @Test
    (
        groups = { "integration", "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE" },
        description = "Give UNCLEARED_USER permission to file records in CATEGORY_ONE.",
        dependsOnGroups = { "GROUP_UNCLEARED_USER_EXISTS", "GROUP_CATEGORY_ONE_EXISTS" }
    )
    public void unclearedUserCanFileInCategoryOne()
    {
        openPage(filePlan, RM_SITE_ID, createPathFrom("documentlibrary"));
        filePlan.getRecordCategory(RECORD_CATEGORY_ONE).clickonManagePermissions();
        managePermissions.setPermissions(UNCLEARED_USER, UNCLEARED_USER, UNCLEARED_USER, "Read and File");
        assertEquals("Read and File", managePermissions.getPermission(UNCLEARED_USER, UNCLEARED_USER, UNCLEARED_USER));
        managePermissions.clickOnOK();
    }
}
