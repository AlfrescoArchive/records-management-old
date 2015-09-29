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
package org.alfresco.po.rm.browse;

import org.alfresco.po.share.browse.BrowseListItemFactory;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 */
@Component
public class RMBrowseListItemFactory extends BrowseListItemFactory
{
    /**
     * @see org.alfresco.po.share.browse.BrowseListItemFactory#getBeanName(org.openqa.selenium.WebElement)
     */
    protected String getBeanName(WebElement row) 
    {
        String result = "record";
        
        String src = getImage(row).getAttribute("src");
        if (src.contains("record-category"))
        {
            result = "recordCategory";
        }
        else if (src.contains("unfiled-record-folder"))
        {
            result = "unfiledRecordFolder";
        }
        else if (src.contains("record-folder") || (src.contains("stub-folder")))
        {
            result = "recordFolder";
        }
        else if (src.contains("hold"))
        {
            result = "hold";
        }
        else if (src.contains("transfer"))
        {
            result = "transfer";
        }
        
        return result;
    }
}
