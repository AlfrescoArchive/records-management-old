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
package org.alfresco.test;

import org.alfresco.po.share.site.create.SiteType;

/**
 * Base unit test
 * 
 * @author Roy Wetherall
 */
public class BaseRmUnitTest extends BaseTest
{    
    /**
     * Helper method to create the RM site
     */
    protected void createRMSite()
    {
        if (!existsRmSite())
        {
            // then create it
            openPage(userDashboardPage)
                .getMySitesDashlet()
                .clickOnCreateSite()
                .setSiteType(SiteType.RM_SITE)
                .clickOnOk();
        }
    }
    
    /**
     * Helper method to delete the RM site
     */
    protected void deleteRMSite()
    {
        // open user dashboard
        openPage(userDashboardPage);
        
        if (existsRmSite())
        {
            // delete rm site
            userDashboardPage
                .getMySitesDashlet()
                .clickOnDeleteSite(RM_SITE_ID);
        }
    }
    
    /**
     * Helper method to determine if the RM site already exists
     * or not.
     */
    protected boolean existsRmSite()
    {
        return openPage(userDashboardPage)
                   .getMySitesDashlet()
                   .siteExists(RM_SITE_ID);
        
    }
}
