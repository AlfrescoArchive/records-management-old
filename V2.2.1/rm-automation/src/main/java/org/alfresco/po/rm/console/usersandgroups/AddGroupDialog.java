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
package org.alfresco.po.rm.console.usersandgroups;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 */
@Component
public class AddGroupDialog extends AddAuthorityDialog
{
    /** authority name selector */
    private static final By AUTHORITY_NAME_SELECTOR = By.cssSelector("h3");
    
    /** results */
    @FindBy(id="rm-search-groupfinder-results")
    private WebElement results;
    
    /**
     * @see org.alfresco.po.rm.console.usersandgroups.AddAuthorityDialog#getResultsWebElement()
     */
    @Override
    protected WebElement getResultsWebElement()
    {
        return results;
    }  
    
    /**
     * @see org.alfresco.po.rm.console.usersandgroups.AddAuthorityDialog#getAuthorityName(org.openqa.selenium.WebElement)
     */
    @Override
    protected String getAuthorityName(WebElement element)
    {
        // get the element containing the authority name
        WebElement nameElement = element.findElement(AUTHORITY_NAME_SELECTOR);
        return nameElement.getText().trim();        
    }
}
