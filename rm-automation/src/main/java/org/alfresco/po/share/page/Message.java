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
package org.alfresco.po.share.page;

import static org.alfresco.po.common.util.Utils.webDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
     * Waits for message to be hidden.
     * If this message is initially visible this method will wait until either it is invisible or until a timeout occurs.
     *
     * @return upon invisibility of this message or upon expiry of timeout.
     */
    public void waitUntillHidden()
    {
        boolean shown = false;
        try
        {
            webDriverWait(1).until(visibilityOfElementLocated(MESSAGE_SELECTOR));
            shown = true;
        }
        catch (TimeoutException exception)
        {
            // do nothing and carry on          
        }
        
        if (shown)
        {
            try
            {
                webDriverWait(5).until(invisibilityOfElementLocated(MESSAGE_SELECTOR));
            }
            catch (TimeoutException exception)
            {
                // do nothing and carry on
            }
        }
    }
    
    /**
     * Wait for the message to appear or for a timeout to occur.
     *
     * @return upon visibility of this message or upon expiry of timeout.
     */
    public void waitUntillVisible()
    {
        try
        {
            webDriverWait().until(visibilityOfElementLocated(MESSAGE_SELECTOR));
        }
        catch (TimeoutException exception)
        {
            // do nothing and carry on
        }        
    }
}
