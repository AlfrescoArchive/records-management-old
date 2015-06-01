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
package org.alfresco.po.share.browse.documentlibrary;

import static org.alfresco.po.common.util.Utils.elementExists;

import org.alfresco.po.share.browse.BrowseListItemFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

/**
 * Document library list item factory
 *
 * @author Roy Wetherall
 */
@Component
public class DocumentLibraryListItemFactory extends BrowseListItemFactory
{
    /** is record indicator */
    private static By isRecordIndicatorSelector = By.cssSelector("img[alt='rm-is-record']");

    /**
     * @see org.alfresco.po.share.browse.BrowseListItemFactory#getBeanName(org.openqa.selenium.WebElement)
     */
    @Override
    protected String getBeanName(WebElement row)
    {
        String result = "document";

        String src = getImage(row).getAttribute("src");
        if (src.contains("folder"))
        {
            result = "folder";
        }
        else if (elementExists(row, isRecordIndicatorSelector))
        {
            result = "inplaceRecord";
        }

        return result;
    }

}
