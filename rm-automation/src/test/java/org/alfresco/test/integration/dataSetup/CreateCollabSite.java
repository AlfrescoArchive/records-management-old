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

import org.alfresco.po.share.admin.usertrashcan.UserTrashcanPage;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

/**
 * Create Collaboration Site for Integration tests
 *
 * @author David Webster
 */
public class CreateCollabSite extends BaseTest {

    /**
     * collab site dashboard
     */
    @Autowired
    private CollaborationSiteDashboard siteDashboard;

    /**
     * user trashcan
     */
    @Autowired
    private UserTrashcanPage userTrashcan;

    /**
     * my sites dashlet
     */
    @Autowired
    private MySitesDashlet mySitesDashlet;

    /**
     * Regression test execution
     */
    @Test
            (
                    groups = {"integration-dataSetup-collab"},
                    description = "Create Collaboration Site"
            )
    public void createFilePlan() {
        // create collaboration site
        openPage(userDashboardPage);

        // create site if it doesn't exist
        if (!mySitesDashlet.siteExists(COLLAB_SITE_ID)) {
            mySitesDashlet
                    .clickOnCreateSite()
                    .setSiteName(COLLAB_SITE_NAME)
                    .setSiteURL(COLLAB_SITE_ID)
                    .setSiteDescription(DESCRIPTION)
                    .clickOnOk();
        }

        // upload document
        siteDashboard
                .getNavigation()
                .clickOnDocumentLibrary()
                .getToolbar()
                .clickOnFile()
                .uploadFile(DOCUMENT);
    }

    /**
     * delete collaboration site
     */
    @AfterSuite
    protected void deleteCollaborationSite() {
        // check for existence of site
        if (mySitesDashlet.siteExists(COLLAB_SITE_ID))
        {
            // delete site
            mySitesDashlet.clickOnDeleteSite(COLLAB_SITE_ID);
            assertFalse(mySitesDashlet.siteExists(COLLAB_SITE_ID));

            // open the user trash can and empty it
            openPage(userTrashcan, getAdminName())
                .clickOnEmpty()
                .clickOnConfirm(userTrashcan);
        }
    }
}
