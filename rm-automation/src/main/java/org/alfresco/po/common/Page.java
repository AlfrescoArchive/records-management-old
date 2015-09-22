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
package org.alfresco.po.common;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * Page 
 * 
 * @author	Roy Wetherall
 */
public abstract class Page extends Renderable 
{
    /** last rendered page */
	private static Renderable lastRenderedPage;
	
	/** 
	 * @return {@link Renderable}  last rendered page
	 */
	public static final Renderable getLastRenderedPage()
	{
		return Page.lastRenderedPage;
	}
	
	/**
	 * @see org.alfresco.po.common.renderable.Renderable#render()
	 */
	@Override
	public <T extends Renderable> T render() 
	{
		T page = super.render();
		Page.lastRenderedPage = page;
		return page;
	}
    
	/**
	 * Get page title
	 */
    public String getPageTitle()
    {
        return webDriver.getTitle();
    }

    /**
     * Close page
     */
    public void closePage()
    {
        webDriver.close();
    }

    public void refreshCurrentPage()
    {
        Actions actions = new Actions(Utils.getWebDriver());
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();   
    }        
}
