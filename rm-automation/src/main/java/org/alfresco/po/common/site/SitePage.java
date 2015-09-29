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
package org.alfresco.po.common.site;

import java.text.MessageFormat;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.share.page.SharePage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Site page
 * 
 * @author Roy Wetherall
 */
public abstract class SitePage<N extends SiteNavigation> extends SharePage
{
    /** site page URL */
    private static final String PAGE_URL = "/page/site/{0}/{1}";
    
    /** navigation */
    @Autowired
    @RenderableChild
    private N navigation;
    
    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        if (context.length == 2)
        {
            // get the site id
            String siteId = context[0];
            if (!StringUtils.isNotBlank(siteId))
            {
                throw new RuntimeException("Site id is empty in site page URL context.");
            }
            
            // get the site page
            String sitePage = context[1];
            if (!StringUtils.isNotBlank(sitePage))
            {
                throw new RuntimeException("Site page is empty in site page URL context.");
            }
            
            return MessageFormat.format(PAGE_URL, siteId, sitePage);
        }
        else
        {
            throw new RuntimeException("Site id and site page are expected context.");
        }
    }
    
    /**
     * @return  get share page navigation 
     */
    public N getNavigation()
    {
        return navigation;
    }
}
