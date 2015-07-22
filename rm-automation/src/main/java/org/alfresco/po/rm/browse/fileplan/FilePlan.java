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
package org.alfresco.po.rm.browse.fileplan;

import java.util.List;

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.browse.RMBrowsePage;
import org.alfresco.po.rm.browse.RMBrowsePlanList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * File plan 
 * 
 * @author Roy Wetherall
 */
@Component
public class FilePlan extends RMBrowsePage<RMBrowsePlanList, FilePlanToolbar> 
{
    @FindBy(css="div.crumb a.folder")
    private List<Link> folderBreadcrumb;

    @FindBy(xpath=".//span[@class='transfers']/a")
    private Link transfersFilter;

    @FindBy(xpath=".//span[@class='holds']/a")
    private Link holdsFilter;

    @FindBy(xpath=".//span[@class='unfiledRecords']/a")
    private Link unfiledRecordsFilter;
    
    /**
     * Helper method to get the named record category from the list
     */
    public RecordCategory getRecordCategory(String recordCategoryName)
    {
        return getList().get(recordCategoryName, RecordCategory.class);
    }
    
    /**
     * Helper method to get the named record folder from the list
     */
    public RecordFolder getRecordFolder(String recordFolderName)
    {
        return getList().get(recordFolderName, RecordFolder.class);
    }
    
    /**
     * Helper method to get the named record from the list
     */
    public Record getRecord(String recordName)
    {
        return getList().getByPartialName(recordName, Record.class);
    }

    /**
     * Navigate up to the last folder
     * @return
     */
    public FilePlan navigateUp()
    {
        Link link = folderBreadcrumb.get(folderBreadcrumb.size() - 1);
        link.click();
        Utils.waitForStalenessOf(link);
        return render();
    }
    
    public FilePlan navigateToContainer()
    {
        List<WebElement> containers = Utils.getWebDriver().findElements(By.cssSelector("span[class='folder-link folder-open'] a"));
        if (!containers.isEmpty()) {
            containers.get(containers.size() - 1).click();
        }
        return render();
    }

}
