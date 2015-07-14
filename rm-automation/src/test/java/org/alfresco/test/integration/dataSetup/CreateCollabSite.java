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

import org.alfresco.dataprep.SiteService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.share.admin.usertrashcan.UserTrashcanPage;
import org.alfresco.po.share.browse.documentlibrary.DocumentActions;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Create Collaboration Site for Integration tests
 *
 * @author David Webster
 */
public class CreateCollabSite extends BaseTest implements DocumentActions
{
    /** user trashcan */
    @Autowired
    private UserTrashcanPage userTrashcan;
    /** my sites dashlet */
    @Autowired
    private MySitesDashlet mySitesDashlet;
    /** The document library page. */
    @Autowired
    private DocumentLibrary documentLibrary;
   
    /** data prep services */
    @Autowired private UserService userService;
    @Autowired private SiteService siteService;
    
    /**
     * Regression test execution
     */
    @Test
    (
        groups = { "integration", "GROUP_COLLABORATION_SITE_EXISTS" },
        description = "Create Collaboration Site"
    )
    public void createCollabSite()
    {
        // create collaboration site
        openPage(userDashboardPage);

        // create site
        mySitesDashlet.clickOnCreateSite()
            .setSiteName(COLLAB_SITE_NAME)
            .setSiteURL(COLLAB_SITE_ID)
            .setSiteDescription(DESCRIPTION)
            .clickOnOk();
    }

    /** Create a document. */
    @Test
    (
        groups = { "integration", "GROUP_DOCUMENT_EXISTS" },
        description = "Create In-Place Record",
        dependsOnGroups = { "GROUP_COLLABORATION_SITE_EXISTS" }
    )
    public void createDocument()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        documentLibrary.getToolbar()
            .clickOnFile()
            .uploadFile(DOCUMENT);
    }

    /** Create an in-place record. */
    @Test
    (
        groups = { "integration", "GROUP_IN_PLACE_RECORD_EXISTS" },
        description = "Create In-Place Record",
        dependsOnGroups = { "GROUP_COLLABORATION_SITE_EXISTS", "GROUP_RM_SITE_EXISTS" }
    )
    public void declareInplaceRecord()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        // upload document
        documentLibrary.getToolbar()
            .clickOnFile()
            .uploadFile(IN_PLACE_RECORD);

        // Declare as in-place record.
        documentLibrary.getDocument(IN_PLACE_RECORD)
            .clickOnLink()
            .getDocumentActionsPanel()
            .clickOnAction(ACTION_DECLARE_RECORD);
    }

    /** Create a document that is shared with "Quick Share". */
    @Test
    (
        groups = { "integration", "GROUP_SHARED_DOCUMENT_EXISTS" },
        description = "Create Collaboration Site",
        dependsOnGroups = { "GROUP_COLLABORATION_SITE_EXISTS" }
    )
    public void createSharedDocument()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);

        // upload document
        documentLibrary.getToolbar()
            .clickOnFile()
            .uploadFile(SHARED_DOCUMENT);

        // Share document
        documentLibrary.getDocument(SHARED_DOCUMENT)
            .clickOnLink()
            .getSocialActions()
            .clickShareDocument();
    }

    /** Add the RM_MANAGER to the collaboration site. */
    @Test
    (
        groups = { "integration", "GROUP_RM_MANAGER_IN_COLLAB_SITE" },
        description = "Add the RM_MANAGER to the collaboration site",
        dependsOnGroups = { "GROUP_COLLABORATION_SITE_EXISTS", "GROUP_RM_MANAGER_EXISTS" }
    )
    public void addRMManagerToCollabSite() throws Exception
    {
        userService.inviteUserToSiteAndAccept(getAdminName(), getAdminPassword(), RM_MANAGER, COLLAB_SITE_ID, "SiteManager");
    }

    /** Add the UNCLEARED_USER to the collaboration site. */
    @Test
    (
        groups = { "integration", "GROUP_UNCLEARED_USER_IN_COLLAB_SITE" },
        description = "Add the RM_MANAGER to the collaboration site",
        dependsOnGroups = { "GROUP_COLLABORATION_SITE_EXISTS", "GROUP_UNCLEARED_USER_EXISTS" }
    )
    public void addUnclearedUserToCollabSite() throws Exception
    {
        userService.inviteUserToSiteAndAccept(getAdminName(), getAdminPassword(), UNCLEARED_USER, COLLAB_SITE_ID, "SiteManager");
    }

    /**
     * delete collaboration site
     */
    @AfterSuite
    protected void deleteCollaborationSite() throws Exception
    {
        // delete site
        if (siteService.exists(COLLAB_SITE_ID, getAdminName(), getAdminPassword()))
        {
            siteService.delete(getAdminName(), getAdminPassword(), "", COLLAB_SITE_ID);
        }
    }

}
