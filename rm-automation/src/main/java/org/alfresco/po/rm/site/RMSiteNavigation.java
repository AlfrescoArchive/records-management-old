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
package org.alfresco.po.rm.site;

import org.alfresco.po.common.site.SiteNavigation;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.search.RecordsSearch;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * RM Site Navigation
 * 
 * @author Roy Wetherall
 */
@Component
public class RMSiteNavigation extends SiteNavigation 
{
    /** rm search link */
    @FindBy(css="div[id='HEADER_SITE_RMSEARCH'] a")    
    private Link search;
    
    /** rm management console link */
    @FindBy(css="div[id='HEADER_SITE_RM_MANAGEMENT_CONSOLE'] a")    
    private Link managementConsole;
    
    /** rm site dashboard */
    @Autowired
    private RMSiteDashboard siteDashboard;
    
    /** file plan */
    @Autowired
    private FilePlan filePlan;
    
    /** */
    @Autowired
    private RecordsSearch recordsSearch;
    
    /**
     * Click on the site dashboard link
     */
    public RMSiteDashboard clickOnSiteDashboard()
    {
        dashboard.click();
        return siteDashboard.render();
    }
    
    /**
     * Click on the file plan link
     */
    public FilePlan clickOnFilePlan()
    {
        documentLibrary.click();
        return filePlan.render();
    }
    
    /**
     * Click on search records link
     */
    public RecordsSearch clickOnRecordsSearch()
    {      
        Utils.waitFor(ExpectedConditions.elementToBeClickable(search.getWrappedElement()));
        search.click();
        return recordsSearch.render();
    }
}
