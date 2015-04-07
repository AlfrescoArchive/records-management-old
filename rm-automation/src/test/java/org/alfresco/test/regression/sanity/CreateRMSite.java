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

import org.alfresco.po.rm.site.RMSiteDashboard;
import org.alfresco.po.share.site.create.CreateSiteDialog;
import org.alfresco.po.share.site.create.SiteType;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

/**
 * Create RM site
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Test
(
   groups = {"RMA-2664", "sanity"},
   description = "Create RM Site"
)
public class CreateRMSite extends BaseTest
{
    /** rm site dashboard */
    @Autowired
    private RMSiteDashboard rmSiteDashboard;
    
    /** my sites dashlet */
    @Autowired
    private MySitesDashlet mySitesDahslet;    
    
    /** create site dialog */
    @Autowired
    private CreateSiteDialog createSiteDialog;
    
    /**
     * Create RM site sanity test
     */
    @Test public void createRMSite()
    {     
    	// create RM site
        openPage(userDashboardPage);
        
        // create RM site
        mySitesDahslet
            .clickOnCreateSite()						
        	.setSiteType(SiteType.RM_SITE)
            .clickOnOk();
        
        // navigate back to the user dashboard
        openPage(userDashboardPage);
       
        // ensure the rm site exists
        Assert.assertTrue(mySitesDahslet.siteExists(RM_SITE_ID));   
       
        // enter the rm site via the my sites dashlet
        mySitesDahslet.clickOnRMSite(RM_SITE_ID);
    }
    

    @AfterSuite(alwaysRun=true)
    protected void deleteRMSite()
    {     
        openPage(userDashboardPage);
        
        // check for existence of site
        if (mySitesDahslet.siteExists(RM_SITE_ID))
        {       
            // delete site
            mySitesDahslet.clickOnDeleteSite(RM_SITE_ID);
            Assert.assertFalse(mySitesDahslet.siteExists(RM_SITE_ID));
        }
    }
}
