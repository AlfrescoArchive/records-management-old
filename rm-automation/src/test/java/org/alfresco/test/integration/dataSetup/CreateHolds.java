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

import org.alfresco.po.rm.browse.holds.Holds;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Create holds
 *
 * @author Roy Wetherall
 * @since 2.4.a
 */
public class CreateHolds extends BaseTest
{
    /** Holds page */
    @Autowired
    private Holds holds;

    @Test
    (
        groups = { "integration", "GROUP_HOLDS_EXIST" },
        description = "Create File Plan",
        dependsOnGroups = { "GROUP_RM_SITE_EXISTS" }
    )

    public void createHolds()
    {
        // open the RM site and navigate to holds page
        openPage(userDashboardPage).getMySitesDashlet()
            .clickOnRMSite(RM_SITE_ID)
            .getNavigation()
            .clickOnFilePlan()
            .getFilterPanel()
            .clickOnHolds();

        // create hold1
        holds.getToolbar()
            .clickOnNewHold()
            .setReason(REASON)
            .setName(HOLD1)
            .setDescription(DESCRIPTION)
            .clickOnSave();

        // create hold2
        holds.getToolbar()
            .clickOnNewHold()
            .setReason(REASON)
            .setName(HOLD2)
            .setDescription(DESCRIPTION)
            .clickOnSave();
    }
}
