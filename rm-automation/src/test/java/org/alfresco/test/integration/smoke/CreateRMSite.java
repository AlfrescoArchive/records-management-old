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
package org.alfresco.test.integration.smoke;

import org.alfresco.dataprep.SiteService;
import org.alfresco.po.share.site.create.SiteType;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;


/**
 * Test that creates the RM site
 * 
 * @author Oana Nechiforescu
 */
public class CreateRMSite extends BaseTest
{
    /** data prep services */
    @Autowired private SiteService siteService;
    
    /** my sites dashlet */
    @Autowired
    private MySitesDashlet mySitesDashlet;
    
    @Test(groups="createRMSite") 
    public void createRMSite() throws Exception
    {
        try
        {
            if (!siteService.exists(RM_SITE_ID, getAdminName(), getAdminPassword()))
            {
                // create RM site
                openPage(userDashboardPage);
                mySitesDashlet
                        .clickOnCreateSite()
                        .setSiteType(SiteType.RM_SITE)
                        .clickOnOk();

                // navigate back to the user dashboard
                openPage(userDashboardPage);
                assertTrue(siteService.exists(RM_SITE_ID, getAdminName(), getAdminPassword()));
            }
        }
        catch (Exception ex)
        {
            throw new Exception(ex);
        }
    }
}
