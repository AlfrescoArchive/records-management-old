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
package org.alfresco.po.share.browse;

import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Browse list item factory
 * 
 * @author Roy Wetherall
 */
public abstract class BrowseListItemFactory
{
    /** application context */
    @Autowired
    private ApplicationContext applicationContext;
    
    /** image selector */
    private By imageSelector = By.cssSelector("td[class$='thumbnail'] img");
    
    /**
     * Helper method to get image
     */
    protected WebElement getImage(WebElement row)
    {
        Utils.checkMandatoryParam("row", row);
        return Utils.waitForFind(row, imageSelector);
    }
        
    /**
     * Get list item
     */
    public ListItem getItem(WebElement row)
    {
        Utils.checkMandatoryParam("row", row);
        ListItem result = (ListItem)applicationContext.getBean(getBeanName(row));        
        result.setRow(row);        
        return result;
    }
    
    /**
     * Based on the information in the row, determine the list item bean name
     * that should be created.
     */
    protected abstract String getBeanName(WebElement row);
}
