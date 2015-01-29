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
package org.alfresco.po.share.page;

import static org.alfresco.po.common.util.Utils.webDriverWait;

import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

/**
 * Share page message component
 * 
 * @author Roy Wetherall
 */
@Component
public class Message extends Renderable
{
    /** message selector */
    private static final By MESSAGE_SELECTOR = By.cssSelector("div.bd");
    
    /**
     * Wait for message to be hidden
     */
    public void waitUntillHidden()
    {
        boolean shown = false;
        try
        {
            webDriverWait(1).until(
               ExpectedConditions.visibilityOfElementLocated(MESSAGE_SELECTOR));
            shown = true;
        }
        catch (TimeoutException exception)
        {
            // do nothing and carry on           
        }
        
        if (shown == true)
        {
            try
            {
                webDriverWait().until(
                       ExpectedConditions.invisibilityOfElementLocated(MESSAGE_SELECTOR));            
            }
            catch (TimeoutException exception)
            {
                // do nothing and carry on
            }
        }
    }
    
    /**
     * Wait for the message to appear
     */
    public void waitUntillVisible()
    {
        try
        {
            webDriverWait().until(
               ExpectedConditions.visibilityOfElementLocated(MESSAGE_SELECTOR));
        }
        catch (TimeoutException exception)
        {
            // do nothing and carry on
        }        
    }
}
