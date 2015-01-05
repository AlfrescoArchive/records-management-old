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

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.site.SiteNavigation;
import org.alfresco.po.common.site.SitePage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Browse page
 * 
 * @author Roy Wetherall
 */
public abstract class BrowsePage<N extends SiteNavigation, L extends BrowseList<?>, S extends Toolbar> extends SitePage<N>
{
    /** list view */
    @RenderableChild
    @Autowired
    private L list;

    /** toolbar */
    @RenderableChild
    @Autowired
    private S toolbar;
    
    /**
     * @return L list view
     */
    public L getList()
    {
        return list;
    }
    
    /**
     * @return S toolbar
     */
    public S getToolbar()
    {
        return toolbar;
    }
    
    /**
     * Navigate to the the given path in the list view
     * 
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BrowsePage<?,?,?>> T navigateTo(Class<T> clazz, String ... path)
    {
        return (T)navigateTo(this, path);
    }
    
    /**
     * Navigate to the the given path in the list view
     * 
     * @param path
     * @return
     */
    public BrowsePage<?,?,?> navigateTo(String ... path)
    {
        return navigateTo(this, path);
    }
    
    /**
     * Helper to navigate to a given path in the list view from the given browse page
     * 
     * @param browsePage    browse page
     * @param path
     * @return
     */
    private BrowsePage<?,?,?> navigateTo(BrowsePage<?,?,?> browsePage, String ... path)
    {
        for (String name : path)
        {
            // try and find the list item
            ListItem listItem = browsePage.getList().getByPartialName(name);
            if (listItem == null)
            {
                throw new RuntimeException("Unable to navigate to " + name);
            }
            
            // click on the list item
            browsePage = (BrowsePage<?,?,?>)listItem.clickOnLink(this);
        }        
        return browsePage;
    }
}
