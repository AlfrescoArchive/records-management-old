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

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.rm.site.RMSiteNavigation;
import org.alfresco.po.share.browse.BrowseList;
import org.alfresco.po.share.browse.BrowsePage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RM browse page
 * 
 * @author Roy Wetherall
 */
public abstract class RMBrowsePage<L extends BrowseList<?>, S extends Toolbar> extends BrowsePage<RMSiteNavigation, L, S>
{
    /** rm browse panel set */
    @Autowired
    @RenderableChild
    private FilePlanFilterPanel filePlanFilterPanel;
    
    /** 
     * @return  file plan filter panel
     */
    public FilePlanFilterPanel getFilterPanel()
    {
        return filePlanFilterPanel;
    }
}
