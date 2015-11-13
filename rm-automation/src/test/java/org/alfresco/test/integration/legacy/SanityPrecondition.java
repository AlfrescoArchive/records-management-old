/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import static org.alfresco.test.TestData.COLLABORATOR;
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
 *
 * @author Oana
 */
public class SanityPrecondition extends BaseTest
{
     
    String uploadedInplaceRecord = "in-place record";
    String createdInplaceRecord = "created in-place record";
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
    
     /** user service */
    @Autowired private UserService userService;
    
       

/**
 * Method to be used to add precondition for DeclareInPlaceRecord Test
 * see Preconditions from https://issues.alfresco.com/jira/browse/RM-2366
 */    
    
 @Test
 (
         groups={"preconditionForSanity"}
 )
public void prepareEnvironmentForDeclareInPlaceRecord()
{
    createRMSiteIfDoesNotExist();
    createCollaborationSiteIfDoesNotExist();
    uploadDocumentToCollaborationSite(uploadedInplaceRecord);
    createDocumentInCollaborationSite(createdInplaceRecord, "default content");
    createRMAdminIfNotExists();
    inviteUserToSiteAs(COLLABORATOR, COLLAB_SANITY_ID, "SiteContributor");
}

 public void createRMSiteIfDoesNotExist()
    {
        openPage(userDashboardPage);

        // check for existence of site
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
         
         if(!mySitesDashlet.siteExists(COLLAB_SANITY_ID))
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
        RecordsManagementService service = new RecordsManagementService();
        if (!userService.userExists("admin", "admin", RM_ADMIN)) 
        {
            service.createUserAndAssignToRole("admin", "admin", RM_ADMIN, DEFAULT_PASSWORD, DEFAULT_EMAIL, UsersAndGroupsPage.ROLE_RM_ADMIN, FIRST_NAME, LAST_NAME);
        }
    }    
    
    public void inviteUserToSiteAs(String username, String siteID, String role)
    { 
        if(userService.userExists("admin", "admin", username))
        {
        return;
        }    
        try 
        {
            userService.create("admin", "admin", username, DEFAULT_PASSWORD, DEFAULT_EMAIL, FIRST_NAME, LAST_NAME);
            userService.inviteUserToSiteAndAccept(getAdminName(), getAdminPassword(), username, siteID, role);
        } 
        catch (Exception ex)
        {
            Logger.getLogger("error:").log(Level.SEVERE, null, ex);
        }
    }        
    
}
