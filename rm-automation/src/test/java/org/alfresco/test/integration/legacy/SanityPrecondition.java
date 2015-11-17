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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.alfresco.dataprep.RecordsManagementService;
import org.alfresco.dataprep.UserService;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.rm.console.usersandgroups.UsersAndGroupsPage;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.alfresco.po.share.site.create.SiteType;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import static org.alfresco.test.TestData.COLLAB_SANITY_ID;
import static org.alfresco.test.TestData.COLLAB_SITE;
import static org.alfresco.test.TestData.DEFAULT_EMAIL;
import static org.alfresco.test.TestData.DEFAULT_PASSWORD;
import static org.alfresco.test.TestData.DESCRIPTION;
import static org.alfresco.test.TestData.FIRST_NAME;
import static org.alfresco.test.TestData.LAST_NAME;
import static org.alfresco.test.TestData.RM_ADMIN;
import static org.alfresco.test.TestData.RM_SITE_ID;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Class to cover the precondition creation for all the tests in legacy module
 * 
 * @author Oana Nechiforescu
 */
public class SanityPrecondition extends BaseTest
{   
    String uploadedInplaceRecord = "in-place record";
    String createdInplaceRecord = "created in-place record";
    
    /** data prep services */
    @Autowired private RecordsManagementService service;
    @Autowired private UserService userService;
    
    /** my sites dashlet */
    @Autowired
    private MySitesDashlet mySitesDashlet;
   
    /** collab site dashboard */
    @Autowired
    private CollaborationSiteDashboard siteDashboard;
    
    /** file plan browse view*/
    @Autowired
    private FilePlan filePlan;

    /** document library browse view */
    @Autowired
    private DocumentLibrary documentLibrary;

    /** unfiled records browse view */
    @Autowired
    private UnfiledRecords unfiledRecords;
    
    /**
     * Method to be used to add precondition for DeclareInPlaceRecord Test
     * see Preconditions from https://issues.alfresco.com/jira/browse/RM-2366
     */    
    @Test(
            groups = {"preconditionForSanity"}
    )
    public void prepareEnvironmentForDeclareInPlaceRecord() 
    {
        createRMSiteIfDoesNotExist();
        createCollaborationSiteIfDoesNotExist();
        uploadDocumentToCollaborationSite(uploadedInplaceRecord);
        createDocumentInCollaborationSite(createdInplaceRecord, "default content");
        createRMAdminIfNotExists();
        createAndInviteUserToSiteAs(COLLABORATOR, COLLAB_SANITY_ID, "SiteContributor");  
    }

    public void createRMSiteIfDoesNotExist()
    {
        openPage(userDashboardPage);

        // check for existence of RM site
        if (!mySitesDashlet.siteExists(RM_SITE_ID)) 
        {
            // create RM site
            mySitesDashlet
                    .clickOnCreateSite()
                    .setSiteType(SiteType.RM_SITE)
                    .clickOnOk();

            // navigate back to the user dashboard
            openPage(userDashboardPage);

            // ensure the rm site exists
            Assert.assertTrue(mySitesDashlet.siteExists(RM_SITE_ID));

            // enter the rm site via the my sites dashlet
            mySitesDashlet.clickOnRMSite(RM_SITE_ID);
        }
    }

    public void createCollaborationSiteIfDoesNotExist() 
    {  
        openPage(userDashboardPage);

        // check for existence of Collaboration site
        if (!mySitesDashlet.siteExists(COLLAB_SANITY_ID)) 
        {
            // create collaboration site
            mySitesDashlet.clickOnCreateSite()
                    .setSiteName(COLLAB_SITE)
                    .setSiteURL(COLLAB_SANITY_ID)
                    .setSiteDescription(DESCRIPTION)
                    .clickOnOk();

            // navigate back to the user dashboard
            openPage(userDashboardPage);

            // ensure the collab site exists
            Assert.assertTrue(mySitesDashlet.siteExists(COLLAB_SANITY_ID));
        }
    }

    public void uploadDocumentToCollaborationSite(String documentName) 
    {
        openPage(documentLibrary, COLLAB_SANITY_ID);
        // upload document
        documentLibrary
                .getToolbar()
                .clickOnUpload()
                .uploadFile(documentName);
    }

    public void createDocumentInCollaborationSite(String documentName, String content) 
    {
        openPage(documentLibrary, COLLAB_SANITY_ID);
        // create document
        documentLibrary
                .getToolbar()
                .clickOnCreateTextFile()
                .createTextFile(documentName, content);
    }

    public void createRMAdminIfNotExists()
    {
        String encodedRMAdminUser = RM_ADMIN.replaceAll(" ", "%20");
        service.createUserAndAssignToRole(getAdminName(), getAdminPassword(), encodedRMAdminUser, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
    }

    public void createAndInviteUserToSiteAs(String username, String siteID, String role) 
    {
        if(!userService.userExists(getAdminName(), getAdminPassword(), username))
        {
        try 
        {
            userService.create(getAdminName(), getAdminPassword(), username, DEFAULT_PASSWORD, DEFAULT_EMAIL, FIRST_NAME, LAST_NAME);
            userService.inviteUserToSiteAndAccept(getAdminName(), getAdminPassword(), username, siteID, role);
        } 
        catch (Exception ex)
        {
            Logger.getLogger("").log(Level.SEVERE, null, ex);
        }
        }   
    }

}
