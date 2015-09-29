package org.alfresco.po.share.userdashboard.dashlet;

import static org.junit.Assert.*;

import org.alfresco.po.share.site.create.CreateSiteDialog;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by tatiana.kalinovskaya on 03.11.2014.
 */
@Test (groups = {"unit-test"})
public class MySitesDashletUnitTest extends BaseTest
{
    @Autowired
    private MySitesDashlet mySitesDashlet;

    @Autowired
    private CreateSiteDialog createSiteDialog;

    @BeforeMethod
    public void beforeMethod()
    {
        // get "my sites" dashlet
        openPage(userDashboardPage).getMySitesDashlet();
    }

    @Test
    private void verifyMySitesDashletActions()
    {
        // open the create site dialog
        createSiteDialog = mySitesDashlet.clickOnCreateSite();
        // enter site details
        String collabSite = "mysitedashlet" + COLLAB_SITE_ID;
        createSiteDialog.setSiteName(collabSite);
        // create the site
        createSiteDialog.clickOnOk();

        openPage(userDashboardPage);
        
        // verify created site is listed in "my sites" dashlet
        assertTrue(mySitesDashlet.siteExists(collabSite));

        // verify possibility to click on created site from "my sites" dashlet
        // TODO modify the assert to verify the Site Dashboard opens on click
        assertNotNull(mySitesDashlet.clickOnCollaborationSite(collabSite));

        openPage(userDashboardPage);
        
        // delete the site
        mySitesDashlet.clickOnDeleteSite(collabSite);
        // verify deleted site is not listed in "my sites" dashlet
        assertFalse(mySitesDashlet.siteExists(collabSite));
    }

}
