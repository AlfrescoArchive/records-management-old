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
package org.alfresco.po.share.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.share.site.create.Compliance;
import org.alfresco.po.share.site.create.CreateSiteDialog;
import org.alfresco.po.share.site.create.SiteType;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Create site dialog unit test
 * 
 * @author Roy Wetherall
 * @author Tatiana Kalinovskaya
 */
@Test (groups = {"unit-test"})
public class CreateSiteDialogUnitTest extends BaseTest
{
    @Autowired
    private CreateSiteDialog createSiteDialog;

    @BeforeClass
    public void beforeMethod()
    {
        openPage(userDashboardPage).getMySitesDashlet().clickOnCreateSite();
    }

    @Test
    public void checkDefaultDialogValues()
    {
        // assert that the default site type is "collaboration"
        assertEquals(SiteType.COLLABORATION_SITE, createSiteDialog.getSiteType());

        // check that the other fields on the dialog are empty
        assertTrue(createSiteDialog.getSiteName().isEmpty());
        assertTrue(createSiteDialog.getSiteURL().isEmpty());
        assertTrue(createSiteDialog.getSiteDescription().isEmpty());

        // TODO check that the Ok button is disabled and the cancel button is
        // enabled
    }

    @Test(dependsOnMethods="checkDefaultDialogValues")
    public void checkValuesSetWhenCreatingRMSite()
    {
        // set RM site type
        createSiteDialog.setSiteType(SiteType.RM_SITE);

        assertEquals("Records Management", createSiteDialog.getSiteName());

        assertEquals("rm", createSiteDialog.getSiteURL());

        assertEquals("Records Management Site", createSiteDialog.getSiteDescription());

        assertEquals(SiteType.RM_SITE, createSiteDialog.getSiteType());

        assertEquals("alfresco-rm-createSite-instance-isPublic", createSiteDialog.getVisibility());
        assertFalse(createSiteDialog.isModeratedVisibilityEnabled());
        assertFalse(createSiteDialog.isPrivateVisibilityEnabled());

        assertTrue(createSiteDialog.isComplianceDisplayed());
        assertEquals(Compliance.STANDARD, createSiteDialog.getCompliance());

        assertFalse(createSiteDialog.isSiteNameInputEnabled());

        assertFalse(createSiteDialog.isSiteURLInputEnabled());

    }
}
