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
import org.alfresco.test.TestData;
import static org.alfresco.test.TestData.COLLAB_SITE;
import static org.alfresco.test.TestData.DEFAULT_EMAIL;
import static org.alfresco.test.TestData.DEFAULT_PASSWORD;
import static org.alfresco.test.TestData.DESCRIPTION;
import static org.alfresco.test.TestData.FIRST_NAME;
import static org.alfresco.test.TestData.LAST_NAME;
import static org.alfresco.test.TestData.RM_ADMIN;
import static org.alfresco.test.TestData.RM_SITE_ID;
import static org.alfresco.test.TestData.SANITY_COLLAB_SITE_ID;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 *
 * @author Oana Nechiforescu
 */
public class DeclareInPlaceRecord extends BaseTest
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
    

    @Test
    (
            groups = {"legacy"},
            dependsOnGroups = {"preconditionForSanity"}
    )
    public void declareInplaceRecord() 
    {
        
    }        

   
    
    
}